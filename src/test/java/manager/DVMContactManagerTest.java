package manager;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DVMContactManagerTest {

    DVMContactManager manager = new DVMContactManager();

    @Test
    void searchDrinkTest() {
        manager.searchDrink("12", 12);
    }

    @Test
    void reqAdvancePaymentTest() {

    }

}