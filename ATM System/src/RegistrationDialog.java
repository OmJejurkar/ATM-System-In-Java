    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.ActionEvent;
    import java.awt.event.ActionListener;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    public class RegistrationDialog extends JDialog implements ActionListener {
        private final JTextField usernameField = new JTextField(20);
        private final JPasswordField passwordField = new JPasswordField(20);
        private final JButton registerButton = new JButton("Register");

        public RegistrationDialog(Frame parent) {
            super(parent, "Sign Up", true);
            setSize(300, 150);
            setLocationRelativeTo(parent);
            setLayout(new GridLayout(3, 2));

            add(new JLabel("Username:"));
            add(usernameField);

            add(new JLabel("Password:"));
            add(passwordField);

            add(new JLabel()); // Empty Label
            add(registerButton);

            registerButton.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(registerButton)) {
                String username = usernameField.getText();
                char[] passwordChars = passwordField.getPassword();
                String password = new String(passwordChars);
        
                Connection dbConnection = null;
                PreparedStatement insertStatement = null;
        
                try {
                    dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaatmproject", "root", "Omi@2005");
                    String insertQuery = "INSERT INTO atm (Name, Pin) VALUES (?, ?)";
                    insertStatement = dbConnection.prepareStatement(insertQuery);
        
                    insertStatement.setString(1, username);
                    insertStatement.setString(2, password);
        
                    int rowsAffected = insertStatement.executeUpdate();
        
                    if (rowsAffected > 0) {
                        System.out.println("User registered successfully.");
                    } else {
                        System.out.println("User registration failed.");
                    }
                    dispose();
                    SwingUtilities.invokeLater(() -> {
                        new Start();
                    });
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        if (insertStatement != null) {
                            insertStatement.close();
                        }
                        if (dbConnection != null) {
                            dbConnection.close();
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } 
}
