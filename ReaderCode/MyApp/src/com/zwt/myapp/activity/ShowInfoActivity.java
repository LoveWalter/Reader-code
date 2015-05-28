package com.zwt.myapp.activity;

import java.io.FileInputStream;
import java.io.InputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zwt.myapp.R;
import com.zwt.myapp.config.Config;

public class ShowInfoActivity extends Activity {

	private String username, name, qq, email, touxiang_res;
	private TextView txtUsername, txtName, txtQQ, txtEmail;
	private Button mLogout;
	private Button btn_Download;
	private ImageView iv_touxiang;
	Bitmap bm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showinfo);
		txtUsername = (TextView) findViewById(R.id.txt_username);
		txtName = (TextView) findViewById(R.id.txt_nicheng);
		txtQQ = (TextView) findViewById(R.id.txt_qq);
		txtEmail = (TextView) findViewById(R.id.txt_email);
		iv_touxiang=(ImageView)findViewById(R.id.iv_touxiang);
		SharedPreferences share = getSharedPreferences(Config.USER_XML_PATH,
				Activity.MODE_PRIVATE);
		username = share.getString("username", "");
		name = share.getString("name", "");
		qq = share.getString("qq", "");
		email = share.getString("email", "");
		touxiang_res = share.getString("local", "");// TODO 得到默认头像地址
		System.out.println(username + name + qq + email + touxiang_res);
		txtUsername.setText(username);
		txtName.setText(name);
		txtQQ.setText(qq);
		txtEmail.setText(email);
		bm = getBitmapByFilePath(touxiang_res);
		System.out.println("bm:"+bm);
		iv_touxiang.setImageBitmap(bm);
		mLogout = (Button) findViewById(R.id.btn_logout);
		mLogout.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SharedPreferences share = getSharedPreferences(
						Config.USER_XML_PATH, Activity.MODE_PRIVATE);
				SharedPreferences.Editor edit = share.edit();
				edit.putString("APP_ID", "");
				edit.putString("username", "");
				edit.putString("name", "");
				edit.putString("qq", "");
				edit.putString("email", "");
				edit.commit();
				Intent i = new Intent();
				i.setClass(ShowInfoActivity.this, MainActivity.class);
				startActivity(i);
				getParent().finish();
				finish();
			}
		});
		btn_Download=(Button) findViewById(R.id.btn_Download);
		btn_Download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(ShowInfoActivity.this, PersonDownloadActivity.class);
				i.putExtra("username", username);
				startActivity(i);
			}
		});
		
		
	}

	// 根据图片路径 返回Bitmap对象
	public static Bitmap getBitmapByFilePath(String filePath) {
		try {
			InputStream in = new FileInputStream(filePath);

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
			opts.inPurgeable = true;// 允许可清除
			opts.inInputShareable = true;// 以上options的两个属性必须联合使用才会有效果
			Bitmap bitmap = null;
			try {
				// 实例化Bitmap
				bitmap = BitmapFactory.decodeStream(in, null, opts);
				return bitmap;
			} catch (OutOfMemoryError e) {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
