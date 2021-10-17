package ru.ifmo.rain.akimov;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.ifmo.rain.akimov.database.SQLDataBase;
import ru.ifmo.rain.akimov.servlet.AddProductServlet;
import ru.ifmo.rain.akimov.servlet.GetProductsServlet;
import ru.ifmo.rain.akimov.servlet.QueryServlet;

public class Main {
    public static void main(String[] args) throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("test");

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(dataBase)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(dataBase)),"/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(dataBase)),"/query");

        server.start();
        server.join();
    }
}