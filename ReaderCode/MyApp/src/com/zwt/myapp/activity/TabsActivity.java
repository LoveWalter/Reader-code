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
	 * ˼·����ʼ������Ķ� GridView �������鼮��intent ��ʾ��������ܣ��� ��Button���ü���ʱ��
	 */
	private GridView gv_RecentlyReadBook;
	private ImageView imageItem;// GridView�е�ͼ���ļ�
	private TextView textItem;// GridView�е�Text
	private EditText username;// �� �е� username
	private EditText password;// ���е� password
	private Button okButton;// ���е�ȷ����ť
	private Button cancelButton;// ���е�ȡ����ť
	private ImageButton imgWeiboLogin;// ΢����¼��ť
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		TabHost th = getTabHost();
		th.addTab(th
				.newTabSpec("tab1")
				.setIndicator("����Ķ�", getResources().getDrawable(R.drawable.a1))
				.setContent(new Intent(this, BookViewActivity.class)));
		th.addTab(th
				.newTabSpec("tab2")
				.setIndicator("�������", getResources().getDrawable(R.drawable.a2))
				.setContent(new Intent(this, ImportActivity.class)));
		th.addTab(th
				.newTabSpec("tab3")
				.setIndicator("�������", getResources().getDrawable(R.drawable.a3))
				.setContent(new Intent(this, OnlineRead.class)));
		th.addTab(th.newTabSpec("tab4")
				.setIndicator("��", getResources().getDrawable(R.drawable.a1))
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
	 * ����Tab��ǩ����ɫ�����������ɫ
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
			tv.setTypeface(Typeface.SERIF, 2); // ��������ͷ��
			if (tabHost.getCurrentTab() == i) {// ѡ��
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.black));
			} else {// ��ѡ��
				tv.setTextColor(this.getResources().getColorStateList(
						android.R.color.black));
			}
		}
	}
}
