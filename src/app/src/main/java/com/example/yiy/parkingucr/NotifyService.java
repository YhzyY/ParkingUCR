package com.example.yiy.parkingucr;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

import java.util.Calendar;

public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("ParkingUCR")
                .setContentText("Log your steps for today")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pIntent)
                .setSound(sound)
                .setTicker("ParkingUCR notification")
                .build();

        mNotify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNM.notify(1, mNotify);
    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MINUTE, 23);
//        calendar.set(Calendar.HOUR, 19);
//        calendar.set(Calendar.AM_PM, Calendar.PM);
//        calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        long triggerAtTime =calendar.getTimeInMillis();
//        //此处设置开启AlarmReceiver这个Service
//        Intent i = new Intent(this, AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        //ELAPSED_REALTIME_WAKEUP表示让定时任务的出发时间从系统开机算起，并且会唤醒CPU。
//        manager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        //在Service结束后关闭AlarmManager
//        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        Intent i = new Intent(this, AlarmReceiver.class);
//        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager.cancel(pi);
//    }

}