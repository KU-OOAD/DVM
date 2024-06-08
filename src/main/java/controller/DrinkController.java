package controller;

import com.google.gson.Gson;
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
            dos.writeBytes(("HTTP/1.1 200 OK \r\n Content Type: text/json;charset=utf-8\r\n\r\n"));

            DrinkManager manager = new DrinkManager();
            List list = manager.getMenuInfo();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            byte[] b = json.getBytes(StandardCharsets.UTF_8);
            System.out.println(json);
            System.out.println("ok");

            dos.write(b);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}