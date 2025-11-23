package server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class client {
    public static void main(String[] args){
        int port = 6379;
        try{
            Socket socket = new Socket("localhost", port);
            OutputStream out = socket.getOutputStream();

            String command = "*3\r\n$3\r\nSET\r\n$5\r\nmykey\r\n$7\r\nmyvalue\r\n";

            System.out.println("Sending: " + command.replace("\r\n", "\\r\\n"));
            out.write(command.getBytes());
            out.flush();

            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
