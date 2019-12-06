package com.jni.face;

public class FaceSetting {
    public void testFaceSetting() {
      
        // 非人脸的置信度阈值，取值范围0~1，取0则认为所有检测出来的结果都是人脸，默认0.5
        Face.setNotFaceThr( (float) 0.5 );
        // 最小人脸尺寸：需要检测到的最小人脸尺寸，比如需要检测到30*30的人脸则设置为30，
        // 该尺寸占图像比例越小则检测速度越慢，具体请参考性能指标章节。默认值100
        Face.setMinFaceSize(100);
        // 跟踪到目标后执行检测的时间间隔，单位毫秒，默认300，值越小越会更快发现新目标，但是cpu占用率会提高、处理速度变慢
        Face.setTrackByDetectionInterval(300);
        // 未跟踪到目标时的检测间隔，单位毫秒，默认300，值越小越快发现新目标，但是cpu占用率会提高、处理速度变慢
        Face.setDetectInVideoInterval(300);
    }
}
