package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int port = 6379;
        System.out.println("Logs from your Redis Clone:");

        // 1. SETUP: Create the Handler ONCE.
        // This ensures the Cache persists across different clients.
        commandHandler ch = new commandHandler();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            serverSocket.setReuseAddress(true);
            System.out.println("Server is listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                // Try-with-resources ensures socket closes automatically
                try (clientSocket) {
                    java.io.InputStream in = clientSocket.getInputStream();
                    if (in == null) continue;

                    // 2. PARSE
                    respHandler rn = new respHandler();
                    List<String> parsed = rn.parse(in);
                    System.out.println("Command received: " + parsed);

                    // 3. EXECUTE (Using the persistent handler)
                    String res = ch.exec(parsed);

                    // 4. RESPOND
                    OutputStream out = clientSocket.getOutputStream();
                    out.write(res.getBytes());
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.out.println("Server exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
}