package db;

import data.Drink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class DrinkDBManagerTest {

    private final DrinkDBManager manager = DrinkDBManager.getManager();

    @BeforeEach
    public void setUp() {
        // 테스트 전 초기 상태를 설정하는 메서드입니다.
        // 테스트용 DB 파일을 초기화합니다.
        // 이 부분은 실제 DB 파일을 초기화하는 코드로 대체해야 합니다.
    }

    @Test
    public void testHasDrink() {
        // 재고가 있는 음료 테스트
        Assertions.assertTrue(manager.hasDrink("01", 1)); // 콜라 1개
        // 재고가 없는 음료 테스트
        Assertions.assertFalse(manager.hasDrink("05", 100)); // 밀크티 100개
    }

    @Test
    public void testGetDrink() {
        // 재고가 충분한 경우
        Drink drink = manager.getDrink("02", 1); // 사이다 1개
        Assertions.assertNotNull(drink);
        Assertions.assertEquals("사이다", drink.getDrinkName());
        Assertions.assertEquals(1, drink.getDrinkNum());

        // 재고가 부족한 경우
        drink = manager.getDrink("01", 50); // 콜라 50개 요청
        Assertions.assertNull(drink); // 재고가 부족하므로 null이어야 함
    }

    @Test
    public void testGetDrinkQuantity() {
        // 특정 음료의 재고 수량 확인
        int quantity = manager.getDrinkQuantity("03"); // 녹차
        Assertions.assertEquals(10, quantity);
    }

    @Test
    public void testSetDrink() {
        // 음료 재고 갱신 테스트
        boolean result = manager.setDrink("02", 7); // 사이다 7개
        Assertions.assertTrue(result);
        // 존재하지 않는 음료 갱신 테스트
        result = manager.setDrink("21", 3); // 존재하지 않는 음료 코드
        Assertions.assertFalse(result);
    }

    @Test
    public void testGetAllDrinkStatus() {
        // 모든 음료의 재고 현황 확인
        List<Drink> drinks = manager.getAllDrinkStatus();
        Assertions.assertEquals(7, drinks.size()); // 초기화된 음료 수량 확인
        // 첫 번째 음료 확인
        Drink firstDrink = drinks.get(0);
        Assertions.assertEquals(1, firstDrink.getId());
        Assertions.assertEquals("콜라", firstDrink.getDrinkName());
    }

    @Test
    public void testGetMenuList() {
        // 전체 메뉴 목록 확인
        List<Drink> menuList = manager.getMenuList();
        Assertions.assertEquals(20, menuList.size());
        // 첫 번째 메뉴 확인
        Drink firstMenu = menuList.get(0);
        Assertions.assertEquals(1, firstMenu.getId());
        Assertions.assertEquals("콜라", firstMenu.getDrinkName());
    }
}
