package com.zwt.myapp.activity;


import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.zwt.myapp.R;
import com.zwt.myapp.adapter.LocalDownAdapter;
import com.zwt.myapp.bean.FileState;
import com.zwt.myapp.database.DownloadDao;
import com.zwt.myapp.service.DownloadService;
import com.zwt.myapp.util.AppConstant;

public class LocalDownActivity extends Activity 
{
	private DownloadDao dao=null;//用来与数据库交互
	private ListView listView=null;
	public List<FileState> list=null;//用于存放要显示的列表
	private LocalDownAdapter adapter;//自定义adapter
	private UpdateReceiver myReceiver;//广播接收器
    
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local);
		dao=new DownloadDao(this);
		myReceiver = new UpdateReceiver(this);
		//对广播进行注册
		myReceiver.registerAction(AppConstant.LocalActivityConstant.update_action);
	}
	
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		//从数据库的localdown_info表中获取数据
		list = dao.getFileState();
		initUi();
	}
	
	
	
	/**
	 * 初始化UI 
	 * **/
	private void initUi()
	{
		listView = (ListView)this.findViewById(R.id.listview);
		adapter = new LocalDownAdapter(this,list,dao);
		listView.setAdapter(adapter);
		//点击item可以暂停下载，开始下载
		listView.setOnItemClickListener(new OnItemClickListener()
		{

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				TextView text=(TextView)view.findViewById(R.id.local_musicname);
				TextView tvfileurl=(TextView) view.findViewById(R.id.local_fileurl);
				String musicName=text.getText().toString();
				String fileurl=tvfileurl.getText().toString();
				FileState fileState=list.get(position);
				Intent intent = new Intent();
				intent.setClass(LocalDownActivity.this, DownloadService.class);
				intent.putExtra("musicName", musicName);
				intent.putExtra("fileurl", fileurl);
				intent.putExtra("flag","setState");//标志着数据从localdownactivity传送
				intent.putExtra("fileState", fileState);
				LocalDownActivity.this.startService(intent);//这里启动service
			}
			
		});
	}
	
	@Override
	protected void onDestroy() 
	{
		dao.updateFileDownState(list);//当activity退出时,更新localdown_info这个表
		super.onDestroy();
	}
	
	class UpdateReceiver extends BroadcastReceiver
	{
		private Context context;
		public UpdateReceiver(Context context)
		{
			this.context=context;
		}
		
		public void registerAction(String action)
		{
			IntentFilter intentFilter=new IntentFilter();
			intentFilter.addAction(action);
			registerReceiver(this, intentFilter);
		}
		
		@Override
		public void onReceive(Context context, Intent intent)
		{
			//接收来自DownloadService传送过来的数据,并且更新进度条
			if(intent.getAction().equals(AppConstant.LocalActivityConstant.update_action))
			{
				String url=intent.getStringExtra("url");
				int completeSize = intent.getIntExtra("completeSize", 0);
				for(int i=0;i<list.size();i++)
				{
					FileState fileState=list.get(i);
					if(fileState.getUrl().equals(url))
					{
						fileState.setCompleteSize(completeSize);
						list.set(i, fileState);//更新list中的数据
						break;
					}
				}
				adapter.setList(list);//更新完list的数据要把它重新传入adapter，这样才能更新
				adapter.notifyDataSetChanged();
			}
		}
	}

}
