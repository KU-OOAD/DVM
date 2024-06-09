package controller;

import data.Card;
import data.DVM;
import data.Drink;
import data.VerificationCode;
import manager.DVMContactManager;
import manager.DrinkManager;
import manager.PaymentManager;
import manager.VerificationManager;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PayController implements Controller {

    private String drinkType;

    private int drinkNum;

    private Card card;

    private DVM dvm;

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
        System.out.println("pay body: " + data);

        if(Objects.equals(url, "/pay/setDrink")) setDrink(dos, data);
        else if(Objects.equals(url, "/pay/setDrinkNum")) setDrinkNum(dos, data);
        else if(Objects.equals(url, "/pay/isPayAvailable")) isPayAvailable(dos, data);
        else if(Objects.equals(url, "/pay/isPrepayAvailable")) isPrepayAvailable(dos, data);
        else if(Objects.equals(url, "/pay/pay")) pay(dos, data);
        else if(Objects.equals(url, "/pay/prepay")) prepay(dos, data);
    }

    private void setDrink(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        drinkType = body;
        dos.flush();
    }

    private void setDrinkNum(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        drinkNum = Integer.parseInt(body);
        dos.flush();
    }

    private void isPayAvailable(DataOutputStream dos, String body) throws IOException {
        DrinkManager drinkManager = new DrinkManager();
        if(drinkManager.hasDrink(drinkType, drinkNum))
            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        else {
            System.out.println("don't have drink");
            DVMContactManager contactManager = new DVMContactManager();
            dvm = contactManager.searchDrink(drinkType, drinkNum);
            if(dvm != null) {
                String res = "HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\n";
                res += dvm.getX();
                res += " ";
                res += dvm.getY();
                dos.writeBytes(res);
            } else dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nno"));
        }
        dos.flush();
    }

    private void isPrepayAvailable(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nok"));
        dos.flush();
    }

    private void pay(DataOutputStream dos, String body) throws IOException {
        PaymentManager paymentManager = new PaymentManager();
        String res = paymentManager.reqPay(new Card(body));
        if(Objects.equals(res, "ok")) {
            DrinkManager drinkManager = new DrinkManager();
            Drink drink = drinkManager.getDrink(drinkType, drinkNum);

            String str = "HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\n" + drink.getDrinkName() + " " + drink.getDrinkNum();
            byte[] b = str.getBytes(StandardCharsets.UTF_8);
            dos.write(b);
        } else dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\n" + res));
        dos.flush();
    }

    private void prepay(DataOutputStream dos, String body) throws IOException {
        VerificationManager verificationManager = new VerificationManager();
        String code = verificationManager.getVerificationCode();

        DVMContactManager contactManager = new DVMContactManager();
        if(contactManager.reqAdvancePayment(drinkType, drinkNum, code, dvm)) {
            PaymentManager paymentManager = new PaymentManager();
            String res = paymentManager.reqPay(new Card(body));

            if(Objects.equals(res, "ok")) {
                VerificationCode verifyCode = new VerificationCode(code, drinkType, drinkNum);
                verificationManager.saveCode(verifyCode);

                dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\n" + code));
            }
        } else dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nno"));
        dos.flush();
    }

}
