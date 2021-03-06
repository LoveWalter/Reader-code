package com.zwt.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 书签数据库的建立
 * 
 * @author
 * 
 */
public class MarkDBHelper extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "mark.db";
	private static int DATABASE_VERSION = 1;
	private String PATH = "path";
	private String s = null;

	public MarkDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.s = "mark";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i("TAG", s);
		String sql = "CREATE TABLE " + s + " ( " + PATH + " text not null, "
				+ "begin int not null default 0,"
				+ " word text not null , time text not null,name text not null);";
		db.execSQL(sql);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
