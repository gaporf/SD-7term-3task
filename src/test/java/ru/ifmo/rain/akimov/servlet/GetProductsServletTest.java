package ru.ifmo.rain.akimov.servlet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ifmo.rain.akimov.database.SQLDataBase;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.Mockito.when;

public class GetProductsServletTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doThrow(new AssertionError("Expected only text/html")).when(response).setContentType(Mockito.anyString());
        Mockito.doNothing().when(response).setContentType("text/html");

        Mockito.doThrow(new AssertionError("Expected only OK status")).when(response).setStatus(Mockito.anyInt());
        Mockito.doNothing().when(response).setStatus(HttpServletResponse.SC_OK);
    }

    private void addProduct(final AddProductServlet servlet, final String product, final long price) throws IOException {
        when(request.getParameter("name")).thenReturn(product);
        when(request.getParameter("price")).thenReturn(Long.toString(price));
        when(response.getWriter()).thenReturn(new PrintWriter(System.out));
        try {
            servlet.doGet(request, response);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void noProductsTest() throws Exception {
        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        final SQLDataBase dataBase = new SQLDataBase("GetProductsServletTest1", "--drop-old-table");
        final GetProductsServlet getProductsServlet = new GetProductsServlet(dataBase);
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
        final SQLDataBase dataBase = new SQLDataBase("GetProductsServletTest2", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        addProduct(addProductServlet, "product", 1337);

        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        Mockito.when(response.getWriter()).thenReturn(printWriter);

        final GetProductsServlet getProductsServlet = new GetProductsServlet(dataBase);
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
        final SQLDataBase dataBase = new SQLDataBase("GetProductsServletTest3", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        addProduct(addProductServlet, "apple", 100);
        addProduct(addProductServlet, "banana", 70);
        addProduct(addProductServlet, "milk", 120);
        addProduct(addProductServlet, "cola", 75);

        final GetProductsServlet getProductsServlet = new GetProductsServlet(dataBase);
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
