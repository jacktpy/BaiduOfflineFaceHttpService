package com.jni.face;

// rgb+depth活体检测信息
public class RgbDepthInfo {
    // 以下为人脸信息，参见TrackFaceInfo定义
    public int[] landmarks;
    public float[] headPose;
    public float score;
    public int faceId;

    public float width; // rectangle width
    public float angle; // rectangle tilt angle [-45 45] in degrees
    public float centerY; // rectangle center y
    public float centerX; // rectangle center x
    public float conf;
    // 以下为活体信息
    public float rgbScore; // rgb检测分数
    public float depthScore; // depth检测分数
}
