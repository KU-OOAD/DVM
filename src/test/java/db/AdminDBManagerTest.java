package db;

import data.AdminAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AdminDBManagerTest {

    private static final String ORIGINAL_DB_PATH = "src/main/resources/adminAccount.txt";
    private static final String BACKUP_DB_PATH = "src/main/resources/adminAccount_backup.txt";
    private AdminDBManager manager;

    @BeforeEach
    void setUp() {
        // Backup the original database file
        try {
            Files.copy(Path.of(ORIGINAL_DB_PATH), Path.of(BACKUP_DB_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to back up the original database file.");
        }

        // Set up the test database file
        try (FileWriter fileWriter = new FileWriter(ORIGINAL_DB_PATH)) {
            fileWriter.write(
                    "admin1\n" +
                            "password1\n" +
                            "---\n" +
                            "admin2\n" +
                            "password2\n");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to set up test database file.");
        }

        // Initialize AdminDBManager
        manager = AdminDBManager.getManager();
    }

    @AfterEach
    void tearDown() {
        // Restore the original database file
        try {
            Files.delete(Path.of(ORIGINAL_DB_PATH));
            Files.move(Path.of(BACKUP_DB_PATH), Path.of(ORIGINAL_DB_PATH));
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to restore the original database file.");
        }
    }

    @Test
    void testCheckUser_ValidAccount() {
        AdminAccount validAccount = new AdminAccount("admin1", "password1");
        assertTrue(manager.checkUser(validAccount), "Valid account should return true.");
    }

    @Test
    void testCheckUser_InvalidAccount() {
        AdminAccount invalidAccount = new AdminAccount("admin3", "password3");
        assertFalse(manager.checkUser(invalidAccount), "Invalid account should return false.");
    }

    @Test
    void testCheckUser_WrongPassword() {
        AdminAccount wrongPasswordAccount = new AdminAccount("admin1", "wrongpassword");
        assertFalse(manager.checkUser(wrongPasswordAccount), "Wrong password should return false.");
    }

    @Test
    void testCheckUser_EmptyFile() {
        try (FileWriter fileWriter = new FileWriter(ORIGINAL_DB_PATH)) {
            // Create an empty test database file
            fileWriter.write("");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to set up empty test database file.");
        }

        AdminAccount account = new AdminAccount("admin1", "password1");
        assertFalse(manager.checkUser(account), "Empty file should return false.");
    }
}
