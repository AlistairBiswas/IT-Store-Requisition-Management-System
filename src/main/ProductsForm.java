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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import database.DB;
import server.ServerClient;
import table.TableCustom;

/**
 *
 * @author Rafeed
 */
public class ProductsForm extends javax.swing.JPanel implements RefreshButtonFunction {

    private ServerClient client;

    /**
     * Creates new form ProductsForm
     */
    public ProductsForm() {
        initComponents();
    }

    public ProductsForm(ServerClient client) {
        this();
        this.client = client;
        TableCustom.apply(jScrollPane1, TableCustom.TableType.MULTI_LINE);
        TableActionEvent event = new TableActionEvent() {
            @Override
            public void onEdit(int row) {
                JLabel categoryField = new JLabel(productTable.getModel().getValueAt(row, 2).toString());
                categoryField.setFont(new Font("SansSerif", Font.BOLD, 14));
                JLabel mNameField = new JLabel(productTable.getModel().getValueAt(row, 1).toString());
                mNameField.setFont(new Font("SansSerif", Font.BOLD, 14));
                JTextField priceField = new JTextField(productTable.getModel().getValueAt(row, 4).toString());
                JTextField quantityField = new JTextField(productTable.getModel().getValueAt(row, 3).toString());
                final JComponent[] inputs = new JComponent[]{
                    new JLabel("Category:"),
                    categoryField,
                    new JLabel("Model Name:"),
                    mNameField,
                    new JLabel("Price:"),
                    priceField,
                    new JLabel("Quantity:"),
                    quantityField};
                int result = JOptionPane.showConfirmDialog(null, inputs, "Update Product", JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        client.sendRequest("UPDATE_PRODUCT", mNameField.getText(), priceField.getText(), quantityField.getText());
                        if ("PRODUCT_UPDATED".equals(client.getResponse())) {
                            JOptionPane.showMessageDialog(null, "Updated Successfully");
                            refresh();
                        } else if ("PRODUCT_UPDATE_FAILED".equals(client.getResponse())) {
                            JOptionPane.showMessageDialog(null, "Failed to update product.", "Product Update Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        Logger.getLogger(ProductsForm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                }
            }

            @Override
            public void onDelete(int row) {
                try {
                    client.sendRequest("DELETE_PRODUCT", productTable.getModel().getValueAt(row, 0).toString());
                    if ("PRODUCT_DELETION_SUCCESSFUL".equals(client.getResponse())) {
                        refresh();
                    } else if ("PRODUCT_DELETION_FAILED".equals(client.getResponse())) {
                        JOptionPane.showMessageDialog(null, "Failed to delete product.", "Product Delete Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProductsForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        productTable.getColumnModel().getColumn(5).setCellRenderer(new TableActionCellRender());
        productTable.getColumnModel().getColumn(5).setCellEditor(new TableActionCellEditor(event));
        showProduct();
    }

    private ArrayList<Product> productList() {
        ArrayList<Product> list = new ArrayList<>();
        try {
            ResultSet rs = new DB().executeQuery("SELECT * FROM Product");
            Product p;
            while (rs.next()) {
                p = new Product(rs.getInt("ModelID"), rs.getString("ModelName"), rs.getString("Category"), rs.getInt("Quantity"), rs.getDouble("Price"));
                list.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProductsForm.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    private void showProduct() {
        ArrayList<Product> products = productList();
        DefaultTableModel model = (DefaultTableModel) productTable.getModel();
        int rowCount = model.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            model.removeRow(i);
        }
        Object[] row = new Object[5];
        for (int i = 0; i < products.size(); i++) {
            row[0] = products.get(i).getModelID();
            row[1] = products.get(i).getModelName();
            row[2] = products.get(i).getCategory();
            row[3] = products.get(i).getQuantity();
            row[4] = products.get(i).getPrice();
            model.addRow(row);
        }
    }

    public void actionButton() {
        JTextField categoryField = new JTextField();
        JTextField mNameField = new JTextField();
        JTextField priceField = new JTextField();
        JTextField quantityField = new JTextField();
        final JComponent[] inputs = new JComponent[]{
            new JLabel("Category:"),
            categoryField,
            new JLabel("Model Name:"),
            mNameField,
            new JLabel("Price:"),
            priceField,
            new JLabel("Quantity:"),
            quantityField,};
        int result = JOptionPane.showConfirmDialog(null, inputs, "Add A New Product", JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                client.sendRequest("ADD_PRODUCT", mNameField.getText(), categoryField.getText(), quantityField.getText(), priceField.getText());
                if ("PRODUCT_ADDED".equals(client.getResponse())) {
                    refresh();
                } else if ("PRODUCT_ADD_FAILED".equals(client.getResponse())) {
                    JOptionPane.showMessageDialog(null, "Failed to add product.", "Product Add Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                Logger.getLogger(ProductsForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        productTable = new javax.swing.JTable();

        setOpaque(false);

        productTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Model ID", "Model Name", "Category", "Quantity", "Price", "Action"
            }
        ));
        jScrollPane1.setViewportView(productTable);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 621, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 452, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable productTable;
    // End of variables declaration//GEN-END:variables

    @Override
    public void refresh() {
        showProduct();
    }
}
