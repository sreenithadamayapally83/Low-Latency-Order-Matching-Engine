public class Order {
    public enum Side { BUY, SELL }

    private final String orderId;
    private final Side side;
    private final double price;
    private int quantity;

    public Order(String orderId, Side side, double price, int quantity) {
        this.orderId = orderId;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and helper methods
    public String getOrderId() { return orderId; }
    public Side getSide() { return side; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity(int amount) {
        this.quantity -= amount;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | Price: %.2f | Qty: %d", orderId, side, price, quantity);
    }
}