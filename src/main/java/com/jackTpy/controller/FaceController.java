package com.jackTpy.controller;

import cn.hutool.core.codec.Base64;
import com.jackTpy.bean.WebResult;
import com.jni.face.Face;
import com.jni.struct.FeatureInfo;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;



@RestController
@RequestMapping("/face")
public class FaceController {

     @PostMapping("/faceFeature")
     public WebResult faceFeature(@RequestBody Map<String ,String> faceParam){
         String faceImg = faceParam.get("faceImg");
         // 将Base64字符串解码为字节数组
         byte[] imageBytes = Base64.decode(faceImg);
         // 使用OpenCV的imdecode函数从输入流加载图像到Mat对象
         Mat mat = Imgcodecs.imdecode(new MatOfByte(imageBytes), Imgcodecs.IMREAD_COLOR);
         long matAddr = mat.getNativeObjAddr();
         // type 0： 表示rgb 生活照特征值 1: 表示rgb证件照特征值 2：表示nir近红外特征值
         int type = 0;
         FeatureInfo[] feaList = Face.faceFeature(matAddr, type);
         mat.release();  // 用完释放，防止内存泄漏
         if (feaList == null || feaList.length == 0) {
             System.out.println("get feature fail");
             return WebResult.fail(0,"特征点提取失败");
         }else {
//             System.out.println("get feature success");
             return WebResult.ok(feaList);
         }
     }
}
