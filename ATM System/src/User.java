import java.text.DecimalFormat;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {

    private final String username;
    private final int password;
    private double balance;
    private Connection dbConnection;
    
    public User(String username, int password) {

        this.username = username;
        this.password = password;
        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaatmpoject", "root", "Omi@2005");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loadBalance();

    }

    @Override
    public String toString() {
        return username;
    }


    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password == password.hashCode();
    }

    public double getBalance() {
        return balance;
    }

    public void withdraw(double input) {
        if (balance >= input){
            balance -= input;
            try {
                String updateQuery = "UPDATE atm SET Balance = ? WHERE Name = ?";
                PreparedStatement updateStatement = dbConnection.prepareStatement(updateQuery);
                updateStatement.setDouble(1, balance);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            
        }
    }

    public void deposit(double input) {
        balance += input;
        try {
            String updateQuery = "UPDATE atm SET Balance = ? WHERE Name = ?";
            PreparedStatement updateStatement = dbConnection.prepareStatement(updateQuery);
            updateStatement.setDouble(1, balance);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBalance() {
        try {
            String query = "SELECT balance FROM atm WHERE Name = ?";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                balance = result.getDouble("balance");
            } else {
                // User doesn't exist, initialize with a random balance
                DecimalFormat df = new DecimalFormat("0.00");
                balance = Double.parseDouble(df.format(Math.random() * 1000));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeDatabaseConnection() {
        try {
            if (dbConnection != null) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}