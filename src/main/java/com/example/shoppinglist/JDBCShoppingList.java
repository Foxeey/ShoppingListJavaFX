package com.example.shoppinglist;
import java.sql.*;
import java.util.ArrayList;

/**
 * This class is used to update a record in DB table
 * using PreparedStatement.
 * @author w3spoint
 */
public class JDBCShoppingList {

    public static void AddNewRecord(String productName, String amount){
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        int nextID = getNextID() + 1;
        boolean exists = recordExists(productName);

        String query = "INSERT INTO shoppinglist (ID, productName, amount) VALUES(?,?,?)";
        if(!exists) {
            try{
                //get connection
                conn = JDBCUtil.getConnection();

                //create preparedStatement
                preparedStatement = conn.prepareStatement(query);

                //set values
                preparedStatement.setInt(1, nextID);
                preparedStatement.setString(2, productName);
                preparedStatement.setString(3, amount);

                //execute query
                preparedStatement.executeUpdate();

                //close connection
                preparedStatement.close();
                conn.close();

                System.out.println("Record updated successfully.");
            }catch(Exception e){
                e.printStackTrace();
            }
        } else {
            recordUpdate(productName, amount);
        }
    }
    public static int getNextID() {
        Connection conn = null;
        Statement statement = null;
        int nextID = -1;

        String query = "SELECT MAX(ID) AS nextID FROM shoppinglist";

        try {
            conn = JDBCUtil.getConnection();

            statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                nextID = rs.getInt("nextID");
            }

            statement.close();
            conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextID;
    }
    public static boolean recordExists(String productName) {
        boolean exists = false;
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String result = "";

        String query = "SELECT productName FROM shoppinglist WHERE productName = ?";

        try {
            conn = JDBCUtil.getConnection();

            preparedStatement = conn.prepareStatement(query);

            preparedStatement.setString(1, productName);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                result = rs.getString("productName");
            }
            if (!result.isEmpty()) {
                exists = true;
            }

            preparedStatement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(exists);
        return exists;
    }
    public static void recordUpdate(String productName, String amount) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        int nextID = getNextID() + 1;

        String query = "UPDATE shoppinglist SET amount = ? WHERE productName = ?";

        try {
            conn = JDBCUtil.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, amount);
            preparedStatement.setString(2, productName);

            preparedStatement.executeUpdate();

            preparedStatement.close();
            conn.close();

            System.out.println("Record updated sucessfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void recordDelete(String productName) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        boolean exists = recordExists(productName);

        String query = "DELETE FROM shoppinglist WHERE productName = ?";

        if(exists) {

            try {
                conn = JDBCUtil.getConnection();
                preparedStatement = conn.prepareStatement(query);
                preparedStatement.setString(1, productName);

                preparedStatement.executeUpdate();
                preparedStatement.close();
                conn.close();

                System.out.println("Record deleted sucessfully.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("brak rekordu");
        }
    }
    public static void getData(ArrayList<String> data) {
        Connection conn = null;
        Statement statement = null;


        String query = "SELECT productName, amount FROM shoppinglist";

        try {
            conn = JDBCUtil.getConnection();
            statement = conn.createStatement();

            ResultSet rs = statement.executeQuery(query);
            while(rs.next()) {
                data.add(rs.getString("productName"));
            }

            conn.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static String getAmount(String productName) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String amount = "";

        String query = "SELECT amount FROM shoppinglist WHERE productName = ?";

        try {
            conn = JDBCUtil.getConnection();
            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, productName);



            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                amount = rs.getString("amount");
            }
            preparedStatement.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return amount;
    }
    public static void searchData(String searchedProductName, ArrayList<String> searchData) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;

        String query = "SELECT productName FROM shoppinglist WHERE productName LIKE ?";

        try {
            conn = JDBCUtil.getConnection();

            preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, searchedProductName + "%");

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()) {
                searchData.add(rs.getString("productName"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}