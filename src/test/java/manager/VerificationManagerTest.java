package manager;

import data.VerificationCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VerificationManagerTest {

    private VerificationManager manager = new VerificationManager();

    @Test
    void getVerificationCode() {
        String code = manager.getVerificationCode();
        System.out.println(code);
        assertEquals(code.getClass(), String.class);
        assertFalse(code.isBlank());
    }

    @Test
    void saveCodeAndVerifyTest() {
        String strCode = manager.getVerificationCode();
        VerificationCode code = new VerificationCode(strCode, "01", 1);
        manager.saveCode(code);
        VerificationCode code2 = manager.verifyCode(strCode);
        assertSame(code.getCode(), code2.getCode());
        assertEquals(code.getDrinkType(), code2.getDrinkType());
        assertSame(code.getDrinkNum(), code2.getDrinkNum());
    }

}
