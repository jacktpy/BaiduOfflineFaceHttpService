package com.jni.face;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.videoio.VideoCapture;

import java.awt.*;

public class FaceTrack {
    // 人脸检测（传入图片文件路径）
    // 最大人脸检测（传入图片文件路径）
    // 最大人脸检测（传入图片二进制文件buffer)
    // usb 摄像头实时人脸检测
    // ShowVideo video1 = new ShowVideo();
    // FaceLivenessTrackType type = FaceLivenessTrackType.TRACKBYMAT;
    /*
     * std::vector<TrackFaceInfo> *& out:传出的检测后人脸信息 const char* img : Input img,1 is base64 img, 2 is file path int
     * imgType : Input img's coding type ,1 is base64 img, 2 is file path int maxTrackObjNum:
     * 最多要tracking人数，例如设置为1则只tracking一个人，如果设置为3则最多tracking 3个人。 return 为返回的检测人脸数量
     */
    public void testFaceTrack() {
        // 传图片地址方式人脸检测
        // testTrackByImgPath();
        // testTrackByImgBuf();
        testTrackByMat();

    }

    // 传图片地址方式人脸检测
    public void testTrackByImgPath() {
        String imgPath = "d:/2.jpg";
        // 参数3为设置的最大检测人数
        TrackFaceInfo[] out = Face.track(imgPath, 3);
        if (out != null) {
            for (int i = 0; i < out.length; i++) {
                TrackFaceInfo info = out[i];
                if (info != null) {
                    System.out.println("i info" + info.width);
                }
            }
        }
    }

    // 传图片二进制buf方式人脸检测
    public void testTrackByImgBuf() {
        byte[] bufs = ImageBuf.getImageBuffer("d:/2.jpg");
        // 参数3为设置的最大检测人数
        TrackFaceInfo[] out = Face.trackByBuf(bufs, bufs.length, 3);
        if (out != null) {
            for (int i = 0; i < out.length; i++) {
                TrackFaceInfo info = out[i];
                if (info != null) {
                    System.out.println("i info" + info.width);
                }
            }
        }
    }

    // 传图片opencv视频帧方式人脸检测
    public void testTrackByMat() {

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
        while (true) {
            boolean have = capture.read(frame);
            Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                break;
            }
            if (!frame.empty()) {
                long matAddr = frame.getNativeObjAddr();
                int maxFace = 3; // 检测最大人脸数
                TrackFaceInfo[] infos = Face.trackByMat(matAddr, maxFace);
                if (infos != null) {
                    int size = infos.length;
                    for (int i = 0; i < size; i++) {
                        TrackFaceInfo info = infos[i];
                        RotatedRect box = ShowVideo.boundingBox(info.landmarks, info.landmarks.length);
                        ShowVideo.drawRotatedBox(frame, box, new Scalar(0, 255, 0));
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

    // 检测最大人脸
    public void testFaceTrackMax() {
        // testTrackMaxByImgPath();
        // testTrackMaxByImgBuf();
        testTrackMaxByMat();

    }

    // 传图片地址方式最大人脸检测
    public void testTrackMaxByImgPath() {
        String imgPath = "d:/2.jpg";
        // 参数3为设置的最大检测人数
        TrackFaceInfo info = Face.trackMax(imgPath);
        if (info != null) {
            System.out.println("i info" + info.width);
        }

    }

    // 传图片二进制buf方式最大人脸检测
    public void testTrackMaxByImgBuf() {
        byte[] bufs = ImageBuf.getImageBuffer("d:/2.jpg");
        // 参数3为设置的最大检测人数
        TrackFaceInfo info = Face.trackMaxByBuf(bufs, bufs.length);
        if (info != null) {
            System.out.println("i info" + info.width);
        }

    }

    // 传图片opencv视频帧方式最大人脸检测
    public void testTrackMaxByMat() {
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
        while (true) {
            boolean have = capture.read(frame);
            Core.flip(frame, frame, 1); // Win上摄像头
            if (!have) {
                break;
            }
            if (!frame.empty()) {
                long matAddr = frame.getNativeObjAddr();

                TrackFaceInfo info = Face.trackMaxByMat(matAddr);
                if (info != null) {
                    RotatedRect box = ShowVideo.boundingBox(info.landmarks, info.landmarks.length);
                    ShowVideo.drawRotatedBox(frame, box, new Scalar(0, 255, 0));

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

}
