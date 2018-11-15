package com.example.yiy.parkingucr;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //设置通知内容并在onReceive()这个函数执行时开启
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification=new Notification(R.drawable.ic_menu_camera,"用电脑时间过长了！"
                ,System.currentTimeMillis());

        notification.defaults = Notification.DEFAULT_ALL;
        manager.notify(1, notification);


        //再次开启Service这个服务，从而可以
        Intent i = new Intent(context, NotifyService.class);
        context.startService(i);
    }
}