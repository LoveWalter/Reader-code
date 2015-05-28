package com.zwt.myapp.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpDownloader {
	private URL url = null;

	public String download(String urlStr) {
		StringBuffer stringbuffer = new StringBuffer();
		String line;
		BufferedReader bufferReader = null;
		try {
			// 创建一个URL对象
			url = new URL(urlStr);
			// 得到一个HttpURLConnection对象
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			// 得到IO流，使用IO流读取数据
			bufferReader = new BufferedReader(new InputStreamReader(
					httpUrlConnection.getInputStream()));
			while ((line = bufferReader.readLine()) != null) {
				stringbuffer.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return stringbuffer.toString();

	}

	// 该函数返回整形 -1：代表下载文件出错 ;0：代表下载文件成功; 1：代表文件已经存在

	public int download(String urlStr, String path, String fileName) {
		InputStream inputstream = null;
		System.out.println("download:" + urlStr);
		System.out.println("download:" + path);
		System.out.println("download:" + fileName);
		FileUtils fileUtils = new FileUtils();
		if (fileUtils.isExist(path + fileName))
			return 1;
		else {
			try {
				inputstream = getFromUrl(urlStr);
			} catch (IOException e) {
				e.printStackTrace();
			}
			File file = fileUtils.writeToSDPATHFromInput(path, fileName,
					inputstream);
			if (file != null)
				return 0;
			else
				return -1;
		}
	}

	// 根据url字符串得到输入流
	public InputStream getFromUrl(String urlStr) throws IOException {
		url = new URL(urlStr);
		System.out.println("getFromUrl:" + urlStr);
		HttpURLConnection httpUrlConnection = (HttpURLConnection) url
				.openConnection();
		httpUrlConnection.setRequestMethod("GET");
		httpUrlConnection.setRequestProperty("Charset", "UTF-8");
		httpUrlConnection.connect();
		InputStream input = httpUrlConnection.getInputStream();
		return input;
	}

	// 根据url字符串把读取的结果放入File中
	public boolean saveTxtFromUrl(String urlStr, String path) {
		InputStream input = null;
		FileOutputStream fos = null;
		try {
			url = new URL(urlStr);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url
					.openConnection();
			httpUrlConnection.connect();
			input = httpUrlConnection.getInputStream();
			File file = new File(path);
			fos = new FileOutputStream(file);
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = input.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				input.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}