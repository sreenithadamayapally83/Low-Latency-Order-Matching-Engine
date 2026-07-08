# Low-Latency Multi-Threaded Order Matching Engine

A high-performance, concurrent financial order matching engine built in Java. The engine uses price-time priority (FIFO) scheduling to match incoming limit/market orders across multiple simulated market participants.

## Performance Metrics (Captured under load)
* **Average Latency:** 230,722.60 ns (~230 microseconds)
* **p50 (Median Latency):** 75,000 ns (75 microseconds)
* **p99 (Worst-case Latency):** 1,934,000 ns (1.93 milliseconds)

## Tech Stack & Key Concepts
* **Language:** Java 17
* **Data Structures:** Custom OrderBook using `TreeMap` (for automated O(log N) price sorting) and `LinkedList` (for FIFO time priority allocation).
* **Concurrency:** Thread-safe execution using `synchronized` blocks and `ExecutorService` thread pools to simulate active concurrent market traffic safely without data corruption.
