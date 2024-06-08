package manager;

import data.VerificationCode;
import db.VerificationCodeDBManager;

import java.util.Random;

public class VerificationManager {

    public String getVerificationCode() {
        // TODO implement here
        //StringBuilder stringBuilder = new StringBuilder();

//        while(true){
//            Random random = new Random();
//
//            for(int i=0; i<10; i++){ //랜덤한 난수 생성
//                if(random.nextBoolean()){
//                    stringBuilder.append(random.nextInt(10));
//                }else{ //랜덤한 알파벳 생성
//                    if(random.nextBoolean()){ //랜덤한 대문자 알파벳 생성
//                        stringBuilder.append((char)(random.nextInt(26)+65));
//                    }else{ //랜덤한 소문자 알파벳 생성
//                        stringBuilder.append((char)(random.nextInt(26)+97));
//                    }
//                }
//            }
//
//            String tempCode = stringBuilder.toString();
//            //여기서 반환값이 bool 이니까 if 문에 그냥 조건 하나만 더 추가할게.
//            VerificationCode verifyCodeResult = verifyCode(tempCode);
//
//            if(verifyCodeResult.getCode().isEmpty() && verifyCodeResult.getDrinkType().isEmpty() && verifyCodeResult.getDrinkNum()==0){ //중복 인증코드 없음
//                boolean codeSaveSuccessResult = saveCode(tempCode); //인증코드 저장
//
//                if(codeSaveSuccessResult){ //코드 저장 성공 여부 확인
//                    break; //코드 저장 성공
//                }
//                //코드 저장 실패. 코드 재생성 필요함
//            }
//            //중복 인증코드 있음. 코드 재생성 필요함
//            stringBuilder.setLength(0); //stringBuilder 초기화
//        }

        return "adsf13";
    }////////VerificationManage의 getVerificationCode 매개변수를 void -> String drinkType, int drinkNum 로 바꿔야 함


    public boolean saveCode(VerificationCode verificationCode) {
        // TODO implement here
        VerificationCodeDBManager verificationCodeDBManager = VerificationCodeDBManager.getManager();
        return verificationCodeDBManager.saveCode(verificationCode); //verificationCodeDBManager에게 인증코드 저장 요청
    }

    public VerificationCode verifyCode(String code) {
        // TODO implement here
        VerificationCodeDBManager verificationCodeDBManager = VerificationCodeDBManager.getManager();
        return verificationCodeDBManager.checkCode(code); //verificationCodeDBManager에게 중복 인증코드인지 확인
    }
}