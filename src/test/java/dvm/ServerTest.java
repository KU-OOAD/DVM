package dvm;

import data.Drink;
import db.DrinkDBManager;
import org.junit.jupiter.api.*;
import util.JsonParser;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerTest {

    private static Thread serverThread;
    private final String ip = "localhost";
    private final int port = 9001;

    @BeforeAll
    static void setUp() {
        serverThread = new Thread(() -> {
            try {
                String[] args = {""};
                DVMSimulationServer.main(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    @Test
    void serverRunningTest() throws IOException {
        Socket socket = new Socket(ip, port);

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        writer.println("GET /error HTTP/1.1");

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String response = builder.toString();

        String[] token = response.split(" ");
        Assertions.assertEquals(token[1], "200");

        socket.close();
    }

    @Test
    void getDrinkTest() throws IOException {
        Socket socket = new Socket(ip, port);

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        writer.println("GET /drink HTTP/1.1");

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();
        String[] body = response.split("\n\n");

        DrinkDBManager dbManager = DrinkDBManager.getManager();
        List<Drink> list = dbManager.getMenuList();

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
        Assertions.assertEquals(body[1], str.toString());
        //System.out.println(body[1]);

        socket.close();
    }

    @Test
    void reqDrinkQuantityTest() throws IOException {
        Socket socket = new Socket(ip, port);
        String jsonBody = "{" +
                "  \"msg_type\": \"req_stock\"," +
                "  \"src_id\": \"Team1\"," +
                "  \"dst_id\": \"0\"," +
                "  \"msg_content\": {" +
                "    \"item_code\": \"05\"," +
                "    \"item_num\": 25" +
                "  }" +
                "}";

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        writer.println(jsonBody);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();

        JsonParser parser = new JsonParser();
        parser.parse(response);
        Map<String, Object> map = parser.parse(response);
        Map<String, Object> content = (Map<String, Object>) map.get("msg_content");

        assertEquals(map.get("msg_type").toString(), "resp_stock", "request drink quantity method failed!");
        assertEquals(map.get("src_id").toString(), "Team2", "request drink quantity method failed!");
        assertEquals(map.get("dst_id").toString(), "Team1", "request drink quantity method failed!");

        assertEquals(content.get("item_code").toString(), "05", "request drink quantity method failed!");
        assertEquals(content.get("coor_x"), 12, "request drink quantity method failed!");
        assertEquals(content.get("coor_y"), 7, "request drink quantity method failed!");

        socket.close();
    }

    @Test
    void reqAdvancePaymentTest() throws IOException {
        Socket socket = new Socket(ip, port);
        String jsonBody = "{" +
                "  \"msg_type\": \"req_prepay\"," +
                "  \"src_id\": \"Team1\"," +
                "  \"dst_id\": \"0\"," +
                "  \"msg_content\": {" +
                "    \"item_code\": \"05\"," +
                "    \"item_num\": 5," +
                "    \"cert_code\": \"cxf01\"" +
                "  }" +
                "}";

        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        writer.println(jsonBody);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();

        JsonParser parser = new JsonParser();
        parser.parse(response);
        Map<String, Object> map = parser.parse(response);
        Map<String, Object> content = (Map<String, Object>) map.get("msg_content");

        assertEquals(map.get("msg_type").toString(), "resp_stock", "request drink quantity method failed!");
        assertEquals(map.get("src_id").toString(), "Team2", "request drink quantity method failed!");
        assertEquals(map.get("dst_id").toString(), "Team1", "request drink quantity method failed!");

        assertEquals(content.get("item_code").toString(), "05", "request drink quantity method failed!");
        assertEquals(content.get("item_num"), 5, "request drink quantity method failed!");
        assertEquals(content.get("availability"), true, "request drink quantity method failed!");

        socket.close();
    }

    @Test
    @Order(1)
    void setDrinkTest() throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        String body = "01";
        writer.println("POST /pay/setDrink HTTP/1.1\r\nContent-Length: " + body.length() + "\r\n\r\n" + body);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();
        String[] res = response.split("\n\n");
        socket.close();

        assertEquals(res[1], "ok\n");
    }

    @Test
    @Order(2)
    void setDrinkNumTest() throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        String body = "2";
        writer.println("POST /pay/setDrinkNum HTTP/1.1\r\nContent-Length: " + body.length() + "\r\n\r\n" + body);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();
        String[] res = response.split("\n\n");
        socket.close();

        assertEquals(res[1], "ok\n");
    }

    @Test
    @Order(3)
    void isPayAvailableTest() throws IOException {
        Socket socket = new Socket(ip, port);
        OutputStream os = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(os, true);
        String body = "";
        writer.println("GET /pay/isPayAvailable HTTP/1.1\r\nContent-Length: " + body.length() + "\r\n\r\n" + body);

        InputStream inputStream = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder builder = new StringBuilder();
        String line;
        while((line = reader.readLine()) != null) {
            builder.append(line).append("\n");
        }
        String response = builder.toString();
        String[] res = response.split("\n\n");
        socket.close();

        assertEquals(res[1], "ok\n");
    }

}
