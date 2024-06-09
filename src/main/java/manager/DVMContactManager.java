package manager;

import data.DVM;

import java.io.*;
import java.net.Socket;
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;


public class DVMContactManager {

    private List<String> anotherDVMAddresses = new ArrayList<>();

    public DVM searchDrink(String drinkType, int drinkNum) {
        //usecase3, usecase11
        // AnotherVM에게 searchDrink(drinkType, drinkNum) 실행시키기. drinkInfo 리턴받기

        List<String> serverIps = List.of("turtle-hwan.iptime.org", "13.124.36.230", "13.124.36.231"); // 서버 IP 주소 목록들, 수정 요함

        String myServerIp = "13.124.36.229"; // 서버 IP 주소
        int port = 30303; // 서버 포트 번호

        String message = String.format("{ \"msg_type\": \"req_stock\", \"src_id\": \"Team2\", \"dst_id\": \"0\", \"msg_content\": { \"item_code\": \"%s\", \"item_num\": %d } }",
                drinkType, drinkNum);

        for (String serverIp : serverIps) {
            try {
                // 소켓 생성 및 서버 연결
                Socket socket = new Socket(serverIp, port);
                System.out.println("서버에 연결되었습니다: " + serverIp);

                // 서버로 메시지 전송
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println(message);
                System.out.println("메시지를 보냈습니다: " + message);

                // 서버로부터 응답 수신
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String response = reader.readLine();
                System.out.println("서버로부터 응답을 받았습니다: " + response);

                // JSON 응답 파싱
                JSONObject jsonResponse = new JSONObject(response);
                String msgType = jsonResponse.getString("msg_type");

                if ("resp_stock".equals(msgType)) {
                    JSONObject msgContent = jsonResponse.getJSONObject("msg_content");
                    String receivedDrinkType = msgContent.getString("item_code");
                    int receivedDrinkNum = msgContent.getInt("item_num");
                    int coordX = msgContent.getInt("coor_x");
                    int coordY = msgContent.getInt("coor_y");

                    // 응답 분석
                    if (receivedDrinkType.equals(drinkType) && receivedDrinkNum >= drinkNum) {
                        System.out.println("재고가 충분합니다.");
                        anotherDVMAddresses.add(coordX + " " + coordY);
                    } else {
                        System.out.println("재고가 충분하지 않습니다.");
                    }
                }

                // 소켓 닫기
                socket.close();
                System.out.println("소켓을 닫았습니다.");
            } catch (IOException e) {
                System.err.println("서버에 연결 실패: " + e.getMessage());
                e.printStackTrace();
            } catch (JSONException e) {
                System.err.println("응답 파싱 실패: ");
                e.printStackTrace();
            }
        }

        return calculateNearestVM();
    }


    private DVM calculateNearestVM() {
        DVM nearestDVM = null;
        double nearestDistance = Double.MAX_VALUE;

        // 현재 위치를 기준으로 가장 가까운 자판기를 계산
        int currentX = 1; // 현재 위치의 X 좌표 (실제 좌표를 넣어야 함)
        int currentY = 1; // 현재 위치의 Y 좌표 (실제 좌표를 넣어야 함)

        for (String address : anotherDVMAddresses) {
            String[] coords = address.split(" ");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            double distance = Math.sqrt(Math.pow(x - currentX, 2) + Math.pow(y - currentY, 2));

            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestDVM = new DVM(x, y);
            }
        }

        return nearestDVM;
    }

    public boolean reqAdvancePayment(String drinkType, int drinkNum, String code) {
        //usecase4 : anotherVM과의 소통. 선결제 가능 여부 확인 요청
        //usecase6

        List<String> serverIps = List.of("13.124.36.229", "13.124.36.230", "13.124.36.231"); // 서버 IP 주소 목록들, 수정 요함

        String myServerIp = "13.124.36.229"; // 서버 IP 주소
        int port = 9001; // 서버 포트 번호

        String message = String.format("{ \"msg_type\": \"req_prepay\", \"src_id\": \"Team2\", \"dst_id\": \"0\", \"msg_content\": { \"item_code\": \"%s\", \"item_num\": %d, \"cert_code\": \"%s\" } }",
                drinkType, drinkNum, code);

        for (String serverIp : serverIps) {
            try {
                // 소켓 생성 및 서버 연결
                Socket socket = new Socket(serverIp, port);
                System.out.println("서버에 연결되었습니다: " + serverIp);

                // 서버로 메시지 전송
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                writer.println(message);
                System.out.println("메시지를 보냈습니다: " + message);

                // 서버로부터 응답 수신
                InputStream inputStream = socket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String response = reader.readLine();
                System.out.println("서버로부터 응답을 받았습니다: " + response);

                // 응답 분석
                if (response != null && !response.isEmpty()) {
                    JSONObject jsonResponse = new JSONObject(response);
                    String msgType = jsonResponse.getString("msg_type");

                    if ("resp_prepay".equals(msgType)) {
                        JSONObject msgContent = jsonResponse.getJSONObject("msg_content");
                        String availability = msgContent.getString("availability");
                        if ("T".equals(availability)) {
                            // 소켓 닫기
                            socket.close();
                            System.out.println("소켓을 닫았습니다.");
                            return true;
                        }
                    }
                }

                // 소켓 닫기
                socket.close();
                System.out.println("소켓을 닫았습니다.");

            } catch (IOException e) {
                System.err.println("서버에 연결 실패: " + serverIp);
                e.printStackTrace();
            } catch (JSONException e) {
                System.err.println("응답 파싱 실패: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

}

