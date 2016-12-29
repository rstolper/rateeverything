package com.romanstolper.rateeverything.item.persistence;

import com.romanstolper.rateeverything.item.domain.Item;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import com.romanstolper.rateeverything.item.domain.ItemId;
import com.romanstolper.rateeverything.item.domain.Rating;
import com.romanstolper.rateeverything.user.domain.UserId;

import javax.inject.Singleton;

/**
 * H2 storage layer
 */
@Singleton
public class H2ItemPersistence implements ItemPersistence {

    private Connection connection;
    private PreparedStatement deleteItemStmt;
    private PreparedStatement insertItemStmt;
    private PreparedStatement updateItemStmt;
    private PreparedStatement getItemStmt;
    private PreparedStatement getAllItemsStmt;
    private PreparedStatement getAllItemsByUserStmt;

    public H2ItemPersistence(String dbUrl) {
        try {
            // TODO pass this as a command line arg?
            Class.forName("org.h2.Driver");
            connection = DriverManager.getConnection(dbUrl);
            deleteItemStmt = connection.prepareStatement("delete from Items where userId = ? and itemId = ?");
            insertItemStmt = connection.prepareStatement("insert into Items (itemId, userId, name, category, rating, creationDate) values (?, ?, ?, ?, ?, ?)");
            updateItemStmt = connection.prepareStatement("update Items set name = ?, category = ?, rating = ?, creationDate = ? where userId = ? and itemId = ?");
            getItemStmt = connection.prepareStatement("select itemId, userId, name, category, rating, creationDate from Items where userId = ? and itemId = ?");
            getAllItemsStmt = connection.prepareStatement("select itemId, userId, name, category, rating, creationDate from Items");
            getAllItemsByUserStmt = connection.prepareStatement("select itemId, userId, name, category, rating, creationDate from Items where userId = ?");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed: " + e.getMessage());
        }
    }

    @Override
    public Item deleteItem(UserId userId, ItemId itemId) {
        try {
            Item itemBeforeDeletion = getItem(userId, itemId);

            deleteItemStmt.setString(1, userId.getValue());
            deleteItemStmt.setString(2, itemId.getValue());
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
            int paramIdx = 1;
            updateItemStmt.setString(paramIdx++, item.getName());
            updateItemStmt.setString(paramIdx++, item.getCategory());
            updateItemStmt.setInt(paramIdx++, item.getRating().getNumericValue());
            updateItemStmt.setTimestamp(paramIdx++, new Timestamp(item.getCreationDate().toEpochMilli()));
            updateItemStmt.setString(paramIdx++, item.getUserId().getValue());
            updateItemStmt.setString(paramIdx++, item.getItemId().getValue());
            updateItemStmt.execute();

            Item itemAfterUpdate = getItem(item.getUserId(), item.getItemId());
            return itemAfterUpdate;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item update failed: " + e.getMessage());
        }
    }

    @Override
    public Item insertItem(Item item) {
        try {
            ItemId newItemId = ItemIdGen.newId();
            int paramIdx = 1;
            insertItemStmt.setString(paramIdx++, newItemId.getValue());
            insertItemStmt.setString(paramIdx++, item.getUserId().getValue());
            insertItemStmt.setString(paramIdx++, item.getName());
            insertItemStmt.setString(paramIdx++, item.getCategory());
            insertItemStmt.setInt(paramIdx++, item.getRating().getNumericValue());
            insertItemStmt.setTimestamp(paramIdx++, new Timestamp(Instant.now().toEpochMilli()));
            insertItemStmt.execute();
            Item insertedItem = getItem(item.getUserId(), newItemId);
            return insertedItem;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Item insert failed: " + e.getMessage());
        }
    }

    @Override
    public Collection<Item> getItemsForUser(UserId userId) {
        Collection<Item> items;
        try {
            getAllItemsByUserStmt.setString(1, userId.getValue());
            ResultSet result = getAllItemsByUserStmt.executeQuery();
            items = itemsFromResultSet(result);
        } catch (SQLException e) {
            e.printStackTrace();
            items = null;
        }
        return items;
    }

    @Override
    public Collection<Item> getAllItems() {
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
    public Item getItem(UserId userId, ItemId itemId) {
        try {
            getItemStmt.setString(1, userId.getValue());
            getItemStmt.setString(2, itemId.getValue());
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
            String itemId = resultSet.getString(paramIdx++);
            String userId = resultSet.getString(paramIdx++);
            String name = resultSet.getString(paramIdx++);
            String category = resultSet.getString(paramIdx++);
            int rating = resultSet.getInt(paramIdx++);
            Timestamp creationDate = resultSet.getTimestamp(paramIdx++);

            items.add(new Item(new ItemId(itemId), new UserId(userId), name, category, Rating.valueOfNumericValue(rating), creationDate.toInstant()));
        }
        return items;
    }
}
