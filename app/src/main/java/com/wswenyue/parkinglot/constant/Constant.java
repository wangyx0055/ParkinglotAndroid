package com.wswenyue.parkinglot.constant;

/**
 * Created by wswenyue on 2015/5/27.
 */
public  class Constant {

    public final static String SERVER_IP = "192.168.3.110";//定义好的
//    public final static String SERVER_IP = "192.168.3.129";

    public final static int SERVER_PORT = 8989;

    public static final String OPEN_IDENTIFER = "OPEN";	//打开1号门
    public static final String OPEN02_IDENTIFER = "OPEN2";	//打开2号门
    public static final String OPEN03_IDENTIFER = "OPEN3";	//打开3号门

    public final static int MSG_WHAT_SENDMSG = 0x345;

    /**
     * 手机端发送过来的命令定义
     * */

    public static final String Login = "LOGIN";
    public static final String Register = "REGISTER";
    public static final String Reset = "RESET";
    public static final String Pay_ZFB = "PAY_ZFB";
    public static final String Pay_CZK = "PAY_CZK";

    public static final String CMD = "CMD";

    /**定义服务器回送的指令代码
     * 回送给手机端的指令
     * */
    public static final String Login_Succeed = "LOGIN_SUCCEED";
    public static final String Login_Fail  = "LOGIN_FAIL";

    public static final String Register_Succeed = "REGISTER_SUCCEED";
    public static final String Register_Fail = "REGISTER_FAIL";

    public static final String Rsset_Succeed = "RESET_SUCCEED";
    public static final String Rsset_Fail = "RESET_FAIL";

    public static final String Pay_Succeed = "PAY_SUCCEED";
    public static final String Pay_Fail = "PAY_FAIL";

    /** 异常指令 */
    public static final String Server_Unknown = "UNKNOWN_CODE";
    public static final String Server_CMD_Execution_Succeed = "CMD_EXECUTION_SUCCEED";
    public static final String Server_CMD_Repuat = "CMD_REPUAT";//指令重复执行
    /**
     * 权限管理
     * */
    public static final String Authority_Permission_denied= "PERMISSION_DENIED";//权限不够
    public static final String Authority_Not_Allowed= "NOT_ALLOWED";//权限不够
    public static final String Authority_Area_Outside= "AREA_OUTSIDE";//区域之外


    /** 广播发送*/
    public static final String BroadCastSend= "BROADCASTSEND";//

    /** */
    public static final String TAG_CONNECTFAILURE = "CONNECT_FAILURE";
    public static final String TAG_FAILURE = "FAILURE";
}
