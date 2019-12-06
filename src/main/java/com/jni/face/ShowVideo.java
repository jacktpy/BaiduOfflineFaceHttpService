package com.jni.face;

import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowVideo {

    static {
        System.loadLibrary("./opencv-dll/opencv_java320");
    }

    public void show(FaceLivenessTrackType type) {
        System.loadLibrary("./opencv-dll/opencv_java320");

        // 打开摄像头或者视频文件
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
                RotatedRect box;
                int maxTrackObjNum = 3;
                switch (type) {
                    case TRACKBYMAT: {
                        long matAddr = frame.getNativeObjAddr();
                        TrackFaceInfo[] out = Face.trackByMat(matAddr, maxTrackObjNum);
                        for (int index = 0; index < out.length; index++) {
                            box = boundingBox(out[index].landmarks, out[index].landmarks.length);
                            drawRotatedBox(frame, box, new Scalar(0, 255, 0));
                        }
                        break;
                    }
                    // case IRLIVENESSCHECKBYMAT:
                    // break;
                    // 双目RGB和IR静脉活体检测(传入opencv的视频帧)
                    // case RGBIRLIVENESSCHECK:
                    // res = Face.rgbIrLivenessCheck(long rgb_mat, long ir_mat,
                    // float rgb_score, float ir_score);
                    // System.out.println("rgbIrLivenessCheck res :"+res);
                    // break;
                    // 双目RGB和IR静脉活体检测(传入opencv的视频帧,返回rgb视频帧人脸信息)
                    // case RGBIRLIVENESSCHECKREINFO:
                    // res = Face.rgbIrLivenessCheckReInfo(TrackFaceInfo[] out, long rgb_mat, long ir_mat,
                    // float rgb_score, float ir_score);
                    // break;
                    // case RGBLIVENESSCHECKBYMAT:
                    // break;
                    // 双目RGB和depth静脉活体检测(传入opencv的视频帧)
                    // case RGBDEPTHLIVENESSCHECK:
                    // res = Face.rgbDepthLivenessCheck(long rgb_mat, long depth_mat,
                    // float rgb_score, float depth_score);
                    // break;
                    // 双目RGB和depth静脉活体检测(传入opencv的视频帧,返回rgb视频帧人脸信息)
                    // case RGBDEPTHLIVENESSCHECKREINFO:
                    // res = Face.rgbDepthLivenessCheckReInfo(TrackFaceInfo[] out, long rgb_mat, long depth_mat,
                    // float rgb_score, float depth_score);
                    // break;
                    // case GETFACEFEATUREBYMAT:
                    // break;
                    // 获取人脸特征值（传入opencv视频帧及人脸信息，适应于多人脸）
                    case GETFACEFEATUREBYFACE: {
                        /*
                         * float[] fea = new float[512]; long matAddr1 = frame.getNativeObjAddr(); TrackFaceInfo[] out1
                         * = Face.trackByMat(matAddr1, maxTrackObjNum); for (int index = 0; index < out1.length; index
                         * ++) { int dimcount = Face.getFaceFeatureByFace(matAddr1, out1[index], fea); }
                         */
                        break;
                    }
                    default:
                        break;
                }

                gui.imshow(conver2Image(frame));
                gui.repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static RotatedRect boundingBox(int[] landmarks, int size) {
        int minX = 1000000;
        int minY = 1000000;
        int maxX = -1000000;
        int maxY = -1000000;
        for (int i = 0; i < size / 2; ++i) {
            minX = (minX < landmarks[2 * i] ? minX : landmarks[2 * i]);
            minY = (minY < landmarks[2 * i + 1] ? minY : landmarks[2 * i + 1]);
            maxX = (maxX > landmarks[2 * i] ? maxX : landmarks[2 * i]);
            maxY = (maxY > landmarks[2 * i + 1] ? maxY : landmarks[2 * i + 1]);
        }
        int width = ((maxX - minX) + (maxY - minY)) / 2;
        float angle = 0;
        Point center = new Point((minX + maxX) / 2, (minY + maxY) / 2);
        return new RotatedRect(center, new Size(width, width), angle);
    }

    public static void drawRotatedBox(Mat img, RotatedRect box, Scalar color) {
        Point[] vertices = new Point[4];
        box.points(vertices);
        for (int j = 0; j < 4; j++) {
            Imgproc.line(img, vertices[j], vertices[(j + 1) % 4], color);
        }
    }

    public static BufferedImage conver2Image(Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int dims = mat.channels();
        int[] pixels = new int[width * height];
        byte[] rgbdata = new byte[width * height * dims];
        mat.get(0, 0, rgbdata);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        int index = 0;
        int r = 0;
        int g = 0;
        int b = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (dims == 3) {
                    index = row * width * dims + col * dims;
                    b = rgbdata[index] & 0xff;
                    g = rgbdata[index + 1] & 0xff;
                    r = rgbdata[index + 2] & 0xff;
                    pixels[row * width + col] =
                            ((255 & 0xff) << 24) | ((r & 0xff) << 16) | ((g & 0xff) << 8) | b & 0xff;
                }
                if (dims == 1) {
                    index = row * width + col;
                    b = rgbdata[index] & 0xff;
                    pixels[row * width + col] =
                            ((255 & 0xff) << 24) | ((b & 0xff) << 16) | ((b & 0xff) << 8) | b & 0xff;
                }
            }
        }
        setRGB(image, 0, 0, width, height, pixels);
        return image;
    }

    public static void setRGB(BufferedImage image, int x, int y, int width, int height, int[] pixels) {
        int type = image.getType();
        if (type == BufferedImage.TYPE_INT_ARGB || type == BufferedImage.TYPE_INT_RGB) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }

}
