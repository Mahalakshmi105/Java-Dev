package Internship.TechOctanet;

import java.sql.*;
import java.util.*;

    /**
     * ATM Simulation Application
     * This class provides methods to interact with the ATM, such as checking balance,
     * withdrawing money, depositing money, changing PIN, and viewing transaction history.
     */
    public class ATMSimulation {

        // Database connection details
        static final String DB_URL = "jdbc:mysql://localhost:3306/ATM_db";
        static final String USER = "root";
        static final String PASS = "lakshmi";

        static int userPin;
        static int userId;
        static double balance;

        static Scanner sc = new Scanner(System.in);

        /**
         * Method to connect to the database.
         * @return Connection object
         */
        public static Connection connectDatabase() {
            try {
                return DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

        /**
         * Method to fetch the user's current balance from the database.
         */
        public static void checkBalance() {
            try (Connection conn = connectDatabase()) {
                PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("SELECT balance FROM Accounts WHERE user_id = ?");
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    balance = rs.getDouble("balance");
                    System.out.println("Your current balance: " + balance);
                } else {
                    System.out.println("Account not found.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method to withdraw a specified amount from the user's account.
         * @param amount the amount to withdraw
         */
        public static void withDraw(int amount) {
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive amount.");
            } else if (amount > balance) {
                System.out.println("Insufficient balance. Your current balance is " + balance);
            } else {
                balance -= amount;
                updateBalance();
                System.out.println("Transaction Successful. Your available balance is " + balance);
                recordTransaction("Withdrawal", amount);
            }
        }

        /**
         * Method to deposit a specified amount into the user's account.
         * @param amount the amount to deposit
         */
        public static void depositMoney(int amount) {
            if (amount <= 0) {
                System.out.println("Invalid amount. Please enter a positive amount.");
            } else {
                balance += amount;
                updateBalance();
                System.out.println("Transaction Successful. Your available balance is " + balance);
                recordTransaction("Deposit", amount);
            }
        }

        /**
         * Method to update the user's balance in the database.
         */
        public static void updateBalance() throws NullPointerException {
            try (Connection conn = connectDatabase()) {
                try (PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("UPDATE Accounts SET balance = ? WHERE user_id = ?")) {
                    {
                        stmt.setDouble(1, balance);
                        stmt.setInt(2, userId);
                        stmt.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method to change the user's PIN.
         */
        public static void pinChange() {
            System.out.println("Enter your new PIN: ");
            int newPin = sc.nextInt();
            System.out.println("Re-enter your new PIN for confirmation: ");
            int reEnterPin = sc.nextInt();

            if (newPin == reEnterPin) {
                try (Connection conn = connectDatabase();
                     PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("UPDATE Users SET Pin = ? WHERE user_id = ?")) {
                    stmt.setInt(1, newPin);
                    stmt.setInt(2, userId);
                    stmt.executeUpdate();
                    userPin = newPin;
                    System.out.println("PIN changed successfully.");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("PIN mismatch. Please try again.");
            }
        }

        /**
         * Method to record a transaction in the transaction history table.
         * @param type the type of transaction (Deposit/Withdrawal)
         * @param amount the amount involved in the transaction
         */
        public static void recordTransaction(String type, double amount) {
            try (Connection conn = connectDatabase();
                 PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("INSERT INTO Transactions (user_id, type, amount) VALUES (?, ?, ?)")) {
                stmt.setInt(1, userId);
                stmt.setString(2, type);
                stmt.setDouble(3, amount);
                stmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method to display the transaction history of the user.
         */
        public static void showTransactionHistory() {
            try (Connection conn = connectDatabase();
                 PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("SELECT * FROM Transactions WHERE user_id = ?")) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (!rs.isBeforeFirst()) {
                    System.out.println("No transactions have been made yet.");
                } else {
                    while (rs.next()) {
                        System.out.println("Date: " + rs.getTimestamp("transaction_date") +
                                ", Type: " + rs.getString("type") +
                                ", Amount: " + rs.getDouble("amount"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Main method that serves as the entry point for the application.
         * Handles user authentication and provides a menu for ATM operations.
         * @param args command-line arguments (not used)
         */
        public static void main(String[] args) {
            boolean flag = false;
            System.out.println("Welcome to the ATM!");
            System.out.println("Please enter your PIN.");
            userPin = sc.nextInt();

            try (Connection conn = connectDatabase();
                 PreparedStatement stmt = Objects.requireNonNull(conn).prepareStatement("SELECT * FROM Users WHERE Pin = ?")) {
                stmt.setInt(1, userPin);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    userId = rs.getInt("user_id");

                    do {
                        System.out.println("Choose an option:");
                        System.out.println("1. Balance Inquiry");
                        System.out.println("2. Withdrawal");
                        System.out.println("3. Deposit");
                        System.out.println("4. Change PIN");
                        System.out.println("5. Transaction History");
                        System.out.println("6. Exit");
                        int choice = sc.nextInt();

                        switch (choice) {
                            case 1:
                                checkBalance();
                                break;
                            case 2:
                                System.out.println("Enter the amount to withdraw: ");
                                int withdrawAmount = sc.nextInt();
                                withDraw(withdrawAmount);
                                break;
                            case 3:
                                System.out.println("Enter the amount to deposit: ");
                                int depositAmount = sc.nextInt();
                                depositMoney(depositAmount);
                                break;
                            case 4:
                                pinChange();
                                break;
                            case 5:
                                showTransactionHistory();
                                break;
                            case 6:
                                System.out.println("Thank you for using the ATM. Goodbye!");
                                flag = true;
                                break;
                            default:
                                System.out.println("Invalid choice. Please select a valid option.");
                                break;
                        }
                    } while (!flag);
                } else {
                    System.out.println("Incorrect PIN.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }