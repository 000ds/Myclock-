package com.example.myclock;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * @author 曹晓东 ,Created on 2020/5/1
 * 联系： 2806029294@qq.com
 */
public class ClockReceiver extends BroadcastReceiver {

    private int code;

    @Override
    public void onReceive(Context context, Intent intent) {
            code = intent.getIntExtra("requestcode", 0);
            Intent intent1=new Intent(context,ClockService.class);
            intent1.putExtra("flag","ClockReceiver");
            intent1.putExtra("code",code+"");
            context.startService(intent1);

    }



}
