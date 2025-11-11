package za.ac.iie.prog5121;

import javax.swing.JOptionPane;
@SuppressWarnings("unused")
public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCell;
    private boolean lastLoginSuccessful = false;

    public boolean checkUsername(String username) {
        if (username.contains("_") && username.length() <= 5) {
            JOptionPane.showMessageDialog(null, "Username successfully captured.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                "Username is not correctly formatted.\nPlease ensure that your username contains an underscore and is no more than five characters in length.");
            return false;
        }
    }

    public boolean checkPasswordComplexity(String password) {
        boolean length = password.length() >= 8;
        boolean capital = password.matches(".*[A-Z].*");
        boolean number = password.matches(".*\\d.*");
        boolean special = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        if (length && capital && number && special) {
            JOptionPane.showMessageDialog(null, "Password successfully captured.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                "Password is not correctly formatted.\nPlease ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            return false;
        }
    }

    public boolean checkCellNumber(String cellNumber) {
        if (cellNumber.startsWith("+27") && cellNumber.length() == 12) {
            JOptionPane.showMessageDialog(null, "Cell phone number successfully added.");
            return true;
        } else {
            JOptionPane.showMessageDialog(null,
                "Cell phone number incorrectly formatted or does not contain international code (+27).");
            return false;
        }
    }

    public String registerUser(String username, String password, String cellNumber) {
        if (!checkUsername(username)) return "Username is incorrectly formatted.";
        if (!checkPasswordComplexity(password)) return "Password does not meet the complexity requirements.";
        if (!checkCellNumber(cellNumber)) return "Cell number incorrectly formatted.";

        registeredUsername = username;
        registeredPassword = password;
        registeredCell = cellNumber;

        return "User registered successfully!";
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        if (enteredUsername.equals(registeredUsername) && enteredPassword.equals(registeredPassword)) {
            lastLoginSuccessful = true;
            return true;
        } else {
            lastLoginSuccessful = false;
            return false;
        }
    }

    public String returnLoginStatus(boolean loginStatus) {
        if (loginStatus) {
            return "Welcome back " + registeredUsername + "!\nIt’s great to see you.";
        } else {
            return "Login failed — username or password incorrect.";
        }
    }
}       
        
  