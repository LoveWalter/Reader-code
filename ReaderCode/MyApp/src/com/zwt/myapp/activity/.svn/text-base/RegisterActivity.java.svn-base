package com.zwt.myapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zwt.myapp.R;
import com.zwt.myapp.config.Config;
import com.zwt.myapp.net.RegisterNet;
import com.zwt.myapp.util.CipherUtil;

public class RegisterActivity extends Activity implements OnClickListener {
	private static final String TAG = "MainActivity";
	private ImageView iv_touxiang;
	private Button btn_selectTouxiang;
	private Button btn_register_ok, btn_register_cancle;
	private EditText et_Username, et_Password, et_RPassword, et_Email, et_qq,
			et_nickname;
	private String picPath = null;
	// 得到的信息
	private String username, password, rpassword, email, qq, nickname, res,
			localRes;
	// MD5加密
	private String pwd_md5;
	private TextView tv_result, tv_local_result;
	UploadFileTask uploadFileTask;
	CipherUtil cu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		initDate();
	}

	private void initDate() {
		iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
		btn_selectTouxiang = (Button) findViewById(R.id.btn_selectTouxiang);
		btn_register_cancle = (Button) findViewById(R.id.btn_register_cancle);
		btn_register_ok = (Button) findViewById(R.id.btn_register_ok);
		et_Username = (EditText) findViewById(R.id.et_register_username);
		et_Password = (EditText) findViewById(R.id.et_register_password);
		et_RPassword = (EditText) findViewById(R.id.et_register_rpassword);
		et_Email = (EditText) findViewById(R.id.et_register_email);
		et_qq = (EditText) findViewById(R.id.et_register_qq);
		et_nickname = (EditText) findViewById(R.id.et_register_nickname);
		tv_result = (TextView) findViewById(R.id.tv_result);
		tv_local_result = (TextView) findViewById(R.id.tv_local_result);
		// 按钮监听事件
		btn_selectTouxiang.setOnClickListener(this);
		btn_register_cancle.setOnClickListener(this);
		btn_register_ok.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_selectTouxiang:
			/***
			 * 这个是调用android内置的intent，来过滤图片文件 ，同时也可以过滤其他的
			 */
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			// 回调图片类使用的
			startActivityForResult(intent, RESULT_CANCELED);
			break;
		case R.id.btn_register_cancle:
			uploadFileTask.onCancelled();
			RegisterActivity.this.finish();
		case R.id.btn_register_ok:
			// 先获取控件信息
			this.username = et_Username.getText().toString().trim();
			this.password = et_Password.getText().toString().trim();
			this.email = et_Email.getText().toString().trim();
			this.qq = et_qq.getText().toString().trim();
			this.nickname = et_nickname.getText().toString().trim();
			this.res = (String) tv_result.getText();
			this.localRes = picPath;
			pwd_md5 = cu.generatePassword(password);
			System.out.println("从控件中获得的路径:" + res);
			// 控件信息
			final ProgressDialog pd = ProgressDialog.show(
					RegisterActivity.this, "Register",
					"Registering!Please Wait...");
			// 执行上传
			new RegisterNet(username, pwd_md5, nickname, email, qq, res,
					localRes, new RegisterNet.SuccessCallback() {
						@Override
						public void onSuccess() {
							pd.dismiss();
							Toast.makeText(RegisterActivity.this, "Success...",
									Toast.LENGTH_LONG).show();
							SharedPreferences share = getSharedPreferences(
									Config.USER_XML_PATH, Activity.MODE_PRIVATE);
							SharedPreferences.Editor edit = share.edit();
							edit.putString("APP_ID", Config.APP_ID);
							edit.putString("username", username);
							edit.putString("name", nickname);
							edit.putString("qq", qq);
							edit.putString("email", email);
							edit.putString("res", res);
							edit.putString("local", localRes);
							edit.commit();
							Intent i = new Intent();
							i.setClass(RegisterActivity.this,
									TabsActivity.class);
							i.putExtra("username", username);
							i.putExtra("name", nickname);
							i.putExtra("qq", qq);
							i.putExtra("email", email);
							System.out.println("The Value from Server:"
									+ username + "/" + password + "/" + qq);
							System.out.println("LoginActivity I:" + i);
							startActivity(i);
							finish();
						}
					}, new RegisterNet.FailCallback() {

						@Override
						public void onFail() {
							pd.dismiss();
							Toast.makeText(RegisterActivity.this,
									"Register Fail.Please Try again.",
									Toast.LENGTH_LONG).show();
							return;
						}
					});
		default:
			break;
		}
	}

	/**
	 * 获取控件信息
	 */
	public void getWidgetData() {
		username = et_Username.getText().toString().trim();
		password = et_Password.getText().toString().trim();
		email = et_Email.getText().toString().trim();
		qq = et_qq.getText().toString().trim();
		nickname = et_nickname.getText().toString().trim();
		res = (String) tv_result.getText();
		System.out.println("\n从控件中获得的东西:" + username + "***" + password + "***"
				+ nickname + "***" + qq + "***" + email + "***" + res);
	}

	/**
	 * 回调执行的方法
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			/**
			 * 当选择的图片不为空的话，在获取到图片的途径
			 */
			Uri uri = data.getData();
			Log.e(TAG, "uri = " + uri);
			try {
				String[] pojo = { MediaStore.Images.Media.DATA };
				Cursor cursor = managedQuery(uri, pojo, null, null, null);
				if (cursor != null) {
					ContentResolver cr = this.getContentResolver();
					int colunm_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					cursor.moveToFirst();
					String path = cursor.getString(colunm_index);
					/***
					 * 这里加这样一个判断主要是为了第三方的软件选择，比如：使用第三方的文件管理器的话，你选择的文件就不一定是图片了，
					 * 这样的话，我们判断文件的后缀名 如果是图片格式的话，那么才可以
					 */
					if (path.endsWith("jpg") || path.endsWith("png")) {
						picPath = path;
						Bitmap bitmap = BitmapFactory.decodeStream(cr
								.openInputStream(uri));
						tv_local_result.setText(picPath);
						iv_touxiang.setImageBitmap(bitmap);
						// 开始上传图片
						System.out.println("点击之后得到的path：" + picPath);
						uploadFileTask = new UploadFileTask(this,
								Config.IMAGEUPLOAD_URL, tv_result);
						uploadFileTask.execute(picPath);
					} else {
						alert();
					}
				} else {
					alert();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
			/**
			 * 回调使用
			 */
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

	/**
	 * 检测输入内容是否正确
	 */
	@SuppressLint("NewApi")
	private boolean isCorrect() {
		if (username.isEmpty()) {
			Toast.makeText(this, "Please Input Your Username",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (password.isEmpty()) {
			Toast.makeText(this, "Please Input Your Password",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (rpassword.isEmpty()) {
			Toast.makeText(this, "Please Input Your Check Password",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (email.isEmpty()) {
			Toast.makeText(this, "Please Input Your E-Mail", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if (!email.matches("\\w+@(\\w+.)+[a-z]{2,3}")) {
			Toast.makeText(this, "Please Insure Your E-Mail", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if (qq.isEmpty()) {
			Toast.makeText(this, "Please Input Your QQ", Toast.LENGTH_LONG)
					.show();
			return false;
		}
		if (nickname.isEmpty()) {
			Toast.makeText(this, "Please Input Your NickName",
					Toast.LENGTH_LONG).show();
			return false;
		}
		if (!password.equals(rpassword)) {
			Toast.makeText(this, "Please Check Your P&R is correct",
					Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

	private void alert() {
		Dialog dialog = new AlertDialog.Builder(this).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						picPath = null;
					}
				}).create();
		dialog.show();
	}

}
