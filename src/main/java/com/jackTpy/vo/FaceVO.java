package com.jackTpy.vo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;


@Data
public class FaceVO {
    @NotEmpty(message = "人脸图片1不能为空")
   private String img1;//base64Img1
    @NotEmpty(message = "人脸图片2不能为空")
   private String img2;//base64Img1
}
