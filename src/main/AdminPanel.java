/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import drawer.Drawer;
import drawer.DrawerController;
import drawer.DrawerItem;
import drawer.EventDrawer;
import fButton.floating.FloatingButtonUI;
import java.awt.Color;
import java.awt.Component;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayer;
import message.MessageDialog;
import database.DB;
import server.ServerClient;

/**
 *
 * @author Rafeed
 */
public class AdminPanel extends javax.swing.JFrame {

    private DrawerController drawer;
    private main.ProductsForm products;
    private Socket socket;

    private ServerClient manager;
    private Component currentComponent;

    /**
     * Creates new form AdminPanel
     */
    public AdminPanel() {
        initComponents();
    }

    public AdminPanel(int id, Socket socket) {
        this();
        try {
            this.socket = socket;
            manager = new ServerClient(id, socket);
            orderRequest = new OrderRequestForm(manager);
            resolvedDelivery = new ResolvedDeliveryForm(manager);
            unresolvedDelivery = new UnresolvedDeliveryForm(manager);
            products = new ProductsForm(manager);
            currentComponent = products;
            JLayer l = new JLayer(currentComponent, new FloatingButtonUI());
            mainMenu.add(l, "card3");
            titleBar.init(this);
            drawer = Drawer.newDrawer(this)
                    .header(new AdminHeader())
                    .space(5)
                    .enableScroll(true)
                    .addChild(new DrawerItem("Order Request").build())
                    .addChild(new DrawerItem("Resolved Delivery").build())
                    .addChild(new DrawerItem("Unresolved Delivery").build())
                    .addChild(new DrawerItem("Products").build())
                    .addFooter(new DrawerItem("Sign out").icon(new ImageIcon(getClass().getResource("/icon/exit.png"))).build())
                    .event(new EventDrawer() {
                        @Override
                        public void selected(int i, DrawerItem di) {
                            //System.out.println(i);
                            switch (i) {
                                case 0:
                                    currentComponent = orderRequest;
                                    showForm(currentComponent);
                                    break;
                                case 1:
                                    currentComponent = resolvedDelivery;
                                    showForm(currentComponent);
                                    break;
                                case 2:
                                    currentComponent = unresolvedDelivery;
                                    showForm(currentComponent);
                                    break;
                                case 3:
                                    currentComponent = products;
                                    showForm(l);
                                    break;
                                case 4: {
                                    try {
                                        socket.close();
                                        //System.out.println("Client disconnected: ID " + id + " Role " + "REQUISITION_MANAGER");
                                    } catch (IOException ex) {
                                        Logger.getLogger(UserPanel.class.getName()).log(Level.SEVERE, null, ex);
                                    } finally {
                                        dispose();
                                        Login login = new Login();
                                        login.setVisible(true);
                                    }
                                    break;
                                }
                            }
                        }
                    })
                    .build();
        } catch (IOException ex) {
            Logger.getLogger(AdminPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentComponent = orderRequest;
        showForm(currentComponent);
    }

    private void showForm(Component c) {
        mainMenu.removeAll();
        mainMenu.repaint();
        mainMenu.revalidate();

        mainMenu.add(c);
        mainMenu.repaint();
        mainMenu.revalidate();
    }
    
    private void refresh(Object form) {
        if (form instanceof OrderRequestForm) {
            OrderRequestForm c = (OrderRequestForm) form;
            c.refresh();
        } else if (form instanceof ResolvedDeliveryForm) {
            ResolvedDeliveryForm c = (ResolvedDeliveryForm) form;
            c.refresh();
        } else if (form instanceof UnresolvedDeliveryForm) {
            UnresolvedDeliveryForm c = (UnresolvedDeliveryForm) form;
            c.refresh();
        } else if (form instanceof ProductsForm) {
            ProductsForm c = (ProductsForm) form;
            c.refresh();
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

        jLayeredPane1 = new javax.swing.JLayeredPane();
        titleBar = new titlebar.SimpleTitleBar();
        jPanel1 = new javax.swing.JPanel();
        menuButton = new javax.swing.JButton();
        refreshButton = new javax.swing.JButton();
        mainMenu = new javax.swing.JPanel();
        orderRequest = new main.OrderRequestForm();
        resolvedDelivery = new main.ResolvedDeliveryForm();
        unresolvedDelivery = new main.UnresolvedDeliveryForm();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);

        jLayeredPane1.setBackground(new java.awt.Color(255, 255, 255));
        jLayeredPane1.setOpaque(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        menuButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/menu1.png"))); // NOI18N
        menuButton.setBorder(null);
        menuButton.setBorderPainted(false);
        menuButton.setContentAreaFilled(false);
        menuButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        menuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuButtonActionPerformed(evt);
            }
        });

        refreshButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icon/refresh.png"))); // NOI18N
        refreshButton.setBorder(null);
        refreshButton.setBorderPainted(false);
        refreshButton.setContentAreaFilled(false);
        refreshButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(refreshButton, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(menuButton, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(refreshButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        mainMenu.setLayout(new java.awt.CardLayout());
        mainMenu.add(orderRequest, "card5");
        mainMenu.add(resolvedDelivery, "card3");
        mainMenu.add(unresolvedDelivery, "card4");

        jLayeredPane1.setLayer(titleBar, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane1.setLayer(mainMenu, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane1Layout = new javax.swing.GroupLayout(jLayeredPane1);
        jLayeredPane1.setLayout(jLayeredPane1Layout);
        jLayeredPane1Layout.setHorizontalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(titleBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jLayeredPane1Layout.setVerticalGroup(
            jLayeredPane1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jLayeredPane1Layout.createSequentialGroup()
                .addComponent(titleBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLayeredPane1)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuButtonActionPerformed
        if (drawer.isShow()) {
            drawer.hide();
        } else {
            drawer.show();
        }
    }//GEN-LAST:event_menuButtonActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refresh(currentComponent);
    }//GEN-LAST:event_refreshButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminPanel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminPanel().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel mainMenu;
    private javax.swing.JButton menuButton;
    private main.OrderRequestForm orderRequest;
    private javax.swing.JButton refreshButton;
    private main.ResolvedDeliveryForm resolvedDelivery;
    private titlebar.SimpleTitleBar titleBar;
    private main.UnresolvedDeliveryForm unresolvedDelivery;
    // End of variables declaration//GEN-END:variables
}
