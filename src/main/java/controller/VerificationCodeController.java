package controller;

import data.Drink;
import data.VerificationCode;
import manager.DrinkManager;
import manager.VerificationManager;

import java.io.*;
import java.lang.module.FindException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class VerificationCodeController implements Controller {

    @Override
    public void execute(String url, BufferedReader br, OutputStream os) throws IOException {
        DataOutputStream dos = new DataOutputStream(os);

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

        verifyCode(dos, data);
    }

    private void verifyCode(DataOutputStream dos, String body) throws IOException {
        VerificationManager verificationManager = new VerificationManager();
        VerificationCode code = verificationManager.verifyCode(body);

        DrinkManager drinkManager = new DrinkManager();
        Drink d = drinkManager.getDrink(code.getDrinkType(), code.getDrinkNum());
        System.out.println("st" + d);
        if(d == null) {
            System.out.println(d);
            dos.writeBytes(("HTTP/1.1 200 OK \r\n Content Type: text/json;charset=utf-8 \r\n\r\nno"));
        } else {
            System.out.println(d);
            String str = "HTTP/1.1 200 OK \r\n Content Type: text/json;charset=utf-8 \r\n\r\n" + d.getDrinkName() + d.getDrinkNum();
            dos.write(str.getBytes(StandardCharsets.UTF_8));
        }
        dos.flush();
        System.out.println("fin");
    }

}