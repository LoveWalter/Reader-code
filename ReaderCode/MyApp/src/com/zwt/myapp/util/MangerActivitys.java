package com.zwt.myapp.util;

import java.util.ArrayList;
import java.util.List;

public class MangerActivitys {
	public static List<Object> activitys = new ArrayList<Object>();

	public static void addActivity(Object object) // 添加新創建的activity
	{
		activitys.add(object);
	}

}
