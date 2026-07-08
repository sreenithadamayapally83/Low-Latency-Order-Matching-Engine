import java.util.*;

public class OrderBook {
    // Buy orders: sorted HIGHEST price to lowest
    private final TreeMap<Double, LinkedList<Order>> buyOrders = new TreeMap<>(Collections.reverseOrder());

    // Sell orders: sorted LOWEST price to highest
    private final TreeMap<Double, LinkedList<Order>> sellOrders = new TreeMap<>();

    // Add an order to the book if it can't be matched immediately
    public void addOrder(Order order) {
        TreeMap<Double, LinkedList<Order>> book = (order.getSide() == Order.Side.BUY) ? buyOrders : sellOrders;

        // If this price level doesn't exist yet, create a new queue for it
        book.putIfAbsent(order.getPrice(), new LinkedList<>());
        // Add the order to the end of the queue (Time Priority)
        book.get(order.getPrice()).add(order);
    }

    // Getters to look inside the book
    public TreeMap<Double, LinkedList<Order>> getBuyOrders() { return buyOrders; }
    public TreeMap<Double, LinkedList<Order>> getSellOrders() { return sellOrders; }
}