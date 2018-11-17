package com.example.yiy.parkingucr;

import android.app.Notification;
import android.app.Notification.Style;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
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

    public void notifyUser(String[] getData){

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("ParkingUCR")
                .setContentText("Lot name: " + getData[0] + ", free sapces: " + getData[1] + "\n"
                        +"Lot name: " + getData[2] + ", free sapces: " + getData[3] + "\n"
                        +"Lot name: " + getData[4] + ", free sapces: " + getData[5] + "\n"
                        +"Lot name: " + getData[6] + ", free sapces: " + getData[7] + "\n"
                        +"Lot name: " + getData[8] + ", free sapces: " + getData[9] + "\n")
                .setSmallIcon(R.drawable.ic_menu_camera)
                .setContentIntent(pIntent)
                .setSound(sound)
                .setActions()
                .setTicker("ParkingUCR notification")
                .build();
        mNotify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNM.notify(1, mNotify);



//        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
//        inboxStyle
//                .setBigContentTitle("ParkingUCR")
//                .setSummaryText("SummaryText")
//                .addLine("Lot name: " + getData[0] + ", free sapces: " + getData[1])
//                .addLine("Lot name: " + getData[2] + ", free sapces: " + getData[3])
//                .addLine("Lot name: " + getData[4] + ", free sapces: " + getData[5])
//                .addLine("Lot name: " + getData[6] + ", free sapces: " + getData[7])
//                .addLine("Lot name: " + getData[8] + ", free sapces: " + getData[9])
//                .build();
//        mBuilder.setStyle(inboxStyle); //设置列表样式
//        Notification notification = inboxStyle.build();
//        mBuilder.notify(100, notification);


//        Notification notification = new Notification.InboxStyle(new Notification.Builder(this)
//                .setTicker("ParkingUCR notification")
//                .setSmallIcon(R.drawable.ic_menu_camera)
//                .setContentTitle("ParkingUCR")
//                .setSound(sound)
//                .setContentIntent(pIntent))
//                .addLine("Lot name: " + getData[0] + ", free sapces: " + getData[1])
//                .addLine("Lot name: " + getData[2] + ", free sapces: " + getData[3])
//                .addLine("Lot name: " + getData[4] + ", free sapces: " + getData[5])
//                .addLine("Lot name: " + getData[6] + ", free sapces: " + getData[7])
//                .addLine("Lot name: " + getData[8] + ", free sapces: " + getData[9])
//                .setBigContentTitle("Here Your Messages")
//                .setSummaryText("+3 more")
//                .build();
//        mNM.notify(1, notification);


//        @SuppressWarnings("deprecation") NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("Event tracker")
//                .setContentText("Events received");
//
//        NotificationCompat.InboxStyle inboxStyle =
//                new NotificationCompat.InboxStyle();
//
//        inboxStyle.setBigContentTitle("Event tracker details:");
//
//            inboxStyle
//                .setBigContentTitle("ParkingUCR")
//                .setSummaryText("SummaryText")
//                .addLine("Lot name: " + getData[0] + ", free sapces: " + getData[1])
//                .addLine("Lot name: " + getData[2] + ", free sapces: " + getData[3])
//                .addLine("Lot name: " + getData[4] + ", free sapces: " + getData[5])
//                .addLine("Lot name: " + getData[6] + ", free sapces: " + getData[7])
//                .addLine("Lot name: " + getData[8] + ", free sapces: " + getData[9]);
//
//        mBuilder.setStyle(inboxStyle);
//
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//// mId allows you to update the notification later on.
//        mNotificationManager.notify(1, mBuilder.build());
    }

    public String[] accessInfo(){
         String[] getData = new String[]{"","","","","","","","","",""};

        JSONObject jsonObject;
        try {
            for(int i = 0; i <6; i = i + 2){
                String URL = Lot[i];
                System.out.println("这是NotifyService获取的网址 ： " + URL);
                String initial_results = HtmlService.getHtml(URL);
                System.out.println("这是NotifyService获取的原始数据 ： " + initial_results);
                String results = initial_results.substring(7, initial_results.length() - 1);
                System.out.println("这是NotifyService最后要解析的数据-----" + results);

                /**---------------------------- 解析 ------------------------*/
                jsonObject = new JSONObject(results);
                System.out.println("这是NotifyService的jsonObject-----" + jsonObject.toString());
                /**---------------------------------------------------------------------------*/
                System.out.println("这是NotifyService第一个元素status-----" + jsonObject.getString("status"));
                if (jsonObject.get("status") == "OK") {
                    System.out.println("NotifyService搜索失败，搜索结果为： " + jsonObject.getString("status"));
                    System.out.println(jsonObject.getString("status"));

                } else {
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    System.out.println("这是NotifyService的jsonArray----" + jsonArray);
                    JSONObject obj = jsonArray.getJSONObject(0);
                    System.out.println("这是obj -----" + obj);
                    String location_name = obj.getString("location_name");
                    JSONArray data = obj.getJSONArray("data");
                    System.out.println("这是NotifyService的data------" + data);
                    JSONObject data1 = data.getJSONObject(0);

                    System.out.println("这是data1------" + data1);
                    String total_spaces = data1.getString("total_spaces");
                    System.out.println("total_space------" + total_spaces);
                    String free_spaces = data1.getString("free_spaces");
                    System.out.println("free_spaces------" + free_spaces);
                    String date_time = data1.getString("date_time");
                    System.out.println("date_time------" + date_time);

                    getData[i] = location_name;
                    getData[i + 1] = free_spaces;
                }
            }
        } catch (Exception e) {
            Log.i("NotifyService解析错误", "执行了解析错误");
        }
        return getData;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String[] data = accessInfo();
            notifyUser(data);
        }
    };

}