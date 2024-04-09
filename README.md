### 使用前提
1. 该服务只能用于windows系统，不支持windows server
2. 购买baidu离线sdk人脸sdk激活，激活查看激活说明

### 接口服务说明
1. 服务默认端口58080 修改端口可以通过修改startup.bat启动脚本  加入 --server.port=端口  
2. 目前只提供一个服务  人脸特征提取接口  
http://127.0.0.1:58080/baidu-face-service/face/faceFeature
请求参数   application/json
{"faceImg":"base64字符串"}
3. 如需其他接口  还需自行开发

### 部署：
1. 开发环境直接启动 springboot 的 BaiduFaceHttpServiceApplication即可
2. 生成环境部署
编译后把baiduFaceHttpService.jar 和 doc文件夹下面的文件copy到  主目录下

启动说明 两种方式
1. 前台启动，双击startup.bat即可
2. 后台服务启动  修改winsw.xml 文件  value 为 "当前目录的路径"
<env name="BAT_HOME" value="当前目录的路径"/>
3. 用管理员权限运行   《安装服务.bat》   文件
4. 在任务管理器找到 服务里面的 baidu-face-http-service  启动会停止即可

### 常见问题
启动报错：Can't find dependent libraries
解决方案：安装微软常用运行库合集 https://www.onlinedown.net/soft/10041123.htm
