package com.romanstolper.rateeverything.startup;

import java.sql.*;

/**
 * Created by roman on 1/18/2015.
 */
public class SetupH2DatabaseUsers {

    public static String dbLoc = (System.getenv("H2DB")!=null?System.getenv("H2DB"):"~/dev/tools/h2db");
    public static String dbName = "rateeverything";
    public static String dbUrl = "jdbc:h2:" + dbLoc + "/" + dbName;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(dbUrl);
        Statement statement = connection.createStatement();
        statement.execute("create table Users(id varchar(255) primary key)");
        statement.close();
        connection.close();
    }
}
