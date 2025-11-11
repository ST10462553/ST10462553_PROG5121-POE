package za.ac.iie.prog5121;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MessageManager {
    public static class ManagedMessage {
        private Message message;
        private String sender;
        private String status; // "SENT", "STORED", "DISCARDED"

        public ManagedMessage(Message message, String sender, String status) {
            this.message = message;
            this.sender = sender;
            this.status = status;
        }

        public Message getMessage() { return message; }
        public String getSender() { return sender; }
        public String getStatus() { return status; }

        public void setStatus(String status) { this.status = status; }
    }

    private List<ManagedMessage> managedMessages;
    private final String storageFilename = "messages.json";
    private Gson gson;

    public MessageManager() {
        managedMessages = new ArrayList<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        // Load previously stored messages from messages.json into managedMessages with sender unknown -> "Unknown"
        loadStoredMessagesFromFile();
    }

    // Populate some test data (useful for demonstration & testing)
    public void populateTestData() {
        managedMessages.clear();
        // Create sample messages: note Message constructor: Message(String messageID, String recipient, String messageText)
        managedMessages.add(new ManagedMessage(new Message("1111111111", "+27830000001", "Short msg"), "Alice", "SENT"));
        managedMessages.add(new ManagedMessage(new Message("2222222222", "+27830000002", "A bit longer message example"), "Bob", "STORED"));
        managedMessages.add(new ManagedMessage(new Message("3333333333", "+27830000003", "This is the longest message in the sample dataset, it should be detected by the longest-message routine."), "Charlie", "SENT"));
        managedMessages.add(new ManagedMessage(new Message("4444444444", "+27830000002", "Hello again Bob"), "Alice", "DISCARDED"));
        managedMessages.add(new ManagedMessage(new Message("5555555555", "+27830000004", "Short 2"), "Eve", "STORED"));

        // Pre-create hashes for these messages to mirror normal flow:
        for (ManagedMessage mm : managedMessages) {
            mm.getMessage().createMessageHash();
        }
    }

    // Display sender -> recipient list for all messages with status SENT
    public String displaySenderRecipientForAllSent() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sender -> Recipient for SENT messages:\n");
        boolean found = false;
        for (ManagedMessage mm : managedMessages) {
            if ("SENT".equalsIgnoreCase(mm.getStatus())) {
                sb.append(mm.getSender()).append(" -> ").append(mm.getMessage().getRecipient()).append("\n");
                found = true;
            }
        }
        if (!found) sb.append("[No SENT messages found]\n");
        return sb.toString();
    }

    // Find and display the longest message by character length
    public String findAndDisplayLongestMessage() {
        ManagedMessage longest = managedMessages.stream()
                .max(Comparator.comparingInt(m -> m.getMessage().getMessageText().length()))
                .orElse(null);

        if (longest == null) return "No messages available.";

        Message m = longest.getMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("Longest message details:\n");
        sb.append("Sender: ").append(longest.getSender()).append("\n");
        sb.append("Recipient: ").append(m.getRecipient()).append("\n");
        sb.append("Message ID: ").append(m.getMessageID()).append("\n");
        sb.append("Length: ").append(m.getMessageText().length()).append("\n");
        sb.append("Message: ").append(m.getMessageText()).append("\n");
        return sb.toString();
    }

    // Search by message ID
    public String searchByMessageID(String messageID) {
        for (ManagedMessage mm : managedMessages) {
            if (mm.getMessage().getMessageID().equals(messageID)) {
                Message m = mm.getMessage();
                return formatManagedMessage(mm);
            }
        }
        return "Message with ID " + messageID + " not found.";
    }

    // Search messages by recipient number
    public String searchMessagesByRecipient(String recipientNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append("Messages to recipient ").append(recipientNumber).append(":\n");
        boolean found = false;
        for (ManagedMessage mm : managedMessages) {
            if (mm.getMessage().getRecipient().equals(recipientNumber)) {
                sb.append(formatManagedMessageShort(mm)).append("\n");
                found = true;
            }
        }
        if (!found) sb.append("[No messages found for this recipient]\n");
        return sb.toString();
    }

    // Delete a message by its hash from managedMessages and from messages.json (returns true if deleted)
    public boolean deleteMessageByHash(String messageHash) {
        boolean removedFromManaged = false;
        Iterator<ManagedMessage> it = managedMessages.iterator();
        while (it.hasNext()) {
            ManagedMessage mm = it.next();
            if (messageHash.equals(mm.getMessage().getMessageHash())) {
                it.remove();
                removedFromManaged = true;
            }
        }

        // Also remove from messages.json file (stored messages)
        try {
            File f = new File(storageFilename);
            if (!f.exists()) return removedFromManaged; // nothing else to do

            Type listType = new TypeToken<List<Message>>() {}.getType();
            List<Message> fileMessages;
            try (FileReader reader = new FileReader(f)) {
                fileMessages = gson.fromJson(reader, listType);
            }

            if (fileMessages == null) fileMessages = new ArrayList<>();

            boolean removedFromFile = fileMessages.removeIf(msg -> messageHash.equals(msg.getMessageHash()));

            if (removedFromFile) {
                // write back the remaining list
                try (FileWriter writer = new FileWriter(f)) {
                    gson.toJson(fileMessages, writer);
                    writer.flush();
                }
            }
            return removedFromManaged || removedFromFile;
        } catch (Exception ex) {
            // ignore errors but return what we removed in memory
            return removedFromManaged;
        }
    }

    // Display a full report string showing counts and details
    public String displayFullReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== QuickChat Full Report ===\n");

        long sentCount = managedMessages.stream().filter(m -> "SENT".equalsIgnoreCase(m.getStatus())).count();
        long storedCount = managedMessages.stream().filter(m -> "STORED".equalsIgnoreCase(m.getStatus())).count();
        long discardedCount = managedMessages.stream().filter(m -> "DISCARDED".equalsIgnoreCase(m.getStatus())).count();

        sb.append("Total messages managed: ").append(managedMessages.size()).append("\n");
        sb.append("SENT: ").append(sentCount).append("\n");
        sb.append("STORED: ").append(storedCount).append("\n");
        sb.append("DISCARDED: ").append(discardedCount).append("\n\n");

        sb.append("Detailed list:\n");
        for (ManagedMessage mm : managedMessages) {
            sb.append(formatManagedMessageShort(mm)).append(" [").append(mm.getStatus()).append("]\n");
        }

        return sb.toString();
    }

    // Utility: readable full format for a ManagedMessage
    private String formatManagedMessage(ManagedMessage mm) {
        Message m = mm.getMessage();
        StringBuilder sb = new StringBuilder();
        sb.append("Sender: ").append(mm.getSender()).append("\n");
        sb.append("Recipient: ").append(m.getRecipient()).append("\n");
        sb.append("Message ID: ").append(m.getMessageID()).append("\n");
        sb.append("Hash: ").append(m.getMessageHash()).append("\n");
        sb.append("Status: ").append(mm.getStatus()).append("\n");
        sb.append("Message: ").append(m.getMessageText()).append("\n");
        return sb.toString();
    }

    // Short format used in lists
    private String formatManagedMessageShort(ManagedMessage mm) {
        Message m = mm.getMessage();
        return "[" + m.getMessageID() + "] " + mm.getSender() + " -> " + m.getRecipient() + " : " + abbreviate(m.getMessageText(), 50);
    }

    private String abbreviate(String s, int max) {
        if (s == null) return "";
        if (s.length() <= max) return s;
        return s.substring(0, max - 3) + "...";
    }

    // Load stored messages from messages.json into managedMessages (status = STORED, sender unknown)
    private void loadStoredMessagesFromFile() {
        try {
            File f = new File(storageFilename);
            if (!f.exists()) return;

            Type listType = new TypeToken<List<Message>>() {}.getType();
            List<Message> fileMessages;
            try (FileReader reader = new FileReader(f)) {
                fileMessages = gson.fromJson(reader, listType);
            }
            if (fileMessages == null) fileMessages = new ArrayList<>();

            for (Message m : fileMessages) {
                // ensure hash exists
                if (m.getMessageHash() == null || m.getMessageHash().isEmpty()) {
                    m.createMessageHash();
                }
                managedMessages.add(new ManagedMessage(m, "Unknown", "STORED"));
            }
        } catch (Exception e) {
            // ignore read errors — manager will still work with in-memory lists
        }
    }

    // Expose list size for tests
    public int managedMessagesCount() {
        return managedMessages.size();
    }

    // Expose raw managed messages for tests or advanced inspection
    public List<ManagedMessage> getManagedMessages() {
        return managedMessages;
    }

    // Add a ManagedMessage (useful for UI integration)
    public void addManagedMessage(ManagedMessage mm) {
        if (mm != null) {
            if (mm.getMessage().getMessageHash() == null || mm.getMessage().getMessageHash().isEmpty()) {
                mm.getMessage().createMessageHash();
            }
            managedMessages.add(mm);
            // if status is STORED, write to file
            if ("STORED".equalsIgnoreCase(mm.getStatus())) {
                // append to messages.json
                appendMessageToJsonFile(mm.getMessage());
            }
        }
    }

    // Append a Message to messages.json file (keeps file consistent)
    private void appendMessageToJsonFile(Message newMsg) {
        try {
            File f = new File(storageFilename);
            List<Message> fileMessages = new ArrayList<>();
            if (f.exists()) {
                Type listType = new TypeToken<List<Message>>() {}.getType();
                try (FileReader reader = new FileReader(f)) {
                    fileMessages = gson.fromJson(reader, listType);
                }
                if (fileMessages == null) fileMessages = new ArrayList<>();
            }
            fileMessages.add(newMsg);
            try (FileWriter writer = new FileWriter(f)) {
                gson.toJson(fileMessages, writer);
                writer.flush();
            }
        } catch (Exception e) {
            // ignore
        }
    }

    // Helper to show report in a pop-up (for MessageSender integration)
    public void showFullReportDialog() {
        String report = displayFullReport();
        JOptionPane.showMessageDialog(null, report, "QuickChat Full Report", JOptionPane.INFORMATION_MESSAGE);
    }
}
