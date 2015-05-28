package com.zwt.myapp.service;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import com.zwt.myapp.bean.FileState;
import com.zwt.myapp.bean.LoadInfo;
import com.zwt.myapp.database.DownloadDao;
import com.zwt.myapp.util.AppConstant;
import com.zwt.myapp.util.Downloader;


public class DownloadService extends Service 
{
	public static Map<String,Downloader> downloaders=new HashMap<String, Downloader>();;
	//���ÿ�������ļ���ɵĳ���
	private Map<String,Integer> completeSizes=new HashMap<String, Integer>();
	//���ÿ�������ļ����ܳ���
	private Map<String,Integer> fileSizes=new HashMap<String, Integer>();
	private Downloader downloader;
	public DownloadDao dao=null;
	private Handler mHandler=new Handler()
	{

		/**
		 * ����Download��ÿ���̴߳������������
		 * **/
		@Override
		public void handleMessage(Message msg)
		{
			if(msg.what==1)
			{
				String url = (String)msg.obj;
				int length = msg.arg1;
				int completeSize=completeSizes.get(url);
				int fileSize=fileSizes.get(url);
				completeSize+=length;
				completeSizes.put(url, completeSize);
				if(completeSize==fileSize)
				{
					//����ļ��������,����localdown_info�����ļ���״̬Ϊ0
					//ֻ�����������ж϶�����activity��Ϊ����activityû��������״̬ʱҲ�ܸ���
					dao.updateFileState(url);
				}
				Intent intent = new Intent();
				intent.setAction(AppConstant.LocalActivityConstant.update_action);
				intent.putExtra("completeSize", completeSize);
				intent.putExtra("url", url);
				DownloadService.this.sendBroadcast(intent);
			}
		}
		
	};
	


	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}	
	@Override
	public void onCreate()
	{
		super.onCreate();
		dao=new DownloadDao(this);
	}
	
	@Override
	public void onStart(Intent intent, int startId)
	{
		String musicName=intent.getStringExtra("musicName");
		String fileurl=intent.getStringExtra("fileurl");
		System.out.println(fileurl);
		String flag=intent.getStringExtra("flag");
		if(flag.equals("startDownload"))
		{
			//��������Ǵ�networkActivity�д��͹����ģ���ֱ�ӿ�ʼ����
			startDownload(musicName,fileurl);
		}
		if(flag.equals("setState"))
		{
			FileState fileState=intent.getParcelableExtra("fileState");
			//��ֹ�û�����Ѿ�������ɵ��ļ�
			if(fileState.getState()!=0)
			{
				//��������Ǵ�LocalDownActivity�д��͹����ģ�˵����Ϊ��Ҫ�ı�״̬��������ͣ
				setState(musicName,fileurl);
			}
		}
		if(flag.equals("delete"))
		{
			//��������Ǵ�LocalDownAdapter���͹����ģ�˵����Ϊ��Ҫɾ������
			//String url=AppConstant.NetworkConstant.downPath+musicName;
			String url=fileurl;
			deleteData(url);
		}
		super.onStart(intent, startId);
	}

	public void startDownload(String musicName,String fileurl)
	{
		//�ȴ����ݿ����жϣ�����ļ��Ƿ��Ѿ��������б���
		if(!dao.isHasFile(musicName))
		{
			Toast.makeText(getApplicationContext(), "�ļ��Ѿ����ڣ�", Toast.LENGTH_SHORT).show();
			return;
		}
		int threadCount=3;//�����߳���
		//String downPath=AppConstant.NetworkConstant.downPath+musicName;//���ص�ַ
		String downPath=fileurl;//���ص�ַ
		String savePath=AppConstant.NetworkConstant.savePath;//�����ַ
		downloader = downloaders.get(downPath);
		if(downloader==null)
		{
			downloader = new Downloader(downPath,savePath,musicName,threadCount,this,mHandler);
			downloaders.put(downPath, downloader);//������һ���µ�������,����������뵽������������ȥ
		}
		if (downloader.isdownloading())//�������������ʲôҲ����
           return;
		//LoadInfo��һ��ʵ����,�����װ��һЩ��������Ҫ����Ϣ,ÿ��loadinfo��Ӧ1��������
        LoadInfo loadInfo =downloader.getDownloaderInfors();
		FileState fileState = new FileState(musicName,downPath,loadInfo.getComplete(),loadInfo.getFileSize(),1);
		dao.insertFileState(fileState,this);//��localdown_info���в���һ����������
		completeSizes.put(downPath, loadInfo.getComplete());
		fileSizes.put(downPath, fileState.getFileSize());
		 // ���÷�����ʼ����
        downloader.download();
	}
	
	/**
	 * �������ط��������������ͣ�ˣ����ô˷�����������
	 * **/
	public void reStartDownload(String musicName,String fileurl)
	{
		int threadCount=3;//�����߳���
		//String downPath=AppConstant.NetworkConstant.downPath+musicName;//���ص�ַ
		String downPath=fileurl;
		String savePath=AppConstant.NetworkConstant.savePath;//�����ַ
		downloader = downloaders.get(downPath);
		if(downloader==null)
		{
			downloader = new Downloader(downPath,savePath,musicName,threadCount,this,mHandler);
			downloaders.put(downPath, downloader);//������һ���µ�������,����������뵽������������ȥ
		}
		if (downloader.isdownloading())//�������������ʲôҲ����
           return;
		//LoadInfo��һ��ʵ����,�����װ��һЩ��������Ҫ����Ϣ,ÿ��loadinfo��Ӧ1��������
        LoadInfo loadInfo =downloader.getDownloaderInfors();
		completeSizes.put(downPath, loadInfo.getComplete());
		 // ���÷�����ʼ����
        downloader.download();
	}
	
	/**
	 * �ı��ļ�״̬�ķ���������ļ��������أ��ͻ���ͣ�������ͣ��ʼ����
	 * **/
	public void setState(String musicName,String fileurl)
	{
		//String url=AppConstant.NetworkConstant.downPath+musicName;
		String url=fileurl;
		Downloader downloader=downloaders.get(url);
		if(downloader!=null)
		{
			System.out.println("setState method");
			if(downloader.isdownloading())
			{
				System.out.println("run setState in pause");
				downloader.pause();//������������ͣ
			}
			else
			{
				if(downloader.isPause())
				{
					downloader.reset();//�Ѿ�ֹͣ�Ϳ�ʼ����
					this.reStartDownload(musicName,fileurl);
				}
			}
		}
		else//���downloaders��û��url������,�϶��Ǵ�����ͣ״̬��ֱ�ӿ�ʼ����
		{
			reStartDownload(musicName,fileurl);
		}
	}
	
	private void deleteData(String url)
	{
		if(downloaders.get(url)!=null)
		{
			downloaders.get(url).delete(url);
			downloaders.get(url).reset();
			downloaders.remove(url);
		}
		if(completeSizes.get(url)!=null)
		{
			completeSizes.remove(url);
		}
		if(fileSizes.get(url)!=null)
		{
			fileSizes.remove(url);
		}
	}
	
	
}
