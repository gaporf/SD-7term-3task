package ru.ifmo.rain.akimov.servlet;


import ru.ifmo.rain.akimov.database.DataBaseProduct;
import ru.ifmo.rain.akimov.database.SQLDataBase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class GetProductsServlet extends HttpServlet {
    final SQLDataBase dataBase;

    public GetProductsServlet(final SQLDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final List<DataBaseProduct> products = dataBase.getProducts();
        response.getWriter().println("<html><body>");
        for (final DataBaseProduct product : products) {
            response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
        }
        response.getWriter().println("</body></html>");

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}