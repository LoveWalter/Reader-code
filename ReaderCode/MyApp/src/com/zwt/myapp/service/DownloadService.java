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
	//存放每个下载文件完成的长度
	private Map<String,Integer> completeSizes=new HashMap<String, Integer>();
	//存放每个下载文件的总长度
	private Map<String,Integer> fileSizes=new HashMap<String, Integer>();
	private Downloader downloader;
	public DownloadDao dao=null;
	private Handler mHandler=new Handler()
	{

		/**
		 * 接收Download中每个线程传输过来的数据
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
					//如果文件下载完成,更新localdown_info表中文件的状态为0
					//只所以在这里判断而不在activity是为了在activity没有启动的状态时也能更新
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
			//如果数据是从networkActivity中传送过来的，就直接开始下载
			startDownload(musicName,fileurl);
		}
		if(flag.equals("setState"))
		{
			FileState fileState=intent.getParcelableExtra("fileState");
			//防止用户点击已经下载完成的文件
			if(fileState.getState()!=0)
			{
				//如果数据是从LocalDownActivity中传送过来的，说明是为了要改变状态，例如暂停
				setState(musicName,fileurl);
			}
		}
		if(flag.equals("delete"))
		{
			//如果数据是从LocalDownAdapter传送过来的，说明是为了要删除歌曲
			//String url=AppConstant.NetworkConstant.downPath+musicName;
			String url=fileurl;
			deleteData(url);
		}
		super.onStart(intent, startId);
	}

	public void startDownload(String musicName,String fileurl)
	{
		//先从数据库中判断，这个文件是否已经在下载列表了
		if(!dao.isHasFile(musicName))
		{
			Toast.makeText(getApplicationContext(), "文件已经存在！", Toast.LENGTH_SHORT).show();
			return;
		}
		int threadCount=3;//定义线程数
		//String downPath=AppConstant.NetworkConstant.downPath+musicName;//下载地址
		String downPath=fileurl;//下载地址
		String savePath=AppConstant.NetworkConstant.savePath;//保存地址
		downloader = downloaders.get(downPath);
		if(downloader==null)
		{
			downloader = new Downloader(downPath,savePath,musicName,threadCount,this,mHandler);
			downloaders.put(downPath, downloader);//创建完一个新的下载器,必须把它加入到下载器集合里去
		}
		if (downloader.isdownloading())//如果正在下载则什么也不做
           return;
		//LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
        LoadInfo loadInfo =downloader.getDownloaderInfors();
		FileState fileState = new FileState(musicName,downPath,loadInfo.getComplete(),loadInfo.getFileSize(),1);
		dao.insertFileState(fileState,this);//在localdown_info表中插入一条下载数据
		completeSizes.put(downPath, loadInfo.getComplete());
		fileSizes.put(downPath, fileState.getFileSize());
		 // 调用方法开始下载
        downloader.download();
	}
	
	/**
	 * 重新下载方法，如果下载暂停了，调用此方法重新下载
	 * **/
	public void reStartDownload(String musicName,String fileurl)
	{
		int threadCount=3;//定义线程数
		//String downPath=AppConstant.NetworkConstant.downPath+musicName;//下载地址
		String downPath=fileurl;
		String savePath=AppConstant.NetworkConstant.savePath;//保存地址
		downloader = downloaders.get(downPath);
		if(downloader==null)
		{
			downloader = new Downloader(downPath,savePath,musicName,threadCount,this,mHandler);
			downloaders.put(downPath, downloader);//创建完一个新的下载器,必须把它加入到下载器集合里去
		}
		if (downloader.isdownloading())//如果正在下载则什么也不做
           return;
		//LoadInfo是一个实体类,里面封装了一些下载所需要的信息,每个loadinfo对应1个下载器
        LoadInfo loadInfo =downloader.getDownloaderInfors();
		completeSizes.put(downPath, loadInfo.getComplete());
		 // 调用方法开始下载
        downloader.download();
	}
	
	/**
	 * 改变文件状态的方法，如果文件正在下载，就会暂停，如果暂停则开始下载
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
				downloader.pause();//正在下载则暂停
			}
			else
			{
				if(downloader.isPause())
				{
					downloader.reset();//已经停止就开始下载
					this.reStartDownload(musicName,fileurl);
				}
			}
		}
		else//如果downloaders中没有url的数据,肯定是处于暂停状态，直接开始下载
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
