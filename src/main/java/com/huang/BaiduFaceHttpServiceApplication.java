package com.huang;

import com.jni.face.Face;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author huangsq
 * @Date 2019/12/5 10:33
 */
@SpringBootApplication
public class BaiduFaceHttpServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaiduFaceHttpServiceApplication.class);
    }


    static {
        // sdk初始化
        Face api = new Face();
        // 初始化sdk
        // 若采用证件照模式，请把id_card设为true，否则为false，证件照模式和非证件照模式提取的人脸特征值不同，
        // 不能混用
        boolean idCard = true;
        api.sdkInit(idCard);
    }

}
