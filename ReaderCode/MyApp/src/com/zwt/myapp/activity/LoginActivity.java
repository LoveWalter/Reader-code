package com.zwt.myapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zwt.myapp.R;
import com.zwt.myapp.config.Config;
import com.zwt.myapp.net.LoginNet;
import com.zwt.myapp.util.CipherUtil;

public class LoginActivity extends Activity {

	private EditText etLUsername, etLPassword;
	private String username, password, password_md5;
	CipherUtil cu = new CipherUtil();
	private Button btnLLogin, btnLCancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		etLUsername = (EditText) findViewById(R.id.etLusername);
		etLPassword = (EditText) findViewById(R.id.etLpassword);
		btnLLogin = (Button) findViewById(R.id.btnLok);
		btnLCancle = (Button) findViewById(R.id.btnLcancle);

		btnLLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = etLUsername.getText().toString().trim();
				password = etLPassword.getText().toString().trim();
				if (TextUtils.isEmpty(username)) {
					Toast.makeText(LoginActivity.this, "Username is Empty",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(LoginActivity.this, "Password is Empty",
							Toast.LENGTH_LONG).show();
					return;
				}
				if (!(TextUtils.isEmpty(username))
						&& !(TextUtils.isEmpty(password))) {
					password_md5 = cu.generatePassword(password);
					final ProgressDialog pd = ProgressDialog.show(
							LoginActivity.this, "Connection",
							"Connecting the server,Please wait");
					new LoginNet(username, password_md5,
							new LoginNet.SuccessCallback() {
								@Override
								public void onSuccess(String username,
										String password, String name, String qq,String local,String email) {
									pd.dismiss();
									Toast.makeText(LoginActivity.this,
											"Login Success", Toast.LENGTH_LONG)
											.show();
									SharedPreferences share = getSharedPreferences(
											Config.USER_XML_PATH,
											Activity.MODE_PRIVATE);
									SharedPreferences.Editor edit = share
											.edit();
									edit.putString("APP_ID", Config.APP_ID);
									edit.putString("username", username);
									edit.putString("name", name);
									edit.putString("qq", qq);
									edit.putString("email", email);
									edit.putString("local", local);
									edit.commit();
									Intent i = new Intent();
									i.setClass(LoginActivity.this,
											TabsActivity.class);
									i.putExtra("username", username);
									// i.putExtra("password", password);
									i.putExtra("name", name);
									i.putExtra("qq", qq);
									System.out.println("The Value from Server:"
											+ username + "/" + password + "/"
											+ qq);
									System.out.println("LoginActivity I:" + i);
									startActivity(i);
									finish();

								}
							}, new LoginNet.FailCallback() {
								@Override
								public void onFail() {
									pd.dismiss();
									Toast.makeText(LoginActivity.this,
											"Login Failed", Toast.LENGTH_LONG)
											.show();
								}
							});
				}

			}
		});
		btnLCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginActivity.this.finish();
			}
		});
	}

}
