package com.zwt.myapp.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.zwt.myapp.R;
import com.zwt.myapp.util.AppConstant;
/**
	 * 创建框架，2个标签
	 * 1.网络音频
	 * 2.本地下载
	 * **/
//start:这里创建一个网络音频的标签
//创建一个intent，用于跳转到标签对应的activity
//创建标签页
//设置信息显示板,也就是标签的名称
//设置标签对应的内容
//添加标签
//end
//start:这里创建一个本地下载的标签
//end
////设置当前显示哪个标签 
public class PersonDownloadActivity extends TabActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_download);
		createFrame();
	}
	
	/**
	 * 创建框架，2个标签
	 * 1.网络音频
	 * 2.本地下载
	 * **/
	private void createFrame()
	{
		TabHost tabHost = this.getTabHost();
		//start:这里创建一个网络音频的标签
		//创建一个intent，用于跳转到标签对应的activity
		Intent networkIntent = new Intent(); 
		networkIntent.setClass(PersonDownloadActivity.this, NetworkActivity.class);
		TabHost.TabSpec networkSpec= tabHost.newTabSpec("network");//创建标签页
		networkSpec.setIndicator(AppConstant.MainConstant.netTabName);//设置信息显示板,也就是标签的名称
		networkSpec.setContent(networkIntent);//设置标签对应的内容
		tabHost.addTab(networkSpec);//添加标签
		//end
		//start:这里创建一个本地下载的标签
		Intent localIntent = new Intent();
		localIntent.setClass(PersonDownloadActivity.this, LocalDownActivity.class);
		TabHost.TabSpec localSpec=tabHost.newTabSpec("local");
		localSpec.setIndicator(AppConstant.MainConstant.localTabName);
		localSpec.setContent(localIntent);
		tabHost.addTab(localSpec);
		//end
		////设置当前显示哪个标签 
		tabHost.setCurrentTab(0);
	}
	

}
