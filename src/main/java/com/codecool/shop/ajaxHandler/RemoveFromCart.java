package com.codecool.shop.ajaxHandler;

import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.orderData.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet(urlPatterns = {"/remove-from-cart"})
public class RemoveFromCart extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //empty the cart
        if (req.getParameter("setCartEmpty") != null) {
            Order.getInstance().emptyCartList();
        } else {
        // remove items
            String id = req.getParameter("id");
            int productId = Integer.parseInt(id);
            Product product = ProductDaoMem.getInstance().find(productId);

            Order.getInstance().removeFromCartList(product);
            Order.getInstance().setTotal();
            resp.sendRedirect("/cart");
        }
    }
}
