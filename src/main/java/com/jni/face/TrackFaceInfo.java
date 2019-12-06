package com.jni.face;

public class TrackFaceInfo {

    public int[] landmarks;
    public float[] headPose;
    public float score;
    public int faceId;

    public float width; // rectangle width
    public float angle; // rectangle tilt angle [-45 45] in degrees
    public float centerY; // rectangle center y
    public float centerX; // rectangle center x
    public float conf;
}
