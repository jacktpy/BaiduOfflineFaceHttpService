package com.jni.face;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

// 获取人脸质量的类及demo
public class FaceQuality {

    public void testFaceQuality() {
        testFaceQualByPath();
        testFaceQualByBuf();
        testFaceQualByMat();
        testFaceQualByFace();
    }

    public void testFaceQualByPath() {
        // 通过传入人脸图片地址获取人脸质量
        String strQual = Face.faceQuality("C:\\Users\\huangsq\\Desktop\\img\\1.jpg");
        System.out.println("strAttr is:" + strQual);
    }

    public void testFaceQualByBuf() {
        // 通过传入图片二进制buffer获取人脸质量
        byte[] bufs = ImageBuf.getImageBuffer("d:/2.jpg");
        String strQual = Face.faceQualityByBuf(bufs, bufs.length);
        System.out.println("strAttrBuf is:" + strQual);
    }

    public void testFaceQualByMat() {
        // 通过传入opencv视频帧人脸质量
        Mat mat = Imgcodecs.imread("d:/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        String strQualMat = Face.faceQualityByMat(matAddr);
        System.out.println("strAttrMat is:" + strQualMat);
    }

    public void testFaceQualByFace() {
        // 传入opencv视频帧及检测到的人脸信息，适应于多人脸
        int objNum = 3;
        Mat mat = Imgcodecs.imread("d:/2.jpg");
        long matAddr = mat.getNativeObjAddr();
        TrackFaceInfo[] out = Face.trackByMat(matAddr, objNum);
        if (out != null && out.length > 0) {
            for (int index = 0; index < out.length; index++) {
                TrackFaceInfo info = out[index];
                String strQual = Face.faceQualityByFace(matAddr, info);
                System.out.println("faceAttrByFace is:" + strQual);
            }
        }
    }
}
