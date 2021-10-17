package ru.ifmo.rain.akimov.database;

public class SQLDataBaseException extends RuntimeException {
    public SQLDataBaseException(final String message) {
        super(message);
    }

    public SQLDataBaseException(final String message, final Exception e) {
        super(message, e);
    }
}
