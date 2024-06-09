package manager;

import controller.DrinkController;
import data.Drink;
import db.DBManager;
import db.DrinkDBManager;

import java.util.*;

public class DrinkManager {

    private List<Drink> menuList;

    public DrinkManager() {
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        menuList = drinkDBManager.getMenuList();
    }

    public Boolean hasDrink(String drinkType, int drinkNum) {
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        return drinkDBManager.hasDrink(drinkType, drinkNum);
    }

    public Drink getDrink(String drinkType, int drinkNum) {
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        return drinkDBManager.getDrink(drinkType, drinkNum);
    }

    public List<Drink> getMenuInfo() {
        return menuList;
    }///////////getMenuInfo의 리턴값을 void -> List<Drink>로 변경

    public int getDrinkQuantity(String drinkType) {
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        return drinkDBManager.getDrinkQuantity(drinkType);
    }///////////getDrinkQuantity의 리턴값을 void -> int로 변경

    public boolean manageDrink(String drinkType, int drinkNum) {    // drinkNum 얘가 양수던 음수던 보내주게 함
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        return drinkDBManager.setDrink(drinkType, drinkNum);
    }///////////manageDrink 파라미터 drinkNum의 타입을 String -> int로 변경
    ///////////manageDrink 리턴값을 void -> boolean 변경

    public List<Drink> reqAmountOfDrink() {
        DrinkDBManager drinkDBManager = DrinkDBManager.getManager();
        this.menuList = drinkDBManager.getAllDrinkStatus();
        return menuList;
    }

}

//////////지금까지 5개 고침. DrinkManager getMenuInfo, DrinkManager getDrinkQuantity, AdminDBManager checkUser, DrinkManager manageDrink, DrinkManager manageDrink

///////AdminAccountManager logout 질문
