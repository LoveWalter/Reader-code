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
	 * ���б���ʾ����,Ϊ�˷���,�����ֶ�����б�
	 * **/
	private void showList() {
		// �ӷ������õ����� ��䵽List��
		lvFiles = (ListView) findViewById(R.id.lvFiles);
		fileList = new ArrayList<FileBean>();
		adapter = new FileAdapter(this,fileList);
		HttpUtils.getFileListJSON(Config.GET_FILE_URL, getFileHandler);
		lvFiles.setAdapter(adapter);
	}

	/**
	 * �������ͼƬ��ִ���������
	 * 
	 * @param v
	 *            �������ͼƬ
	 * 
	 * **/
	public void startDownload(View v) {
		// �õ�����ͼƬ�ĸ��ؼ�,Ҳ����item��RelativeLayout����
		RelativeLayout layout = (RelativeLayout) v.getParent();
		// �õ������Item�ϵ�TextView
		TextView musicName = (TextView) layout
				.findViewById(R.id.network_filename);
		TextView fileurl=(TextView) layout.findViewById(R.id.network_fileurl);
		Intent intent = new Intent();
		intent.setClass(NetworkActivity.this, DownloadService.class);
		intent.putExtra("musicName", musicName.getText().toString());// ���������Item�ϵ��������ƴ���ȥ
		intent.putExtra("fileurl", fileurl.getText().toString());
		System.out.println("startDownload:"+fileurl.getText().toString());
		intent.putExtra("flag", "startDownload");// ��ǩ,��־�������Ǵ�netWorkActivity�д��͵�
		this.startService(intent);// ������ذ�ť������service��������
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