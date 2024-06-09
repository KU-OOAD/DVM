package manager;

import data.Card;

import java.util.Random;

public class PaymentManager {

    public String reqPay(Card card) {
        class Bank{
            public String reqPay(Card card){
                Random random = new Random();
                return "ok";
//                if(random.nextBoolean()){ //결제 성공
//                    return "ok";
//                }else { //결제 실패
//                    if(random.nextBoolean()){ //"exceed card limits"로 인한 실패
//                        return "errMsg:ExceedLimits";
//                    }else{ //"fail to purchase"로 인한 실패
//                        return "errMsg:failPurchase";
//                    }
//                }
            }
        }
        Bank bank = new Bank();

        return bank.reqPay(card);
    }

}