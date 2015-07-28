package com.wswenyue.parkinglot.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.wswenyue.parkinglot.R;

public class HomeActivity extends BasicActivity {

    private ImageView mControl;
    private Button btPayZFB,btPayCZK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mControl = (ImageView) this.findViewById(R.id.img_control);
        btPayZFB = (Button) this.findViewById(R.id.bt_pay_zhifubao);
        btPayCZK = (Button) this.findViewById(R.id.bt_pay_chongzhika);

        mControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btPayZFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HomeActivity.this, PayZFBActivity.class);
                startActivity(intent);
            }
        });
        btPayCZK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( HomeActivity.this, PayCZKActivity.class);
                startActivity(intent);

            }
        });

    }

}
