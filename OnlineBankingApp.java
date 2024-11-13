import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

// Account class to handle individual bank accounts
class Account {
    private String accountNumber;
    private String name;
    private double balance;

    public Account(String accountNumber, String name, double initialBalance) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.balance = initialBalance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
}

// BankingSystem class to manage multiple accounts
class BankingSystem {
    private Map<String, Account> accounts;

    public BankingSystem() {
        accounts = new HashMap<>();
    }

    public void createAccount(String accountNumber, String name, double initialBalance) {
        if (!accounts.containsKey(accountNumber)) {
            Account account = new Account(accountNumber, name, initialBalance);
            accounts.put(accountNumber, account);
            JOptionPane.showMessageDialog(null, "Account created successfully.");
        } else {
            JOptionPane.showMessageDialog(null, "Account with this number already exists.");
        }
    }

    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    public void deposit(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            account.deposit(amount);
            JOptionPane.showMessageDialog(null, "Deposited: " + amount);
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    public void withdraw(String accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            account.withdraw(amount);
            JOptionPane.showMessageDialog(null, "Withdrew: " + amount);
        } else {
            JOptionPane.showMessageDialog(null, "Account not found.");
        }
    }

    public String getBalance(String accountNumber) {
        Account account = getAccount(accountNumber);
        return (account != null) ? String.format("Balance: $%.2f", account.getBalance()) : "Account not found.";
    }
}

// Main application class with GUI for Online Banking
public class OnlineBankingApp {
    private static BankingSystem bankingSystem = new BankingSystem();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    // Login Frame
    static class LoginFrame extends JFrame {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public LoginFrame() {
            setTitle("Online Banking - Login");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(300, 200);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(3, 2));

            JLabel usernameLabel = new JLabel("Username:");
            usernameField = new JTextField();
            JLabel passwordLabel = new JLabel("Password:");
            passwordField = new JPasswordField();
            JButton loginButton = new JButton("Login");

            loginButton.addActionListener(new LoginButtonListener());

            add(usernameLabel);
            add(usernameField);
            add(passwordLabel);
            add(passwordField);
            add(loginButton);
        }

        private class LoginButtonListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Simple hardcoded login for demonstration purposes
                if (username.equals("user") && password.equals("pass")) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Login successful!");
                    new MainFrame().setVisible(true);
                    dispose(); // Close the login window
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Main Frame with banking options
    static class MainFrame extends JFrame {
        private JTextField accountNumberField, nameField, amountField;
        private JLabel balanceLabel;

        public MainFrame() {
            setTitle("Online Banking - Main Menu");
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setSize(400, 300);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(6, 2));

            accountNumberField = new JTextField();
            nameField = new JTextField();
            amountField = new JTextField();
            balanceLabel = new JLabel("Balance: $0.00");

            JButton createAccountButton = new JButton("Create Account");
            JButton depositButton = new JButton("Deposit");
            JButton withdrawButton = new JButton("Withdraw");
            JButton checkBalanceButton = new JButton("Check Balance");

            createAccountButton.addActionListener(e -> createAccount());
            depositButton.addActionListener(e -> deposit());
            withdrawButton.addActionListener(e -> withdraw());
            checkBalanceButton.addActionListener(e -> checkBalance());

            add(new JLabel("Account Number:"));
            add(accountNumberField);
            add(new JLabel("Account Holder Name:"));
            add(nameField);
            add(new JLabel("Amount:"));
            add(amountField);
            add(createAccountButton);
            add(depositButton);
            add(withdrawButton);
            add(checkBalanceButton);
            add(balanceLabel);
        }

        private void createAccount() {
            String accountNumber = accountNumberField.getText();
            String name = nameField.getText();
            double initialBalance = parseAmount();
            bankingSystem.createAccount(accountNumber, name, initialBalance);
        }

        private void deposit() {
            String accountNumber = accountNumberField.getText();
            double amount = parseAmount();
            bankingSystem.deposit(accountNumber, amount);
            updateBalanceLabel(accountNumber);
        }

        private void withdraw() {
            String accountNumber = accountNumberField.getText();
            double amount = parseAmount();
            bankingSystem.withdraw(accountNumber, amount);
            updateBalanceLabel(accountNumber);
        }

        private void checkBalance() {
            String accountNumber = accountNumberField.getText();
            updateBalanceLabel(accountNumber);
        }

        private void updateBalanceLabel(String accountNumber) {
            balanceLabel.setText(bankingSystem.getBalance(accountNumber));
        }

        private double parseAmount() {
            try {
                return Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid amount format!", "Error", JOptionPane.ERROR_MESSAGE);
                return 0;
            }
        }
    }
}