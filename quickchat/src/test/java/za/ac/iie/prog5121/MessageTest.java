package za.ac.iie.prog5121;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MessageTest {

    @Test
    public void testValidMessageID() {
        Message msg = new Message("1234567890", "+27831112222", "Hello!");
        assertTrue("Valid message ID should pass", msg.checkMessageID());
    }

    @Test
    public void testInvalidMessageID() {
        Message msg = new Message("12345", "+27831112222", "Hi!");
        assertFalse("Invalid message ID should fail", msg.checkMessageID());
    }

    @Test
    public void testValidRecipient() {
        Message msg = new Message("1234567890", "+27831112222", "Hey!");
        assertTrue("Valid recipient should pass", msg.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipient() {
        Message msg = new Message("1234567890", "0831112222", "Hi!");
        assertFalse("Invalid recipient should fail", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashGeneration() {
        Message msg = new Message("1234567890", "+27831112222", "Test Message");
        String hash = msg.createMessageHash();
        assertNotNull("Hash should not be null", hash);
        assertEquals("Hash should match stored hash", hash, msg.getMessageHash());
    }

    @Test
    public void testStoreMessageToFile() {
        // Create message and generate hash
        Message msg = new Message("1234567890", "+27831112222", "Stored message test");
        msg.createMessageHash();
        msg.storeMessageToFile();

        // Check if file exists
        File file = new File("messages.json");
        assertTrue("JSON file should exist after storing message", file.exists());

        // Verify JSON content
        try (FileReader reader = new FileReader(file)) {
            Gson gson = new Gson();
            Type messageListType = new TypeToken<List<Message>>() {}.getType();
            List<Message> messages = gson.fromJson(reader, messageListType);
            assertTrue("JSON file should contain at least one message", messages.size() > 0);
        } catch (Exception e) {
            fail("Error reading messages.json: " + e.getMessage());
        }
    }
}
