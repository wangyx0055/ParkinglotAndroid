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
import android.widget.ImageView;
import android.widget.Toast;

import com.wswenyue.parkinglot.R;
import com.wswenyue.parkinglot.constant.Constant;
import com.wswenyue.parkinglot.service.MyService;


public class MainActivity extends BasicActivity {

    private Button btOpen = null;
    private Button btXiuxi = null;
    private Button btYule = null;

    private ImageView mHome;

    private String uname = null;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btOpen = (Button) this.findViewById(R.id.bt_open);
        btXiuxi = (Button) this.findViewById(R.id.bt_xiuxi);
        btYule = (Button) this.findViewById(R.id.bt_yule);

        mHome = (ImageView) this.findViewById(R.id.img_my);

        //获取用户名
        getData();

        receiver = new BroadcastMain();
        //新添代码，在代码中注册广播接收程序
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BroadCastSend);
        registerReceiver(receiver, filter);



        //添加事件监听
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg(Constant.OPEN_IDENTIFER);
//                btOpen.setClickable(false);
            }
        });

        //添加事件监听
        btXiuxi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg(Constant.OPEN02_IDENTIFER);
//                btXiuxi.setClickable(false);
            }
        });

        //添加事件监听
        btYule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMsg(Constant.OPEN03_IDENTIFER);
//                btYule.setClickable(false);
            }
        });

        mHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    public void getData() {
        SharedPreferences sharedPreferences = getSharedPreferences("parkinglotInfo", 0);
        uname = sharedPreferences.getString("uname", "");
    }

    public void SendMsg(String cmd) {
        Message message = new Message();
        message.what = Constant.MSG_WHAT_SENDMSG;
        message.obj = Constant.CMD + "#" + uname + "#" + cmd;
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
//            //设置三个按钮可用
//            btOpen.setClickable(true);
//            btXiuxi.setClickable(true);
//            btYule.setClickable(true);

            if(MsgStr.equals(Constant.Authority_Permission_denied)){
                Toast.makeText(MainActivity.this,"请登录后再操作", Toast.LENGTH_SHORT).show();
            }else if(MsgStr.equals(Constant.Authority_Not_Allowed)){
                Toast.makeText(MainActivity.this,"请进入停车场，再操作", Toast.LENGTH_SHORT).show();
            }else if(MsgStr.equals(Constant.Server_CMD_Execution_Succeed)){
                Toast.makeText(MainActivity.this,"门已打开，请尽快通过", Toast.LENGTH_SHORT).show();
            }else if(MsgStr.equals(Constant.Server_CMD_Repuat)){
                Toast.makeText(MainActivity.this,"您的指令正在执行，请勿重复。", Toast.LENGTH_SHORT).show();
            }else if(MsgStr.equals(Constant.Authority_Area_Outside)){
                Toast.makeText(MainActivity.this,"请到指定区域操作", Toast.LENGTH_SHORT).show();
            }
        }
    }

}