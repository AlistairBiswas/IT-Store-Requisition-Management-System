/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import cell.TableActionCellEditor;
import cell.TableActionCellRender;
import cell.TableActionEvent;
import java.awt.Font;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import database.DB;
import server.ServerClient;
import table.TableCustom;

/**
 *
 * @author Rafeed
 */
public class PendingOrdersForm extends javax.swing.JPanel implements RefreshButtonFunction {

    private ServerClient client;
    double price;

    /**
     * Creates new form OrderRequestForm
     */
    public PendingOrdersForm() {
        initComponents();
    }

    public PendingOrdersForm(ServerClient client) {
        this();
        this.client = client;
        TableCustom.apply(jScrollPane1, TableCustom.TableType.MULTI_LINE);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                String orderID = pendingTable.getModel().getValueAt(row, 0).toString();
                JLabel orderIDField = new JLabel(orderID);
                orderIDField.setFont(new Font("SansSerif", Font.PLAIN, 14));
                JLabel pNameField = new JLabel();
                pNameField.setFont(new Font("SansSerif", Font.PLAIN, 14));
                pNameField.setText(pendingTable.getModel().getValueAt(row, 1).toString());
                JSpinner quantityField = new JSpinner();
                quantityField.setValue(Integer.parseInt(pendingTable.getModel().getValueAt(row, 2).toString()));
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Order ID: "),
                    orderIDField,
                    new JLabel("Product Name:"),
                    pNameField,
                    new JLabel("Change Quantity:"),
                    quantityField};
                int result = JOptionPane.showConfirmDialog(null, inputs, "Update Quantity", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        client.sendRequest("UPDATE_ORDER", orderID, pNameField.getText(), quantityField.getValue().toString());
                        if ("PRODUCT_NOT_FOUND".equals(client.getResponse())) {
                            JOptionPane.showMessageDialog(null, "Sorry, unable to process your updated request as the desired product is not available anymore.", "Update Error", JOptionPane.ERROR_MESSAGE);
                        } else if ("ORDER_UPDATED".equals(client.getResponse())) {
                            JOptionPane.showMessageDialog(null, "Request updated successfully.");
                            showPendingOrders();
                        } else if ("ORDER_COULD_NOT_UPDATE".equals(client.getResponse())) {
                            JOptionPane.showMessageDialog(null, "Sorry, unable to process your updated request as the desired quantity exceeds the current stock availability.", "Update Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(PendingOrdersForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            @Override
            public void onDelete(int row) {
                String orderID = pendingTable.getModel().getValueAt(row, 0).toString();
                try {
                    client.sendRequest("DELECT_ORDER", orderID);
                    if ("DELETION_SUCCESS".equals(client.getResponse())) {
                        showPendingOrders();
                    } else if ("DELETION_FAILED".equals(client.getResponse())) {
                        JOptionPane.showMessageDialog(null, "Sorry, unable to delete your order. Please try again", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(PendingOrdersForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        pendingTable.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        pendingTable.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));
        showPendingOrders();
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
        pendingTable = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 255));
        setOpaque(false);

        pendingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Model Name", "Quantity", "Price", "Date", "Action"
            }
        ));
        pendingTable.setRowHeight(40);
        jScrollPane1.setViewportView(pendingTable);

        tableScrollButton1.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 897, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableScrollButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable pendingTable;
    private table.TableScrollButton tableScrollButton1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        showPendingOrders();
    }

    private ArrayList<Order> pendingOrders() {
        ArrayList<Order> list = new ArrayList<>();
        try {
            ResultSet rs = new DB().executeQuery("SELECT * FROM ActiveUserRequest WHERE BranchID = " + "'" + client.getUserID() + "' " + "AND Status = 'pending' ORDER BY OrderID DESC");
            Order order;
            while (rs.next()) {
                order = new Order(rs.getInt("OrderID"), rs.getInt("ProductID"), rs.getInt("Quantity"), rs.getDouble("Price"), rs.getString("Date"), rs.getString("Status"), rs.getInt("BranchID"));
                list.add(order);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PendingOrdersForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void showPendingOrders() {
        ArrayList<Order> pendingOrders = pendingOrders();
        DefaultTableModel model = (DefaultTableModel) pendingTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        Object[] data = new Object[5];
        for (int i = 0; i < pendingOrders.size(); i++) {
            data[0] = pendingOrders.get(i).getOrderID();
            data[1] = pendingOrders.get(i).getProductName();
            data[2] = pendingOrders.get(i).getQuantity();
            data[3] = pendingOrders.get(i).getPrice();
            data[4] = pendingOrders.get(i).getDate();
            model.addRow(data);
        }
    }
}
