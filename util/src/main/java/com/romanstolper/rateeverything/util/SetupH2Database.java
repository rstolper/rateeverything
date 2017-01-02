package com.romanstolper.rateeverything.util;

import org.h2.tools.DeleteDbFiles;

import java.sql.*;

/**
 * Utility to make the initial H2 database. Cannot run the server before running this.
 */
public class SetupH2Database {

    public static String dbLoc = (System.getenv("H2DB")!=null?System.getenv("H2DB"):"~/dev/tools/h2db");
    public static String dbName = "rateeverything";
    public static String dbUrl = "jdbc:h2:" + dbLoc + "/" + dbName;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // delete the database named 'test' in the user home directory
        DeleteDbFiles.execute(dbLoc, dbName, true);

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(dbUrl);
        Statement statement = connection.createStatement();
        statement.execute("create table Items(itemId varchar(128) primary key, userId varchar(128), name varchar(128), category varchar(255), rating int, creationDate Timestamp)");
//        statement.execute("create table Users(userId varchar(128) primary key)");
        statement.execute("insert into Items values('1', 'Hello', 'nocat', 'roman', 1, '2014-01-01')");
        ResultSet rs;
        rs = statement.executeQuery("select * from Items");
        while (rs.next()) {
            System.out.println(rs.getString("name"));
        }
        //statement.execute("create table UserItems(userId varchar(255), itemId long)");
        statement.close();
        connection.close();
    }
}
