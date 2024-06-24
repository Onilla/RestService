package com.homework.exception;

import java.sql.SQLException;

public class DBException extends RuntimeException {
    public DBException(SQLException message) {
        super(message);
    }
}
