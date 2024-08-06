# 🏧ATM Simulation Application

This project is a simulation of an ATM (Automated Teller Machine) application, implemented in Java with JDBC for database connectivity. The application provides functionalities such as balance inquiry, withdrawal, deposit, PIN change, and transaction history. It uses a MySQL database to store user data, account balances, and transaction details.

## **🗃️Database Setup:**

To set up the database, use the following SQL commands:
```sql
CREATE DATABASE ATM_db;
USE ATM_db;

CREATE TABLE Users (
    user_id INT(50),
    Pin INT(5),
    Name VARCHAR(30) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO Users (user_id, Pin, Name) VALUES
    (1001, 1234, 'Anu'),
    (1004, 2345, 'Benny'),
    (1009, 3455, 'Carol'),
    (2007, 8765, 'Ranjith');

CREATE TABLE Accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    balance DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

INSERT INTO Accounts (user_id, balance) VALUES
    (1001, 500.0),
    (1004, 7000.0),
    (1009, 10000.0),
    (2007, 2000.0);

CREATE TABLE Transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    type VARCHAR(50), -- 'Deposit' or 'Withdrawal'
    amount DECIMAL(10, 2),
    transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);

INSERT INTO Transactions (user_id, type, amount) VALUES
    (1001, 'Deposit', 200.0),
    (1001, 'Withdrawal', 50.0),
    (1004, 'Deposit', 500.0),
    (1004, 'Withdrawal', 100.0),
    (1009, 'Deposit', 100.0),
    (1009, 'Withdrawal', 200.0),
    (2007, 'Deposit', 290.0),
    (2007, 'Withdrawal', 300.0);

-- Displaying all tables data
SELECT * FROM Users;
SELECT * FROM Accounts;
SELECT * FROM Transactions;
```


## **🏗️Application Structure:**

The main class, ATMSimulation, provides methods to interact with the ATM:

*💵checkBalance()*: Fetches the current balance from the database.

*💸withDraw(int amount)*: Withdraws a specified amount.

*💰depositMoney(int amount)*: Deposits a specified amount.

*🔑pinChange()*: Changes the users PIN.

*📝recordTransaction(String type, double amount)*: Records a transaction.

*📦showTransactionHistory()*: Displays the transaction history.


## **📋Prerequisites:**

Java Development Kit (JDK): Ensure that JDK is installed on your machine.

MySQL Database: The application connects to a MySQL database. Make sure you have MySQL installed and configured.

JDBC Driver: The application uses the MySQL JDBC driver for database connectivity. Make sure to include it in your project.


## **▶️How to Run:**

1.Clone the repository: - git clone https://github.com/Mahalakshmi105/Java-Dev.git

2.Set up the database using the provided SQL script.

3.Modify the database connection details in the ATMSimulation class if necessary.

4.Compile and run the ATMSimulation class.


### **🖥️Sample Output:**

```
Welcome to ATM Simulation!

Please enter your user ID:
2007

Authentication successful!

Please select an option:
1. Balance Inquiry
2. Deposit
3. Withdraw
4. Transaction History
5. Exit

Option: 1
Your current balance is: $2000.00

Please select an option:
1. Balance Inquiry
2. Deposit
3. Withdraw
4. Transaction History
5. Exit

Option: 2
Enter deposit amount: 50000
Deposit successful! New balance is: $52000.00

Please select an option:
1. Balance Inquiry
2. Deposit
3. Withdraw
4. Transaction History
5. Exit

Option: 3
Enter withdrawal amount: 5000
Withdrawal successful! New balance is: $47000.00

Please select an option:
1. Balance Inquiry
2. Deposit
3. Withdraw
4. Transaction History
5. Exit

Option: 4
Transaction History:
1. Deposit: $290.00 (Date: YYYY-MM-DD)
2. Withdrawal: $300.00 (Date: YYYY-MM-DD)
3. Deposit: $50000.00 (Date: YYYY-MM-DD)
4. Withdrawal: $5000.00 (Date: YYYY-MM-DD)

Please select an option:
1. Balance Inquiry
2. Deposit
3. Withdraw
4. Transaction History
5. Exit

Option: 5
Thank you for using ATM Simulation. Goodbye!
```
## **🌟Future Enhancements:**

Implement a graphical user interface (GUI) for the application.
Add more robust error handling and validation.
Include additional features like account creation and transfer.


## **📜License:**

This project is licensed under the MIT License - see the LICENSE file for details.


## **👩🏻‍💼Author:**

***Mahalakshmi*** - [GitHub](https://github.com/Mahalakshmi105/Java-Dev "Click here to see!")


## **🙏Acknowledgments:**

Thanks to the developers of Java and MySQL for providing excellent development tools.
