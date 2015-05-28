package com.zwt.myapp.util;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.zwt.myapp.config.Config;
import com.zwt.myapp.database.BookDBHelper;

public class MyApplication extends Application {
	
	public static BookDBHelper bookDB;
	private static Context context;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("hck", "oncreat");
		context = getApplicationContext();
		initDateBase();
		
		
	}

	private static void initDateBase() {
		bookDB = new BookDBHelper(context, Config.DATABASE_TABKE);
	}
	

	
}
