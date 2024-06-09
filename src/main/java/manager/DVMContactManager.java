package manager;

import data.DVM;

import java.io.*;
import java.net.Socket;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import util.JsonParser;


public class DVMContactManager {

    private final List<String> anotherDVMAddresses = new ArrayList<>();

    public DVMContactManager() {
        anotherDVMAddresses.add("13.124.36.229:9001");
        anotherDVMAddresses.add("43.202.249.230:9001");
    }

    public DVM searchDrink(String drinkType, int drinkNum) {
        String message = String.format("{ \"msg_type\": \"req_stock\", \"src_id\": \"Team2\", \"dst_id\": \"0\", \"msg_content\": { \"item_code\": \"%s\", \"item_num\": %d } }",
                drinkType, drinkNum);

        List<String> list = new ArrayList<>();
        for (String address : anotherDVMAddresses) {
            try {
                // 소켓 생성 및 서버 연결
                String[] arr = address.split(":");
                Socket socket = new Socket(arr[0], Integer.parseInt(arr[1]));
                System.out.println("Connect to server: " + arr[0]);

                // 서버로 메시지 전송
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println(message);
                System.out.println("Send message: " + message);

                // 서버로부터 응답 수신
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String response = reader.readLine();
                System.out.println("Get Response: " + response);

                // JSON 응답 파싱
                JsonParser parser = new JsonParser();
                Map<String, Object> map = parser.parse(response);
                String msgType = map.get("msg_type").toString();

                if ("resp_stock".equals(msgType)) {
                    Map<String, Object> msgContent = (Map<String, Object>) map.get("msg_content");
                    String receivedDrinkType = msgContent.get("item_code").toString();
                    int receivedDrinkNum = (int)msgContent.get("item_num");
                    int coordX = (int)msgContent.get("coor_x");
                    int coordY = (int)msgContent.get("coor_y");

                    // 응답 분석
                    if (receivedDrinkType.equals(drinkType) && receivedDrinkNum >= drinkNum) {
                        list.add(coordX + " " + coordY + " " + address);
                    }
                }

                // 소켓 닫기
                socket.close();
            } catch (IOException e) {
                System.err.println("서버에 연결 실패: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                System.err.println("응답 파싱 실패: ");
                e.printStackTrace();
            }
        }

        return calculateNearestVM(list);
    }


    private DVM calculateNearestVM(List<String> list) {
        DVM nearestDVM = null;
        double nearestDistance = Double.MAX_VALUE;

        // 현재 위치를 기준으로 가장 가까운 자판기를 계산
        int currentX = 12; // 현재 위치의 X 좌표 (실제 좌표를 넣어야 함)
        int currentY = 7; // 현재 위치의 Y 좌표 (실제 좌표를 넣어야 함)

        for (String address : list) {
            String[] coords = address.split(" ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            double distance = Math.sqrt(Math.pow(x - currentX, 2) + Math.pow(y - currentY, 2));

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestDVM = new DVM(x, y, coords[2]);
            }
        }

        return nearestDVM;
    }

    public boolean reqAdvancePayment(String drinkType, int drinkNum, String code, DVM dvm) {
        //usecase4 : anotherVM과의 소통. 선결제 가능 여부 확인 요청
        //usecase6

        String message = String.format("{ \"msg_type\": \"req_prepay\", \"src_id\": \"Team2\", \"dst_id\": \"0\", \"msg_content\": { \"item_code\": \"%s\", \"item_num\": %d, \"cert_code\": \"%s\" } }",
                drinkType, drinkNum, code);
        try {
            String[] arr = dvm.getAddress().split(":");
            // 소켓 생성 및 서버 연결
            Socket socket = new Socket(arr[0], Integer.parseInt(arr[1]));

            // 서버로 메시지 전송
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            writer.println(message);
            System.out.println("Send advance payment msg: " + message);

            // 서버로부터 응답 수신
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String response = reader.readLine();
            System.out.println("Get response: " + response);

            // 응답 분석
            if (response != null && !response.isEmpty()) {
                JsonParser parser = new JsonParser();
                Map<String, Object> map = parser.parse(response);
                String msgType = map.get("msg_type").toString();

                if ("resp_prepay".equals(msgType)) {
                    Map<String, Object> msgContent = (Map<String, Object>) map.get("msg_content");
                    Boolean availability = (Boolean) msgContent.get("availability");
                    if (availability) {
                        // 소켓 닫기
                        socket.close();
                        return true;
                    }
                }
            }
            // 소켓 닫기
            socket.close();
        } catch (IOException e) {
            System.err.println("서버에 연결 실패");
            e.printStackTrace();
        } catch (JSONException e) {
            System.err.println("응답 파싱 실패: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}

