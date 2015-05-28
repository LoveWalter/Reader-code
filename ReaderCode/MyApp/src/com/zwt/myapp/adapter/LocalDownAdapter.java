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
		public TextView musicname;//��������
		public TextView fileurl;//��������
		public ProgressBar progressBar;//������
		public TextView percent;//�ٷֱ�
		public ImageView delete;//ɾ��
	}
	
	
	/**
	 * ͨ��getCount()����֪���ж��ٸ�Item��Ҫչʾ
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
		//���б��еõ�Ҫ��ʾ������
		FileState fileState= list.get(position);
		final String name= fileState.getMusicName();
		final String fileurl=fileState.getUrl();
		//���״̬��0�������Ѿ�������ɵ�
		if(fileState.getState()==0)
		{
			String subfilename=fileState.getMusicName();
			if (subfilename.length()>8) {
				subfilename=subfilename.substring(0,8);
			}
			holder.musicname.setText(subfilename);
			holder.fileurl.setText(fileurl);
			//������ɵ��ļ���������������
			holder.progressBar.setVisibility(ProgressBar.INVISIBLE);
			//����Ϊ������
			holder.percent.setText(AppConstant.AdapterConstant.down_over);
			//ʵ��ɾ����ť
			holder.delete.setOnClickListener(new View.OnClickListener()
			{
				public void onClick(View V)
				{
					//�����ݿ���ɾ��
					dao.deleteFileState(name);
					//���б���ɾ��
					list.remove(file_position);
					Intent intent = new Intent();
					intent.putExtra("musicName", name);
					intent.putExtra("fileurl", fileurl);
					intent.putExtra("flag", "delete");
					intent.setClass(context,DownloadService.class);
					context.startService(intent);
					//�ڴ洢����ɾ��
					File file=new File(fileurl);
					if(file.delete())
					{
						System.out.println(AppConstant.AdapterConstant.delete);
					}
					notifyDataSetChanged();
				}
					
			});
		}
		//���״̬��1,������������
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
				//���ļ��������
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