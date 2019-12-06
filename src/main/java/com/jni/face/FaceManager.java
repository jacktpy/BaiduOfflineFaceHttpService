package com.jni.face;

public class FaceManager {
    public void testFaceManager() {
        testFaceUserAdd();
        // testFaceUserUpdate();
        // testFaceDelete();
        // testUserInfo();
        // testUserList();
        // testGroupList();
    }

    public void testFaceUserAdd() {
        // 传入图片文件地址注册
        String strUserAdd = Face.userAdd("test_user", "test_group", "d:\\2.jpg", "test_user_info");
        System.out.println("strUserAdd is:" + strUserAdd);
        
        Face.clearTrackedFaces();

        // 传入二进制图片buffer
        byte[] imgBuf = ImageBuf.getImageBuffer("d:\\4.jpg");
        String strBuf = Face.userAddByBuf("test_user1", "test_group", imgBuf, imgBuf.length, "test_user_info");
        System.out.println("strAddByBuf is:" + strBuf);

        // 人脸注册(传特征值）
        // byte[] feature = new byte[2048];
        // String strFea = Face.userAddByFeature("test_user1", "test_group",
        // feature, feature.length, "user_info");
        // System.out.println("userAddByFeature is:" + strFea);

    }

    // 人脸更新
    public void testFaceUserUpdate() {
        // 传入图片文件地址更新
        String res = Face.userUpdate("test_user1", "test_group", "d:\\4.jpg", "test_user_info2");
        System.out.println("userUpdate is:" + res);

        // 人脸更新(传二进制图片buf）
        byte[] bufs = ImageBuf.getImageBuffer("d:/8.jpg");
        String bufRes = Face.userUpdateByBuf("test_user1", "test_group", bufs, bufs.length, "user_info");
        System.out.println("userUpdateByBuf is:" + bufRes);
    }

    // 删除
    public void testFaceDelete() {
        // 人脸删除
        String res = Face.userFaceDelete("test_user1", "test_group", "b6d8e657b5acd4dbae98efed64ea7c4b");
        System.out.println("user_face_delete res is:" + res);

        // 用户删除
        res = Face.userDelete("test_user1", "test_group");
        System.out.println("userDelete res is:" + res);
    }

    // 组操作
    public void testGroupManager() {

        // 组添加
        String res = Face.groupAdd("test_group");
        System.out.println("groupAdd res is:" + res);
        // 组删除
        res = Face.groupDelete("test_group");
        System.out.println("groupDelete res is:" + res);
    }

    // 查询用户信息
    public void testUserInfo() {
        String res = Face.getUserInfo("test_user1", "test_group");
        System.out.println("getUserInfo res is:" + res);
    }

    // 用户组列表查询
    public void testUserList() {
        String res = Face.getUserList("test_group", 0, 100);
        System.out.println("getUserList res is:" + res);
    }

    // 组列表查询
    public void testGroupList() {
        String res = Face.getGroupList(0, 100);
        System.out.println("getGroupList res is:" + res);
    }
}
