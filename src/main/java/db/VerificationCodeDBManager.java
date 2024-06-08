package db;

import data.VerificationCode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VerificationCodeDBManager implements DBManager {

    private String dbPath = "src/main/resources/verificationCode.txt";

    private static final VerificationCodeDBManager manager = new VerificationCodeDBManager();

    private VerificationCodeDBManager() {
        // 파일이 존재하는지 확인하고, 없으면 생성
        try {
            FileReader fileReader = new FileReader(dbPath);
            fileReader.close();
        } catch (IOException e) {
            try {
                FileWriter fileWriter = new FileWriter(dbPath);
                fileWriter.write(
                        "콜라 10 asd123asd1\n" +
                                "사이다 10 asd123asd2\n" +
                                "홍차 10 asd123asd3\n" +
                                "녹차 10 asd123asd4\n" +
                                "밀크티 10 asd123asd5\n");
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static VerificationCodeDBManager getManager() {
        return manager;
    }

    public VerificationCode checkCode(String code) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dbPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 3 && parts[2].equals(code)) {
                    String drinkType = parts[0];
                    int drinkNum = Integer.parseInt(parts[1]);
                    return new VerificationCode(code, drinkType, drinkNum);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Boolean saveCode(VerificationCode code) {
        if (checkCode(code.getCode()) != null) {
            return false;
        }

        try (FileWriter fileWriter = new FileWriter(dbPath, true)) {
            fileWriter.write(code.getDrinkType() + " " + code.getDrinkNum() + " " + code.getCode() + "\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}