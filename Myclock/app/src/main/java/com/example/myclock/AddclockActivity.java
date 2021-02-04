package com.example.myclock;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
/**
 * @author 曹晓东 ,Created on 2020/5/1
 * 联系： 2806029294@qq.com
 */
public class AddclockActivity extends AppCompatActivity {
    private PickerView minute_pv;
    private PickerView second_pv;
    private Calendar mCalendar;
    private int mHour,mMinuts;
    private String timeDitance;
    private int minute,hour;
    private TextView distance;
    private ConstraintLayout clrepeatChoose;
    private TextView repeatType;
    private ConstraintLayout clvibrateChoose;
    private TextView ifvibrat;
    private ConstraintLayout clmusicChoose;
    private TextView ifmusic;
    private TextView addYes,addCancel;
    private MyDatabaseHelper myDatabaseHelper;
    private SQLiteDatabase db;
    String url2;
    MediaPlayer mediaPlayer;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();//隐藏ActionBar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明化通知栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setContentView(R.layout.activity_addclock);
        //请求通知权限
        NotificationAccess.gotoOpen(this);
        preferences =  this.getSharedPreferences("session",MODE_PRIVATE);
        init();

    }

    private void init(){
        minute_pv = (PickerView) findViewById(R.id.minute_pv);
        second_pv = (PickerView) findViewById(R.id.second_pv);
        distance=findViewById(R.id.distance);
        List<String> data = new ArrayList<String>();
        List<String> seconds = new ArrayList<String>();
        for (int i = 0; i < 24; i++)
        {
            if (i<10) {
                data.add("0" + i);
            }else {
                data.add(i+"");
            }
        }
        for (int i = 0; i < 60; i++)
        {
            seconds.add(i < 10 ? "0" + i : "" + i);
        }
        minute=30;
        hour=12;
        timeDitance=new CalTime().cal(minute,hour);
        distance.setText(timeDitance);
        minute_pv.setData(data);
        second_pv.setData(seconds);

        minute_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {

                hour=Integer.parseInt(text);
                timeDitance=new CalTime().cal(hour,minute);
                distance.setText(timeDitance);
            }
        });
        second_pv.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                minute=Integer.parseInt(text);
                timeDitance=new CalTime().cal(hour,minute);
                distance.setText(timeDitance);
            }
        });

        clrepeatChoose=findViewById(R.id.clrepeatChoose);
        repeatType=findViewById(R.id.repeatContent);
        clrepeatChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items=new String[]{"仅一次","周一到周五","每天"};
                AlertDialog.Builder builder=new AlertDialog.Builder(AddclockActivity.this);
                builder.setTitle("选择重复类型");

                //添加列表项
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        repeatType.setText(items[i]);
                    }
                });
                //创建并显示
                builder.create().show();
            }
        });
        clvibrateChoose=findViewById(R.id.clvibrateChoose);
        ifvibrat=findViewById(R.id.vibrateContent);
        clvibrateChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items=new String[]{"是","否"};
                AlertDialog.Builder builder=new AlertDialog.Builder(AddclockActivity.this);
                builder.setTitle("是否开启振动");

                //添加列表项
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ifvibrat.setText(items[i]);
                    }
                });
                //创建并显示
                builder.create().show();
            }
        });

        clmusicChoose=findViewById(R.id.clmusicChoose);
        ifmusic=findViewById(R.id.musicContent);
        clmusicChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(AddclockActivity.this,MusicChoose.class);
//                startActivity(intent);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("audio/mp3");//设置类型，我这里是任意类型，任意后缀的可以这样写。
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,1);
            }
        });

        myDatabaseHelper=new MyDatabaseHelper(getApplicationContext(),"Items.db",null,1);
        db=myDatabaseHelper.getWritableDatabase();
        addYes=findViewById(R.id.addYes);
        addYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cValue = new ContentValues();
                if (minute<10){
                    cValue.put("time",hour+":0"+minute);
                }else {
                    cValue.put("time",hour+":"+minute);
                }
                cValue.put("create_time",System.currentTimeMillis()+"");
                cValue.put("repeat",repeatType.getText().toString());
                cValue.put("isSwitchOn",1);
                cValue.put("ifVibrate",ifvibrat.getText().toString());
                db.insert("clocks",null,cValue);
                Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddclockActivity.this,ClockActivity.class);
                intent.putExtra("addType","yes");
                startActivity(intent);
                finish();
            }
        });
        addCancel=findViewById(R.id.addCancel);
        addCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AddclockActivity.this,ClockActivity.class);
                startActivity(intent);
                intent.putExtra("addType","cancel");
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(AddclockActivity.this,ClockActivity.class);
        startActivity(intent);
        finish();
    }
    //文件路径
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){//是否选择，没选择就不会继续
            try {
                Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                Log.d("文件路径--",uri+"");
                String url = FileUtils2.getPath(AddclockActivity.this,uri);
                url2 = url.trim();
                ifmusic.setText(url2);
                //请求通知权限
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("sound", url2);
                editor.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void play(String path) {

        try {
            //        重置音频文件，防止多次点击会报错
            mediaPlayer.reset();
//        调用方法传进播放地址
            mediaPlayer.setDataSource(path);
//            异步准备资源，防止卡顿
            mediaPlayer.prepareAsync();
//            调用音频的监听方法，音频准备完毕后响应该方法进行音乐播放
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
