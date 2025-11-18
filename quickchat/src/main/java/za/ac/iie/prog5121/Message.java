package za.ac.iie.prog5121;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class Message {

    private String messageID;
    private String recipient;
    private String messageText;
    private String messageHash;
    private static int totalMessages = 0;

    //  Constructor
    public Message(String messageID, String recipient, String messageText) {
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
    }

    //  Validation: Message ID must be exactly 10 digits
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    //  Validation: Recipient cell must start with +27 and be 12 characters
    public boolean checkRecipientCell() {
        return recipient != null && recipient.startsWith("+27") && recipient.length() == 12;
    }

    //  Create a unique hash for message content
    public String createMessageHash() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String input = messageID + messageText;
            byte[] hashBytes = md.digest(input.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            messageHash = sb.toString();
            return messageHash;

        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(null, "Error creating message hash: " + e.getMessage());
            return null;
        }
    }

    //  Handle user decision for message sending/storing
    public void sendMessage() {
        String[] options = {"Send", "Discard", "Store"};
        int choice = JOptionPane.showOptionDialog(null,
                "Message ID: " + messageID +
                        "\nRecipient: " + recipient +
                        "\nMessage: " + messageText +
                        "\n\nWhat would you like to do?",
                "Message Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        switch (choice) {
            case 0: // Send
                JOptionPane.showMessageDialog(null, "Message sent successfully!");
                totalMessages++;
                break;

            case 1: // Discard
                JOptionPane.showMessageDialog(null, "Message discarded.");
                break;

            case 2: // Store to JSON
                storeMessageToFile();
                break;

            default:
                JOptionPane.showMessageDialog(null, "Action cancelled.");
                break;
        }
    }

    //  Store message data in a JSON file using Gson
    public void storeMessageToFile() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String filename = "messages.json";
        File messageFile = new File(filename);

        List<Message> messages = new ArrayList<>();

        // Read existing messages if the file exists
        if (messageFile.exists()) {
            try (FileReader reader = new FileReader(messageFile)) {
                Type messageListType = new TypeToken<List<Message>>() {}.getType();
                messages = gson.fromJson(reader, messageListType);

                if (messages == null) {
                    messages = new ArrayList<>();
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error reading existing messages: " + e.getMessage());
            }
        }

        // Add the new message to the list
        messages.add(this);

        // Write all messages back to the JSON file
        try (FileWriter writer = new FileWriter(messageFile)) {
            gson.toJson(messages, writer);
            writer.flush();
            JOptionPane.showMessageDialog(null, "Message successfully stored in messages.json!");
            totalMessages++;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error writing to messages.json: " + e.getMessage());
        }
    }

    //  Return total message count
    public static int returnTotalMessages() {
        return totalMessages;
    }

    //  Getters
    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }
}
