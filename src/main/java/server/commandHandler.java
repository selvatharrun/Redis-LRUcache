package server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.List;
import server.LRUcache;

public class commandHandler {

    //concurrent hashmap since java is going to do multi threading. which potentially means we are fucked if we used a hashmap.
    //conc hashmap lets us add shit on multiple requests simultaneously.

    static LRUcache lru = new LRUcache(5);


//    public final static Map<String, String> map = new ConcurrentHashMap<>();


    public static String exec(List<String> arr){
        String function = arr.get(0);

        switch(function){
            case "PING":
                return "+PONG\r\n";

            case "ECHO":
                // ECHO hello -> returns "hello" as a Bulk String
                if (arr.size() < 2) return "-ERR wrong number of arguments for 'echo'\r\n";
                return serializeBulkString(arr.get(1));

            case "SET":
                if(arr.size() < 3){
                    return "-ERR wrong number of arguments \r\n";
                }
                lru.set(arr.get(1) , arr.get(2));
//                map.put(arr.get(1) , arr.get(2));
                return "+OK\r\n";

            case "GET":
                if(arr.size() < 2){
                    return "-ERR wrong number of arguments \r\n";
                }
//                String x = map.get(arr.get(1));
                String x = lru.get(arr.get(1));
                if(x == null){
                    return "$-1\r\n";
                }

                return serializeBulkString(x);

            default:
                return "-ERR unknown command\r\n";
        }
    }

    private static String serializeBulkString(String response){
        return "$"+response.length()+ "\r\n" + response + "\r\n";
    }
}