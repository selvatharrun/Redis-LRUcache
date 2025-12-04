# Java Redis Clone & Custom LRU Cache

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Systems Programming](https://img.shields.io/badge/Systems-Programming-black?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Active%20Dev-green?style=for-the-badge)

A lightweight, from-scratch implementation of a Redis-like key-value store in Java.

This project bridges the gap between high-level application development and low-level systems engineering. It serves as a practical exploration of **Network Protocols**, **Byte-Level Parsing**, and **Low-Level Data Structures**, moving beyond standard library abstractions to understand how databases function under the hood.

---

## 🚀 Key Features

### ⚡ Custom LRU Cache Implementation
* **Hand-Rolled Data Structures:** Replaces standard Java Collections (like `LinkedHashMap`) with a manual implementation of a **Doubly Linked List** combined with a **HashMap**. This provides granular control over node manipulation and memory usage.
* **O(1) Performance:** Achieves strict constant time complexity for both `GET` and `SET` operations by maintaining direct pointers to nodes.
* **Smart Eviction:** Automatically identifies and unlinks the least recently accessed item when capacity is exceeded, ensuring memory safety.

### 🔌 RESP Protocol Parser
* **Native Byte Parsing:** Implements a raw byte stream parser for the **Redis Serialization Protocol (RESP)**.
* **No External Deps:** Manually interprets RESP data types such as Arrays (`*`) and Bulk Strings (`$`) without relying on helper libraries.
* **Client Compatibility:** Fully compatible with the official `redis-cli`, allowing standard Redis tools to interact with this server seamlessly.

### 🌐 Networking Layer
* **Current:** Blocking I/O model (`ServerSocket`) handling sequential client requests to establish TCP baselines.
* **Future:** Active migration to **Java NIO (Non-blocking I/O)**. The goal is to implement a single-threaded Event Loop using `Selector` to mimic the high-concurrency architecture of native Redis.

### 🏗️ Architecture & DI
* **Decoupled Components:** Storage logic (`LRUCache`) is injected into the command layer (`CommandHandler`) to avoid global state.
* **Testability:** Designed for unit testing with swappable mock caches.

---

## 🧠 Technical Deep Dive: The LRU Storage Engine

Instead of wrapping `java.util.LinkedHashMap`, the eviction logic is implemented manually to demonstrate pointer manipulation and hash lookups.

### Internal Structure
The cache utilizes two primary components working in tandem:
1.  **HashMap<String, Node>:** Provides O(1) access to any node via its key.
2.  **Doubly Linked List:** Maintains access order. The **Head** is the most recently used; the **Tail** is the candidate for eviction.

```mermaid
graph TD;
    subgraph HashMap
    K1[Key: "user:1"]
    K2[Key: "user:2"]
    end

    subgraph Doubly Linked List
    N1[Node: "user:1"]
    N2[Node: "user:2"]
    N3[Node: "user:3"]
    end

    K1 --> N1
    K2 --> N2
    N1 <--> N2
    N2 <--> N3
    
    style N1 fill:#bbf,stroke:#333,stroke-width:2px,color:black
    style N3 fill:#f96,stroke:#333,stroke-width:2px,color:black
