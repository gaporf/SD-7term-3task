package ru.ifmo.rain.akimov.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SQLDataBase {
    final String name;

    public SQLDataBase(final String name) {
        this(name, "");
    }

    public SQLDataBase(final String name, final String parameters) {
        this.name = name;
        if (parameters.contains("--drop-old-table")) {
            dropOldTable();
        }
        if (!parameters.equals("") && !parameters.equals("--drop-old-table")) {
            throw new SQLDataBaseException("Incorrect parameters");
        }
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql =
                    "create table if not exists Products" +
                            "(id integer primary key autoincrement not null," +
                            " name text not null, " +
                            " price int not null)";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException e) {
            throw new SQLDataBaseException("Can't create database", e);
        }
    }

    private void dropOldTable() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql =
                    "drop table if exists Products";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't drop table", exception);
        }
    }

    public void addProduct(final String product, final long price) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            String sql =
                    "insert into Products " +
                            "(name, price) values " +
                            "('" + product + "', " + price + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException e) {
            throw new SQLDataBaseException("Can't insert product", e);
        }
    }

    public List<DataBaseProduct> getProducts() {
        final List<DataBaseProduct> products = new ArrayList<>();
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select name, price from Products");
            while (rs.next()) {
                products.add(new DataBaseProduct(rs.getString("name"), rs.getInt("price")));
            }
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't select products", exception);
        }
        return products;
    }

    public long getSum() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select sum(price) from Products");
            return rs.getLong(1);
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't get sum", exception);
        }
    }

    public int getCount() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from Products");
            return rs.getInt(1);
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't get count", exception);
        }
    }

    public Optional<DataBaseProduct> getProductWithMinimalPrice() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Products order by price limit 1");
            if (!rs.next()) {
                return Optional.empty();
            } else {
                return Optional.of(new DataBaseProduct(rs.getString("name"), rs.getLong("price")));
            }
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't get product with minimal price", exception);
        }
    }

    public Optional<DataBaseProduct> getProductWithMaximalPrice() {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Products order by price desc limit 1");
            if (!rs.next()) {
                return Optional.empty();
            } else {
                return Optional.of(new DataBaseProduct(rs.getString("name"), rs.getLong("price")));
            }
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't get product with minimal price", exception);
        }
    }
}
