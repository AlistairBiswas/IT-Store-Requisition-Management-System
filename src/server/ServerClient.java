/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Rafeed
 */
public class ServerClient {
    
    private int userID;
    public Socket socket;
    
    BufferedReader in;
    PrintWriter out;
    //ObjectInputStream oin;
    //ObjectOutputStream oout;
    
    public ServerClient(int userID, Socket socket) throws IOException {
        this.userID = userID;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        //oin = new ObjectInputStream(socket.getInputStream());
        //oout = new ObjectOutputStream(socket.getOutputStream());
    }
    
    private String formatMessage(String ... messages) {
        StringBuilder buffer = new StringBuilder();
        String delim = "";
        for (String message : messages) {
            buffer.append(delim);
            delim = "|";
            buffer.append(message);
        }
        return buffer.toString();
    }
    
    public void sendRequest(String Command, String ... messages) throws IOException {
        String message = Command + "|" + getUserID() + "|" + formatMessage(messages);
        out.println(message);
    }
    
    // command|clientid|productid|Quantity|Price
    
    public String getResponse() throws IOException {
        return in.readLine();
    }
    
    //public Object getResponseAsObject() throws ClassNotFoundException, IOException {
    //    return oin.readObject();
    //}

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
    
}
