package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static void main(String[] args) {
        int port = 6379;
        System.out.println("Logs from your Redis Clone:");

        // 1. Bind to port 6379
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            // This allows us to restart the server immediately without waiting for the port to free up
            serverSocket.setReuseAddress(true);
            System.out.println("Server is listening on port " + port);

            // 2. Wait for a connection (Blocking call)
            while (true) {
                Socket clientSocket = serverSocket.accept();
                java.io.InputStream in = clientSocket.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead = in.read(buffer);

                if(bytesRead > 0){
                    String msg = new String(buffer,0,bytesRead);
                    System.out.println(msg);
                    System.out.println("Debug View: " + msg.replace("\r", "\\r").replace("\n", "\\n"));
                }

                // In RESP, Simple Strings start with "+" and end with "\r\n"
                OutputStream out = clientSocket.getOutputStream();
                String response = "+PONG\r\n";
                out.write(response.getBytes());
                out.flush();

                // Close immediately for now
                clientSocket.close();

            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}