package com.zwt.myapp.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zwt.myapp.R;
import com.zwt.myapp.bean.FileState;
import com.zwt.myapp.database.DownloadDao;
import com.zwt.myapp.service.DownloadService;
import com.zwt.myapp.util.AppConstant;


public class LocalDownAdapter extends BaseAdapter 
{
	private List<FileState> list=null;
	private LayoutInflater inflater=null;
	private DownloadDao dao=null;
	private Context context;
	
	public LocalDownAdapter(Context context,List<FileState> list,DownloadDao dao)
	{
		inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.list=list;
		this.dao=dao;
		this.context=context;
	}
	
	
	
    class ViewHolder
	{
		public TextView musicname;//音乐名称
		public TextView fileurl;//音乐名称
		public ProgressBar progressBar;//进度条
		public TextView percent;//百分比
		public ImageView delete;//删除
	}
	
	
	/**
	 * 通过getCount()方法知道有多少个Item需要展示
	 * **/
	public int getCount()
	{
		return list.size();
	}

	public Object getItem(int position)
	{
		// TODO Auto-generated method stub
		return list.get(position);
	}

	public long getItemId(int position)
	{
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		final int file_position=position;
		if(convertView==null)
		{
			convertView = inflater.inflate(R.layout.local_item,null);
			holder=new ViewHolder();
			holder.musicname = (TextView) convertView.findViewById(R.id.local_musicname);
			holder.fileurl = (TextView) convertView.findViewById(R.id.local_fileurl);
			holder.progressBar=(ProgressBar)convertView.findViewById(R.id.down_progressBar);
			holder.percent = (TextView) convertView.findViewById(R.id.percent_text);
			holder.delete = (ImageView) convertView.findViewById(R.id.local_delete);
			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}
		//从列表中得到要显示的数据
		FileState fileState= list.get(position);
		final String name= fileState.getMusicName();
		final String fileurl=fileState.getUrl();
		//如果状态是0，则是已经下载完成的
		if(fileState.getState()==0)
		{
			String subfilename=fileState.getMusicName();
			if (subfilename.length()>8) {
				subfilename=subfilename.substring(0,8);
			}
			holder.musicname.setText(subfilename);
			holder.fileurl.setText(fileurl);
			//下载完成的文件，进度条被隐藏
			holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
			//设置为已下载
			holder.percent.setText(AppConstant.AdapterConstant.down_over);
			//实现删除按钮
			holder.delete.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View V)
				{
					//从数据库中删除
					dao.deleteFileState(name);
					//从列表中删除
					list.remove(file_position);
					Intent intent = new Intent();
					intent.putExtra("musicName", name);
					intent.putExtra("fileurl", fileurl);
					intent.putExtra("flag", "delete");
					intent.setClass(context,DownloadService.class);
					context.startService(intent);
					//在存储器中删除
					File file=new File(fileurl);
					if(file.delete())
					{
						System.out.println(AppConstant.AdapterConstant.delete);
					}
					notifyDataSetChanged();
				}
					
			});
		}
		//如果状态是1,则是正在下载
		if(fileState.getState()==1)
		{
				int completeSize=fileState.getCompleteSize();
				int fileSize=fileState.getFileSize();
				float num=(float)completeSize/(float)fileSize;
				int result=(int)(num*100);
				System.out.println(fileState.getMusicName());
				String subfilename=fileState.getMusicName();
				holder.musicname.setText(subfilename);
				holder.fileurl.setText(fileState.getUrl());
				holder.progressBar.setVisibility(ProgressBar.VISIBLE);
				holder.progressBar.setProgress(result);
				holder.percent.setText(result+"%");
				holder.delete.setOnClickListener(new View.OnClickListener()
				{
					public void onClick(View V)
					{
						dao.deleteFileState(name);
						Intent intent = new Intent();
						intent.putExtra("musicName", name);
						intent.putExtra("fileurl", fileurl);
						intent.putExtra("flag", "delete");
						intent.setClass(context,DownloadService.class);
						context.startService(intent);
						list.remove(file_position);
						File file=new File(fileurl);
						if(file.delete())
						{
							System.out.println(AppConstant.AdapterConstant.delete);
						}
						notifyDataSetChanged();
					}
						
				});
				//当文件下载完成
				if(fileState.getCompleteSize()==fileState.getFileSize())
				{
					fileState.setState(0);
					list.set(position, fileState);
					holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
					holder.percent.setText(AppConstant.AdapterConstant.down_over);
				}
		}
		return convertView;
	}

	public List<FileState> getList() {
		return list;
	}

	public void setList(List<FileState> list) {
		this.list = list;
	}
}
