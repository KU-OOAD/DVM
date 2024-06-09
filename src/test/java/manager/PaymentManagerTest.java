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

    private PaymentManager paymentManager;
//    private Random mockRandom;

    @BeforeEach
    void setUp() {

    }

    @Test
    void reqPaySuccessTest() {
        Card card = new Card("1234-5678-9012-3456");

        class Bank{
            public String reqPay(Card card){
                Random random = new Random();

                if(true){ //결제 성공
                    return "ok";
                }else { //결제 실패
                    if(random.nextBoolean()){ //"exceed card limits"로 인한 실패
                        return "errMsg:ExceedLimits";
                    }else{ //"fail to purchase"로 인한 실패
                        return "errMsg:failPurchase";
                    }
                }
            }
        }
        Bank bank = new Bank();
        String result = bank.reqPay(card);

        assertEquals("ok", result);
    }

    @Test
    void reqPayExceedLimitsTest() {
        Card card = new Card("1234-5678-9012-3456");

        class Bank{
            public String reqPay(Card card){
                Random random = new Random();

                if(false){ //결제 성공
                    return "ok";
                }else { //결제 실패
                    if(true){ //"exceed card limits"로 인한 실패
                        return "errMsg:ExceedLimits";
                    }else{ //"fail to purchase"로 인한 실패
                        return "errMsg:failPurchase";
                    }
                }
            }
        }
        Bank bank = new Bank();
        String result = bank.reqPay(card);

        assertEquals("errMsg:ExceedLimits", result);
    }

    @Test
    void reqPayFailPurchaseTest() {
        Card card = new Card("1234-5678-9012-3456");

        class Bank{
            public String reqPay(Card card){
                Random random = new Random();

                if(false){ //결제 성공
                    return "ok";
                }else { //결제 실패
                    if(false){ //"exceed card limits"로 인한 실패
                        return "errMsg:ExceedLimits";
                    }else{ //"fail to purchase"로 인한 실패
                        return "errMsg:failPurchase";
                    }
                }
            }
        }
        Bank bank = new Bank();
        String result = bank.reqPay(card);

        assertEquals("errMsg:failPurchase", result);
    }
}
