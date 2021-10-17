package ru.ifmo.rain.akimov.servlet;


import ru.ifmo.rain.akimov.database.DataBaseProduct;
import ru.ifmo.rain.akimov.database.SQLDataBase;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

public class QueryServlet extends HttpServlet {
    final SQLDataBase dataBase;

    public QueryServlet(final SQLDataBase dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");

        if ("max".equals(command)) {
            runCommand(
                    response,
                    () -> {
                        final StringBuilder stringBuilder = new StringBuilder();
                        final Optional<DataBaseProduct> product = dataBase.getProductWithMaximalPrice();
                        product.ifPresent(p -> stringBuilder.append(p.getName()).append("\t").append(p.getPrice()).append("</br>"));
                        if (!stringBuilder.isEmpty()) {
                            stringBuilder.append(System.lineSeparator());
                        }
                        return stringBuilder.toString();
                    },
                    "<h1>Product with max price: </h1>"
            );
        } else if ("min".equals(command)) {
            runCommand(
                    response,
                    () -> {
                        final StringBuilder stringBuilder = new StringBuilder();
                        final Optional<DataBaseProduct> product = dataBase.getProductWithMinimalPrice();
                        product.ifPresent(p -> stringBuilder.append(p.getName()).append("\t").append(p.getPrice()).append("</br>"));
                        if (!stringBuilder.isEmpty()) {
                            stringBuilder.append(System.lineSeparator());
                        }
                        return stringBuilder.toString();
                    },
                    "<h1>Product with min price: </h1>");
        } else if ("sum".equals(command)) {
            runCommand(response, () -> dataBase.getSum() + System.lineSeparator(), "Summary price: ");
        } else if ("count".equals(command)) {
            runCommand(response, () -> dataBase.getCount() + System.lineSeparator(), "Number of products: ");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void runCommand(final HttpServletResponse response, final Supplier<String> supplier, final String comment) throws IOException {
        response.getWriter().println("<html><body>");
        response.getWriter().println(comment);
        response.getWriter().print(supplier.get());
        response.getWriter().println("</body></html>");
    }
}