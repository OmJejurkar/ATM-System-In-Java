import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Summary extends JFrame implements ActionListener {

    private final JButton logOutButton = new JButton("Log Out");
    private final JButton newOrderButton = new JButton("New Order");
    private final JButton cancelButton = new JButton("Close");

    private final User user;

    public Summary(User user) {
        this.user = user;

        setSize(300, 150);
        setTitle("State Bank Of India");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(new JLabel("Your new balance is"));
        mainPanel.add(new JLabel("+ " + user.getBalance() + " Rs"));
        add(mainPanel, BorderLayout.CENTER);

        //ButtonPanel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(logOutButton);
        buttonPanel.add(newOrderButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        logOutButton.addActionListener(this);
        newOrderButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton))
            System.exit(0);
        else if (e.getSource().equals(logOutButton)){
            new Start();
        }else {
            new Account(user);
        }
        setVisible(false);
    }
}
