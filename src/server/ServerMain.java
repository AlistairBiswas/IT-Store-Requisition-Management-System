/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Rafeed
 */
public class ServerMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //int port;
        int port = 8000;
        /*if (args.length != 1) {
            System.err.println("Usage: java ServerMain <port>");
            return;
        }
        port = Integer.parseInt(args[0]);*/
        Server server = new Server(port);
        // Start the CLI functionality in a separate thread
        Thread cliThread = new Thread(() -> {
            server.startCLI();
        });
        cliThread.start();
        server.initializeClient();
    }

}
