package manager;

import data.VerificationCode;
import db.VerificationCodeDBManager;

import java.util.Random;

public class VerificationManager {

    public String getVerificationCode() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        for(int i=0; i<10; i++){ //랜덤한 난수 생성
            if(random.nextBoolean()){
                stringBuilder.append(random.nextInt(10));
            }else{ //랜덤한 알파벳 생성
                if(random.nextBoolean()){ //랜덤한 대문자 알파벳 생성
                    stringBuilder.append((char)(random.nextInt(26)+65));
                }else{ //랜덤한 소문자 알파벳 생성
                    stringBuilder.append((char)(random.nextInt(26)+97));
                }
            }
        }

        return stringBuilder.toString();
    }////////VerificationManage의 getVerificationCode 매개변수를 void -> String drinkType, int drinkNum 로 바꿔야 함


    public boolean saveCode(VerificationCode verificationCode) {
        VerificationCodeDBManager verificationCodeDBManager = VerificationCodeDBManager.getManager();
        return verificationCodeDBManager.saveCode(verificationCode); //verificationCodeDBManager에게 인증코드 저장 요청
    }

    public VerificationCode verifyCode(String code) {
        VerificationCodeDBManager verificationCodeDBManager = VerificationCodeDBManager.getManager();
        return verificationCodeDBManager.checkCode(code); //verificationCodeDBManager에게 중복 인증코드인지 확인
    }
}