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
public class Client {

    private int id;
    private String name;
    private String password;
    private int role;
    
    public Client() {
    }

    public Client(String name, String password, int role) {
        id = 0;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public int authenticate() {
        if (role == 0) {
            try {
                ResultSet rs = new DB().executeQuery("Select * from Manager where AdminName = '" + name + "' and Password = '" + password + "'");
                if (rs.next()) {
                    int tID = rs.getInt("AdminID");
                    return tID;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        } else {
            try {
                ResultSet rs = new DB().executeQuery("Select * from Users where Branch = '" + name + "' and Password = '" + password + "'");
                if (rs.next()) {
                    int tID = rs.getInt("BranchID");
                    return tID;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            return 0;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

}
