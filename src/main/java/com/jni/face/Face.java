package com.jni.face;

// import org.opencv.core.Core;

public class Face {
    // sdk初始化
    public native void sdkInit(boolean idCard);

    // sdk销毁（资源释放)
    public native void sdkDestroy();

    // 判断是否授权
    public native boolean isAuth();

    // 获取设备指纹 deviceId
    public native String getDeviceId();

    /*
     * 人脸检测（返回检测到的人脸数组） String imgPath 要检测的图片路径 int maxTrackObjNum: 最多要检测的人数，例如设置为1则只检测一个人，如果设置为3则最多tracking 3个人。
     */
    public static native TrackFaceInfo[] track(String imgPath, int maxTrackObjNum);

    /*
     * 人脸检测（返回检测到的人脸数组） byte[] bufs 要检测的图片二进制buf int len 要检测的图片二进制buf长度 int maxTrackObjNum:
     * 最多要检测的人数，例如设置为1则只检测一个人，如果设置为3则最多tracking 3个人。
     */
    public static native TrackFaceInfo[] trackByBuf(byte[] bufs, int len, int maxTrackObjNum);

    public static native TrackFaceInfo[] trackByMat(long matAddr, int maxTrackObjNum);

    /*
     * 人脸检测（返回检测到的最大人脸） String imgPath 要检测的图片路径 。
     */
    public static native TrackFaceInfo trackMax(String imgPath);

    // 人脸检测（返回检测到的最大人脸）buf为人脸二进制图片 len为图片大小
    public static native TrackFaceInfo trackMaxByBuf(byte[] bufs, int len);

    // 人脸检测（返回检测到的最大人脸）matAddr为opencv的视频帧地址
    public static native TrackFaceInfo trackMaxByMat(long matAddr);

    // 获取人脸属性(传入图片路径)
    public static native String faceAttr(String filePath);

    // 获取人脸属性(传入二进制图片buffer)
    public static native String faceAttrByBuf(byte[] bufs, int len);

    // 获取人脸属性(传入opencv视频帧)
    public static native String faceAttrByMat(long matAddr);

    // 获取人脸属性(传入opencv视频帧和前期检测到的人脸信息，适应于多人脸)
    public static native String faceAttrByFace(long matAddr, TrackFaceInfo info);

    // 传入图片获取人脸质量
    public static native String faceQuality(String filePath);

    // 传入二进制图片buffer获取人脸质量
    public static native String faceQualityByBuf(byte[] bufs, int len);

    // 传入opencv视频帧获取人脸质量
    public static native String faceQualityByMat(long matAddr);

    // 传入opencv视频帧及检测到人脸信息，适应于多人脸
    public static native String faceQualityByFace(long matAddr, TrackFaceInfo info);

    // 人脸注册(传图片地址)
    public static native String userAdd(String userId, String groupId, String fileName, String userInfo);

    // 人脸注册(传二进制图片buf）
    public static native String userAddByBuf(String userId, String groupId, byte[] bufs, int bufLen, String userInfo);

    // 人脸注册(传特征值）
    public static native String userAddByFeature(String userId, String groupId, byte[] feas, int feaLen,
            String userInfo);

    // 人脸更新
    public static native String userUpdate(String userId, String groupId, String fileName, String userInfo);

    // 人脸更新(传二进制图片buf）
    public static native String userUpdateByBuf(String userId, String groupId, byte[] bufs, int bufLen,
            String userInfo);

    // 人脸删除
    public static native String userFaceDelete(String userId, String groupId, String faceToken);

    // 用户删除
    public static native String userDelete(String userId, String groupId);

    // 组添加
    public static native String groupAdd(String groupId);

    // 组删除
    public static native String groupDelete(String groupId);

    // 查询用户信息
    public static native String getUserInfo(String userId, String groupId);

    // 用户组列表查询
    public static native String getUserList(String groupId, int start, int length);

    // 组列表查询
    public static native String getGroupList(int start, int length);

    // 1:1人脸对比(传图片路径)
    public static native String match(String image1, String image2);

    // 人脸对比（传入opencv视频帧）
    public static native String matchByMat(long matAddr1, long matAddr2);

    // 1:N人脸识别
    public static native String identify(String image, String groupIdList, String userId, int userTopNum);

    // 1:N人脸识别(和整个库比较,需要提前调loadDbFace（）)
    public static native String identifyDB(String image, int userTopNum);

    // 1:N人脸识别(传入opencv视频帧)
    public static native String identifyByMat(long matAddr, String groupIdList, String userId, int userTopNum);

    // 1:N人脸识别(传入opencv视频帧)(和整个库比较,需要提前调loadDbFace（）)
    public static native String identifyByMatDB(long matAddr, int userTopNum);

    // 1:N人脸识别(传入二进制图片buf)
    public static native String identifyByBuf(byte[] bufs, int bufLen, String groupIdList, String userId,
            int userTopNum);

    // 1:N人脸识别(传入二进制图片buf)(和整个库比较,需要提前调loadDbFace（）)
    public static native String identifyByBufDB(byte[] bufs, int bufLen, int userTopNum);

    // 1:N人脸识别(传入特征值feature)
    public static native String identifyByFeature(byte[] features, int feaLen, String groupIdList, String userId,
            int userTopNum);

    // 1:N人脸识别(传入特征值feature)(和整个库比较,需要提前调loadDbFace（）)
    public static native String identifyByFeatureDB(byte[] features, int feaLen, int userTopNum);

    // 加载数据库人脸库到内存（数据库数据通过userAdd等注册入库）
    public static native boolean loadDbFace();

    // 以下为人脸参数设置
    // 非人脸的置信度阈值，取值范围0~1，取0则认为所有检测出来的结果都是人脸，默认0.5
    public static native void setNotFaceThr(float thr);

    // 最小人脸尺寸：需要检测到的最小人脸尺寸，比如需要检测到30*30的人脸则设置为30，
    // 该尺寸占图像比例越小则检测速度越慢，具体请参考性能指标章节。默认值30
    public static native void setMinFaceSize(int size);

    // 跟踪到目标后执行检测的时间间隔，单位毫秒，默认300，值越小越会更快发现新目标，但是cpu占用率会提高、处理速度变慢
    public static native void setTrackByDetectionInterval(int intervalInMs);

    // 未跟踪到目标时的检测间隔，单位毫秒，默认300，值越小越快发现新目标，但是cpu占用率会提高、处理速度变慢
    public static native void setDetectInVideoInterval(int intervalInMs);

    // 清除跟踪到的人脸信息
    public static native void clearTrackedFaces();

    // 获取特征值(通过传入图片地址,返回2048个byte）
    public static native byte[] getFaceFeature(String image);

    // 获取特征值（通过传入opencv视频帧,返回2048个byte)
    public static native byte[] getFaceFeatureByMat(long mat);

    // 获取特征值（传入二进制图片buf,返回2048个byte）
    public static native byte[] getFaceFeatureByBuf(byte[] bufs, int bufLen);

    // 获取特征值（传入opencv视频帧和需要获取特征值的人脸信息，适应于多人脸）
    public static native byte[] getFaceFeatureByFace(long mat, TrackFaceInfo info);

    // 特征值比较
    public static native float compareFeature(byte[] f1, int f1Len, byte[] f2, int f2Len);

    // 活体
    // rgb+ir(可见光+红外双目活体)(传入图片二进制buf)
    public static native RgbIrInfo rgbAndIrLivenessCheckByBuf(byte[] rgbBufs, int rgbLen, byte[] irBufs, int irLen);

    // rgb+ir(可见光+红外双目活体)(传入opencv视频帧)
    public static native RgbIrInfo rgbAndIrLivenessCheckByMat(long rgbAddr, long irAddr);

    // rgb+depth(可见光+深度双目活体)(传入图片二进制buf)
    public static native RgbDepthInfo rgbAndDepthLivenessCheckByBuf(byte[] rgbBufs, int rgbLen, byte[] depthBufs,
            int depthLen);

    // rgb+depth(可见光+深度双目活体)(传入opencv视频帧)
    public static native RgbDepthInfo rgbAndDepthLivenessCheckByMat(long rgbAddr, long depthAddr);

    // 初始化奥比mini摄像头
    public static native long newOrbeMini();

    // 打开奥比mini摄像头
    public static native int openOrbeMini(long camera, long rgbAddr, long depthAddr);

    // 释放奥比mini摄像头
    public static native void deleteOrbeMini(long addr);

    // 初始化奥比deeyea或atlas摄像头（同时适应于deeyea镜头或atlas镜头）
    public static native long newOrbeDeeyea();

    // 打开奥比deeyea或atlas摄像头（同时适应于deeyea镜头或atlas镜头）
    public static native int openOrbeDeeyea(long camera, long rgbAddr, long depthAddr);

    // 释放奥比deeyea或atlas摄像头（同时适应于deeyea镜头或atlas镜头）
    public static native void deleteOrbeDeeyea(long addr);

    // 初始化华杰艾米摄像头
    public static native long newHjimi();

    // 打开华杰艾米摄像头
    public static native boolean openHjimi(long camera, long rgbAddr, long depthAddr);

    // 释放华杰艾米摄像头
    public static native void deleteHjimi(long addr);

    static {
        // 加载dll
        System.loadLibrary("BaiduFaceApi");
        System.loadLibrary("./opencv-dll/opencv_java320");
    }

    public static void main(String[] args) {
        // sdk初始化
        Face api = new Face();
        // 初始化sdk
        // 若采用证件照模式，请把id_card设为true，否则为false，证件照模式和非证件照模式提取的人脸特征值不同，
        // 不能混用
        boolean idCard = false;
        api.sdkInit(idCard);
        // 获取设备指纹
        String deviceId = api.getDeviceId();
        System.out.println("device id is:" + deviceId);
        // 人脸设置
//         FaceSetting faceSet = new FaceSetting();
//         faceSet.testFaceSetting();
        // 加载数据库到内存（前提是通过了user_add等添加数据到数据库后数据库有数据)
        // api.loadDbFace();
        // 人脸属性
//        FaceAttr faceAttr = new FaceAttr();
       // faceAttr.testFaceAttr();
        // 人脸质量
//        FaceQuality faceQual = new FaceQuality();
//         faceQual.testFaceQuality();
        // 人脸检测
//        FaceTrack track = new FaceTrack();
       // track.testFaceTrack();
        // track.testFaceTrackMax();

        // 人脸管理
//        FaceManager faceMag = new FaceManager();
//        faceMag.testFaceManager();
        // 人脸比较
        FaceCompare faceCom = new FaceCompare();
        faceCom.testFaceCompare();
        // 双目活体(rgb+ir和rgb+depth)
//        FaceLiveness faceLiveness = new FaceLiveness();
//        faceLiveness.testFaceLiveness();
        System.out.println("end face");
    }
}
