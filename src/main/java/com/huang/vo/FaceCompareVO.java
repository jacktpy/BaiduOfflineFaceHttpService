package com.huang.vo;

import javax.validation.constraints.NotEmpty;

/**
 * @Author huangsq
 * @Date 2019/12/5 10:18
 */
public class FaceCompareVO {

    @NotEmpty(message = "人脸图片1不能为空")
   private String img1;//base64Img1
    @NotEmpty(message = "人脸图片2不能为空")
   private String img2;//base64Img1

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }
}
