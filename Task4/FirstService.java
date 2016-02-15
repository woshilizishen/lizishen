package com.example.lenovo6.task4;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class FirstService extends Service {
    @Nullable
    Timer ti;
    public IBinder onBind(Intent intent) {
        return null;
    }

    //获取前台应用包名
    public String getForeground() {
        ActivityManager activityManager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        //获取到当前所有进程
        List<ActivityManager.RunningAppProcessInfo> processInfoList = activityManager.getRunningAppProcesses();
        if (processInfoList ==null || processInfoList.isEmpty()) {
            return "";
        }
        //遍历进程列表，找到第一个前台进程
        for (ActivityManager.RunningAppProcessInfo processInfo : processInfoList) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return processInfo.processName;
            }
        }
        return "";
    }


    //Service主体，定时重复
    public int onStartCommand(Intent intent, int flags, int startId) {
        ti = new Timer();
        class Task extends TimerTask {
            public void run(){
                judge();
            }
            public void judge() {
                Log.e("包名", getForeground());
                if(!Objects.equals(getForeground(), "com.example.lenovo6.task4")) {
                    Intent intent = new Intent(FirstService.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }
        Task task = new Task();
        ti.schedule(task, 0, 500);
        return  START_NOT_STICKY;
    }

    public void onDestroy() {
        ti.cancel();
    }


}
