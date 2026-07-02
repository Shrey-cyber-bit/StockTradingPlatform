import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class StockTradingConsoleApp {

    private static final double STARTING_CASH = 100000.00;
    private static Market market;
    private static Portfolio portfolio;
    private static Scanner scanner;

    public static void main(String[] args) {
        market = new Market();
        portfolio = FileManager.loadPortfolio(STARTING_CASH);
        scanner = new Scanner(System.in);

        market.refreshMarket();
        printBanner();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    viewMarket();
                    break;
                case "2":
                    buyStock();
                    break;
                case "3":
                    sellStock();
                    break;
                case "4":
                    viewPortfolio();
                    break;
                case "5":
                    viewHistory();
                    break;
                case "6":
                    market.refreshMarket();
                    System.out.println("\nMarket prices refreshed.\n");
                    break;
                case "7":
                    FileManager.savePortfolio(portfolio);
                    System.out.println("\nPortfolio saved. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("\nInvalid choice. Please enter a number from 1-7.\n");
            }
        }

        scanner.close();
    }

    private static void printBanner() {
        
        System.out.printf("Starting Cash Balance: Rs.%.2f%n", portfolio.getCashBalance());
        System.out.println("==================================================");
    }

    private static void printMenu() {
        System.out.println("\n--------------- MAIN MENU ---------------");
        System.out.println("1. View Market Data");
        System.out.println("2. Buy Stock");
        System.out.println("3. Sell Stock");
        System.out.println("4. View Portfolio");
        System.out.println("5. View Transaction History");
        System.out.println("6. Refresh Market Prices");
        System.out.println("7. Save & Exit");
        System.out.println("------------------------------------------");
        System.out.print("Enter your choice: ");
    }

    private static void viewMarket() {
        System.out.println("\n----------------------- LIVE MARKET DATA -----------------------");
        System.out.printf("%-6s %-22s %-12s %-10s %-10s%n", "Symbol", "Company", "Price", "Change", "Change %");
        for (Stock s : market.getAllStocks()) {
            String arrow = s.getChangeAmount() >= 0 ? "+" : "";
            System.out.printf("%-6s %-22s Rs.%-9.2f %s%-9.2f %s%.2f%%%n",
                    s.getSymbol(), s.getCompanyName(), s.getCurrentPrice(),
                    arrow, s.getChangeAmount(), arrow, s.getChangePercent());
        }
        System.out.println("-------------------------------------------------------------------");
    }

    private static void buyStock() {
        System.out.print("\nEnter stock symbol to buy: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Unknown symbol: " + symbol);
            return;
        }

        System.out.printf("Current price of %s: Rs.%.2f%n", stock.getSymbol(), stock.getCurrentPrice());
        System.out.print("Enter quantity to buy: ");
        int quantity = readInt();
        if (quantity == -1) return;

        String result = portfolio.buy(stock, quantity);
        System.out.println(result);
        FileManager.savePortfolio(portfolio);
    }

    private static void sellStock() {
        System.out.print("\nEnter stock symbol to sell: ");
        String symbol = scanner.nextLine().trim().toUpperCase();
        Stock stock = market.getStock(symbol);
        if (stock == null) {
            System.out.println("Unknown symbol: " + symbol);
            return;
        }

        System.out.printf("Current price of %s: Rs.%.2f%n", stock.getSymbol(), stock.getCurrentPrice());
        System.out.print("Enter quantity to sell: ");
        int quantity = readInt();
        if (quantity == -1) return;

        String result = portfolio.sell(stock, quantity);
        System.out.println(result);
        FileManager.savePortfolio(portfolio);
    }

    private static void viewPortfolio() {
        System.out.println("\n------------------------- YOUR PORTFOLIO -------------------------");
        System.out.printf("Cash Balance:    Rs.%.2f%n", portfolio.getCashBalance());
        System.out.printf("Holdings Value:  Rs.%.2f%n", portfolio.getHoldingsValue(market));
        System.out.printf("Net Worth:       Rs.%.2f%n", portfolio.getNetWorth(market));
        System.out.println("--------------------------------------------------------------------");

        Map<String, Holding> holdings = portfolio.getHoldings();
        if (holdings.isEmpty()) {
            System.out.println("You currently hold no stocks.");
        } else {
            System.out.printf("%-6s %-6s %-14s %-14s %-10s%n", "Symbol", "Qty", "Avg Buy Price", "Current Price", "P/L (Rs.)");
            for (Holding h : holdings.values()) {
                Stock s = market.getStock(h.getSymbol());
                double currentPrice = (s != null) ? s.getCurrentPrice() : 0;
                double pl = h.getProfitLoss(currentPrice);
                String sign = pl >= 0 ? "+" : "";
                System.out.printf("%-6s %-6d Rs.%-11.2f Rs.%-11.2f %s%.2f%n",
                        h.getSymbol(), h.getQuantity(), h.getAverageBuyPrice(), currentPrice, sign, pl);
            }
        }
        System.out.println("--------------------------------------------------------------------");
    }

    private static void viewHistory() {
        System.out.println("\n------------------------ TRANSACTION HISTORY ------------------------");
        List<Transaction> history = portfolio.getHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            for (int i = history.size() - 1; i >= 0; i--) {
                System.out.println(history.get(i));
            }
        }
        System.out.println("-----------------------------------------------------------------------");
    }
        private static int readInt() {
        String input = scanner.nextLine().trim();
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) {
                System.out.println("Quantity must be a positive whole number.");
                return -1;
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number: " + input);
            return -1;
        }
    }
}
