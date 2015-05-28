package com.zwt.myapp.activity;

import java.io.File;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

import com.zwt.myapp.config.Config;

public class UploadFileTask extends AsyncTask<String, Void, String> {
	/**
	 * 可变长的输入参数，与AsyncTask.exucute()对应
	 */
	private TextView tv_result;
	private ProgressDialog pdialog;
	private Activity context = null;
	private String result = null;
	private String url;

	public UploadFileTask(Activity ctx, String requestURL, TextView tv) {
		this.context = ctx;
		this.tv_result = tv;
		this.url = requestURL;
		pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
	}
	public UploadFileTask(Activity ctx, String requestURL) {
		this.context = ctx;
		this.url = requestURL;
		pdialog = ProgressDialog.show(context, "正在加载...", "系统正在处理您的请求");
	}
	@Override
	protected void onPostExecute(String result) {
		// 返回HTML页面的内容
		pdialog.dismiss();
		// if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
		if(result.equals(UploadPDFUtils.SUCCESS)){
			//表示PDF上传并转码成功
			Toast.makeText(context, "PDF上传并转码成功", Toast.LENGTH_LONG).show();
		}else if(result.equals(UploadUtils.FAILURE)){
			Toast.makeText(context, "上传失败", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context, "上传成功", Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected String doInBackground(String... params) {
		File file = new File(params[0]);
		if (file.getName().contains(".png") || file.getName().contains("jpg")) {
			System.out.println("执行--图片--");
			return UploadUtils.uploadFile(file, this.url);
		}else{
			System.out.println("执行--pdf--");
			return UploadPDFUtils.uploadFile(file, this.url);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}
}