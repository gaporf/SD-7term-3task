package ru.ifmo.rain.akimov.servlet;

import ru.ifmo.rain.akimov.database.SQLDataBase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddProductServlet extends HttpServlet {
    final SQLDataBase dataBase;

    public AddProductServlet(final SQLDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String productName = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        try {
            dataBase.addProduct(productName, price);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}