package com.wswenyue.parkinglot.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wswenyue.parkinglot.R;
import com.wswenyue.parkinglot.constant.Constant;
import com.wswenyue.parkinglot.service.MyService;

public class LoginActivity extends BasicActivity {

    //定义界面上的UserName和Passwd文本框
    private EditText userName, passwd;
    private String uname = "";
    private String upasswd = "";
    //定义界面上的三个按钮
    private Button login ;
    private Button forgetPassword;
    private Button register;
    private Intent intent = null;


    private StringBuffer sb = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //获取界面上的两个文本框
        userName = (EditText) findViewById(R.id.usernameView);
        passwd = (EditText) findViewById(R.id.passwordView);
        //获取界面上的三个按钮
        login = (Button) findViewById(R.id.login_bt);
        forgetPassword = (Button) findViewById(R.id.bt_forgetPassword);
        register = (Button) findViewById(R.id.register_bt);

        receiver = new BroadcastMain();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BroadCastSend);
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    public void login(View source) {
        // 处理登陆任务
        uname = userName.getText().toString().trim();
        upasswd = passwd.getText().toString().trim();
        if (isNotNull()) {
            Log.i("Login", "Login...");
            CheckUser();
            Toast.makeText(LoginActivity.this,"Login...",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    public void forgetPassword(View source) {
        // 处理重置密码任务
        intent = new Intent(LoginActivity.this, ResetPasswdActivity.class);
        startActivity(intent);
//        finish();
    }

    public void register(View source) {
        // 处理注册任务
        intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
//        finish();
    }
    public boolean isNotNull(){
        if ("".equals(uname) || uname == null ||
            "".equals(upasswd) || upasswd == null){
            return false;
        }
        return true;
    }

    public void CheckUser(){
        Message message = new Message();
        message.what = Constant.MSG_WHAT_SENDMSG;
        sb = new StringBuffer();
        sb.append(Constant.Login).append("#").append(uname).append("#").append(upasswd);
        message.obj = sb.toString();
        MyService.revHandler.sendMessage(message);
    }

    public void saveData(){
        SharedPreferences sharedPreferences =getSharedPreferences("parkinglotInfo",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(uname != null && !uname.equals("")){
            editor.putString("uname",uname);
            editor.commit();
        }
    }

    BroadcastMain receiver;
    //内部类，实现BroadcastReceiver
    public class BroadcastMain extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String MsgStr = intent.getStringExtra("msg");

            if(MsgStr.equals(Constant.Login_Succeed)){
                saveData();
                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else if(MsgStr.equals(Constant.Login_Fail)){
                Toast.makeText(LoginActivity.this,"用户名和密码错误，请核对后登陆",Toast.LENGTH_SHORT).show();
            }
        }
    }

}

