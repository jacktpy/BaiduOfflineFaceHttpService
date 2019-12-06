package com.jni.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

import java.awt.*;

public class FaceCompare {
    public void testFaceCompare() {
        // 测试获取特征值
        // testFaceFeature();
        // 测试人脸比对
         testFaceMatch();
        // 测试人脸1：N识别(和预加载的库比较)
        // testFaceIdentifyWithDB();
        // 测试人脸1：N识别(和实时查找的库比较)
       //  testFaceIdentify();
        // 测试人脸比对(图片和视频帧比对)
      //  testFaceMatchByImgAndFrame();
    }

    // 1:1人脸比对比接口
    public void testFaceMatch() {
        // 通过图片文件路径对比
        String res = Face.match("C:\\Users\\huangsq\\Desktop\\jianhongface\\1.jpg", "C:\\Users\\huangsq\\Desktop\\jianhongface\\4.jpg");
        System.out.println("path match is:" + res);

        // 人脸对比（传入opencv视频帧）
        Mat mat1 = Imgcodecs.imread("C:\\Users\\huangsq\\Desktop\\jianhongface\\1.jpg");
        Mat mat2 = Imgcodecs.imread("C:\\Users\\huangsq\\Desktop\\jianhongface\\4.jpg");

        long matAddr1 = mat1.getNativeObjAddr();
        long matAddr2 = mat2.getNativeObjAddr();
        res = Face.matchByMat(matAddr1, matAddr2);
        System.out.println("mat match is:" + res);

    }

    // 图片和视频帧比对
    public void testFaceMatchByImgAndFrame() {
        System.loadLibrary("./opencv-dll/opencv_java320");

        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id
        VideoCapture capture = new VideoCapture();
        capture.open(0);

        if (!capture.isOpened()) {
            System.out.println("could not load video data...");
            return;
        }
        int frameWidth = (int) capture.get(3);
        int frameHeight = (int) capture.get(4);
        ImageGUI gui = new ImageGUI();
        gui.createWin("video", new Dimension(frameWidth, frameHeight));
        Mat frame = new Mat();
        // 提取要比对的图片特征值
        byte[] features = Face.getFaceFeature("d:/2.jpg");
        int index = 0;
        while (true) {
            boolean have = capture.read(frame);
            Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                break;
            }
            if (!frame.empty()) {
                RotatedRect box;

                long matAddr = frame.getNativeObjAddr();
                TrackFaceInfo info = Face.trackMaxByMat(matAddr);
                if (info != null) {
                    box = ShowVideo.boundingBox(info.landmarks, info.landmarks.length);
                    ShowVideo.drawRotatedBox(frame, box, new Scalar(0, 255, 0));
                    index++;
                    if (index >= 5) {
                        // 提取视频帧的特征值
                        byte[] feaMat = Face.getFaceFeatureByFace(matAddr, info);
                        // 特征值为2048个byte,此处可设置为每5帧比对一次，否则，每次都比对可能导致视频卡顿
                        if (features.length == 2048 && feaMat.length == 2048) {
                            float score = Face.compareFeature(features, features.length, feaMat, feaMat.length);
                            System.out.println("compare score is:" + score);
                        }
                        index = 0;
                    }
                }

                gui.imshow(ShowVideo.conver2Image(frame));
                gui.repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 1:N比对(1和库里的N比对，库为比对时候实时查找)
    public void testFaceIdentify() {
        // 传入文件路径
        String res = Face.identify("d:/2.jpg", "test_group", "test_user1", 100);
        System.out.println("identify res is:" + res);

        // 人脸识别(传入opencv视频帧)
        Mat mat = Imgcodecs.imread("d:\\2.jpg");
        long matAddr = mat.getNativeObjAddr();
        res = Face.identifyByMat(matAddr, "test_group", "test_user1", 100);
        System.out.println("identifyByMat res is:" + res);

        // 人脸识别(传入二进制图片buf)
        byte[] outBuf = ImageBuf.getImageBuffer("d:\\1.jpg");

        res = Face.identifyByBuf(outBuf, outBuf.length, "test_group", "test_user1", 100);
        System.out.println("identifyByBuf res is:" + res);

        // 人脸识别(传入特征值feature)
        Mat mat3 = Imgcodecs.imread("d:\\2.jpg");
        long matAddr3 = mat3.getNativeObjAddr();
        byte[] features = Face.getFaceFeatureByMat(matAddr3);
        res = Face.identifyByFeature(features, features.length, "test_group", "test_user1", 100);
        System.out.println("identifyByFeature res is:" + res);

    }

    // 1:N比对(1和库里的N比对，库为比对时候先提前加载)
    public void testFaceIdentifyWithDB() {
        // 和整个库比较，需要提前加载库里数据到内存
        Face.loadDbFace();
        // 传入文件路径
        String res = Face.identifyDB("d:/1.jpg", 100);
        System.out.println("identifyDB res is:" + res);

        // 人脸识别(传入opencv视频帧)
        Mat mat = Imgcodecs.imread("d:\\1.jpg");
        long matAddr = mat.getNativeObjAddr();
        res = Face.identifyByMatDB(matAddr, 100);
        System.out.println("identifyByMatDB res is:" + res);

        // 人脸识别(传入二进制图片buf)
        byte[] outBuf = ImageBuf.getImageBuffer("d:\\1.jpg");

        res = Face.identifyByBufDB(outBuf, outBuf.length, 100);
        System.out.println("identifyByBufDB res is:" + res);

        // 人脸识别(传入特征值feature)
        Mat mat3 = Imgcodecs.imread("d:\\1.jpg");
        long matAddr3 = mat3.getNativeObjAddr();
        byte[] features = Face.getFaceFeatureByMat(matAddr3);
        res = Face.identifyByFeatureDB(features, features.length, 100);
        System.out.println("identifyByFeatureDB res is:" + res);

    }

    // 特征值比较
    public void testFaceFeature() {

        // 获取人脸特征值(传入图片)
        byte[] fea = Face.getFaceFeature("d:\\1.jpg");
        if (fea == null || fea.length != 2048) {
            System.out.println("get feature error");
        }
        // 获取人脸特征值(传入opencv视频帧)
        Mat mat3 = Imgcodecs.imread("d:\\2.jpg");
        long matAddr3 = mat3.getNativeObjAddr();
        byte[] feaMat = Face.getFaceFeatureByMat(matAddr3);
        if (feaMat == null || feaMat.length != 2048) {
            System.out.println("get feature error");
        }
        // 获取人脸特征值(传入二进制图片buf)
        byte[] outBuf = ImageBuf.getImageBuffer("d:\\1.jpg");
        byte[] feaBuf = Face.getFaceFeatureByBuf(outBuf, outBuf.length);
        if (feaBuf == null || feaBuf.length != 2048) {
            System.out.println("get feature error");
        }
        // 特征值比对

        Mat m1 = Imgcodecs.imread("d:\\1.jpg");
        long mAddr1 = m1.getNativeObjAddr();
        if (m1.empty()) {
            System.out.println("mat1 is empty,please check img path");
        }
        byte[] fea1 = Face.getFaceFeatureByMat(mAddr1);
        if (fea1 == null || fea1.length != 2048) {
            System.out.println("get feature1 error");
            return;
        }

        Mat m2 = Imgcodecs.imread("d:\\2.jpg");
        if (m2.empty()) {
            System.out.println("mat2 is empty,please check img path");
        }
        long mAddr2 = m2.getNativeObjAddr();
        byte[] fea2 = Face.getFaceFeatureByMat(mAddr2);
        if (fea2 == null || fea2.length != 2048) {
            System.out.println("get feature2 error");
            return;
        }
        float score = Face.compareFeature(fea1, fea1.length, fea2, fea2.length);
        System.out.println("compare score is:" + score);

    }
}
