package com.zwt.myapp.database;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DownloadDBHelper extends SQLiteOpenHelper
{
	
	private static String DATABASE_NAME="down.db";
	private static int version=1;
	
	public DownloadDBHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, version);
	}

	  
    /**
     * ��down.db���ݿ��´���һ��download_info��洢������Ϣ
     * ����һ��localdown_info��洢����������Ϣ
     */
	@Override
	public void onCreate(SQLiteDatabase db)
	{	
		 db.execSQL("create table download_info(_id integer PRIMARY KEY AUTOINCREMENT, thread_id integer, "
	                + "start_pos integer, end_pos integer, compelete_size integer,url varchar(50))");
		 db.execSQL("create table localdown_info(_id integer PRIMARY KEY AUTOINCREMENT,music_name varchar(30),url varchar(50),completeSize integer,fileSize integer,state integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		String sql="drop table if exists download_info";
		String sqlOne="drop table if exists localdown_info";
		db.execSQL(sql);
		db.execSQL(sqlOne);
		onCreate(db);
	}
	
}
