package main;

import model.Stock;
import model.UserStock;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // SAVE DATA TO FILE
    static void saveToFile(double balance, ArrayList<UserStock> portfolio) {
        try {
            FileWriter writer = new FileWriter("portfolio.txt");
            writer.write(balance + "\n");

            for (UserStock us : portfolio) {
                writer.write(us.name + "," + us.price + "," + us.quantity + "\n");
            }

            writer.close();
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    // LOAD DATA FROM FILE
    static double loadFromFile(ArrayList<UserStock> portfolio) {
        double balance = 10000;

        try {
            File file = new File("portfolio.txt");
            if (!file.exists()) {
                return balance;
            }

            Scanner fileScanner = new Scanner(file);
            balance = Double.parseDouble(fileScanner.nextLine());

            while (fileScanner.hasNextLine()) {
                String[] data = fileScanner.nextLine().split(",");
                portfolio.add(new UserStock(
                        data[0],
                        Double.parseDouble(data[1]),
                        Integer.parseInt(data[2])
                ));
            }

            fileScanner.close();
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }

        return balance;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ArrayList<Stock> market = new ArrayList<>();
        ArrayList<UserStock> portfolio = new ArrayList<>();

        market.add(new Stock("TCS", 3500));
        market.add(new Stock("INFY", 1500));
        market.add(new Stock("RELIANCE", 2500));

        double balance = loadFromFile(portfolio);
        int choice;

        do {
            System.out.println("\n=== Stock Trading Platform ===");
            System.out.println("Balance: ₹" + balance);
            System.out.println("1. View Market Stocks");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            switch (choice) {

                case 1:
                    System.out.println("\n--- Market Stocks ---");
                    for (Stock s : market) {
                        System.out.println(s.name + " - ₹" + s.price);
                    }
                    break;

                case 2:
                    System.out.print("Enter stock name: ");
                    String buyName = sc.next();
                    System.out.print("Enter quantity: ");
                    int buyQty = sc.nextInt();

                    Stock selected = null;
                    for (Stock s : market) {
                        if (s.name.equalsIgnoreCase(buyName)) {
                            selected = s;
                            break;
                        }
                    }

                    if (selected == null) {
                        System.out.println("Stock not found.");
                        break;
                    }

                    double cost = selected.price * buyQty;
                    if (cost > balance) {
                        System.out.println("Insufficient balance.");
                        break;
                    }

                    balance -= cost;
                    portfolio.add(new UserStock(selected.name, selected.price, buyQty));
                    System.out.println("Stock purchased successfully.");
                    break;

                case 3:
                    System.out.print("Enter stock name to sell: ");
                    String sellName = sc.next();
                    System.out.print("Enter quantity: ");
                    int sellQty = sc.nextInt();

                    boolean sold = false;
                    for (UserStock us : portfolio) {
                        if (us.name.equalsIgnoreCase(sellName) && us.quantity >= sellQty) {
                            us.quantity -= sellQty;
                            balance += us.price * sellQty;
                            sold = true;
                            System.out.println("Stock sold successfully.");
                            break;
                        }
                    }

                    if (!sold) {
                        System.out.println("Not enough stock to sell.");
                    }
                    break;

                case 4:
                    System.out.println("\n--- Your Portfolio ---");
                    if (portfolio.isEmpty()) {
                        System.out.println("No stocks purchased.");
                    }
                    for (UserStock us : portfolio) {
                        System.out.println(us.name + " | Qty: " + us.quantity + " | Price: ₹" + us.price);
                    }
                    break;

                case 5:
                    saveToFile(balance, portfolio);
                    System.out.println("Data saved. Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        } while (choice != 5);

        sc.close();
    }
}