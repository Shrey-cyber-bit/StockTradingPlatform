import java.util.ArrayList;
import java.util.List;

public class Market {
    private final List<Stock> stocks;

    public Market() {
        stocks = new ArrayList<>();
        seedStocks();
    }

    private void seedStocks() {
        stocks.add(new Stock("TCS", "Tata Consultancy Services", 3850.00));
        stocks.add(new Stock("INFY", "Infosys Ltd.", 1550.00));
        stocks.add(new Stock("RELI", "Reliance Industries", 2950.00));
        stocks.add(new Stock("HDFC", "HDFC Bank Ltd.", 1620.00));
        stocks.add(new Stock("WIPRO", "Wipro Ltd.", 480.00));
        stocks.add(new Stock("ITC", "ITC Ltd.", 435.00));
        stocks.add(new Stock("SBIN", "State Bank of India", 810.00));
        stocks.add(new Stock("TATAM", "Tata Motors Ltd.", 950.00));
    }

    public void refreshMarket() {
        for (Stock s : stocks) {
            s.refreshPrice();
        }
    }

    public List<Stock> getAllStocks() {
        return stocks;
    }

    public Stock getStock(String symbol) {
        for (Stock s : stocks) {
            if (s.getSymbol().equalsIgnoreCase(symbol)) {
                return s;
            }
        }
        return null;
    }
}
