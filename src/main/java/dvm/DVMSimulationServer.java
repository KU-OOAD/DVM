package dvm;

import controller.Controller;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DVMSimulationServer {

    public static void main(String[] args) {
        ControllerMapper mapper = new ControllerMapper();

        int port = 9001;
        ExecutorService service = Executors.newFixedThreadPool(50);

        System.out.println("Server is now loading...");

        try (ServerSocket welcomeSocket = new ServerSocket(port)){
            Socket connection;
            while ((connection = welcomeSocket.accept()) != null) {
                System.out.println("Waiting request...");
                Socket finalConnection = connection;
                // set task to new thread & execute controller
                service.submit(() -> {
                    try(InputStream is = finalConnection.getInputStream();
                        OutputStream os = finalConnection.getOutputStream()) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        String url = "";
                        String startLine = "";
                        char c = (char) br.read();
                        if(c == '{') {
                            url = "/message";
                        } else startLine = br.readLine();
                        String[] startLines = startLine.split(" ");
                        if(url.isBlank()) url = startLines[1];
                        System.out.println("url: " + url);

                        Controller controller = mapper.getController(url);
                        if(controller == null) {
                            DataOutputStream dos =  new DataOutputStream(finalConnection.getOutputStream());
                            dos.writeBytes(("HTTP/1.1 200 OK\r\nContent Type: text/html;charset=utf-8\r\n\r\nError! Wrong url!"));
                            dos.flush();
                            dos.close();
                        } else controller.execute(url, br, os);
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
