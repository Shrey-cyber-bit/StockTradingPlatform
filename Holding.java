import java.io.Serializable;

public class Holding implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String symbol;
    private int quantity;
    private double averageBuyPrice;

    public Holding(String symbol, int quantity, double averageBuyPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
    }

 
    public void addShares(int additionalQuantity, double buyPrice) {
        double totalCost = (quantity * averageBuyPrice) + (additionalQuantity * buyPrice);
        quantity += additionalQuantity;
        averageBuyPrice = totalCost / quantity;
    }

    public boolean removeShares(int quantityToRemove) {
        quantity -= quantityToRemove;
        return quantity <= 0;
    }

    public double getInvestedValue() {
        return quantity * averageBuyPrice;
    }

    public double getCurrentValue(double currentPrice) {
        return quantity * currentPrice;
    }

    public double getProfitLoss(double currentPrice) {
        return getCurrentValue(currentPrice) - getInvestedValue();
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAverageBuyPrice() {
        return averageBuyPrice;
    }
}
