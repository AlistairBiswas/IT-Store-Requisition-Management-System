/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import database.DB;
import server.ServerClient;
import table.TableCustom;

/**
 *
 * @author Rafeed
 */
public class ResolvedDeliveryForm extends javax.swing.JPanel implements RefreshButtonFunction {

    private ServerClient client;
    /**
     * Creates new form ResolvedDeliveryForm
     */
    public ResolvedDeliveryForm() {
        initComponents();
    }
    
    public ResolvedDeliveryForm(ServerClient client) {
        this();
        this.client = client;
        TableCustom.apply(jScrollPane1, TableCustom.TableType.MULTI_LINE);
        showResolvedDelivery();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableScrollButton1 = new table.TableScrollButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "OrderID", "Product Name", "Quantity", "Branch", "Date"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        tableScrollButton1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 831, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private table.TableScrollButton tableScrollButton1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        showResolvedDelivery();
    }
    
    private ArrayList<Order> resolvedDeliveryList() {
        ArrayList<Order> list = new ArrayList<>();
        try {
            ResultSet rs = new DB().executeQuery("SELECT * FROM ResolvedDelivary ORDER BY OrderID DESC");
            Order order;
            while (rs.next()) {
                order = new Order(rs.getInt("OrderID"), rs.getInt("ProductID"), rs.getInt("Quantity"), rs.getDouble("Price"), rs.getString("Date"), "accepted", rs.getInt("BranchID"));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrderRequestForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void showResolvedDelivery() {
        ArrayList<Order> resolveDeliveries = resolvedDeliveryList();
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        Object[] row = new Object[5];
        for (int i = 0; i < resolveDeliveries.size(); i++) {
            row[0] = resolveDeliveries.get(i).getOrderID();
            row[1] = resolveDeliveries.get(i).getProductName();
            row[2] = resolveDeliveries.get(i).getQuantity();
            row[3] = resolveDeliveries.get(i).getBranchName();
            row[4] = resolveDeliveries.get(i).getDate();
            model.addRow(row);
        }
    }

}