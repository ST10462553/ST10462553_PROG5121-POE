package za.ac.iie.prog5121;

import javax.swing.JOptionPane;

public class MessageSender {

    public static void main(String[] args) {
        MessageManager manager = new MessageManager();

        while (true) {
            String[] menuOptions = {"Send Message(s)", "Coming Soon", "Quit", "Data & Reports"};
            int selection = JOptionPane.showOptionDialog(null,
                    "Welcome to QuickChat!\nPlease select an option:",
                    "QuickChat Menu",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, menuOptions, menuOptions[0]);

            if (selection == 0) {
                int count = 0;
                try {
                    String input = JOptionPane.showInputDialog("How many messages would you like to send?");
                    if (input == null) continue;
                    count = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please enter a valid number.");
                    continue;
                }

                for (int i = 1; i <= count; i++) {
                    String messageID = JOptionPane.showInputDialog("Enter Message ID (10 digits):");
                    if (messageID == null) break;
                    String recipient = JOptionPane.showInputDialog("Enter Recipient Cell (+27XXXXXXXXX):");
                    if (recipient == null) break;
                    String messageText = JOptionPane.showInputDialog("Enter Message Text:");
                    if (messageText == null) break;

                    Message msg = new Message(messageID, recipient, messageText);

                    if (!msg.checkMessageID()) {
                        JOptionPane.showMessageDialog(null, "Invalid Message ID. Must be exactly 10 digits.");
                        continue;
                    }
                    if (!msg.checkRecipientCell()) {
                        JOptionPane.showMessageDialog(null, "Invalid Cell Number. Must start with +27 and be 12 characters long.");
                        continue;
                    }

                    msg.createMessageHash();

                    // Ask the user who the sender is (so manager can store sender info)
                    String sender = JOptionPane.showInputDialog("Enter sender name (for reporting):");
                    if (sender == null) sender = "Unknown";

                    // Show Send/Discard/Store via Message.sendMessage — but also add to manager accordingly
                    String[] options = {"Send", "Discard", "Store"};
                    int choice = JOptionPane.showOptionDialog(null,
                            "Message ID: " + messageID + "\nRecipient: " + recipient +
                                    "\nMessage: " + messageText + "\n\nWhat would you like to do?",
                            "Message Options",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                    switch (choice) {
                        case 0: // Send
                            JOptionPane.showMessageDialog(null, "Message sent successfully!");
                            // Add to manager as SENT
                            manager.addManagedMessage(new MessageManager.ManagedMessage(msg, sender, "SENT"));
                            break;
                        case 1: // Discard
                            JOptionPane.showMessageDialog(null, "Message discarded.");
                            manager.addManagedMessage(new MessageManager.ManagedMessage(msg, sender, "DISCARDED"));
                            break;
                        case 2: // Store
                            // store to file and manager as STORED
                            msg.storeMessageToFile();
                            manager.addManagedMessage(new MessageManager.ManagedMessage(msg, sender, "STORED"));
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "Action cancelled.");
                            break;
                    }
                }

                JOptionPane.showMessageDialog(null,
                        "Total messages managed: " + manager.managedMessagesCount(),
                        "Summary", JOptionPane.INFORMATION_MESSAGE);

            } else if (selection == 1) {
                JOptionPane.showMessageDialog(null, "Feature coming soon!");
            } else if (selection == 2) {
                JOptionPane.showMessageDialog(null, "Thank you for using QuickChat!\nGoodbye!");
                System.exit(0);
            } else if (selection == 3) {
                // Data & Reports submenu
                String[] reportOptions = {
                        "Populate Test Data",
                        "Show Sender->Recipient for SENT messages",
                        "Find Longest Message",
                        "Search by Message ID",
                        "Search by Recipient",
                        "Delete by Message Hash",
                        "Show Full Report",
                        "Back to Main Menu"
                };
                int repSel = JOptionPane.showOptionDialog(null,
                        "QuickChat Data & Reports",
                        "Reports Menu",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, reportOptions, reportOptions[0]);

                switch (repSel) {
                    case 0:
                        manager.populateTestData();
                        JOptionPane.showMessageDialog(null, "Test data populated.");
                        break;
                    case 1:
                        JOptionPane.showMessageDialog(null, manager.displaySenderRecipientForAllSent());
                        break;
                    case 2:
                        JOptionPane.showMessageDialog(null, manager.findAndDisplayLongestMessage());
                        break;
                    case 3:
                        String id = JOptionPane.showInputDialog("Enter Message ID to search:");
                        if (id != null) JOptionPane.showMessageDialog(null, manager.searchByMessageID(id));
                        break;
                    case 4:
                        String recipient = JOptionPane.showInputDialog("Enter recipient number to search (+27XXXXXXXXX):");
                        if (recipient != null) JOptionPane.showMessageDialog(null, manager.searchMessagesByRecipient(recipient));
                        break;
                    case 5:
                        String hash = JOptionPane.showInputDialog("Enter message hash to delete:");
                        if (hash != null) {
                            boolean deleted = manager.deleteMessageByHash(hash);
                            JOptionPane.showMessageDialog(null, deleted ? "Message deleted." : "Message not found or could not be deleted.");
                        }
                        break;
                    case 6:
                        manager.showFullReportDialog();
                        break;
                    default:
                        // back / cancel
                        break;
                }
            }
        }
    }
}
