package com.zwt.myapp.util;

import com.zwt.myapp.config.Config;

public class AppConstant 
{
	public class MainConstant
	{
		//标签的名字
		public static final String netTabName="信息下载";
		public static final String localTabName="本地下载";
	}
	
	public class NetworkConstant
	{
		//TODO downPath为下载地址
		public static final String downPath="";
		//savePath为保存音频的路径
		public static final String savePath="/mnt/sdcard/localDown/";

	}
	
	public class AdapterConstant
	{
		public static final String down_over="已下载";
		public static final String delete="文件已经删除！";
	}
	
	public class DownloadServiceConstant
	{
		public static final String downOverAction="down_over_action";
	}
	
	public class LocalActivityConstant
	{
		public static final String update_action="updateUI";
	}
}
