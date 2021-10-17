package ru.ifmo.rain.akimov.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.mockito.Mockito.when;

public class GetProductsServletTest {
    private GetProductsServlet getProductsServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        getProductsServlet = new GetProductsServlet();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "DROP TABLE IF EXISTS PRODUCT";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:test.db")) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    @Test
    public void noProductsTest() throws Exception {
        Mockito.doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setContentType("text/html");

        Mockito.doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setStatus(HttpServletResponse.SC_OK);

        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        try {
            getProductsServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void oneProductTest() throws Exception {
        when(request.getParameter("name")).thenReturn("product");
        when(request.getParameter("price")).thenReturn("1337");
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));

        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        Mockito.doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setContentType("text/html");

        Mockito.doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setStatus(HttpServletResponse.SC_OK);

        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        try {
            getProductsServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "product\t1337</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void manyProductsTest() throws Exception {
        when(request.getParameter("name")).thenReturn("apple");
        when(request.getParameter("price")).thenReturn("100");
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        when(request.getParameter("name")).thenReturn("banana");
        when(request.getParameter("price")).thenReturn("70");
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        when(request.getParameter("name")).thenReturn("milk");
        when(request.getParameter("price")).thenReturn("120");
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        when(request.getParameter("name")).thenReturn("cola");
        when(request.getParameter("price")).thenReturn("75");
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        try {
            final AddProductServlet addProductServlet = new AddProductServlet();
            addProductServlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }

        Mockito.doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setContentType("text/html");

        Mockito.doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setStatus(HttpServletResponse.SC_OK);

        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        try {
            getProductsServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "apple\t100</br>", "banana\t70</br>", "milk\t120</br>", "cola\t75</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }
}
