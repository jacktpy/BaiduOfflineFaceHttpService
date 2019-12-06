package com.jni.face;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

import java.awt.*;

public class FaceLiveness {

    public void testFaceLiveness() {
         rgbIrLivenessCheckByBuf();
//         rgbIrLivenessCheckByMat();

//         rgbDepthTrackWithOrbeMini();
//        rgbDepthTrackWithOrbeDeeyea();
        // rgbDepthTrackWithOrbePro();
        // rgbDepthTrackWithHjimi();

    }

    // 适配ir,rgb双目深度活体检测镜头
    static {
        System.loadLibrary("./opencv-dll/opencv_java320");
    }

    public void rgbIrLivenessCheckByBuf() {
        byte[] rgbBufs = ImageBuf.getImageBuffer("C:\\Users\\huangsq\\Desktop\\img\\4.jpg");
        byte[] irBufs = ImageBuf.getImageBuffer("C:\\Users\\huangsq\\Desktop\\img\\4.jpg");
        RgbIrInfo info = Face.rgbAndIrLivenessCheckByBuf(rgbBufs, rgbBufs.length, irBufs, irBufs.length);
        System.out.println("rgbScore:"+info.rgbScore);
        System.out.println("irScore:"+info.irScore);
        int k = 0;
        k++;
    }

    // 可见光+红外双目活体检测
    public void rgbIrLivenessCheckByMat() {
        System.loadLibrary("./opencv-dll/opencv_java320");
        // 打开摄像头或者视频文件
        // device为0默认打开笔记本电脑自带摄像头，若为0打不开外置usb摄像头
        // 请把device修改为1或2再重试，1，或2为usb插上电脑后，电脑认可的usb设备id(另外也可设备管理器中禁用电脑自带摄像头)
        VideoCapture cap1 = new VideoCapture();
        // 打开device 0
        cap1.open(0);
        if (!cap1.isOpened()) {
            System.out.println("could not load video1 data...");
            return;
        }
        VideoCapture cap2 = new VideoCapture();
        // 打开device 1
        cap2.open(1);
        if (!cap2.isOpened()) {
            System.out.println("could not load video2 data...");
            return;
        }
        int width1 = (int) cap1.get(3);
        int height1 = (int) cap1.get(4);
        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("frame1", new Dimension(width1, height1));
        Mat frame1 = new Mat();

        int width2 = (int) cap2.get(3);
        int height2 = (int) cap2.get(4);
        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("frame2", new Dimension(width2, height2));
        Mat frame2 = new Mat();
        int max_track = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        while (!stop) {
            boolean have1 = cap1.read(frame1);
            Core.flip(frame1, frame1, 1); // Win上摄像头
            if (!have1) {
                break;
            }

            boolean have2 = cap2.read(frame2);
            Core.flip(frame2, frame2, 1); // Win上摄像头
            if (!have2) {
                break;
            }

            if (!frame1.empty() && !frame2.empty()) {
                RotatedRect box;
                System.out.println("get frame ---");
                long matAddr1 = frame1.getNativeObjAddr();
                long matAddr2 = frame2.getNativeObjAddr();
                // 请区分rgb和ir对应的frame(参数第一个为rgb的视频帧，第二个为ir的视频帧)
                RgbIrInfo info = Face.rgbAndIrLivenessCheckByMat(matAddr1, matAddr2);

                if (info != null) {
                    TrackFaceInfo tfinfo = new TrackFaceInfo();
                    tfinfo.landmarks = info.landmarks;
                    box = ShowVideo.boundingBox(tfinfo.landmarks, tfinfo.landmarks.length);
                    ShowVideo.drawRotatedBox(frame1, box, new Scalar(0, 255, 0));
                    System.out.println("rgb score is:" + info.rgbScore);
                    System.out.println("ir score is:" + info.irScore);
                }

                gui1.imshow(ShowVideo.conver2Image(frame1));
                gui1.repaint();

                gui2.imshow(ShowVideo.conver2Image(frame2));
                gui2.repaint();
                index++;
                if (index > max_track) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // 奥比中光mini双目摄像头深度活体
    public void rgbDepthTrackWithOrbeMini() {

        System.loadLibrary("./opencv-dll/opencv_java320"); // 打开摄像头或者视频文件 VideoCapture capture1 = new VideoCapture();

        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("depth", new Dimension(480, 640));

        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("rgb", new Dimension(480, 640));

        Mat cvRgb = new Mat(480, 640, CvType.CV_8UC3);
        Mat cvDepth = new Mat(480, 640, CvType.CV_16UC1);
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();

        long camera = Face.newOrbeMini();
        int max_track = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        while (!stop) {
            int bopen = Face.openOrbeMini(camera, rgbAddr, depthAddr);

            if (!cvRgb.empty() && !cvDepth.empty()) {
                RgbDepthInfo info = Face.rgbAndDepthLivenessCheckByMat(rgbAddr, depthAddr);
                // RgbDepthInfo info = null;
                if (info != null) {
                    TrackFaceInfo tfinfo = new TrackFaceInfo();
                    tfinfo.landmarks = info.landmarks;
                    RotatedRect box = ShowVideo.boundingBox(tfinfo.landmarks, tfinfo.landmarks.length);
                    ShowVideo.drawRotatedBox(cvRgb, box, new Scalar(0, 255, 0));
                    System.out.println("rgb score is:" + info.rgbScore);
                    System.out.println("depth score is:" + info.depthScore);
                }

                Mat newDetph = new Mat();
                cvDepth.convertTo(newDetph, CvType.CV_8UC3);
                gui1.imshow(ShowVideo.conver2Image(newDetph));
                gui1.repaint();

                // 显示rgb图像
                gui2.imshow(ShowVideo.conver2Image(cvRgb));
                gui2.repaint();
                index++;
                if (index > max_track) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Face.deleteOrbeMini(camera);
                e.printStackTrace();
            }

        }
        // 释放镜头资源
        Face.deleteOrbeMini(camera);

    }

    // 奥比中光deeyea或atlas双目摄像头深度活体
    public void rgbDepthTrackWithOrbeDeeyea() {

        System.loadLibrary("./opencv-dll/opencv_java320"); // 打开摄像头或者视频文件
         VideoCapture capture1 = new VideoCapture();
        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("depth", new Dimension(480, 640));

        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("rgb", new Dimension(480, 640));

        Mat cvRgb = new Mat(480, 640, CvType.CV_8UC3);
        Mat cvDepth = new Mat(400, 640, CvType.CV_16UC1);
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();

        long camera = Face.newOrbeDeeyea();
        int max_track = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        while (!stop) {
            int bopen = Face.openOrbeDeeyea(camera, rgbAddr, depthAddr);
            rgbAddr = cvRgb.getNativeObjAddr();
            depthAddr = cvDepth.getNativeObjAddr();
            if (!cvRgb.empty() && !cvDepth.empty()) {

                RgbDepthInfo info = Face.rgbAndDepthLivenessCheckByMat(rgbAddr, depthAddr);
                if (info != null) {
                    TrackFaceInfo tfinfo = new TrackFaceInfo();
                    tfinfo.landmarks = info.landmarks;
                    RotatedRect box = ShowVideo.boundingBox(tfinfo.landmarks, tfinfo.landmarks.length);
                    ShowVideo.drawRotatedBox(cvRgb, box, new Scalar(0, 255, 0));
                    System.out.println("rgb score is:" + info.rgbScore);
                    System.out.println("depth score is:" + info.depthScore);
                }

                Mat newDetph = new Mat();
                cvDepth.convertTo(newDetph, CvType.CV_8UC3);
                gui1.imshow(ShowVideo.conver2Image(newDetph));
                gui1.repaint();
                // 显示rgb图像
                gui2.imshow(ShowVideo.conver2Image(cvRgb));
                gui2.repaint();
                index++;
                if (index > max_track) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Face.deleteOrbeDeeyea(camera);
                e.printStackTrace();
            }

        }
        // 释放镜头资源
        Face.deleteOrbeDeeyea(camera);

    }

    // 奥比中光Pro或Pro s1或蝴蝶双目摄像头深度活体(需要把OpenNI2\Drivers目录的Orbbec_pro.ini
    // 重命名为Orbbec.ini，镜头驱动才能生效
    public void rgbDepthTrackWithOrbePro() {

        System.loadLibrary("./opencv-dll/opencv_java320"); // 打开摄像头或者视频文件 VideoCapture capture1 = new VideoCapture();

        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("depth", new Dimension(480, 640));

        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("rgb", new Dimension(480, 640));

        Mat cvRgb = new Mat(480, 640, CvType.CV_8UC3);
        Mat cvDepth = new Mat(480, 640, CvType.CV_16UC1);
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();

        long camera = Face.newOrbeDeeyea();
        int max_track = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        while (!stop) {
            int bopen = Face.openOrbeDeeyea(camera, rgbAddr, depthAddr);
            rgbAddr = cvRgb.getNativeObjAddr();
            depthAddr = cvDepth.getNativeObjAddr();
            if (!cvRgb.empty() && !cvDepth.empty()) {

                RgbDepthInfo info = Face.rgbAndDepthLivenessCheckByMat(rgbAddr, depthAddr);
                if (info != null) {
                    TrackFaceInfo tfinfo = new TrackFaceInfo();
                    tfinfo.landmarks = info.landmarks;
                    RotatedRect box = ShowVideo.boundingBox(tfinfo.landmarks, tfinfo.landmarks.length);
                    ShowVideo.drawRotatedBox(cvRgb, box, new Scalar(0, 255, 0));
                    System.out.println("rgb score is:" + info.rgbScore);
                    System.out.println("depth score is:" + info.depthScore);
                }

                Mat newDetph = new Mat();
                cvDepth.convertTo(newDetph, CvType.CV_8UC3);
                gui1.imshow(ShowVideo.conver2Image(newDetph));
                gui1.repaint();
                // 显示rgb图像
                gui2.imshow(ShowVideo.conver2Image(cvRgb));
                gui2.repaint();
                index++;
                if (index > max_track) {
                    stop = true;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Face.deleteOrbeDeeyea(camera);
                e.printStackTrace();
            }

        }
        // 释放镜头资源
        Face.deleteOrbeDeeyea(camera);

    }

    // 华杰艾米双目摄像头深度活体
    public void rgbDepthTrackWithHjimi() {

        System.loadLibrary("./opencv-dll/opencv_java320");
        Mat cvRgb = new Mat();
        Mat cvDepth = new Mat();
        long rgbAddr = cvRgb.getNativeObjAddr();
        long depthAddr = cvDepth.getNativeObjAddr();
        long camera = Face.newHjimi();
        ImageGUI gui1 = new ImageGUI();
        gui1.createWin("frame1", new Dimension(640, 640));
        ImageGUI gui2 = new ImageGUI();
        gui2.createWin("frame2", new Dimension(640, 640));
        int max_track = 100000; // 假设的结束条件
        int index = 0;
        boolean stop = false;
        while (!stop) {
            boolean bok = Face.openHjimi(camera, rgbAddr, depthAddr);
            if (!bok) {
                System.out.println("open camera faile");
                Face.deleteHjimi(camera);
                return;
            }
            if (!cvRgb.empty() && !cvDepth.empty()) {
                RgbDepthInfo info = Face.rgbAndDepthLivenessCheckByMat(rgbAddr, depthAddr);
                if (info != null) {
                    TrackFaceInfo tfinfo = new TrackFaceInfo();
                    tfinfo.landmarks = info.landmarks;
                    RotatedRect box = ShowVideo.boundingBox(tfinfo.landmarks, tfinfo.landmarks.length);
                    ShowVideo.drawRotatedBox(cvRgb, box, new Scalar(0, 255, 0));
                    System.out.println("rgb score is:" + info.rgbScore);
                    System.out.println("depth score is:" + info.depthScore);
                }
                gui1.imshow(ShowVideo.conver2Image(cvDepth));
                gui1.repaint();
                gui2.imshow(ShowVideo.conver2Image(cvRgb));
                gui2.repaint();
            }
            index++;
            if (index > max_track){
                stop = true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Face.deleteOrbeMini(camera);
                e.printStackTrace();
            }
        }
        Face.deleteHjimi(camera);
    }
}
