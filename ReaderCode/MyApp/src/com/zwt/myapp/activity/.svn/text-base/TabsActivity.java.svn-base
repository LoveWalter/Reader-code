package com.zwt.myapp.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.zwt.myapp.R;

public class TabsActivity extends TabActivity {

	/*
	 * 思路：初始化最近阅读 GridView ，本地书籍用intent 显示，网上书架，我 用Button设置监听时间
	 */
	private GridView gv_RecentlyReadBook;
	private ImageView imageItem;// GridView中的图标文件
	private TextView textItem;// GridView中的Text
	private EditText username;// 我 中的 username
	private EditText password;// 我中的 password
	private Button okButton;// 我中的确定按钮
	private Button cancelButton;// 我中的取消按钮
	private ImageButton imgWeiboLogin;// 微博登录按钮
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		TabHost th = getTabHost();
		th.addTab(th
				.newTabSpec("tab1")
				.setIndicator("最近阅读", getResources().getDrawable(R.drawable.a1))
				.setContent(new Intent(this, BookViewActivity.class)));
		th.addTab(th
				.newTabSpec("tab2")
				.setIndicator("本地浏览", getResources().getDrawable(R.drawable.a2))
				.setContent(new Intent(this, ImportActivity.class)));
		th.addTab(th
				.newTabSpec("tab3")
				.setIndicator("网上书城", getResources().getDrawable(R.drawable.a3))
				.setContent(new Intent(this, OnlineRead.class)));
		th.addTab(th.newTabSpec("tab4")
				.setIndicator("我", getResources().getDrawable(R.drawable.a1))
				.setContent(new Intent(this, ShowInfoActivity.class)));
		updateTab(th);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 更新Tab标签的颜色，和字体的颜色
	 * 
	 * @param tabHost
	 */
	private void updateTab(final TabHost tabHost) {
		for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
			View view = tabHost.getTabWidget().getChildAt(i);
			TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i)
					.findViewById(android.R.id.title);
			tv.setTextSize(16);
			tv.setGravity(Gravity.CENTER_HORIZONTAL);
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTypeface(Typeface.SERIF, 2); // 设置字体和风格
			if (tabHost.getCurrentTab() == i) {// 选中
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.black));
			} else {// 不选中
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.black));
			}
		}
	}
}
