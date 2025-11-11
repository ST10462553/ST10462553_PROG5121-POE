package za.ac.iie.prog5121;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class LoginTest {

    @Test
    public void testValidUsername() {
        Login login = new Login();
        assertTrue(login.checkUsername("ky_1"));
    }

    @Test
    public void testInvalidUsername() {
        Login login = new Login();
        assertFalse(login.checkUsername("kyle!!!!!"));
    }

    @Test
    public void testValidPassword() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testInvalidPassword() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    public void testValidCell() {
        Login login = new Login();
        assertTrue(login.checkCellNumber("+27838968976"));
    }

    @Test
    public void testInvalidCell() {
        Login login = new Login();
        assertFalse(login.checkCellNumber("08966553"));
    }

    @Test
    public void testRegisterAndLoginSuccess() {
        Login login = new Login();
        login.registerUser("ky_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("ky_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginFail() {
        Login login = new Login();
        login.registerUser("ky_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("ky_1", "wrongpass"));
    }
}
