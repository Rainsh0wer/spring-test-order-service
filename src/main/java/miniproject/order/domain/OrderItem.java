package miniproject.order.domain;

public class OrderItem {
    private final String productId;
    private final int quantity;
    private final double price;

    public OrderItem(String productId, int quantity, double price) {
        if (productId == null || productId.trim().isEmpty()) {
            throw new IllegalArgumentException("ProductId must not be empty");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }
}
