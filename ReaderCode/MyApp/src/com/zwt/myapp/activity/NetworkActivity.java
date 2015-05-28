package com.zwt.myapp.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zwt.myapp.R;
import com.zwt.myapp.adapter.FileAdapter;
import com.zwt.myapp.bean.FileBean;
import com.zwt.myapp.config.Config;
import com.zwt.myapp.service.DownloadService;
import com.zwt.myapp.util.HttpUtils;

public class NetworkActivity extends Activity {

	private ListView lvFiles;
	private FileAdapter adapter;
	private List<FileBean> fileList;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network);
		showList();
	}

	/**
	 * 将列表显示出来,为了方便,这里手动添加列表
	 * **/
	private void showList() {
		// 从服务器得到数据 填充到List中
		lvFiles = (ListView) findViewById(R.id.lvFiles);
		fileList = new ArrayList<FileBean>();
		adapter = new FileAdapter(this,fileList);
		HttpUtils.getFileListJSON(Config.GET_FILE_URL, getFileHandler);
		lvFiles.setAdapter(adapter);
	}

	/**
	 * 点击下载图片会执行这个方法
	 * 
	 * @param v
	 *            所点击的图片
	 * 
	 * **/
	public void startDownload(View v) {
		// 得到下载图片的父控件,也就是item的RelativeLayout布局
		RelativeLayout layout = (RelativeLayout) v.getParent();
		// 得到所点击Item上的TextView
		TextView musicName = (TextView) layout
				.findViewById(R.id.network_filename);
		TextView fileurl=(TextView) layout.findViewById(R.id.network_fileurl);
		Intent intent = new Intent();
		intent.setClass(NetworkActivity.this, DownloadService.class);
		intent.putExtra("musicName", musicName.getText().toString());// 把所点击的Item上的音乐名称传过去
		intent.putExtra("fileurl", fileurl.getText().toString());
		System.out.println("startDownload:"+fileurl.getText().toString());
		intent.putExtra("flag", "startDownload");// 标签,标志着数据是从netWorkActivity中传送的
		this.startService(intent);// 点击下载按钮，启动service进行下载
	}

	private Handler getFileHandler = new Handler() {
		public void handleMessage(Message msg) {
			String jsonData = (String) msg.obj;
			try {
				JSONArray jsonArray = new JSONArray(jsonData);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String fileName = object.getString("filename");
					String filepicurl = object.getString("filepicurl");
					String time = object.getString("time");
					String fileurl = object.getString("fileurl");
					fileList.add(new FileBean(time, fileName, fileurl,
							filepicurl));
				}
				adapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
	};
}