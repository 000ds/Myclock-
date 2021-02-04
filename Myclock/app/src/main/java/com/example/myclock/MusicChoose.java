package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * @author 曹晓东 ,Created on 2020/5/1
 * 联系： 2806029294@qq.com
 */
public class MusicChoose extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_choose);
        mMediaPlayer=new MediaPlayer();
        //请求通知权限
        NotificationAccess.gotoOpen(this);
        preferences =  this.getSharedPreferences("session",MODE_PRIVATE);
        String path=preferences.getString("sound", "");
        //mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.clock);

        if(mMediaPlayer==null){
            mMediaPlayer=new MediaPlayer();
        }else{
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer=new MediaPlayer();
        }
        try {
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }}
