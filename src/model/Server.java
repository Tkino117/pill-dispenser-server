package model;

import controller.cli.CLI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable{
    private final int port;
    private final CLI cli;
    private PrintWriter out;
    private volatile boolean running = true;
    public Server(int port, CLI cli) {
        this.port = port;
        this.cli = cli;
    }
    public void sendMessage(String message) {
        if (out != null) {
            out.println(message);
        }
    }
    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (running) {
                // 接続受け入れ
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected: " + clientSocket.getInetAddress());

                    // set Writer and Reader
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new PrintWriter(clientSocket.getOutputStream(), true);

                    // thread for receiving message
                    Thread receiverThread = new Thread(() -> {
                        try {
                            String message;
                            while (running && (message = in.readLine()) != null) {
                                System.out.println("Received message : " + message);
                            }
                        } catch (IOException e) {
                            System.out.println("Connection lost");
//                            e.printStackTrace();
                        }
                    });
                    receiverThread.start();

                    // main loop
                    while (running && !clientSocket.isClosed()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("Closing connection");
                } catch (IOException e) {
                    System.out.println("Connection lost");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
