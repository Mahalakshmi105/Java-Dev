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
