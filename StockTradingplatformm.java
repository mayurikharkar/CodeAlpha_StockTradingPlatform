import java.util.*;
import java.io.*;

class Stock {
    private String symbol;
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}

class Transaction {
    private String type;
    private String stock;
    private int quantity;
    private double price;

    public Transaction(String type, String stock, int quantity, double price) {
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return type + " | " + stock + " | Qty: "
                + quantity + " | Price: ₹" + price;
    }
}

class User {
    private String name;
    private double balance;

    private HashMap<String, Integer> portfolio;
    private ArrayList<Transaction> history;

    public User(String name, double balance) {
        this.name = name;
        this.balance = balance;
        portfolio = new HashMap<>();
        history = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double amount) {
        balance += amount;
    }

    public void buyStock(Stock stock, int quantity) {

        double cost = stock.getPrice() * quantity;

        if (cost > balance) {
            System.out.println("Insufficient Balance!");
            return;
        }

        balance -= cost;

        portfolio.put(
                stock.getSymbol(),
                portfolio.getOrDefault(stock.getSymbol(), 0) + quantity
        );

        history.add(new Transaction(
                "BUY",
                stock.getSymbol(),
                quantity,
                stock.getPrice()
        ));

        System.out.println("Stock Purchased Successfully!");
    }

    public void sellStock(Stock stock, int quantity) {

        int owned = portfolio.getOrDefault(stock.getSymbol(), 0);

        if (owned < quantity) {
            System.out.println("Not enough shares!");
            return;
        }

        balance += stock.getPrice() * quantity;

        portfolio.put(
                stock.getSymbol(),
                owned - quantity
        );

        history.add(new Transaction(
                "SELL",
                stock.getSymbol(),
                quantity,
                stock.getPrice()
        ));

        System.out.println("Stock Sold Successfully!");
    }

    public void showPortfolio(Map<String, Stock> market) {

        System.out.println("\n===== PORTFOLIO =====");

        double totalValue = 0;

        for (String stockName : portfolio.keySet()) {

            int qty = portfolio.get(stockName);

            if (qty > 0) {

                double value =
                        qty * market.get(stockName).getPrice();

                totalValue += value;

                System.out.println(
                        stockName +
                                " | Qty: " + qty +
                                " | Value: ₹" + value
                );
            }
        }

        System.out.println("Portfolio Value: ₹" + totalValue);
        System.out.println("Available Balance: ₹" + balance);
    }

    public void showHistory() {

        System.out.println("\n===== TRANSACTION HISTORY =====");

        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
            return;
        }

        for (Transaction t : history) {
            System.out.println(t);
        }
    }

    public void savePortfolio() {

        try {

            PrintWriter writer =
                    new PrintWriter("portfolio.txt");

            writer.println("Balance=" + balance);

            for (String stock : portfolio.keySet()) {
                writer.println(stock + "=" + portfolio.get(stock));
            }

            writer.close();

            System.out.println("Portfolio saved!");

        } catch (Exception e) {
            System.out.println("Error saving file.");
        }
    }
}

public class StockTradingplatformm {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Map<String, Stock> market = new HashMap<>();

        market.put("TCS", new Stock("TCS", 3900));
        market.put("INFY", new Stock("INFY", 1700));
        market.put("RELIANCE", new Stock("RELIANCE", 2950));
        market.put("HDFC", new Stock("HDFC", 1800));
        market.put("WIPRO", new Stock("WIPRO", 550));

        User user = new User("Mayuri", 100000);

        int choice;

        do {

            System.out.println("\n===== STOCK TRADING PLATFORM =====");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Transaction History");
            System.out.println("6. Add Balance");
            System.out.println("7. Save Portfolio");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:

                    System.out.println("\n===== MARKET DATA =====");

                    for (Stock stock : market.values()) {

                        System.out.println(
                                stock.getSymbol()
                                        + " : ₹"
                                        + stock.getPrice()
                        );
                    }

                    break;

                case 2:

                    System.out.print("Enter Stock Symbol: ");
                    String buySymbol = sc.next();

                    System.out.print("Enter Quantity: ");
                    int buyQty = sc.nextInt();

                    if (market.containsKey(buySymbol)) {
                        user.buyStock(
                                market.get(buySymbol),
                                buyQty
                        );
                    } else {
                        System.out.println("Invalid Stock!");
                    }

                    break;

                case 3:

                    System.out.print("Enter Stock Symbol: ");
                    String sellSymbol = sc.next();

                    System.out.print("Enter Quantity: ");
                    int sellQty = sc.nextInt();

                    if (market.containsKey(sellSymbol)) {
                        user.sellStock(
                                market.get(sellSymbol),
                                sellQty
                        );
                    } else {
                        System.out.println("Invalid Stock!");
                    }

                    break;

                case 4:
                    user.showPortfolio(market);
                    break;

                case 5:
                    user.showHistory();
                    break;

                case 6:

                    System.out.print("Enter Amount: ₹");
                    double amount = sc.nextDouble();

                    user.addBalance(amount);

                    System.out.println("Balance Added!");

                    break;

                case 7:
                    user.savePortfolio();
                    break;

                case 0:
                    System.out.println("Thank You!");
                    break;

                default:
                    System.out.println("Invalid Choice!");
            }

        } while (choice != 0);

        sc.close();
    }
}
