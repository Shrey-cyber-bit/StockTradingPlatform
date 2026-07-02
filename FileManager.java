 import java.io.*;

/**
 * Handles persisting the Portfolio object to disk and loading it back,
 * so a user's portfolio survives between program runs.
 */
public class FileManager {
    private static final String SAVE_FILE = "portfolio.dat";

    public static void savePortfolio(Portfolio portfolio) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            out.writeObject(portfolio);
        } catch (IOException e) {
            System.out.println("Warning: could not save portfolio to disk (" + e.getMessage() + ")");
        }
    }

    public static Portfolio loadPortfolio(double defaultStartingCash) {
        File file = new File(SAVE_FILE);
        if (!file.exists()) {
            return new Portfolio(defaultStartingCash);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Portfolio) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Warning: could not load saved portfolio, starting fresh (" + e.getMessage() + ")");
            return new Portfolio(defaultStartingCash);
        }
    }
}
