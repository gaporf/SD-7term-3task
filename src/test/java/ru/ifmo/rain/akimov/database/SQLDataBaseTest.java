package ru.ifmo.rain.akimov.database;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class SQLDataBaseTest {
    @Test
    public void addOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test1", "--drop-old-table");
        dataBase.addProduct("product", 1337);
    }

    @Test
    public void addManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test2", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
    }

    @Test
    public void getOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test3", "--drop-old-table");
        dataBase.addProduct("product", 1337);
        final List<DataBaseProduct> products = dataBase.getProducts();
        Assert.assertEquals(List.of(new DataBaseProduct("product", 1337)), products);
    }

    @Test
    public void getManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test4", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
        final List<DataBaseProduct> products = dataBase.getProducts();
        Assert.assertEquals(
                List.of(
                        new DataBaseProduct("apple", 100),
                        new DataBaseProduct("banana", 70),
                        new DataBaseProduct("milk", 120),
                        new DataBaseProduct("cola", 75))
                , products);
    }

    @Test
    public void getSumOfNothingTest() {
        final SQLDataBase dataBase = new SQLDataBase("test5", "--drop-old-table");
        final long sum = dataBase.getSum();
        Assert.assertEquals(0, sum);
    }

    @Test
    public void getSumOfOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test6", "--drop-old-table");
        dataBase.addProduct("product", 1337);
        final long sum = dataBase.getSum();
        Assert.assertEquals(1337, sum);
    }

    @Test
    public void getSumOfManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test7", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
        final long sum = dataBase.getSum();
        Assert.assertEquals(100 + 70 + 120 + 75, sum);
    }

    @Test
    public void getCountOfNothingTest() {
        final SQLDataBase dataBase = new SQLDataBase("test8", "--drop-old-table");
        final int count = dataBase.getCount();
        Assert.assertEquals(0, count);
    }

    @Test
    public void getCountOfOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test9", "--drop-old-table");
        dataBase.addProduct("product", 1337);
        final int count = dataBase.getCount();
        Assert.assertEquals(1, count);
    }

    @Test
    public void getCountOfManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test10", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
        final long count = dataBase.getCount();
        Assert.assertEquals(4, count);
    }

    @Test
    public void getProductWithMinimalPriceFromNothingTest() {
        final SQLDataBase dataBase = new SQLDataBase("test11", "--drop-old-table");
        final Optional<DataBaseProduct> product = dataBase.getProductWithMinimalPrice();
        Assert.assertTrue(product.isEmpty());
    }

    @Test
    public void getProductWithMinimalPriceFromOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test12", "--drop-old-table");
        dataBase.addProduct("product", 1337);
        final Optional<DataBaseProduct> product = dataBase.getProductWithMinimalPrice();
        Assert.assertTrue(product.isPresent());
        Assert.assertEquals(new DataBaseProduct("product", 1337), product.get());
    }

    @Test
    public void getProductWithMinimalPriceFromManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test13", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
        final Optional<DataBaseProduct> product = dataBase.getProductWithMinimalPrice();
        Assert.assertTrue(product.isPresent());
        Assert.assertEquals(new DataBaseProduct("banana", 70), product.get());
    }

    @Test
    public void getProductWithMaximalPriceFromNothingTest() {
        final SQLDataBase dataBase = new SQLDataBase("test14", "--drop-old-table");
        final Optional<DataBaseProduct> product = dataBase.getProductWithMaximalPrice();
        Assert.assertTrue(product.isEmpty());
    }

    @Test
    public void getProductWithMaximalPriceFromOneProductTest() {
        final SQLDataBase dataBase = new SQLDataBase("test15", "--drop-old-table");
        dataBase.addProduct("product", 1337);
        final Optional<DataBaseProduct> product = dataBase.getProductWithMaximalPrice();
        Assert.assertTrue(product.isPresent());
        Assert.assertEquals(new DataBaseProduct("product", 1337), product.get());
    }

    @Test
    public void getProductWithMaximalPriceFromManyProductsTest() {
        final SQLDataBase dataBase = new SQLDataBase("test16", "--drop-old-table");
        dataBase.addProduct("apple", 100);
        dataBase.addProduct("banana", 70);
        dataBase.addProduct("milk", 120);
        dataBase.addProduct("cola", 75);
        final Optional<DataBaseProduct> product = dataBase.getProductWithMaximalPrice();
        Assert.assertTrue(product.isPresent());
        Assert.assertEquals(new DataBaseProduct("milk", 120), product.get());
    }

    @Test
    public void persistentTableTest() {
        {
            final SQLDataBase dataBase = new SQLDataBase("test17", "--drop-old-table");
            dataBase.addProduct("product", 1337);
            final List<DataBaseProduct> products = dataBase.getProducts();
            Assert.assertEquals(List.of(new DataBaseProduct("product", 1337)), products);
        }
        {
            final SQLDataBase dataBase = new SQLDataBase("test17");
            dataBase.addProduct("product", 1337);
            final List<DataBaseProduct> products = dataBase.getProducts();
            Assert.assertEquals(
                    List.of(
                            new DataBaseProduct("product", 1337),
                            new DataBaseProduct("product", 1337)),
                    products);
        }
    }
}
