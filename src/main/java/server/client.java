package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class client {

    // Helper to send a command and print the response
    private static void sendCommand(String label, String command) {
        int port = 6379;
        try (Socket socket = new Socket("localhost", port)) {
            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            System.out.print(label + " -> ");
            out.write(command.getBytes());
            out.flush();

            // Read response
            byte[] buffer = new byte[1024];
            int read = in.read(buffer);
            if (read > 0) {
                System.out.println("Server Replied: " + new String(buffer, 0, read).replace("\r\n", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 1. Fill the Cache (Capacity 5)
        System.out.println("--- FILLING CACHE ---");
        sendCommand("Set A", "*3\r\n$3\r\nSET\r\n$1\r\nA\r\n$1\r\n1\r\n");
        sendCommand("Set B", "*3\r\n$3\r\nSET\r\n$1\r\nB\r\n$1\r\n2\r\n");
        sendCommand("Set C", "*3\r\n$3\r\nSET\r\n$1\r\nC\r\n$1\r\n3\r\n");
        sendCommand("Set D", "*3\r\n$3\r\nSET\r\n$1\r\nD\r\n$1\r\n4\r\n");
        sendCommand("Set E", "*3\r\n$3\r\nSET\r\n$1\r\nE\r\n$1\r\n5\r\n");

        // 2. Trigger Eviction
        System.out.println("\n--- TRIGGER EVICTION ---");
        // Adding 'F' should remove 'A' (the oldest)
        sendCommand("Set F", "*3\r\n$3\r\nSET\r\n$1\r\nF\r\n$1\r\n6\r\n");

        // 3. Verify A is gone
        System.out.println("\n--- VERIFYING A IS GONE ---");
        // Expected: $-1 (Null)
        sendCommand("Get A", "*2\r\n$3\r\nGET\r\n$1\r\nA\r\n");

        // 4. Verify B is still there
        System.out.println("\n--- VERIFYING B IS THERE ---");
        // Expected: $1 2
        sendCommand("Get B", "*2\r\n$3\r\nGET\r\n$1\r\nB\r\n");
    }
}