import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum Type { BUY, SELL }

    private final Type type;
    private final String symbol;
    private final int quantity;
    private final double pricePerShare;
    private final LocalDateTime timestamp;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Transaction(Type type, String symbol, int quantity, double pricePerShare) {
        this.type = type;
        this.symbol = symbol;
        this.quantity = quantity;
        this.pricePerShare = pricePerShare;
        this.timestamp = LocalDateTime.now();
    }

    public double getTotalValue() {
        return quantity * pricePerShare;
    }

    public Type getType() {
        return type;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPricePerShare() {
        return pricePerShare;
    }

    @Override
    public String toString() {
        return String.format("[%s] %-4s %-6s x%-5d @ Rs.%-8.2f = Rs.%.2f",
                timestamp.format(FORMATTER), type, symbol, quantity, pricePerShare, getTotalValue());
    }
}
