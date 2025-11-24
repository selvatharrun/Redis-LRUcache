package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class respHandler { // Class names always start with Capital

    // We return a List<String> so Main.java can use it
    public List<String> parse(InputStream in) {
        List<String> result = new ArrayList<>();

        try {
            // NOTE: In a real server, we don't create a new BufferedReader every time
            // because it might steal bytes from the next request.
            // For now, it's okay, but we will fix this later.
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line = br.readLine();

            // 1. Check if the stream is empty or closed
            if (line == null || !line.startsWith("*")) {
                return null; // Not a valid RESP array start
            }

            // 2. Parse the number of elements ("*3" -> 3)
            int numArgs = Integer.parseInt(line.substring(1));

            // 3. Loop exactly that many times
            for (int i = 0; i < numArgs; i++) {
                // READ 1: The Length Marker (e.g., "$3")
                String lengthLine = br.readLine();
                if (lengthLine == null) break;

                // READ 2: The Actual Data (e.g., "SET")
                String dataLine = br.readLine();
                if (dataLine == null) break;

                result.add(dataLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }
}