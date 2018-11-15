package com.example.yiy.parkingucr;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yiy.Util.Dialogs;
import com.yiy.Util.HtmlService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String Lot [] = {
            "https://streetsoncloud.com/parking/rest/occupancy/id/83?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/82?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/80?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/243?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/238?callback=jQuery",
            "https://streetsoncloud.com/parking/rest/occupancy/id/84?callback=jQuery"
    };

    private SimpleAdapter adapter;
    private PendingIntent pendingIntent = null;

    private static final int SHOW_DATAPICK = 0;   //这4个是时间方面的.
    private static final int DATE_DIALOG_ID = 1;
    private static final int SHOW_TIMEPICK = 2;
    private static final int TIME_DIALOG_ID = 3;


    private final static int NULLS = 0; //off-line
    private final static int NULLDATA = 1; // Search error
    private final static int DATA = 2;
    String update_time;
    private String date;
    private String time;

    List<Map<String, Object>> listviews;
    private ListView listview_data;
    private Dialogs dialogs;

    private int mYear;    //依然是时间方面移植来的
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NULLS:
                    Toast.makeText(MainActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                    break;

                case NULLDATA:
                    AlertDialog.Builder alertdialogbuilder = new AlertDialog.Builder(MainActivity.this);
                    alertdialogbuilder.setTitle("ERROR");
                    alertdialogbuilder.setMessage("no information found, please search again");
                    alertdialogbuilder.setPositiveButton("OK", search_error);
                    AlertDialog alertdialog = alertdialogbuilder.create();
                    alertdialog.show();
                    break;

                case DATA:  //显示查询结果
                    System.out.println("这是listview_data -------: "+listview_data);
                    listview_data.setVisibility(View.VISIBLE);
                    adapter = new SimpleAdapter(MainActivity.this, listviews, R.layout.item,
                            new String[]{
                            "location_address", "location_name", "total_spaces", "free_spaces"},
                            new int[]{
                             R.id.location_address, R.id.location_name, R.id.total_spaces, R.id.free_spaces});
//                    dialogs.dialog.dismiss();
                    ToastUtil.showToast(MainActivity.this, "Last modified:  " + update_time.substring(0,20));
                    /*特效源码！！*/
                    listview_data.setLayoutAnimation(getListAnim());
                    listview_data.setAdapter(adapter);
                    break;
            }
        }

        ;
    };

    /**
     * --------------------以下为日期选择方面,移植来的--------------------
     */
    private void setDateTime() {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDateDisplay();
    }

    private void updateDateDisplay() {
        date = new StringBuilder().append(mYear).append("-").append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1)).append("-").append((mDay < 10) ? "0" + mDay : mDay) + "";
    }
    private void updateTimeDisplay() {
        time = new StringBuilder().append(mHour).append(":").append(mMinute)+ "";
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            /*------这是提示框的确认按钮,点击后选择时间,然后得到网址,执行线程--------*/
            updateDateDisplay();
            System.out.println("这是日期选取："+ date);

        }
    };
    private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener(){

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mHour = hour;
            mMinute = minute;
            updateTimeDisplay();
            System.out.println("这是时间选取："+ time);
            alarmMethod();
        }
    };


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute, true);
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case DATE_DIALOG_ID:
                ((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
                break;
            case TIME_DIALOG_ID:
                ((TimePickerDialog) dialog).updateTime(mHour, mMinute);
                break;
        }
    }

    Handler dateandtimeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MainActivity.SHOW_DATAPICK:
                    showDialog(DATE_DIALOG_ID);
                    break;
                case MainActivity.SHOW_TIMEPICK:
                    showDialog(TIME_DIALOG_ID);
                    break;
            }
        }
    };
    /**------------------------------以上为日期选取-------------------------------------*/



    private void refresh(){
        System.out.println("-----进入Refresh-----");
        listviews = null;
        listviews = new ArrayList<Map<String, Object>>();
        new Thread(new SearchThread()).start();//启动线程,下载内容
        System.out.println("查询线程已经启动");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview_data = findViewById(R.id.listview_data);

        /*------------下面这段是写时间选择方面的--------------*/
        final Calendar c = Calendar.getInstance();
        dialogs = new Dialogs(MainActivity.this);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        /*------------------------------------------------------*/


        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //refresh bar
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refresh",Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();
                refresh();
            }
        });
    }

    class SearchThread implements Runnable {
        @Override
        public void run() {
            JSONObject jsonObject;
            try {
                for(int i = 0; i <6; i ++){
                    String URL = Lot[i];
                    System.out.println("这是获取的网址 ： " + URL);
                    String initial_results = HtmlService.getHtml(URL);
                    System.out.println("这是获取的原始数据 ： " + initial_results);
                    String results = initial_results.substring(7, initial_results.length() - 1);
//                  String results = Lot[i];
                    System.out.println("这是最后要解析的数据-----" + results);

                    /**---------------------------- 解析 ------------------------*/
                    jsonObject = new JSONObject(results);
                    System.out.println("这是jsonObject-----" + jsonObject.toString());
                    /**---------------------------------------------------------------------------*/
                    System.out.println("这是第一个元素status-----" + jsonObject.getString("status"));
                    if (jsonObject.get("status") == "OK") {
                        System.out.println("搜索失败，搜索结果为： " + jsonObject.getString("status"));
                        System.out.println(jsonObject.getString("status"));
                        //下面这三句是在查找不到的时候弹出提示框查询失败
                        Message msg = new Message();
                        msg.what = NULLDATA;
                        handler.sendMessage(msg);
                    } else {
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        System.out.println("这是jsonArray----" + jsonArray);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        System.out.println("这是i ------" + i);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        System.out.println("这是obj -----" + obj);
                        String location_name = obj.getString("location_name");
                        String location_address = obj.getString("location_address");
                        JSONArray data = obj.getJSONArray("data");
                        System.out.println("这是data------" + data);
                        JSONObject data1 = data.getJSONObject(0);

                        System.out.println("这是data1------" + data1);
                        String total_spaces = data1.getString("total_spaces");
                        System.out.println("total_space------" + total_spaces);
                        String free_spaces = data1.getString("free_spaces");
                        System.out.println("free_spaces------" + free_spaces);
                        String date_time = data1.getString("date_time");
                        System.out.println("date_time------" + date_time);

                        HashMap<String, Object> map = new HashMap<String, Object>();
                        System.out.println("将解析数据放入HashMap");
                        map.put("location_name", location_name);
                        map.put("location_address", location_address);
                        map.put("total_spaces", total_spaces);
                        map.put("free_spaces", free_spaces);
                        map.put("date_time", date_time);
                        listviews.add(map);
                        System.out.println("这是listviews ---" + listviews);
                        update_time = date_time;
                        Message msg = new Message();
                        msg.what = DATA;
                        handler.sendMessage(msg);
//                    }
                    }
                }
            } catch (Exception e) {
                Log.i("解析错误", "执行了解析错误");
                Message msg = new Message();
                msg.what = NULLS;
                handler.sendMessage(msg);
            }
        }
    }
    /*-----------以下为listview 特效------------------*/
    public static LayoutAnimationController getListAnim() {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(300);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(500);
        set.addAnimation(animation);
        LayoutAnimationController controller = new LayoutAnimationController(set, 0.5f);
        return controller;
        /*---------------以上为listview 特效------------------*/
    }

    //查询失败的时候的弹出框
    private DialogInterface.OnClickListener search_error = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface arg0, int arg1) {
            listview_data.setVisibility(View.GONE);
        }
    };

    public static class ToastUtil {
        private static Toast toast;
        public static void showToast(Context context, String content) {
            if (toast == null) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
            } else {
                toast.setText(content);
            }
            toast.show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the reminder
            Message msg1 = new Message();
            msg1.what = MainActivity.SHOW_TIMEPICK;
            MainActivity.this.dateandtimeHandler.sendMessage(msg1);
            Message msg = new Message();
            msg.what = MainActivity.SHOW_DATAPICK;
            MainActivity.this.dateandtimeHandler.sendMessage(msg);
//            alarmMethod();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//code from : https://blog.csdn.net/qq_16628781/article/details/51548324
    private void alarmMethod(){
        System.out.println("-----进入alarmMethod-----");

//        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        Intent intent = new Intent();//这个intent会传给目标,可以使用getIntent来获取
//        intent.setClass(this, MainActivity.class);
//        //这里放一个count用来区分每一个通知
//        intent.putExtra("intent", "intent--->" + count);//这里设置一个数据,带过去
//
//        //参数1:context 上下文对象
//        //参数2:发送者私有的请求码(Private request code for the sender)
//        //参数3:intent 意图对象
//        //参数4:必须为FLAG_ONE_SHOT,FLAG_NO_CREATE,FLAG_CANCEL_CURRENT,FLAG_UPDATE_CURRENT,中的一个
//        pendingIntent = PendingIntent.getActivity(this, count, intent, PendingIntent.FLAG_CANCEL_CURRENT);//用户点击该notification后才启动SecondActivity类
//
//        intent.putExtra("intent", "other intent--->" + count);
//        Uri uri = Uri.parse("http:///baidu.com");//我这里还可以给一个uri参数,点击notification可以打开百度首页,但是intent.setClass就不可以要了
//        intent.setData(uri);//设置uri到intent中
//        intent.setAction(Intent.ACTION_VIEW);//设置为展示uri的内容,系统会自动给出可以打开uri的应用,需要你选择
//        //用bundle来传参
//        Bundle bundle = new Bundle();
//        bundle.putString("key", "bundle string--->" + count);
//        intent.putExtra("bundle", bundle);//这里把bundle放到intent中,可以在SecondActivity获取出来
//
//        //参数1,2,3,4和前面的一样
//        //最后一个参数是:指定目标activity如何创建的额外参数
//        pendingIntent = PendingIntent.getActivity(this, count, intent, PendingIntent.FLAG_ONE_SHOT, bundle);//API 16以上
////        pi = PendingIntent.getService(this, 102, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//
//        Notification.Builder builder = new Notification.Builder(this)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentIntent(pendingIntent)
//                .setContentText("this is notification contentText msg--->" + count)
//                .setContentTitle("this is contentTitle--->" + count)
//                .setTicker("this is msg's hint count--->" + count)
//                .setNumber(count);
////        Notification no = new Notification.Builder(this).build();//这个需要在API 16以上才可以用,我这里向下兼容到API 14(4.0)
//        Notification notify2 = builder.getNotification();//而getNotification()方法已经被弃用了
//        count++;
//        notify2.flags |= Notification.FLAG_AUTO_CANCEL;//这里指定这个通知点击之后或者可以被清除按钮清除。 FLAG_NO_CLEAR 通知不能取消
//        //参数1:此notification的识别好
//        nm.notify(101, notify2);

        Intent myIntent = new Intent(this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE, mMinute);
        calendar.set(Calendar.HOUR_OF_DAY, mHour);
        calendar.set(Calendar.DAY_OF_MONTH,mDay);
        calendar.set(Calendar.MONTH,mMonth);
        calendar.set(Calendar.YEAR,mYear);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

}
