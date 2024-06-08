package dvm;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

public class ServerTest {

    private final DVMSimulationServer server = new DVMSimulationServer();

    @Test
    void messageControllerTest() throws IOException {
        String[] args = {"", ""};
        DVMSimulationServer.main(args);

        String ip = "127.0.0.1";
        int port = 9001;

        Socket socket = new Socket(ip, port);

    }

}
