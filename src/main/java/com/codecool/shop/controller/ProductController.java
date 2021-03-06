package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.orderData.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.*;

@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SupplierDao supplierDataStore = SupplierDaoJdbc.getInstance();
        ProductDao productDataStore = ProductDaoJdbc.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoJdbc.getInstance();
        String id = req.getParameter("category");
        String supplierId = req.getParameter("supplier");
        Map params = new HashMap<>();
        params.put("category", productCategoryDataStore.getAll());
        params.put("supplier", supplierDataStore.getAll());
        // change this for sql
        params.put("cartItemCount", Order.getInstance().getNumberOfProducts());

        if (id == null && supplierId == null) {
            params.put("actual_title", productCategoryDataStore.find(1));
            params.put("products", productDataStore.getAll());
        } else if (id != null) {
            params.put("actual_title", productCategoryDataStore.find(Integer.parseInt(id)));
            params.put("products", productDataStore.getBy(productCategoryDataStore.find(Integer.parseInt(id))));
        } else {
            params.put("actual_title", supplierDataStore.find(Integer.parseInt(supplierId)));
            params.put("products", productDataStore.getBy(supplierDataStore.find(Integer.parseInt(supplierId))));
        }

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariables(params);
        engine.process("product/index.html", context, resp.getWriter());
    }
}
