package controller;

import java.io.*;
import java.net.Socket;
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
    }

    private void login(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK \r\n Content Type: text/html;charset=utf-8 \r\n\r\nok"));
        dos.flush();
    }

    private void logout(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK \r\n Content Type: text/html;charset=utf-8 \r\n\r\nok"));
        dos.flush();
    }

    private void manageDrink(DataOutputStream dos, String body) throws IOException {
        dos.writeBytes(("HTTP/1.1 200 OK \r\n Content Type: text/html;charset=utf-8 \r\n\r\n ok"));
        dos.flush();
    }

}