package controller;

import com.google.gson.Gson;
import data.Drink;
import manager.DrinkManager;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class DrinkController implements Controller {

    public List<String> MenuList;

    public DrinkManager drinkManager;

    @Override
    public void execute(String url, BufferedReader br, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);
        getMenuList(dos, url);
    }

    private void getMenuList(DataOutputStream dos, String body) {
        try {
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/json;charset=utf-8\r\n\r\n"));

            DrinkManager manager = new DrinkManager();
            List<Drink> list = manager.getMenuInfo();
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

            dos.write(str.toString().getBytes(StandardCharsets.UTF_8));
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}