package com.wswenyue.parkinglot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.wswenyue.parkinglot.R;

public class Appstart extends BasicActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appstart);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run(){
                Intent intent = new Intent (Appstart.this,LoginActivity.class);
                startActivity(intent);
                Appstart.this.finish();
            }
        }, 1500);
    }

}
