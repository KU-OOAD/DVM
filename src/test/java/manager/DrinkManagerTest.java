package manager;

import data.Drink;
import db.DrinkDBManager;
import org.junit.jupiter.api.*;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DrinkManagerTest {

    DrinkManager drinkManager;

    @BeforeEach
    void setUp() throws Exception {
        drinkManager = new DrinkManager();
    }

    @Test
    void hasDrinkTest() {
        assertTrue(drinkManager.hasDrink("01", 1), "Should return true when drink is available");
        assertFalse(drinkManager.hasDrink("01", 100), "Should return false when drink is not available");
    }

    @Test
    void getDrinkTest() {
        Drink drink = drinkManager.getDrink("01", 1);
        assertNotNull(drink, "Should return a drink object when available");
        assertEquals("콜라", drink.getDrinkName(), "Drink name should be 콜라");
        assertEquals(1000, drink.getDrinkPrice(), "Drink price should be 1000");
        assertEquals(1, drink.getDrinkNum(), "Drink quantity should be 1");

        Drink noDrink = drinkManager.getDrink("01", 100);
        assertNull(noDrink, "Should return null when drink is not available in requested quantity");
    }

    @Test
    void getMenuInfoTest() {
        List<Drink> menuList = drinkManager.getMenuInfo();
        assertNotNull(menuList, "Menu list should not be null");
        assertFalse(menuList.isEmpty(), "Menu list should not be empty");
    }

    @Test
    void getDrinkQuantityTest() {
        drinkManager.manageDrink("01", 9);
        drinkManager.getDrink("01", 2);
        int quantity = drinkManager.getDrinkQuantity("01");
        assertEquals(7, quantity, "Drink quantity should be 1 after getDrink(01, 1)");
    }

    @Test
    void manageDrinkTest() {
        assertTrue(drinkManager.manageDrink("01", 9), "Should return true when drink quantity is updated");
        int quantity = drinkManager.getDrinkQuantity("01");
        assertEquals(9, quantity, "Drink quantity should be updated to 5");
    }

    @Test
    void reqAmountOfDrinkTest() {
        List<Drink> drinks = drinkManager.reqAmountOfDrink();
        assertNotNull(drinks, "Drink list should not be null");
        assertFalse(drinks.isEmpty(), "Drink list should not be empty");
    }

}
