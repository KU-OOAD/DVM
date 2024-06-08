package db;

import data.VerificationCode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class VerificationCodeDBManagerTest {

    private static final String ORIGINAL_DB_PATH = "src/main/resources/verificationCode.txt";
    private static final String BACKUP_DB_PATH = "src/main/resources/verificationCode_backup.txt";
    private VerificationCodeDBManager manager;

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
            fileWriter.write("콜라 10 asd123asd1\n" +
                    "사이다 10 asd123asd2\n" +
                    "홍차 10 asd123asd3\n" +
                    "녹차 10 asd123asd4\n" +
                    "밀크티 10 asd123asd5\n");
        } catch (IOException e) {
            e.printStackTrace();
            fail("Failed to set up test database file.");
        }

        // Initialize VerificationCodeDBManager
        manager = VerificationCodeDBManager.getManager();
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
    void testCheckCode() {
        VerificationCode code = manager.checkCode("asd123asd1");
        assertNotNull(code);
        assertEquals("콜라", code.getDrinkType());
        assertEquals(10, code.getDrinkNum());

        VerificationCode nonExistentCode = manager.checkCode("nonexistentcode");
        assertNull(nonExistentCode);
    }

    @Test
    void testSaveCode() {
        VerificationCode newCode = new VerificationCode("asd123asd6", "포도주스", 15);
        assertTrue(manager.saveCode(newCode));
        assertNotNull(manager.checkCode("asd123asd6"));

        VerificationCode existingCode = new VerificationCode("asd123asd1", "콜라", 10);
        assertFalse(manager.saveCode(existingCode));
    }
}
