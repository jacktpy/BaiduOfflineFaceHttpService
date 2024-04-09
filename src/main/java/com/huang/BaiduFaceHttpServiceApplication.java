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
        /*  sdk初始化 */
        Face api = new Face();
        // model_path为模型文件夹路径，即models文件夹（里面存的是人脸识别的模型文件）
        // 传空为采用默认路径，若想定置化路径，请填写全局路径如：d:\\face （models模型文件夹目录放置后为d:\\face\\models）
        // 若模型文件夹采用定置化路径，则激活文件(license.ini, license.key)也可采用定制化路径放置到该目录如d:\\face\\license
        // 亦可在激活文件默认生成的路径
        String modelPath ="";
        // String modelPath ="d:\\face";
        int res = api.sdkInit(modelPath);
        if (res != 0) {
            System.out.printf("sdk init fail and error =%d\n", res);
        }else {
            System.out.println("sdk init success");
        }
        // 获取设备指纹
        String deviceId = Face.getDeviceId();
        System.out.println("device id:" + deviceId);
        // 获取版本号
        String ver = Face.sdkVersion();
        System.out.println("sdk version:" + ver);
    }

}
