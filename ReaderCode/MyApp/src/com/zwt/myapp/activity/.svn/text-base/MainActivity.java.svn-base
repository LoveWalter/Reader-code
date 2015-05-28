package com.zwt.myapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.zwt.myapp.R;
import com.zwt.myapp.config.Config;

public class MainActivity extends Activity {

	private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (checkTheUser()) {
			Toast.makeText(MainActivity.this, "Skip Login...",
					Toast.LENGTH_LONG);
			Intent i = new Intent(MainActivity.this, TabsActivity.class);
			startActivity(i);
			finish();
		} else {
			setContentView(R.layout.activity_main);
			btnLogin = (Button) findViewById(R.id.btnStartLoginActivity);
			btnRegister = (Button) findViewById(R.id.btnStartRegisterActivity);
			btnLogin.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent i = new Intent(MainActivity.this,
							LoginActivity.class);
					startActivity(i);
					finish();
				}
			});

			btnRegister.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(MainActivity.this,
							RegisterActivity.class);
					startActivity(i);
					finish();
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public boolean checkTheUser() {
		String username, name, qq;
		SharedPreferences share = getSharedPreferences(Config.USER_XML_PATH,
				Activity.MODE_PRIVATE);
		username = share.getString("username", "");
		name = share.getString("name", "");
		qq = share.getString("qq", "");
		System.out.println("checkTheUser:" + username + "/" + name + "/" + qq);
		if ((!username.equals("")
				&& !name.equals("") && !qq.equals(""))
				&& (username != null && name != null && qq != null)) {
			return true;
		} else
			return false;
	}
}
