package com.zwt.myapp.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import android.os.AsyncTask;

import com.zwt.myapp.config.Config;

/**
 * 锟斤拷锟斤拷通讯锟斤拷锟洁，锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟斤拷锟酵ㄑ讹拷锟�
 *
 */
public class NetConnection {
	public NetConnection(final String url, final HttpMethod method,
			final SuccessCallback successCallback,final FailCallback failCallback,
			final String... kvs) {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {

				StringBuffer paramsStr = new StringBuffer();
				for (int i = 0; i < kvs.length; i += 2) {
					paramsStr.append(kvs[i]).append("=").append(kvs[i + 1])
							.append("&");
				}

				try {
					URLConnection uc;
					switch (method) {
					case POST:
						uc = new URL(url).openConnection();
						uc.setDoOutput(true);
						BufferedWriter bw = new BufferedWriter(
								new OutputStreamWriter(uc.getOutputStream(),
										Config.CHARSET));
						bw.write(paramsStr.toString());
						bw.flush();
						break;
					default:
						uc = new URL(url + "?" + paramsStr.toString())
								.openConnection();
						break;
					}
					System.out.println("Request url:"+uc.getURL());
					System.out.println("Request data:"+paramsStr.toString());
					//锟斤拷取锟斤拷锟斤拷
					BufferedReader br=new BufferedReader(new InputStreamReader(uc.getInputStream(),Config.CHARSET));
					String line=null;
					StringBuffer result=new StringBuffer();
					while ((line=br.readLine())!=null) {
						result.append(line);
					}
					System.out.println("Result:"+result);
					return result.toString();
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				return null;
			}
			@Override
			protected void onPostExecute(String result) {
				
				if(result!=null){
					if(successCallback!=null){
						successCallback.onSuccess(result);
					}
				}else {
					if(failCallback!=null){
						failCallback.onFail();
					}
				}
				super.onPostExecute(result);
			}
		}.execute();
	
	}

	public static interface SuccessCallback {
		void onSuccess(String result);
	}

	public static interface FailCallback {
		void onFail();
	}
}
