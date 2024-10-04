import java.net.*;

public class Main {
    public static void main(String[] args) {
        int port = 12345;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            while (true) {
                try (Socket socket = serverSocket.accept()) {
                    System.out.println("New client connected");

                }
            }
        } catch (Exception e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }

    }
}