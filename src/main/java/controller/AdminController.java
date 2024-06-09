package controller;

import data.AdminAccount;
import data.Drink;
import manager.AdminAccountManager;
import manager.DrinkManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

public class AdminController implements Controller {

    @Override
    public void execute(String url, BufferedReader br, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        System.out.println(url);

        int contentLength = 0;
        while (true) {
            final String line = br.readLine();
            if(line.isEmpty()) break;
            if (line.startsWith("Content-Length")) {
                contentLength = Integer.parseInt(line.split(": ")[1]);
            }
        }
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        String data = String.copyValueOf(body);
        System.out.println(data);

        if(Objects.equals(url, "/admin/login")) login(dos, data);
        else if(Objects.equals(url, "/admin/logout")) logout(dos, data);
        else if(Objects.equals(url, "/admin/manage")) manageDrink(dos, data);
        else if(Objects.equals(url, "/admin/amount")) reqAmountOfDrink(dos, data);
    }

    private void login(DataOutputStream dos, String body) throws IOException {
        String[] accounts = body.split(" ");

        AdminAccountManager adminManager = new AdminAccountManager();
        AdminAccount account = new AdminAccount(accounts[0], accounts[1]);
        if(adminManager.checkUser(account)) {
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        } else {
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nno"));
        }
        dos.flush();
    }

    private void logout(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        dos.flush();
    }

    private void manageDrink(DataOutputStream dos, String body) throws IOException {
        String[] arr = body.split(" ");
        int num = Integer.parseInt(arr[1]);
        DrinkManager drinkManager = new DrinkManager();
        if(drinkManager.manageDrink(arr[0], num)) {
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        } else {
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nno"));
        }
        dos.flush();
    }

    private void reqAmountOfDrink(DataOutputStream dos, String body) throws IOException {
        DrinkManager drinkManager = new DrinkManager();
        List<Drink> list = drinkManager.reqAmountOfDrink();

        StringBuilder str = new StringBuilder();
        for (Drink drink : list) {
            str.append(String.format("%02d", drink.getId()));
            str.append(" ");
            str.append(drink.getDrinkName());
            str.append(" ");
            str.append(drink.getDrinkPrice());
            str.append(" ");
            str.append(drink.getDrinkNum());
            str.append("\n");
        }

        dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/json;charset=utf-8\r\n\r\n"));
        dos.write(str.toString().getBytes(StandardCharsets.UTF_8));
        dos.flush();
    }

}