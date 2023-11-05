import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Account extends JFrame implements ActionListener {

  private final JButton depositButton = new JButton("Deposit");
  private final JButton withdrawButton = new JButton("Withdraw");
  private final JButton cancelButton = new JButton("Cancel");

  private final JTextField value = new JTextField();

  private final JLabel error = new JLabel("Please enter a number above 0");

  private final User user;

  public Account(User user) {
    setSize(300, 200);
    setTitle("State Bank Of India");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    this.user = user;

    //welcomePanel
    JPanel welcomePanel = new JPanel(new GridLayout(4, 1));
    welcomePanel.add(new JLabel("Hello, " + user));
    welcomePanel.add(
      new JLabel("Your Balance is: " + user.getBalance() + "Rs")
    );

    JPanel valuePanel = new JPanel(new GridLayout(1, 2));
    valuePanel.add(new JLabel("Enter Amount:"));
    valuePanel.add(value);
    welcomePanel.add(valuePanel);

    welcomePanel.add(error);
    error.setForeground(Color.red);
    error.setVisible(false);

    add(welcomePanel, BorderLayout.CENTER);

    //ButtonPanel
    JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
    buttonPanel.add(depositButton);
    buttonPanel.add(withdrawButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel, BorderLayout.SOUTH);

    depositButton.addActionListener(this);
    withdrawButton.addActionListener(this);
    cancelButton.addActionListener(this);

    setLocationRelativeTo(null); // Center the window on the screen

    setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource().equals(cancelButton)) System.exit(0);

    double input;
    try {
      input = Double.parseDouble(value.getText());
    } catch (Exception ex) {
      error.setVisible(true);
      return;
    }

    if (input <= 0) {
      error.setVisible(true);
      return;
    }

    if (e.getSource().equals(withdrawButton) && input > user.getBalance()) {
      // Display an "Insufficient balance" message
      JOptionPane.showMessageDialog(
        this,
        "Insufficient balance. Your current balance is: " +
        user.getBalance() +
        " Rs",
        "Error",
        JOptionPane.ERROR_MESSAGE
      );
      return;
    }

    String message = "Are you sure to ";
    if (e.getSource().equals(withdrawButton)) message +=
      "withdraw "; else message += "deposit ";
    message += input + " Rs";

    int a = JOptionPane.showConfirmDialog(this, message);
    if (a == JOptionPane.YES_OPTION) {
      if (e.getSource().equals(withdrawButton)) user.withdraw(
        input
      ); else user.deposit(input);

      new Summary(user);
      dispose();
    }
  }
}
