package com.wswenyue.parkinglot.Activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.wswenyue.parkinglot.service.MyService;

public class BasicActivity extends Activity {

    MyService myService ;
    private long exitTime = 0;

    public ServiceConnection myServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName arg0, IBinder service) {
            myService = ((MyService.InterBinder) service).getService();
        }

        public void onServiceDisconnected(ComponentName arg0) {

            myService = null;
        }

    };

    @Override
    public void onBackPressed() {
        if((System.currentTimeMillis() - exitTime) > 2000){
            Toast.makeText(this, "再次按返回键退出", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindService(new Intent(BasicActivity.this, MyService.class),
                myServiceConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(myServiceConnection);
    }
}
