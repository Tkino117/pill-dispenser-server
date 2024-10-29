import model.Server;

import java.net.*;
import java.io.*;

public class Main {
    public static void main(String[] args) {
        int port = 8080;
        Server server = new Server(port);
        Thread serverThread = new Thread(server);
        serverThread.start();
        while (true) {
            try {
                Thread.sleep(3000);
                server.sendMessage("hello from main");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}