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
	private DownloadDao dao=null;//���������ݿ⽻��
	private ListView listView=null;
	public List<FileState> list=null;//���ڴ��Ҫ��ʾ���б�
	private LocalDownAdapter adapter;//�Զ���adapter
	private UpdateReceiver myReceiver;//�㲥������
    
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.local);
		dao=new DownloadDao(this);
		myReceiver = new UpdateReceiver(this);
		//�Թ㲥����ע��
		myReceiver.registerAction(AppConstant.LocalActivityConstant.update_action);
	}
	
	
	@Override
	protected void onResume() 
	{
		super.onResume();
		//�����ݿ��localdown_info���л�ȡ����
		list = dao.getFileState();
		initUi();
	}
	
	
	
	/**
	 * ��ʼ��UI 
	 * **/
	private void initUi()
	{
		listView = (ListView)this.findViewById(R.id.listview);
		adapter = new LocalDownAdapter(this,list,dao);
		listView.setAdapter(adapter);
		//���item������ͣ���أ���ʼ����
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
				intent.putExtra("flag","setState");//��־�����ݴ�localdownactivity����
				intent.putExtra("fileState", fileState);
				LocalDownActivity.this.startService(intent);//��������service
			}
			
		});
	}
	
	@Override
	protected void onDestroy() 
	{
		dao.updateFileDownState(list);//��activity�˳�ʱ,����localdown_info�����
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
			//��������DownloadService���͹���������,���Ҹ��½�����
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
						list.set(i, fileState);//����list�е�����
						break;
					}
				}
				adapter.setList(list);//������list������Ҫ�������´���adapter���������ܸ���
				adapter.notifyDataSetChanged();
			}
		}
	}

}
