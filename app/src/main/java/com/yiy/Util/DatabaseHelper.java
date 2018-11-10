package com.yiy.Util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * SQLiteOpenHelper是一个辅助类，用来管理数据库的创建和版本，它提供两个方面的功能
 * 第一，getReadableDatabase()、getWritableDatabase()可以获得SQLiteDatabase对象，通过该对象可以对数据库进行操作
 * 第二，提供了onCreate()、onUpgrade()两个回调函数，允许我们再创建和升级数据库时，进行自己的操作
 */
public class DatabaseHelper extends SQLiteOpenHelper {

	//private static final String DB_NAME = "TrainInfo.db"; //数据库名称
    private static final String DB_NAME = "PiPiXiaInfo.db"; //数据库名称
	private static final int version = 1; //数据库版本

	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//数据库指令创建三个保存飞机、大巴和火车的文件
		db.execSQL("create table station(TrainCode varchar(20) not null , FirstStation varchar(20) not null , LastStation varchar(20) not null, StartStation varchar(20) not null , StartTime varchar(20) not null, ArriveStation varchar(20) not null , ArriveTime varchar(20) not null , KM  varchar(20) not null , UseDate varchar(20) not null );");
		db.execSQL("create table plane(Company varchar(20) not null , AirlineCode varchar(20) not null , StartDrome varchar(20) not null , ArriveDrome varchar(20) not null , StartTime varchar(20) not null , ArriveTime varchar(20) not null , Mode varchar(20) not null , Week varchar(20) not null);");
        db.execSQL("create table bus(bustype varchar(20) not null , distance varchar(20) not null ,startcity varchar(20) not null, startstation varchar(20) not null , endcity varchar(20) not null,endstation varchar(20) not null ,starttime varchar(20) not null , price varchar(20) not null);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}


