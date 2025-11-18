package za.ac.iie.prog5121;

import javax.swing.JOptionPane;



public class Main {
    public static void main(String[] args) {
        Login login = new Login();

        // Registration Phase
        JOptionPane.showMessageDialog(null, "Welcome to QuickChat Registration!");

        String username = JOptionPane.showInputDialog("Enter username (must contain underscore and be ≤ 5 characters):");
        String password = JOptionPane.showInputDialog("Enter password (must contain capital letter, number, and special character):");
        String cellNumber = JOptionPane.showInputDialog("Enter cell number (+27XXXXXXXXX):");

        String registrationMessage = login.registerUser(username, password, cellNumber);
        JOptionPane.showMessageDialog(null, registrationMessage);

        if (registrationMessage.contains("successfully")) {
            // Proceed to Login
            JOptionPane.showMessageDialog(null, "Please log in to continue.");

            String enteredUsername = JOptionPane.showInputDialog("Enter your username:");
            String enteredPassword = JOptionPane.showInputDialog("Enter your password:");

            boolean loginStatus = login.loginUser(enteredUsername, enteredPassword);
            JOptionPane.showMessageDialog(null, login.returnLoginStatus(loginStatus));

            if (loginStatus) {
                //  Proceed to MessageSender (Part 2)
                JOptionPane.showMessageDialog(null, "Welcome to QuickChat! You can now send or store messages.");
                MessageSender.main(null);
            } else {
                JOptionPane.showMessageDialog(null, "Login failed. Exiting program.");
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed. Please restart the application.");
            System.exit(0);
        }
    }
}
