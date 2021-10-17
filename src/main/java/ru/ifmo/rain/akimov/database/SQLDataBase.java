package ru.ifmo.rain.akimov.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.*;

public class SQLDataBase {
    final String name;

    private void updateSQL(final String sql) {
        try (final Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            final Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (final SQLException e) {
            throw new SQLDataBaseException("Can't update SQL", e);
        }
    }

    private void dropOldTable() {
        updateSQL("drop table if exists Products");
    }

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
        updateSQL("create table if not exists Products" +
                "  (id integer primary key autoincrement not null," +
                "  name text not null, " +
                "  price int not null)");
    }

    public void addProduct(final String product, final long price) {
        updateSQL("insert into Products " +
                "  (name, price) values " +
                "  ('" + product + "', " + price + ")");
    }

    private <R> R querySQL(final String sql, final Function<ResultSet, R> resultSetConsumer) {
        try (Connection c = DriverManager.getConnection("jdbc:sqlite:" + name + ".db")) {
            final Statement stmt = c.createStatement();
            final ResultSet rs = stmt.executeQuery(sql);
            final R result = resultSetConsumer.apply(rs);
            stmt.close();
            return result;
        } catch (final SQLException exception) {
            throw new SQLDataBaseException("Can't execute query", exception);
        }
    }

    public List<DataBaseProduct> getProducts() {
        return querySQL("select name, price from Products", rs -> {
            final List<DataBaseProduct> products = new ArrayList<>();
            try {
                while (rs.next()) {
                    products.add(new DataBaseProduct(rs.getString("name"), rs.getInt("price")));
                }
            } catch (final SQLException exception) {
                throw new SQLDataBaseException("Can't get list of products", exception);
            }
            return products;
        });
    }

    public long getSum() {
        return querySQL("select sum(price) from Products", rs -> {
            try {
                return rs.getLong(1);
            } catch (final SQLException exception) {
                throw new SQLDataBaseException("Can't get sum", exception);
            }
        });
    }

    public int getCount() {
        return querySQL("select count(*) from Products", rs -> {
            try {
                return rs.getInt(1);
            } catch (final SQLException exception) {
                throw new SQLDataBaseException("Can't get count", exception);
            }
        });
    }

    public Optional<DataBaseProduct> getProductWithMinimalPrice() {
        return querySQL("select * from Products order by price limit 1", rs -> {
            try {
                if (!rs.next()) {
                    return Optional.empty();
                } else {
                    return Optional.of(new DataBaseProduct(rs.getString("name"), rs.getLong("price")));
                }
            } catch (final SQLException exception) {
                throw new SQLDataBaseException("Can't get product with minimal price", exception);
            }
        });
    }

    public Optional<DataBaseProduct> getProductWithMaximalPrice() {
        return querySQL("select * from Products order by price desc limit 1", rs -> {
            try {
                if (!rs.next()) {
                    return Optional.empty();
                } else {
                    return Optional.of(new DataBaseProduct(rs.getString("name"), rs.getLong("price")));
                }
            } catch (final SQLException exception) {
                throw new SQLDataBaseException("Can't get product with maximal price", exception);
            }
        });
    }
}
