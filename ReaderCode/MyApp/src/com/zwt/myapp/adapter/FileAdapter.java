package com.zwt.myapp.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zwt.myapp.R;
import com.zwt.myapp.bean.FileBean;

public class FileAdapter extends BaseAdapter{

	private Context context;
	private List<FileBean> fileList;
	public FileAdapter(Context context,List<FileBean> fileList) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.fileList=fileList;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileList.size();
	}

	@Override
	public FileBean getItem(int position) {
		// TODO Auto-generated method stub
		return fileList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.network_item, null);
		}
		TextView tvName=(TextView) convertView.findViewById(R.id.network_filename);
		TextView tvTime=(TextView) convertView.findViewById(R.id.network_filetime);
		TextView tvFileurl=(TextView) convertView.findViewById(R.id.network_fileurl);
		
		FileBean file=fileList.get(position);
		tvName.setText(file.getFilename());
		tvTime.setText(file.getTime());
		tvFileurl.setText(file.getFileurl());
		return convertView;
	}

}
