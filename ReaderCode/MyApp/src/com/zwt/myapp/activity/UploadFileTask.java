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
	 * �ɱ䳤�������������AsyncTask.exucute()��Ӧ
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
		pdialog = ProgressDialog.show(context, "���ڼ���...", "ϵͳ���ڴ�����������");
	}
	public UploadFileTask(Activity ctx, String requestURL) {
		this.context = ctx;
		this.url = requestURL;
		pdialog = ProgressDialog.show(context, "���ڼ���...", "ϵͳ���ڴ�����������");
	}
	@Override
	protected void onPostExecute(String result) {
		// ����HTMLҳ�������
		pdialog.dismiss();
		// if (UploadUtils.SUCCESS.equalsIgnoreCase(result)) {
		if(result.equals(UploadPDFUtils.SUCCESS)){
			//��ʾPDF�ϴ���ת��ɹ�
			Toast.makeText(context, "PDF�ϴ���ת��ɹ�", Toast.LENGTH_LONG).show();
		}else if(result.equals(UploadUtils.FAILURE)){
			Toast.makeText(context, "�ϴ�ʧ��", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(context, "�ϴ��ɹ�", Toast.LENGTH_LONG).show();
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
			System.out.println("ִ��--ͼƬ--");
			return UploadUtils.uploadFile(file, this.url);
		}else{
			System.out.println("ִ��--pdf--");
			return UploadPDFUtils.uploadFile(file, this.url);
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
	}
}