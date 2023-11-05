import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Start extends JFrame implements ActionListener {

    private final JButton cancelButton = new JButton("Cancel");
    private final JButton signUpButton= new JButton("SignUp");
    private final JList<User> userList = new JList<>();
    private final JTextField password = new JTextField();
    private final JLabel error = new JLabel();
    private final JLabel balanceLabel = new JLabel();

    private List<User> useRsFromDatabase;

    public Start() {
        setSize(600, 500);
        setTitle("State Bank Of India");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // UserPanel
        JPanel userPanel = new JPanel(new GridLayout(1, 2));
        userPanel.add(new JLabel("      User"));

        // Fetch useRs from the database
        useRsFromDatabase = getUseRsFromDatabase();

        DefaultListModel<User> userModel = new DefaultListModel<>();

        for (User user : useRsFromDatabase) {
            userModel.addElement(user);
        }

        userList.setModel(userModel);

        userList.addListSelectionListener(e -> {
            User selectedUser = userList.getSelectedValue();
            if (selectedUser != null) {
                balanceLabel.setText("User: " + selectedUser.getUsername() + " - Balance: " + selectedUser.getBalance() + " Rs");
            } else {
                balanceLabel.setText("");
            }
        });
        

        JScrollPane listScroller = new JScrollPane(userList);
        userPanel.add(listScroller);

        JPanel passwordPanel = new JPanel(new GridLayout(2, 2));

        passwordPanel.add(new JLabel("      Password"));
        passwordPanel.add(password);
        passwordPanel.add(new JLabel()); // Empty Label to move the Password label

        passwordPanel.add(error);
        error.setForeground(Color.red);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        mainPanel.add(userPanel);
        mainPanel.add(passwordPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ButtonPanel
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        JButton loginButton = new JButton("Login");
        buttonPanel.add(loginButton);
        buttonPanel.add(signUpButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        loginButton.addActionListener(this);
        signUpButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setLocationRelativeTo(null); // Center the window on the screen

        setVisible(true);
    }

    private List<User> getUseRsFromDatabase() {
        List<User> useRs = new ArrayList<>();
        Connection dbConnection = null;

        try {
            dbConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javaatmproject", "root", "Omi@2005");
            String query = "SELECT name, pin, balance FROM atm";
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String username = result.getString("Name");
                int passwordHash = result.getString("Pin").hashCode();
                useRs.add(new User(username, passwordHash));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (dbConnection != null) {
                    dbConnection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return useRs;
    }
    private static void playAudio(String audioFilePath) {
    try {
        File audioFile = new File(audioFilePath);
        if (audioFile.exists()) {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } else {
            System.err.println("Audio file not found: " + audioFilePath);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(cancelButton))
            System.exit(0);
        else if (e.getSource().equals(signUpButton)) {
        RegistrationDialog registrationDialog = new RegistrationDialog(null);
        registrationDialog.setVisible(true);
    }
        if (userList.getSelectedIndex() == -1)
            error.setText("Please Select a user");
        else if (userList.getSelectedValue().checkPassword(password.getText())) {
            new Account(userList.getSelectedValue());
            dispose();
        } else
            error.setText("Password is wrong");
    }
    public static void main(String[] args) {
        playAudio("C:/xampp/htdocs/M120/src/Login_page.wav");
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
                  
        SwingUtilities.invokeLater(() -> {
            new Start();
        });
    }
}