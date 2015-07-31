#智能自助式停车场---安卓手机端设计

### 主要功能为：

- 用户管理
    - 登录
    - 注册
    - 修改密码
- 充值管理
    - 网银、支付宝充值
    - 充值卡充值
- 控制管理
    - 进/出停车场
    - 进/出娱乐室
    - 进/出休息室

----

### 系统设计思路

手机端不做权限的判断，比如说余额不足，未进入停车场操作等，这些处理都放到核心服务器那边处理，服务器处理完之后把相应的结果反馈给手机端，手机端再判断是否让用户操作，并给予提示。
手机端也不存储数据，数据存储在核心服务器那边，核心服务器才能访问数据库，进行数据的存取。
手机端和服务器端的交互采用的是socket通信。协议为自己定义的。

### APP设计界面图

- 欢迎界面

![欢迎界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_欢迎界面.png)

- 登录界面

![登录界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_登录.png)

- 注册界面

![注册界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_注册.png)

- 控制界面

![控制界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_控制界面.png)

- 支付宝/网银充值界面

![支付宝/网银充值界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_支付宝网银充值.png)

- 充值界面

![充值界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_充值.png)

- 充值卡充值界面

![充值卡充值界面](http://7xj2yt.com1.z0.glb.clouddn.com/app_登录.png)