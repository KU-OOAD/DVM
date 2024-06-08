package db;

import data.AdminAccount;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class AdminDBManager implements DBManager {

    private String dbPath = "src/main/resources/adminAccount.txt";

    private static final AdminDBManager manager = new AdminDBManager();

    private AdminDBManager() {
        // 파일이 존재하는지 확인하고, 없으면 생성
        try {
            FileReader fileReader = new FileReader(dbPath);
            fileReader.close();
        } catch (IOException e) {
            try {
                FileWriter fileWriter = new FileWriter(dbPath);
                fileWriter.write(
                    "admin1\n" +
                        "password1\n" +
                        "---\n" +
                        "admin2\n" +
                        "password2\n");
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static AdminDBManager getManager() {
        return manager;
    }

    public boolean checkUser(AdminAccount account) {
        try (BufferedReader reader = new BufferedReader(new FileReader(dbPath))) {
            String line;
            String id = null;
            String pwd = null;
            while ((line = reader.readLine()) != null) {
                if (line.equals("---")) {
                    if (id != null && pwd != null) {
                        if (id.equals(account.getId()) && pwd.equals(account.getPwd())) {
                            return true;
                        }
                    }
                    id = null;
                    pwd = null;
                } else if (id == null) {
                    id = line;
                } else if (pwd == null) {
                    pwd = line;
                }
            }
            // 마지막 계정 체크
            if (id != null && pwd != null) {
                if (id.equals(account.getId()) && pwd.equals(account.getPwd())) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }////////////checkUser의 리턴값을 void -> boolean으로 변경

}