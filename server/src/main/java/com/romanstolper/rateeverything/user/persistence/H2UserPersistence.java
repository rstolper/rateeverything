package com.romanstolper.rateeverything.user.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;

import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * TODO
 */
@Singleton
public class H2UserPersistence implements UserPersistence {

    private Connection connection;

    public H2UserPersistence(String dbUrl) {
        try {
            // TODO pass this as a command line arg?
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    private Timestamp getCurrentTimestamp() {
        return dateToTimestamp(new java.util.Date());
    }

    private Timestamp dateToTimestamp(java.util.Date date) {
        return new Timestamp(date.getTime());
    }

}
