package manager;

import data.AdminAccount;
import db.AdminDBManager;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class AdminAccountManagerTest {

    private AdminAccountManager adminAccountManager;
    private String tempDbPath = "src/main/resources/tempAdminAccount.txt";

    @BeforeEach
    void setUp() throws Exception {
        adminAccountManager = new AdminAccountManager();
        // Create a temporary DB file
        createTempDbFile();
        // Use reflection to set the private dbPath field in the AdminDBManager instance
        setDbPath(tempDbPath);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Delete the temporary DB file
        deleteTempDbFile();
    }

    @Test
    void checkUserTest() {
        AdminAccount validAccount = new AdminAccount("admin1", "password1");
        AdminAccount invalidAccount = new AdminAccount("admin1", "wrongpassword");

        boolean result = adminAccountManager.checkUser(validAccount);
        assertTrue(result, "User should be valid");

        result = adminAccountManager.checkUser(invalidAccount);
        assertFalse(result, "User should be invalid");
    }

    @Test
    void logoutTest() {
        boolean result = adminAccountManager.logout();
        assertTrue(result, "Logout should always return true");
    }

    private void createTempDbFile() {
        try (FileWriter fileWriter = new FileWriter(tempDbPath)) {
            fileWriter.write(
                    "admin1\n" +
                            "password1\n" +
                            "---\n" +
                            "admin2\n" +
                            "password2\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteTempDbFile() {
        try {
            Files.deleteIfExists(Paths.get(tempDbPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDbPath(String path) throws NoSuchFieldException, IllegalAccessException {
        AdminDBManager adminDBManager = AdminDBManager.getManager();
        java.lang.reflect.Field dbPathField = AdminDBManager.class.getDeclaredField("dbPath");
        dbPathField.setAccessible(true);
        dbPathField.set(adminDBManager, path);
    }
}

