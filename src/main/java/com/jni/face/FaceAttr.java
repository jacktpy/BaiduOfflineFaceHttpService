package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

// 获取人脸属性的类及demo
public class FaceAttr {

    public void testFaceAttr() {
         testFaceAttrByPath();
        // testFaceAttrByBuf();
        // testFaceAttrByMat();
        // testFaceAttrByFace();
    }

    public void testFaceAttrByPath() {
        // 通过传入人脸图片地址获取人脸属性
        String strAttr = Face.faceAttr("d:/2.jpg");
        System.out.println("strAttr is:" + strAttr);
    }

    public void testFaceAttrByBuf() {
        // 通过传入图片二进制buffer获取人脸属性
        byte[] bufs = ImageBuf.getImageBuffer("d:/2.jpg");
        String strAttr = Face.faceAttrByBuf(bufs, bufs.length);
        System.out.println("strAttrBuf is:" + strAttr);
    }

    public void testFaceAttrByMat() {
        // 通过传入opencv视频帧人脸属性
        Mat mat = Imgcodecs.imread("d:/1.jpg");
        long matAddr = mat.getNativeObjAddr();
        String strAttrMat = Face.faceAttrByMat(matAddr);
        System.out.println("strAttrMat is:" + strAttrMat);
    }

    public void testFaceAttrByFace() {
        // 传入opencv视频帧及检测到的人脸信息，适应于多人脸
        int objNum = 3;
        Mat mat = Imgcodecs.imread("d:/1.jpg");
        long matAddr = mat.getNativeObjAddr();
        TrackFaceInfo[] out = Face.trackByMat(matAddr, objNum);
        if (out != null && out.length > 0) {
            for (int index = 0; index < out.length; index++) {
                TrackFaceInfo info = out[index];
                String strAttr = Face.faceAttrByFace(matAddr, info);
                System.out.println("faceAttrByFace is:" + strAttr);
            }
        }
    }

}
