import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Portfolio implements Serializable {
    private static final long serialVersionUID = 1L;

    private double cashBalance;
    private final Map<String, Holding> holdings;
    private final List<Transaction> history;

    public Portfolio(double startingCash) {
        this.cashBalance = startingCash;
        this.holdings = new HashMap<>();
        this.history = new ArrayList<>();
    }

    public String buy(Stock stock, int quantity) {
        if (quantity <= 0) {
            return "Quantity must be greater than zero.";
        }
        double cost = stock.getCurrentPrice() * quantity;
        if (cost > cashBalance) {
            return String.format("Insufficient funds. Need Rs.%.2f but only Rs.%.2f available.", cost, cashBalance);
        }
        cashBalance -= cost;
        Holding existing = holdings.get(stock.getSymbol());
        if (existing == null) {
            holdings.put(stock.getSymbol(), new Holding(stock.getSymbol(), quantity, stock.getCurrentPrice()));
        } else {
            existing.addShares(quantity, stock.getCurrentPrice());
        }
        history.add(new Transaction(Transaction.Type.BUY, stock.getSymbol(), quantity, stock.getCurrentPrice()));
        return String.format("Bought %d share(s) of %s at Rs.%.2f each. Total: Rs.%.2f",
                quantity, stock.getSymbol(), stock.getCurrentPrice(), cost);
    }

    public String sell(Stock stock, int quantity) {
        Holding holding = holdings.get(stock.getSymbol());
        if (holding == null || holding.getQuantity() < quantity) {
            int owned = (holding == null) ? 0 : holding.getQuantity();
            return String.format("Cannot sell %d share(s) of %s. You only own %d.", quantity, stock.getSymbol(), owned);
        }
        if (quantity <= 0) {
            return "Quantity must be greater than zero.";
        }
        double proceeds = stock.getCurrentPrice() * quantity;
        cashBalance += proceeds;
        boolean nowEmpty = holding.removeShares(quantity);
        if (nowEmpty) {
            holdings.remove(stock.getSymbol());
        }
        history.add(new Transaction(Transaction.Type.SELL, stock.getSymbol(), quantity, stock.getCurrentPrice()));
        return String.format("Sold %d share(s) of %s at Rs.%.2f each. Total: Rs.%.2f",
                quantity, stock.getSymbol(), stock.getCurrentPrice(), proceeds);
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Map<String, Holding> getHoldings() {
        return holdings;
    }

    public List<Transaction> getHistory() {
        return history;
    }

    public double getHoldingsValue(Market market) {
        double total = 0;
        for (Holding h : holdings.values()) {
            Stock s = market.getStock(h.getSymbol());
            if (s != null) {
                total += h.getCurrentValue(s.getCurrentPrice());
            }
        }
        return total;
    }

    public double getNetWorth(Market market) {
        return cashBalance + getHoldingsValue(market);
    }
}
