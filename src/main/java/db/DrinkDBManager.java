package db;

import data.Drink;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.parseInt;

public class DrinkDBManager implements DBManager {

    private String dbPath = "src/main/resources/stock.txt";

    private static final DrinkDBManager manager = new DrinkDBManager();

    private DrinkDBManager() {
        // 파일이 존재하는지 확인하고, 없으면 생성
        try {
            FileReader fileReader = new FileReader(dbPath);
            fileReader.close();
        } catch (IOException e) {
            try {
                FileWriter fileWriter = new FileWriter(dbPath);
                fileWriter.write("01 콜라 1000 0\n" +
                                    "02 사이다 1000 0\n" +
                                    "03 녹차 1000 0\n" +
                                    "04 홍차 1000 0\n" +
                                    "05 밀크티 1000 0\n" +
                                    "06 탄산수 1000 0\n" +
                                    "07 보리차 1000 0\n" +
                                    "08 캔커피 1000 0\n" +
                                    "09 물 1000 0\n" +
                                    "10 에너지드링크 1000 0\n" +
                                    "11 유자차 1000 0\n" +
                                    "12 식혜 1000 0\n" +
                                    "13 아이스티 1000 0\n" +
                                    "14 딸기주스 1000 0\n" +
                                    "15 오렌지주스 1000 0\n" +
                                    "16 포도주스 1000 0\n" +
                                    "17 이온음료 1000 0\n" +
                                    "18 아메리카노 1000 0\n" +
                                    "19 핫초코 1000 0\n" +
                                    "20 카페라떼 1000 0\n" +
                                    "---\n" +
                                    "01 콜라 1000 10\n" +
                                    "02 사이다 1000 10\n" +
                                    "03 녹차 1000 10\n" +
                                    "04 홍차 1000 10\n" +
                                    "05 밀크티 1000 10\n" +
                                    "06 탄산수 1000 10\n" +
                                    "07 보리차 1000 10\n");
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static DrinkDBManager getManager() {
        return manager;
    }

    public Boolean hasDrink(String drinkType, int drinkNum) {   // 음료 있냐?
        try (BufferedReader br = new BufferedReader(new FileReader(dbPath))) {
            String line;
            boolean readingMachineStock = false; // --- 하단의 내 자판기 정보에 도달했는지 여부를 나타내는 플래그
            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    readingMachineStock = true; // --- 하단의 내 자판기 정보에 도달했음을 표시
                    continue; // --- 하단의 내 자판기 정보부터 처리하기 위해 다음 라인으로 넘어감
                }
                if (!readingMachineStock) { // --- 하단의 내 자판기 정보에 도달하기 전에는 스킵
                    continue;
                }
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals(drinkType)) {
                    int quantity = parseInt(parts[3]);
                    return quantity >= drinkNum;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Drink getDrink(String drinkType, int drinkNum) { //음료 수령해간다
        try {
            FileReader fileReader = new FileReader(dbPath);
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            Drink selectedDrink = null;
            boolean updated = false;
            boolean readingMachineStock = false; // --- 하단의 내 자판기 정보에 도달했는지 여부를 나타내는 플래그

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    readingMachineStock = true; // --- 하단의 내 자판기 정보에 도달했음을 표시
                    sb.append(line).append("\n");
                    continue; // --- 하단의 내 자판기 정보부터 처리하기 위해 다음 라인으로 넘어감
                }
                if (!readingMachineStock) { // --- 하단의 내 자판기 정보에 도달하기 전에는 스킵
                    sb.append(line).append("\n");
                    continue;
                }
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals(drinkType)) {
                    int quantity = parseInt(parts[3]);
                    if (quantity >= drinkNum) {
                        // 해당 음료의 수량을 차감
                        quantity -= drinkNum;
                        sb.append(parts[0]).append(" ").append(parts[1]).append(" ").append(parts[2]).append(" ").append(quantity).append("\n");
                        updated = true;
                        selectedDrink = new Drink(parseInt(drinkType), parts[1], parseInt(parts[2]), drinkNum);
                    } else {
                        // 요청한 수량보다 재고가 적은 경우
//                        selectedDrink = new Drink(parseInt(drinkType), parts[1], parseInt(parts[2]), quantity);
                        return null;
                    }
                } else {
                    sb.append(line).append("\n");
                }
            }
            br.close();
            if (!updated) {
                return null; // 주어진 음료가 없는 경우
            }
            // 파일에 갱신된 재고 정보를 추가하기
            FileWriter fileWriter = new FileWriter(dbPath, false); // 파일을 다시 쓰기 모드로 열기
            fileWriter.write(sb.toString());
            fileWriter.close();
            return selectedDrink;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getDrinkQuantity(String drinkType) {
        try (BufferedReader br = new BufferedReader(new FileReader(dbPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals(drinkType)) {
                    return parseInt(parts[3]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean setDrink(String drinkType, int drinkNum) {
        try {
            FileReader fileReader = new FileReader(dbPath);
            BufferedReader br = new BufferedReader(fileReader);
            StringBuilder sb = new StringBuilder();
            String line;
            boolean updated = false;
            boolean readingMachineStock = false; // --- 하단의 내 자판기 정보에 도달했는지 여부를 나타내는 플래그

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    readingMachineStock = true; // --- 하단의 내 자판기 정보에 도달했음을 표시
                    sb.append(line).append("\n");
                    continue; // --- 하단의 내 자판기 정보부터 처리하기 위해 다음 라인으로 넘어감
                }
                if (!readingMachineStock) { // --- 하단의 내 자판기 정보에 도달하기 전에는 스킵
                    sb.append(line).append("\n");
                    continue;
                }
                String[] parts = line.split(" ");
                if (parts.length >= 4 && parts[0].equals(drinkType)) {
                    sb.append(parts[0]).append(" ").append(parts[1]).append(" ").append(parts[2]).append(" ").append(drinkNum).append("\n");
                    updated = true;
                } else {
                    sb.append(line).append("\n");
                }
            }
            br.close();
            if (!updated) {
                return false; // 주어진 음료가 없는 경우
            }
            // 파일에 갱신된 재고 정보를 추가하기
            FileWriter fileWriter = new FileWriter(dbPath, false); // 파일을 다시 쓰기 모드로 열기
            fileWriter.write(sb.toString());
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Drink> getAllDrinkStatus() {    // 내 자판기 현황을 전달
        List<Drink> drinks = new ArrayList<>();
        boolean readingMachineStock = false;
        try (BufferedReader br = new BufferedReader(new FileReader(dbPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    readingMachineStock = true;
                    continue;
                }
                if (readingMachineStock) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 4) {
                        int id = parseInt(parts[0]);
                        String name = parts[1];
                        int price = parseInt(parts[2]);
                        int quantity = parseInt(parts[3]);
                        drinks.add(new Drink(id, name, price, quantity));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return drinks;
    }

    public List<Drink> getMenuList() {  // 그냥 전체 20종류의 음료 정보만 반환
        List<Drink> menuList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(dbPath))) {
            String line;

            while ((line = br.readLine()) != null) {
                if (line.equals("---")) {
                    break; // 상단 정보를 다 읽었으면 종료
                }
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    int id = parseInt(parts[0]);
                    String name = parts[1];
                    int price = parseInt(parts[2]);
                    int quantity = parts.length >= 4 ? parseInt(parts[3]) : 0; // 수량이 없으면 0으로 초기화
                    menuList.add(new Drink(id, name, price, quantity));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return menuList;
    }

}