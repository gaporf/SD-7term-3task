package ru.ifmo.rain.akimov.servlet;


import ru.ifmo.rain.akimov.database.DataBaseProduct;
import ru.ifmo.rain.akimov.database.SQLDataBase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class QueryServlet extends HttpServlet {
    final SQLDataBase dataBase;

    public QueryServlet(final SQLDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");
            final Optional<DataBaseProduct> product = dataBase.getProductWithMaximalPrice();
            if (product.isPresent()) {
                response.getWriter().println(product.get().getName() + "\t" + product.get().getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } else if ("min".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");
            final Optional<DataBaseProduct> product = dataBase.getProductWithMinimalPrice();
            if (product.isPresent()) {
                response.getWriter().println(product.get().getName() + "\t" + product.get().getPrice() + "</br>");
            }
            response.getWriter().println("</body></html>");
        } else if ("sum".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");
            response.getWriter().println(dataBase.getSum());
            response.getWriter().println("</body></html>");
        } else if ("count".equals(command)) {
            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");
            response.getWriter().println(dataBase.getCount());
            response.getWriter().println("</body></html>");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}