package com.jni.face;

import com.jni.struct.EyeClose;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * 
 * @ 眼睛闭合检测示例
 *
 */
public class FaceEyeClose {
    //  眼睛闭合检测示例
    public int imageFaceEyeClose() {
        Mat mat = Imgcodecs.imread("images/2.jpg");
        long matAddr = mat.getNativeObjAddr();       
        EyeClose[] eyeList = Face.faceEyeClose(matAddr);
        if (eyeList == null || eyeList.length <= 0) {
            System.out.println("detect no face");
            return -1;
        }
        for (int i = 0; i < eyeList.length; i++) {
            // 第几个人脸
            System.out.println("face index is:" + i);
            // 人脸框宽度
            System.out.println("left eye close conf:" + eyeList[i].leftEyeCloseConf);
            // 人脸框高度
            System.out.println("right eye close conf:" + eyeList[i].rightEyeCloseConf);
        }
        return 0;
    }
}
