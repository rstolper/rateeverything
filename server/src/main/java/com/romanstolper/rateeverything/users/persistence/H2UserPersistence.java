package com.romanstolper.rateeverything.users.persistence;

import com.romanstolper.rateeverything.item.domain.Item;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.startup.SetupH2Database;

import javax.inject.Singleton;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by roman on 1/18/2015.
 */
@Singleton
public class H2UserPersistence implements UserPersistence {

    private Connection connection;
    private PreparedStatement deleteItemStmt;
    private PreparedStatement insertItemStmt;
    private PreparedStatement updateItemStmt;
    private PreparedStatement getItemStmt;
    private PreparedStatement getAllItemsStmt;
    private PreparedStatement getAllItemsByOwnerStmt;
    private PreparedStatement getLatestIdStmt;

//    private long nextItemId = -1;

    public H2UserPersistence() {
        try {
            // TODO pass this as a command line arg?
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(SetupH2Database.dbUrl);
            deleteItemStmt = connection.prepareStatement("delete from Items where id = ?");
            insertItemStmt = connection.prepareStatement("insert into Items (id, name, category, owner, rating, creationDate) values (?, ?, ?, ?, ?, ?)");
            updateItemStmt = connection.prepareStatement("update Items set name = ?, category = ?, owner = ?, rating = ?, creationDate = ? where id = ?");
            getItemStmt = connection.prepareStatement("select id, name, category, owner, rating, creationDate from Items where id = ?");
            getAllItemsStmt = connection.prepareStatement("select id, name, category, owner, rating, creationDate from Items");
            getAllItemsByOwnerStmt = connection.prepareStatement("select id, name, category, owner, rating, creationDate from Items where owner = ?");
            getLatestIdStmt = connection.prepareStatement("select max(id) from Items");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    @Override
    public Item deleteItem(long itemId) {
        try {
            Item itemBeforeDeletion = selectItem(itemId);

            deleteItemStmt.setLong(1, itemId);
            deleteItemStmt.execute();

            return itemBeforeDeletion;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item delete failed: " + e.getMessage());
        }
    }

    @Override
    public Item updateItem(Item item) {
        try {
            long itemId = item.getId();

            int paramIdx = 1;
            updateItemStmt.setString(paramIdx++, item.getName());
            updateItemStmt.setString(paramIdx++, item.getCategory());
            updateItemStmt.setString(paramIdx++, item.getOwner());
            updateItemStmt.setInt(paramIdx++, item.getRating().getNumericValue());
            updateItemStmt.setTimestamp(paramIdx++, dateToTimestamp(item.getCreationDate()));
            updateItemStmt.setLong(paramIdx++, itemId);
            updateItemStmt.execute();

            Item itemAfterUpdate = selectItem(itemId);
            return itemAfterUpdate;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item update failed: " + e.getMessage());
        }
    }

    @Override
    public Item insertItem(Item item) {
        try {
            long itemId = getNewId();
            int paramIdx = 1;
            insertItemStmt.setLong(paramIdx++, itemId);
            insertItemStmt.setString(paramIdx++, item.getName());
            insertItemStmt.setString(paramIdx++, item.getCategory());
            insertItemStmt.setString(paramIdx++, item.getOwner());
            insertItemStmt.setInt(paramIdx++, item.getRating().getNumericValue());
            insertItemStmt.setTimestamp(paramIdx++, getCurrentTimestamp());
            insertItemStmt.execute();
            Item insertedItem = selectItem(itemId);
            return insertedItem;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item insert failed: " + e.getMessage());
        }
    }

    private Timestamp getCurrentTimestamp() {
        return dateToTimestamp(new java.util.Date());
    }

    private Timestamp dateToTimestamp(java.util.Date date) {
        return new Timestamp(date.getTime());
    }

    @Override
    public Collection<Item> selectItemsByOwner(String owner) {
        Collection<Item> items;
        try {
            getAllItemsByOwnerStmt.setString(1, owner);
            ResultSet result = getAllItemsByOwnerStmt.executeQuery();
            items = itemsFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    @Override
    public Collection<Item> selectItems() {
        Collection<Item> items;
        try {
            ResultSet result = getAllItemsStmt.executeQuery();
            items = itemsFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    @Override
    public Item selectItem(long itemId) {
        try {
            getItemStmt.setLong(1, itemId);
            ResultSet resultSet = getItemStmt.executeQuery();
            return itemFromResultSet(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item get failed: " + e.getMessage());
        }
    }

    private Item itemFromResultSet(ResultSet resultSet) throws SQLException {
        Collection<Item> items = itemsFromResultSet(resultSet);
        for (Item item : items) {
            return item;
        }
        return null; // if empty collection
    }

    private Collection<Item> itemsFromResultSet(ResultSet resultSet) throws SQLException {
        Collection<Item> items = new ArrayList<>();
        while (resultSet.next()) {
            int paramIdx = 1;
            long id = resultSet.getLong(paramIdx++);
            String name = resultSet.getString(paramIdx++);
            String category = resultSet.getString(paramIdx++);;
            String owner = resultSet.getString(paramIdx++);
            int rating = resultSet.getInt(paramIdx++);
            Timestamp creationDate = resultSet.getTimestamp(paramIdx++);

            items.add(new Item(id, name, category, owner, Rating.valueOfNumericValue(rating), new java.util.Date(creationDate.getTime())));
        }
        return items;
    }

    private long getNewId() throws SQLException {
        //if (nextItemId == -1) {
        ResultSet resultSet = getLatestIdStmt.executeQuery();
        long maxId = 0;
        while (resultSet.next()) {
            maxId = resultSet.getLong(1);
        }
        return maxId+1;
        //}
        //return nextItemId++;
    }
}
