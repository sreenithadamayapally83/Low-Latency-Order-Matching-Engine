import java.util.LinkedList;
import java.util.TreeMap;

public class MatchingEngine {
    private final OrderBook orderBook = new OrderBook();

    // The core matching logic
    public synchronized void processOrder(Order newOrder) {
        if (newOrder.getSide() == Order.Side.BUY) {
            matchOrder(newOrder, orderBook.getSellOrders());
        } else {
            matchOrder(newOrder, orderBook.getBuyOrders());
        }

        // If the order wasn't fully filled by matching, add the remainder to the book
        if (newOrder.getQuantity() > 0) {
            orderBook.addOrder(newOrder);
        }
    }

    private void matchOrder(Order incomingOrder, TreeMap<Double, LinkedList<Order>> oppositeBook) {
        while (incomingOrder.getQuantity() > 0 && !oppositeBook.isEmpty()) {
            // Get the best available price on the opposite side
            double bestOppositePrice = oppositeBook.firstKey();

            // Check if the prices cross (Can we actually trade?)
            if (incomingOrder.getSide() == Order.Side.BUY && incomingOrder.getPrice() < bestOppositePrice) break;
            if (incomingOrder.getSide() == Order.Side.SELL && incomingOrder.getPrice() > bestOppositePrice) break;

            LinkedList<Order> ordersAtPrice = oppositeBook.get(bestOppositePrice);

            while (!ordersAtPrice.isEmpty() && incomingOrder.getQuantity() > 0) {
                Order restingOrder = ordersAtPrice.peek(); // Get the oldest order at this price (FIFO)

                int matchQty = Math.min(incomingOrder.getQuantity(), restingOrder.getQuantity());

                // Execute trade (In a real system, you'd generate a trade execution log here)
                incomingOrder.reduceQuantity(matchQty);
                restingOrder.reduceQuantity(matchQty);

                System.out.println("TRADE EXECUTION: Matched " + matchQty + " units @ $" + bestOppositePrice);

                // If the resting order is fully filled, remove it from the queue
                if (restingOrder.getQuantity() == 0) {
                    ordersAtPrice.poll();
                }
            }

            // If no more orders exist at this price level, clear the price entry completely
            if (ordersAtPrice.isEmpty()) {
                oppositeBook.remove(bestOppositePrice);
            }
        }
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}