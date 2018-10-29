package com.codecool.shop.ajaxHandler;

import com.codecool.shop.orderData.Order;
import com.codecool.shop.orderData.SendEmail;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/payment-handler"})
public class Payment extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //empty the cart
        SendEmail.sendEmail();
        Order.getInstance().emptyCartList();
        System.out.println("ITT");
        System.out.println(Order.getInstance().getCartList().size());
    }
}