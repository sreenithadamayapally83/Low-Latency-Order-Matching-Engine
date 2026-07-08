import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) {
        MatchingEngine engine = new MatchingEngine();
        int totalOrders = 1000;

        // Thread-safe array to record the latency of every single order
        long[] latencies = new long[totalOrders];
        AtomicInteger counter = new AtomicInteger(0);

        ExecutorService executor = Executors.newFixedThreadPool(4);

        System.out.println("Starting Performance-Tracked Matching Simulation...");

        for (int i = 0; i < totalOrders; i++) {
            final int id = i;
            executor.submit(() -> {
                Order.Side side = (id % 2 == 0) ? Order.Side.BUY : Order.Side.SELL;
                double price = 100.0 + (id % 5);
                int quantity = 10 + (id % 10);

                Order order = new Order("ORD_" + id, side, price, quantity);

                // --- BENCHMARK START ---
                long startTime = System.nanoTime();
                engine.processOrder(order);
                long endTime = System.nanoTime();
                // --- BENCHMARK END ---

                long latency = endTime - startTime;

                // Store latency in the array safely using an atomic index
                int index = counter.getAndIncrement();
                if (index < totalOrders) {
                    latencies[index] = latency;
                }
            });
        }

        executor.shutdown();
        try {
            if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                // Sort latencies to calculate percentiles
                Arrays.sort(latencies);

                // Calculate metrics
                long p50 = latencies[(int) (totalOrders * 0.50)];
                long p95 = latencies[(int) (totalOrders * 0.95)];
                long p99 = latencies[(int) (totalOrders * 0.99)];
                double average = Arrays.stream(latencies).average().orElse(0.0);

                System.out.println("ENGINE LATENCY REPORT (in Nanoseconds)");
                System.out.printf("Average Latency: %.2f ns\n", average);
                System.out.printf("p50 (Median)   : %d ns\n", p50);
                System.out.printf("p95 Percentile : %d ns\n", p95);
                System.out.printf("p99 Percentile : %d ns\n", p99);
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted.");
        }
    }
}