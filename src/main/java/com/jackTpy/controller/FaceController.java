package com.jackTpy.controller;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import com.jackTpy.bean.WebResult;
import com.jni.face.Face;
import com.jni.struct.FeatureInfo;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.Map;



@RestController
@RequestMapping("/face")
public class FaceController {

     @PostMapping("/faceFeature")
     public WebResult faceFeature(@RequestBody Map<String ,String> faceParam){
         String faceImg = faceParam.get("faceImg");
         File tempFile = FileUtil.createTempFile(".jpg", true);
         File file = Base64.decodeToFile(faceImg, tempFile);
         Mat mat = Imgcodecs.imread(file.getPath());
         long matAddr = mat.getNativeObjAddr();
         // type 0： 表示rgb 生活照特征值 1: 表示rgb证件照特征值 2：表示nir近红外特征值
         int type = 0;
         FeatureInfo[] feaList = Face.faceFeature(matAddr, type);

         if (feaList == null || feaList.length <= 0) {
             System.out.println("get feature fail");
             return WebResult.fail(0,"特征点提取失败");
         }else {
             return WebResult.ok(feaList);
         }
     }
}
