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

public class QueryServletTest {

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
    public void sumNothingTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest1", "--drop-old-table");
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        when(request.getParameter("command")).thenReturn("sum");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Summary price: ", "0", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void sumOneProductTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest2", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "product", 1337);
        when(request.getParameter("command")).thenReturn("sum");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Summary price: ", "1337", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void sumManyProductsTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest3", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "apple", 100);
        addProduct(addProductServlet, "banana", 70);
        addProduct(addProductServlet, "milk", 120);
        addProduct(addProductServlet, "cola", 75);
        when(request.getParameter("command")).thenReturn("sum");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Summary price: ", Integer.toString(70 + 100 + 120 + 75), "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void countNothingTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest4", "--drop-old-table");
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        when(request.getParameter("command")).thenReturn("count");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Number of products: ", "0", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void countOneProductTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest5", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "product", 1337);
        when(request.getParameter("command")).thenReturn("count");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Number of products: ", "1", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void countManyProductsTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest6", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "apple", 100);
        addProduct(addProductServlet, "banana", 70);
        addProduct(addProductServlet, "milk", 120);
        addProduct(addProductServlet, "cola", 75);
        when(request.getParameter("command")).thenReturn("count");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "Number of products: ", "4", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void maxNothingTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest7", "--drop-old-table");
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        when(request.getParameter("command")).thenReturn("max");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with max price: </h1>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void maxOneProductTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest8", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "product", 1337);
        when(request.getParameter("command")).thenReturn("max");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with max price: </h1>", "product\t1337</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void maxManyProductsTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest9", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "apple", 100);
        addProduct(addProductServlet, "banana", 70);
        addProduct(addProductServlet, "milk", 120);
        addProduct(addProductServlet, "cola", 75);
        when(request.getParameter("command")).thenReturn("max");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with max price: </h1>", "milk\t120</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void minNothingTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest10", "--drop-old-table");
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        when(request.getParameter("command")).thenReturn("min");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with min price: </h1>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void minOneProductTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest11", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "product", 1337);
        when(request.getParameter("command")).thenReturn("min");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with min price: </h1>", "product\t1337</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void minManyProductsTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest12", "--drop-old-table");
        final AddProductServlet addProductServlet = new AddProductServlet(dataBase);
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");

        addProduct(addProductServlet, "apple", 100);
        addProduct(addProductServlet, "banana", 70);
        addProduct(addProductServlet, "milk", 120);
        addProduct(addProductServlet, "cola", 75);
        when(request.getParameter("command")).thenReturn("min");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("<html><body>", "<h1>Product with min price: </h1>", "banana\t70</br>", "</body></html>"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }

    @Test
    public void incorrectCommandTest() throws Exception {
        final SQLDataBase dataBase = new SQLDataBase("QueryServletTest13", "--drop-old-table");
        final QueryServlet queryServlet = new QueryServlet(dataBase);
        final PrintWriter printWriter = new PrintWriter("test_file.txt");
        when(request.getParameter("command")).thenReturn("avg");
        when(response.getWriter()).thenReturn(printWriter);

        try {
            queryServlet.doGet(request, response);
            printWriter.close();
            final List<String> httpOutput = Files.readAllLines(Paths.get("test_file.txt"));
            Assert.assertEquals(List.of("Unknown command: avg"), httpOutput);
        } catch (final Exception ignored) {
            Assert.fail();
        }
    }
}
