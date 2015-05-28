package com.zwt.myapp.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.DownloadListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zwt.myapp.R;
import com.zwt.myapp.config.Config;

public class OnlineRead extends Activity {
	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onlineread);
		webView = (WebView) findViewById(R.id.webView);
		webView.loadUrl(Config.BOOK_URL);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setDownloadListener(new MyWebViewDownLoadListener());
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});
	}

	private class MyWebViewDownLoadListener implements DownloadListener {
		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}
	}
}
