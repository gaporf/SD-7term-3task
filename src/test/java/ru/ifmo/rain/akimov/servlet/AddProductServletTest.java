package ru.ifmo.rain.akimov.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.mockito.Mockito.when;

public class AddProductServletTest {
    private AddProductServlet addProductServlet;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        addProductServlet = new AddProductServlet();
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
    public void addProductTest() throws IOException {
        when(request.getParameter("name")).thenReturn("product");
        when(request.getParameter("price")).thenReturn("1337");

        Mockito.doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setContentType("text/html");

        Mockito.doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setStatus(HttpServletResponse.SC_OK);

        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        try {
            addProductServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("OK"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }
}
