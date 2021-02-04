package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * @author 曹晓东 ,Created on 2020/5/1
 * 联系： 2806029294@qq.com
 */
public class MainActivity extends AppCompatActivity {


    private ImageView clock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();//隐藏ActionBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏

        setContentView(R.layout.activity_main);

        clock=findViewById(R.id.clock);

        if (ServiceUtil.isRunning(getApplication(),"com.example.clock.ClockService")){
        }else {
            startService(new Intent(MainActivity.this,ClockService.class).putExtra("flag","ClockActivity"));
            Toast.makeText(getApplicationContext(),"服务开启成功",Toast.LENGTH_SHORT).show();
        }
        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ClockActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
