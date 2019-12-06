package com.huang.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.system.SystemUtil;
import com.huang.bean.WebResult;
import com.huang.vo.FaceCompareVO;
import com.jni.face.Face;
import com.jni.face.FaceCompare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.File;
import java.util.Base64;

/**
 * @Author huangsq
 * @Date 2019/12/5 10:03
 */
@RestController
@RequestMapping("/face")
public class FaceController {

    private final static String rootPath = SystemUtil.get("user.dir") + File.separator+"temp_image"+File.separator;

    private final static Logger logger = LoggerFactory.getLogger(FaceController.class);



     @RequestMapping("/compare")
     public WebResult compare(@Valid @RequestBody FaceCompareVO vo){
         String imagePath1  = rootPath + String.valueOf(System.currentTimeMillis())+"-1.jpg";
         String imagePath2  = rootPath + String.valueOf(System.currentTimeMillis())+"-2.jpg";
         FileUtil.writeBytes(Base64.getDecoder().decode(vo.getImg1()),imagePath1);
         FileUtil.writeBytes(Base64.getDecoder().decode(vo.getImg2()),imagePath2);
         String res = Face.match(imagePath1, imagePath2);
         FileUtil.del(imagePath1);
         FileUtil.del(imagePath2);
         JSONObject jsonObject = JSONUtil.parseObj(res);
         int code = Convert.toInt(jsonObject.get("errno"),9999);
         if(code==0){
             Object data = jsonObject.get("data");
             return WebResult.ok(data);
         }
         logger.info("Face.match 失败，请求参数：{}",JSONUtil.toJsonStr(vo));
         logger.info("人脸匹配失败,res:{}",res);
         return WebResult.fail(code,(String)jsonObject.get("msg"));
     }

}
