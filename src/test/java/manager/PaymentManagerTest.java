package manager;

import data.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PaymentManagerTest {

    private final PaymentManager paymentManager = new PaymentManager();

    @Test
    void reqPaySuccessTest() {
        Card card = new Card("1234-5678-9012-3456");

        String response = paymentManager.reqPay(card);

        assertEquals("ok", response);
    }

}
