/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import database.DB;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Order;
import main.Product;

/**
 *
 * @author Rafeed
 */
public class Server {

    private static final Lock lock = new ReentrantLock();
    ServerSocket serverSocket;
    static int port;

    private static List<ClientHandler> connectedClients = new CopyOnWriteArrayList<>();;

    public Server(int port) throws IOException {
        Server.port = port;
        serverSocket = new ServerSocket(port);
        System.out.println("Server started and waiting for connections...");
    }

    public void initializeClient() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();

            // Handle the client connection in a separate thread
            ClientHandler handler = new ClientHandler(socket);
            new Thread(handler).start();
        }
    }

    public void startCLI() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;

            while (running) {
                //System.out.print("Enter a command: ");
                String input = scanner.nextLine().trim();

                String[] parts = input.split(" ");
                String command = parts[0].toLowerCase();

                switch (command) {
                    case "/quit":
                        shutdown();
                        running = false;
                        System.exit(0);
                        break;
                    case "/disconnect":
                        if (parts.length >= 3) {
                            String target = parts[1];
                            String role = parts[2];
                            disconnectClient(target, role);
                        } else {
                            System.out.println("Invalid format. Usage: /disconnect [target] [role]");
                        }
                        break;
                    case "/clients":
                        showAllClients();
                        break;
                    case "/help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Unknown command. Type /help for available commands.");
                }
            }
        }
    }

    private void printHelp() {
        System.out.println("Here is a list of all available commands:");
        System.out.println("-----------------------------------------");
        System.out.println("/quit - shuts down the server.");
        System.out.println("/disconnect [target] [role] - manually disconnects a client.");
        System.out.println("/clients - shows all connected clients.");
        System.out.println("/help - shows this help message.");
    }

    private void shutdown() {
        lock.lock();
        try {
            // Iterate through your list of connected clients and disconnect them
            for (ClientHandler client : connectedClients) {
                try {
                    client.socket.close();
                } catch (IOException ex) {
                    //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
                connectedClients.remove(client);
                //System.out.println("Disconnected client during server shutdown: ID " + client.id + " Role " + client.clientRole);
            }
            // Close the server socket
            try {
                serverSocket.close();
            } catch (IOException ex) {
                //Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Server shutting down.");
        } finally {
            lock.unlock();
        }
    }

    private void disconnectClient(String target, String role) {
        lock.lock();
        try {
            // Iterate through connectedClients list and find the client to disconnect
            for (ClientHandler client : connectedClients) {
                if (client.id == Integer.parseInt(target) && client.clientRole.equalsIgnoreCase(role)) {
                    try {
                        client.socket.close();
                    } catch (IOException ex) {
                        Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    connectedClients.remove(client);
                    //System.out.println("Client disconnected: ID " + client.id + " Role " + client.clientRole);
                    return;
                }
            }
            System.out.println("Client not found for disconnection.");
        } finally {
            lock.unlock();
        }
    }

    private void showAllClients() {
        lock.lock();
        try {
            if (connectedClients.isEmpty()) {
                System.out.println("No clients are currently connected.");
            } else {
                System.out.println("Connected clients:");
                connectedClients.forEach((client) -> {
                    System.out.println("ID: " + client.id + ", Role: " + client.clientRole);
                });
            }
        } finally {
            lock.unlock();
        }
    }

    static class ClientInfo {

        int id;
        String role;

        ClientInfo(int id, String role) {
            this.id = id;
            this.role = role;
        }
    }

    static class ClientHandler implements Runnable {

        private Socket socket;
        int id;
        String clientRole;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            id = 0;
            clientRole = null;
            // Initialize necessary services and database interactions here
        }

        @Override
        public void run() {
            try {
                // Read client input from socket and send appropriate responses
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                //ObjectInputStream oin = new ObjectInputStream(socket.getInputStream());
                //ObjectOutputStream oout = new ObjectOutputStream(socket.getOutputStream());

                String roleMessage = in.readLine(); // Read the role and id message
                String[] messages = roleMessage.split("\\|");
                clientRole = messages[0];
                id = Integer.parseInt(messages[1]);

                System.out.println("Client connected: ID " + id + " " + clientRole + " " + socket.getInetAddress().getHostAddress());

                // Add the client to the connectedClients list
                lock.lock();
                try {
                    connectedClients.add(this);
                } finally {
                    lock.unlock();
                }

                String request;
                // Loop to read and process client commands
                while ((request = in.readLine()) != null) {
                    String[] parts = request.split("\\|");
                    String command = parts[0];
                    if (null != command) // Handle different commands
                    {
                        switch (command) {
                            case "PLACE_ORDER":
                                Order order = new Order(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Double.parseDouble(parts[4]));
                                lock.lock();
                                try {
                                    new DB().updateQuery("INSERT INTO ActiveUserRequest (ProductID, Quantity, Price, Date, BranchID) VALUES (?, ?, ?, GETDATE(), ?)", Integer.toString(order.getProductID()), Integer.toString(order.getQuantity()), Double.toString(order.getPrice()), Integer.toString(order.getBranchID()));
                                    int newOrderID = -1;
                                    ResultSet rs = new DB().executeQuery("SELECT MAX(OrderID) AS NewOrderID FROM ActiveUserRequest");
                                    if (rs.next()) {
                                        newOrderID = rs.getInt("NewOrderID");
                                    }
                                    new DB().updateQuery("INSERT INTO UnresolvedDelivary (OrderID, ProductID, Quantity, Date, BranchID) VALUES (?, ?, ?, GETDATE(), ?)", Integer.toString(newOrderID), Integer.toString(order.getProductID()), Integer.toString(order.getQuantity()), Integer.toString(order.getBranchID()));
                                    out.println("ORDER_PLACED");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    out.println("ORDER_FAILED");
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "UPDATE_ORDER":
                                lock.lock();
                                try {
                                    ResultSet rs = new DB().executeQuery("SELECT * FROM Product WHERE ModelName = '" + parts[3] + "'");
                                    Product product = null;
                                    while (rs.next()) {
                                        product = new Product(rs.getInt("ModelID"), rs.getString("ModelName"), rs.getString("Category"), rs.getInt("Quantity"), rs.getDouble("Price"));
                                    }
                                    if (product != null) {
                                        if (product.getQuantity() < Integer.parseInt(parts[4])) {
                                            out.println("ORDER_COULD_NOT_UPDATE");
                                        } else {
                                            new DB().updateQuery("UPDATE ActiveUserRequest SET Price = TRY_CONVERT(DECIMAL(20,2), ?) * ?, Quantity = ? WHERE OrderID = ?", Double.toString(product.getPrice()), parts[4], parts[4], parts[2]);
                                            new DB().updateQuery("UPDATE UnresolvedDelivary SET Quantity = ? WHERE OrderID = ?", parts[4], parts[2]);
                                            out.println("ORDER_UPDATED");
                                        }
                                    } else {
                                        out.println("PRODUCT_NOT_FOUND");
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "DELECT_ORDER":
                                lock.lock();
                                try {
                                    new DB().updateQuery("DELETE FROM UnresolvedDelivary WHERE OrderID = ?", parts[2]);
                                    new DB().updateQuery("DELETE FROM ActiveUserRequest WHERE OrderID = ?", parts[2]);
                                    out.println("DELETION_SUCCESS");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    out.println("DELETION_FAILED");
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "ADD_PRODUCT":
                                lock.lock();
                                try {
                                    new DB().updateQuery("INSERT INTO Product (ModelName, Category, Quantity, Price) values (?, ?, ?, ?)", parts[2], parts[3], parts[4], parts[5]);
                                    out.println("PRODUCT_ADDED");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    out.println("PRODUCT_ADD_FAILED");
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "UPDATE_PRODUCT":
                                lock.lock();
                                try {
                                    new DB().updateQuery("UPDATE Product SET Price = ?, Quantity = ? WHERE ModelName = ?", parts[3], parts[4], parts[2]);
                                    out.println("PRODUCT_UPDATED");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    out.println("PRODUCT_UPDATE_FAILED");
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "DELETE_PRODUCT":
                                lock.lock();
                                try {
                                    new DB().updateQuery("DELETE FROM Product WHERE ModelID = ?", parts[2]);
                                    out.println("PRODUCT_DELETION_SUCCESSFUL");
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                    out.println("PRODUCT_DELETION_FAILED");
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            case "RESPOND_ORDER":
                                int pQuantity,
                                 oQuantity;
                                lock.lock();
                                try {
                                    if ("accepted".equals(parts[4])) {
                                        ResultSet rs = new DB().executeQuery("SELECT Quantity FROM Product WHERE ModelName = '" + parts[3] + "'");
                                        if (rs.next()) {
                                            pQuantity = Integer.parseInt(rs.getString("Quantity"));
                                            rs = new DB().executeQuery("SELECT Quantity FROM ActiveUserRequest WHERE OrderID = '" + parts[2] + "'");
                                            if (rs.next()) {
                                                oQuantity = Integer.parseInt(rs.getString("Quantity"));
                                                if (oQuantity < pQuantity) {
                                                    new DB().updateQuery("UPDATE ActiveUserRequest SET Status = 'accepted' WHERE OrderID = ?", parts[2]);
                                                    new DB().updateQuery("INSERT INTO ResolvedDelivary (OrderID, ProductID, Quantity, Price, Date, BranchID)\n" + "SELECT OrderID, ProductID, Quantity, Price, Date, BranchID\n" + "FROM ActiveUserRequest\n" + "WHERE OrderID = ?", parts[2]);
                                                    new DB().updateQuery("DELETE FROM UnresolvedDelivary WHERE OrderID = ?", parts[2]);
                                                    pQuantity = pQuantity - oQuantity;
                                                    new DB().updateQuery("UPDATE Product SET Quantity = ? WHERE ModelName = ?", Integer.toString(pQuantity), parts[3]);
                                                    out.println("ORDER_ACCEPTED_SUCCESSFULLY");
                                                } else {
                                                    out.println("ORDER_ACCEPT_FAILED");
                                                }
                                            }
                                        }
                                    } else {
                                        try {
                                            new DB().updateQuery("UPDATE ActiveUserRequest SET Status = 'declined' WHERE OrderID = ?", parts[2]);
                                            new DB().updateQuery("INSERT INTO ResolvedDelivary (OrderID, ProductID, Quantity, Price, Date, BranchID)\n" + "SELECT OrderID, ProductID, Quantity, Price, Date, BranchID\n" + "FROM ActiveUserRequest\n" + "WHERE OrderID = ?", parts[2]);
                                            new DB().updateQuery("DELETE FROM UnresolvedDelivary WHERE OrderID = ?", parts[2]);
                                            out.println("ORDER_DECLINED_SUCCESSFULLY");
                                        } catch (SQLException ex) {
                                            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                            out.println("ORDER_DECLINE_FAILED");
                                        }
                                    }
                                } catch (SQLException ex) {
                                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                                } finally {
                                    lock.unlock();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (IOException ex) {
                //System.out.println("Client disconnected: ID " + id + " " + clientRole);
                //System.err.println("IOException occurred for client: ID " + id + " " + clientRole);
            } finally {
                try {
                    socket.close();
                    connectedClients.remove(this);
                    System.out.println("Client disconnected: ID " + id + " Role " + clientRole);
                } catch (IOException ex) {
                    Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

}
