package rateeverything.startup;

import org.h2.tools.DeleteDbFiles;

import java.sql.*;

/**
 * Created by roman on 1/18/2015.
 */
public class SetupH2Database {

    public static String dbLoc = "~/dev/tools/h2db";
    public static String dbName = "rateeverything";
    public static String dbUrl = "jdbc:h2:" + dbLoc + "/" + dbName;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        // delete the database named 'test' in the user home directory
        DeleteDbFiles.execute(dbLoc, dbName, true);

        Class.forName("org.h2.Driver");
        Connection connection = DriverManager.getConnection(dbUrl);
        Statement statement = connection.createStatement();
        statement.execute("create table Items(id long primary key, name varchar(255), category varchar(255), owner varchar(255), rating int, creationDate Timestamp)");
        statement.execute("create table Users(id varchar(255) primary key)");
        statement.execute("insert into Items values(1, 'Hello', 'nocat', 'roman', 1, '2014-01-01')");
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
