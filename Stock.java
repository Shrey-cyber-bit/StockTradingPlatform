import java.io.Serializable;
import java.util.Random;


public class Stock implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String symbol;
    private final String companyName;
    private double currentPrice;
    private double previousClose;

    private static final Random RANDOM = new Random();

    public Stock(String symbol, String companyName, double startingPrice) {
        this.symbol = symbol;
        this.companyName = companyName;
        this.currentPrice = startingPrice;
        this.previousClose = startingPrice;
    }

    public void refreshPrice() {
        previousClose = currentPrice;
        double changePercent = (RANDOM.nextDouble() * 6.0) - 3.0; 
        double newPrice = currentPrice * (1 + changePercent / 100.0);
        currentPrice = Math.round(newPrice * 100.0) / 100.0;
        if (currentPrice < 1.0) {
            currentPrice = 1.0; 
        }
    }

    public double getChangeAmount() {
        return Math.round((currentPrice - previousClose) * 100.0) / 100.0;
    }

    public double getChangePercent() {
        if (previousClose == 0) return 0;
        return Math.round(((currentPrice - previousClose) / previousClose) * 10000.0) / 100.0;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    @Override
    public String toString() {
        String arrow = getChangeAmount() >= 0 ? "+" : "";
        return String.format("%-6s %-22s Rs.%-10.2f %s%.2f (%s%.2f%%)",
                symbol, companyName, currentPrice, arrow, getChangeAmount(), arrow, getChangePercent());
    }
}
