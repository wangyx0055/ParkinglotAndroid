package com.wswenyue.parkinglot.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

public class RegisterActivity extends Activity {
    //定义界面上的UserName和Passwd文本框
    private EditText userName, passwd, phone, email;
    private String uname = null;
    private String upasswd = null;
    private String uphone = null;
    private String umail = null;
    //定义界面上的按钮
    private Button register;

    private Intent intent = null;
    private StringBuffer sb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取界面上的两个文本框
        userName = (EditText) findViewById(R.id.usernameView);
        passwd = (EditText) findViewById(R.id.passwordView);
        phone = (EditText) findViewById(R.id.phoneView);
        email = (EditText) findViewById(R.id.emailView);
        //获取界面上的三个按钮
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


    public void register(View v) {
        // 处理注册任务
        uname = userName.getText().toString().trim();
        upasswd = passwd.getText().toString().trim();
        uphone = phone.getText().toString().trim();
        umail = email.getText().toString().trim();
        //判断是否为空
        if (isNotNull()) {
            Regiester();
            Toast.makeText(RegisterActivity.this,"亲，稍等马上就好！",Toast.LENGTH_SHORT).show();

            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(RegisterActivity.this, "所有信息均为必填项", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNotNull() {
        if ("".equals(uname) || uname == null ||
            "".equals(upasswd) || upasswd == null ||
            "".equals(uphone) || uphone == null ||
            "".equals(umail) || umail == null) {
            return false;
        }
        return true;
    }

    public void Regiester(){
        Message message = new Message();
        message.what = Constant.MSG_WHAT_SENDMSG;
        sb = new StringBuffer();
        sb.append(Constant.Register).append("#").append(uname)
                .append("#").append(upasswd).append("#")
                .append(umail).append("#").append(uphone);
        message.obj = sb.toString();
        MyService.revHandler.sendMessage(message);
    }


    BroadcastMain receiver;
    //内部类，实现BroadcastReceiver
    public class BroadcastMain extends BroadcastReceiver {
        //必须要重载的方法，用来监听是否有广播发送
        @Override
        public void onReceive(Context context, Intent intent) {
            String MsgStr = intent.getStringExtra("msg");
            Log.i("收到来自服务器的消息", MsgStr);
            if(MsgStr.equals(Constant.Register_Succeed)){
                //注册成功直接跳转到登陆界面
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(RegisterActivity.this,"请检查您的网络",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
