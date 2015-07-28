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

public class ResetPasswdActivity extends Activity {
    //定义界面上的UserName和Passwd文本框
    private EditText newPasswd,confirmPasswd,phone,email;
    private String uNewPasswd = null;
    private String uConfirmPasswd = null;
    private String uphone = null;
    private String umail = null;
    //定义界面上的按钮
    private Button reset;

    private Intent intent = null;
    private StringBuffer sb = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_passwd);

        //获取界面上的两个文本框
        newPasswd = (EditText) findViewById(R.id.passwordView);
        confirmPasswd = (EditText) findViewById(R.id.confirmPasswdView);
        phone = (EditText) findViewById(R.id.phoneView);
        email = (EditText) findViewById(R.id.emailView);
        //获取界面上的按钮
        reset = (Button) findViewById(R.id.resetPasswd_bt);

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

    public void resetPasswd(View v){
        // 处理重置密码任务
        uNewPasswd = newPasswd.getText().toString().trim();
        uConfirmPasswd = confirmPasswd.getText().toString().trim();
        uphone = phone.getText().toString().trim();
        umail = email.getText().toString().trim();
        //判断是否为空
        if(isNotNull()){
            if(uNewPasswd.equals(uConfirmPasswd)){
                reset();
                Toast.makeText(ResetPasswdActivity.this, "修改中...", Toast.LENGTH_SHORT).show();

                intent = new Intent(ResetPasswdActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }else {
                Toast.makeText(ResetPasswdActivity.this, "密码输入不一致，请重新输入", Toast.LENGTH_SHORT).show();
                newPasswd.setText("");
                confirmPasswd.setText("");
            }

        }else {
            Toast.makeText(ResetPasswdActivity.this, "所有信息均为必填项", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isNotNull() {
        if ("".equals(uNewPasswd) || uNewPasswd == null ||
                "".equals(uConfirmPasswd) || uConfirmPasswd == null ||
                "".equals(uphone) || uphone == null ||
                "".equals(umail) || umail == null) {
            return false;
        }
        return true;
    }

    public void reset(){
        Message message = new Message();
        message.what = Constant.MSG_WHAT_SENDMSG;
        sb = new StringBuffer();
        sb.append(Constant.Reset).append("#").append(uNewPasswd)
                .append("#").append(umail).append("#").append(uphone);
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
            if(MsgStr.equals(Constant.Rsset_Succeed)){
                Toast.makeText(ResetPasswdActivity.this, "重置密码成功", Toast.LENGTH_SHORT).show();
                intent = new Intent(ResetPasswdActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }else {
                Toast.makeText(ResetPasswdActivity.this,"网络故障。。。",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
