package com.example.yiy.parkingucr;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.yiy.Util.HtmlService;

import org.json.JSONArray;
import org.json.JSONObject;

public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    String Lot [] = {
            "https://streetsoncloud.com/parking/rest/occupancy/id/83?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/82?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/80?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/243?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/238?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/84?callback=jQuery"
    };

     @Override
     public int onStartCommand(Intent intent, int flags, int startId) {
         Thread thread = new Thread(runnable);
         thread.start();
         return super.onStartCommand(intent, flags, startId);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String[] data = accessInfo();
            notifyUser(data);
        }
    };

    public void notifyUser(String[] getData){
        Log.v("notifyUser","notifyUser called");
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        String channelid = "1";
        Notification notification = new NotificationCompat.Builder(this, channelid)
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setSound(sound)
                .setContentTitle("ParkingUCR")
                .setContentText("the latest information about parking spaces is available")
                .setTicker("ParkingUCR notification")
                .setContentIntent(pIntent)
                .setStyle(new NotificationCompat.InboxStyle()
                        .addLine("Lot name: " + getData[0] + ", free sapces: " + getData[1])
                        .addLine("Lot name: " + getData[2] + ", free sapces: " + getData[3])
                        .addLine("Lot name: " + getData[4] + ", free sapces: " + getData[5])
                        .addLine("Lot name: " + getData[6] + ", free sapces: " + getData[7])
                        .addLine("Lot name: " + getData[8] + ", free sapces: " + getData[9])
                        .addLine("Lot name: " + getData[10] + ", free sapces: " + getData[11]))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(11132, notification);

//        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
//        Notification mNotify = new Notification.Builder(this)
//                .setContentTitle("ParkingUCR")
//                .setContentText(getData[0] + ": " + getData[1] + ""
//                        +getData[2] + ": " + getData[3] + "\n"
//                        +getData[4] + ": " + getData[5] + "\n"
//                        +getData[6] + ": " + getData[7] + "\n"
//                        +getData[8] + ": " + getData[9] + "\n")
//                .setSmallIcon(R.drawable.ic_menu_camera)
//                .setContentIntent(pIntent)
//                .setSound(sound)
//                .setActions()
//                .setTicker("ParkingUCR notification")
//                .build();
//        mNotify.flags |= Notification.FLAG_AUTO_CANCEL;
//        mNM.notify(1, mNotify);
    }

    public String[] accessInfo(){
        String[] getData = new String[]{"","","","","","","","","","","",""};

        JSONObject jsonObject;
        int j = 0;
        try {
            for(int i = 0; i < 11; i = i + 2) {
                String URL = Lot[j];
                String initial_results = HtmlService.getHtml(URL);
                String results = initial_results.substring(7, initial_results.length() - 1);

                /**---------------------------- 解析 ------------------------*/
                jsonObject = new JSONObject(results);
                if (jsonObject.get("status") == "OK") {
                    System.out.println("NotifyService搜索失败，搜索结果为： " + jsonObject.getString("status"));
                    System.out.println(jsonObject.getString("status"));
                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    JSONObject obj = jsonArray.getJSONObject(0);
                    String location_name = obj.getString("location_name");
                    JSONArray data = obj.getJSONArray("data");
                    JSONObject data1 = data.getJSONObject(0);
                    String free_spaces = data1.getString("free_spaces");

                    getData[i] = location_name;
                    Log.v("getdata",location_name);
                    getData[i + 1] = free_spaces;
                    Log.v("getdata",free_spaces);
                    j++;
                }
            }
        } catch (Exception e) {
            Log.i("NotifyService解析错误", "执行了解析错误");
        }
        return getData;
    }
}