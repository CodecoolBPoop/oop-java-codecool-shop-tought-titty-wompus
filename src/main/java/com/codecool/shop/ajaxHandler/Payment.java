package com.codecool.shop.ajaxHandler;

import com.codecool.shop.dao.implementation.jdbc.LineItemDaoJdbc;
import com.codecool.shop.dao.implementation.jdbc.OrderDaoJdbc;
import com.codecool.shop.orderData.LineItem;
import com.codecool.shop.orderData.Order;
import com.codecool.shop.orderData.SendEmail;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;

@WebServlet(urlPatterns = {"/payment-handler"})
public class Payment extends HttpServlet {

    private LineItemDaoJdbc lineItemDaoJdbc = new LineItemDaoJdbc();
    private static int fileNameExtension = 1;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        JSONObject orderJson = new JSONObject();
        JSONArray jsonArr = new JSONArray();
        JSONObject innerJson;

        for (LineItem item: Order.getInstance().getCartList()) {
            innerJson = new JSONObject();
            innerJson.put("productname", item.getProductName());
            innerJson.put("quantity", item.getQuantity());
            innerJson.put("subtotal", item.getSubTotalPrice());

            jsonArr.put(innerJson);
        }
        orderJson.put("total", Order.getInstance().getTotal());
        orderJson.put("cartList", jsonArr);

        JSONObject customerInfo = new JSONObject();
        customerInfo.put("email", Order.getInstance().getCustomer().getEmail());
        customerInfo.put("name", Order.getInstance().getCustomer().getName());
        orderJson.put("customer", customerInfo);

        FileWriter fileWriter = new FileWriter("src/main/webapp/static/json/order" + fileNameExtension++ + ".json");
        fileWriter.write(orderJson.toString());
        fileWriter.close();
        OrderDaoJdbc.getInstance().add(1, Order.getInstance().getTotal(), new Date(System.currentTimeMillis()));
        for (LineItem item : Order.getInstance().getCartList()) {
            lineItemDaoJdbc.add(OrderDaoJdbc.getInstance().getNextOrderId(), item.getQuantity(), item.getProduct().getId());
        }
        Order.getInstance().deleteOrder();
        SendEmail.sendEmail(orderJson);
    }
}