/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import database.DB;

/**
 *
 * @author Rafeed
 */
public class Order {
    
    private int orderID;
    private int productID;
    private String productName;
    private int quantity;
    private double price;
    private String date;
    private String status;
    private int branchID;
    private String branchName;
    
    public Order(int branchID, int productID, int quantity, double productPrice) {
        this.branchID = branchID;
        this.productID = productID;
        this.productName = getProductNameFromDB(productID);
        this.quantity = quantity;
        price = productPrice * quantity;
        orderID = 0;
        date = null;
        status = "pending";
        this.branchName = getBranchNameFromDB(branchID);
    }
    
    public Order(int orderID, int productID, int quantity, double price, String date, String status, int branchID) {
        this.orderID = orderID;
        this.productID = productID;
        this.productName = getProductNameFromDB(productID);
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.status = status;
        this.branchID = branchID;
        this.branchName = getBranchNameFromDB(branchID);
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
    private String getProductNameFromDB(int productID) {
        try {
            ResultSet rs = new DB().executeQuery("SELECT * FROM Product where ModelID = '" + productID + "'");
            while (rs.next()) {
                setProductName(rs.getString("ModelName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getProductName();
    }
    
    private String getBranchNameFromDB(int branchID) {
        try {
            ResultSet rs = new DB().executeQuery("SELECT * FROM Users where BranchID = '" + branchID + "'");
            while (rs.next()) {
                setBranchName(rs.getString("Branch"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Order.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getBranchName();
    }
    
}
