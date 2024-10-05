/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Rafeed
 */
public class Product {
    
    private int modelID;
    private String modelName;
    private String category;
    private int quantity;
    private double price;
    
    public Product() {
    }
    
    public Product(int modelID, String modelName, String category, double price) {
        this.modelID = modelID;
        this.modelName = modelName;
        this.category = category;
        this.quantity = 0;
        this.price = price;
    }

    public Product(int modelID, String modelName, String category, int quantity, double price) {
        this.modelID = modelID;
        this.modelName = modelName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public int getModelID() {
        return modelID;
    }

    public void setModelID(int modelID) {
        this.modelID = modelID;
    }

    private String getModelNameFromDB() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
