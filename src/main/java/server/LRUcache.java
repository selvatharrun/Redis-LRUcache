package server;

import java.util.HashMap;
import java.util.Map;

public class LRUcache {
    private final int maxCap;
    private final Map<String, Node> map;
    private Node head;
    private Node tail;

    public LRUcache(int capacity) {
        this.maxCap = capacity;
        this.map = new HashMap<>();
    }

    // Internal Node class
    private static class Node {
        String key;
        String value;
        Node prev;
        Node next;

        Node(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }

    // --- Helper Methods ---

    private void addToHead(Node node) {
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            node.prev = null; // Ensure new head has no prev
            head = node;
        }
    }

    private void removeNode(Node node) {
        if (node == null) return;

        // 1. Handle Head/Tail pointers
        if (node == head) head = node.next;
        if (node == tail) tail = node.prev;

        // 2. Stitch the neighbors together
        if (node.prev != null) node.prev.next = node.next;
        if (node.next != null) node.next.prev = node.prev;

        // 3. Cleanup the removed node's pointers (Good for GC)
        node.prev = null;
        node.next = null;
    }

    // --- Public API ---

    public synchronized void set(String key, String value) {
        if (map.containsKey(key)) {
            Node node = map.get(key);
            node.value = value;
            // Move to front (Refresh)
            removeNode(node);
            addToHead(node);
            return;
        }

        // Check capacity BEFORE adding
        if (map.size() >= maxCap) {
            // Remove from Map AND List
            if (tail != null) {
                map.remove(tail.key);
                removeNode(tail);
            }
        }

        Node newNode = new Node(key, value);
        addToHead(newNode);
        map.put(key, newNode);
    }

    public synchronized String get(String key) {
        if (!map.containsKey(key)) {
            return null;
        }

        Node node = map.get(key);
        // Move to front (Refresh)
        removeNode(node);
        addToHead(node);

        return node.value;
    }
}