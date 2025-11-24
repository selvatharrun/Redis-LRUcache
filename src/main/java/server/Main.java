package server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import server.respHandler;
import server.commandHandler;

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

                try(clientSocket){
                    java.io.InputStream in = clientSocket.getInputStream();
                    if(in == null) {
                        clientSocket.close();
                    }

//                byte[] buffer = new byte[1024];
//                int bytesRead = in.read(buffer);
//
//                if(bytesRead > 0){
//                    String msg = new String(buffer,0,bytesRead);
//                    System.out.println(msg);
//                    System.out.println("Debug View: " + msg.replace("\r", "\\r").replace("\n", "\\n"));
//                }

                    // In RESP, Simple Strings start with "+" and end with "\r\n"
                    respHandler rn  = new respHandler();
                    List<String> parsed = rn.parse(in);
                    System.out.println(parsed);


                    commandHandler ch = new commandHandler();
                    String res = ch.exec(parsed);
                    System.out.println(ch.map);


                    //here the output stream is used to talk back to the client, (acknowledge)
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