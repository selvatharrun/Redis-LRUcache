**Java Redis Clone & Custom LRU Cache**

A lightweight, from-scratch implementation of a Redis-like key-value store in Java, designed to bridge the gap between high-level application development and low-level systems engineering. This project serves as a practical exploration of Systems Programming, Network Protocols, and Low-Level Data Structures, moving beyond standard library abstractions to understand how databases work under the hood.

Currently, the server implements a robust parser for the RESP (Redis Serialization Protocol) and utilizes a thread-safe, custom-built LRU (Least Recently Used) Cache as its primary storage engine, ensuring efficient memory management and rapid access times.
🚀 Key Features

**Custom LRU Cache Implementation:**

Hand-Rolled Data Structures: Replaces standard Java Collections (like LinkedHashMap) with a manual implementation of a Doubly Linked List combined with a HashMap. This provides granular control over node manipulation and memory usage.

Performance Optimization: Achieves strict O(1) time complexity for both GET and SET operations by maintaining direct pointers to nodes, eliminating the need for traversal during access or updates.

Smart Eviction: Implements an automatic eviction policy that instantly removes the least recently accessed item when the defined capacity is exceeded, ensuring the cache never overflows its memory bounds.

**RESP Protocol Parser:**

Native Byte Parsing: Implements a raw byte stream parser for the Redis Serialization Protocol (RESP). Instead of relying on external libraries, the parser manually interprets RESP data types such as Arrays (*) and Bulk Strings ($).

Client Compatibility: Fully compatible with the official redis-cli, allowing standard Redis tools to interact with this Java server seamlessly.

**Networking Layer:**

Current Architecture: Utilizes a Blocking I/O model (ServerSocket) which handles client requests sequentially. This serves as a baseline for understanding TCP connection lifecycles.
Future Architecture (NIO): actively migrating to Java NIO (Non-blocking I/O). The goal is to implement a single-threaded Event Loop using Selector, mimicking the high-concurrency architecture used by the real Redis to handle thousands of connections efficiently.

**Dependency Injection & Architecture:**

Decoupled Components: The storage logic (LRUCache) is injected into the command execution layer (CommandHandler), avoiding tight coupling and global state.

Testability: This architectural choice facilitates easier unit testing by allowing mock caches to be swapped in during the testing phase.
🧠 Technical Deep Dive

**1. The Storage Engine (LRU Cache)**

Instead of wrapping the convenient java.util.LinkedHashMap, I implemented the eviction logic manually. This approach demonstrates a deep understanding of how pointers and hash lookups interact to create an efficient cache.
Internal Structure: The cache consists of two primary components:
HashMap<String, Node>: Provides constant-time O(1) access to any node given its key.
Doubly Linked List: Maintains the order of access. The head represents the most recently used item, while the tail represents the candidate for eviction.
The 'Node' Class: A custom inner class holding the Key, Value, and pointers to Prev and Next nodes.
Eviction Policy: When a SET operation occurs and size > capacity:
The tail node (least recently used) is identified.
It is unlinked from the Doubly Linked List.
Its key is removed from the HashMap.

Access Pattern: Every time a key is accessed via GET or updated via SET, the corresponding node is "promoted" to the head of the list, ensuring it is safe from immediate eviction.
Operation
Complexity
Description
Get
O(1)
Hash lookup + Pointer manipulation (move to head)
Set
O(1)
Hash lookup/insert + List insertion/deletion
Evict
O(1)
Removal of Tail Node

**2. Supported Commands**

The server currently supports a subset of Redis commands:
PING - Returns PONG.
ECHO [message] - Returns the message.
SET [key] [value] - Stores key-value pair (moves to head of LRU).
GET [key] - Retrieves value (moves to head of LRU) or returns nil.
🛠️ Installation & Usage
Prerequisites
Java JDK 8 or higher.
Netcat (nc) for manual testing (optional).
Building the Project
# Compile all source files
javac server/*.java


Running the Server
# Start the server (Listens on Port 6379)
java server.Main


Running the Test Client
I have included an automated client that tests the LRU eviction logic. It simulates a client populating the cache beyond its capacity and verifies that the oldest keys are correctly evicted.
java server.Client


📂 Project Structure
selvatharrun/Redis-LRUcache
├── README.md
└── server
    ├── Main.java           # Entry point; initializes the ServerSocket and injects dependencies.
    ├── Client.java         # Automated test script that validates eviction logic.
    ├── LRUCache.java       # The core data structure (HashMap + Doubly Linked List).
    ├── CommandHandler.java # Business logic; routes commands (GET/SET) to the cache.
    └── RespHandler.java    # Protocol Parser; converts raw InputStreams into List<String>.


🗺️ Roadmap & Future Optimizations
This project is following the "Build Your Own Redis" learning path.
[x] Phase 1: Basics (TCP, Protocol Parsing, Blocking I/O)
[x] Phase 2: Storage (Custom LRU Implementation)
[ ] Phase 3: Event Loop (Migrate from Blocking I/O to Java NIO Selectors)
[ ] Phase 4: Optimization (Buffer handling and pipelining)
👤 Author
Selvatharrun Building to learn. Coding for performance.
