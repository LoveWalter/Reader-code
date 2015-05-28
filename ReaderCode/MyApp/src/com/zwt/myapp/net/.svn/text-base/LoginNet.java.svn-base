package com.zwt.myapp.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zwt.myapp.config.Config;

/**
 * action=login 闂佸憡鐟ラ崐褰掑汲閿燂拷:username 闂佺儵鍋撻崝宥夊春濞戙垹瑙﹂柨鐕傛嫹 password_md5
 * 闁诲酣娼уΛ娑㈡偉閿燂拷 闁哄鏅滈弻銊ッ洪弽顓炵９缁绢參顥撶粣锟�1 闂佺懓鐡ㄩ崝鏇熸叏閿燂拷 0 婵犮垺鍎肩划鍓ф喆閿燂拷
 *
 */
public class LoginNet {
	public LoginNet(String username, String password,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Config.LOGIN_URL, HttpMethod.POST,
				new NetConnection.SuccessCallback() {
					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObj = new JSONObject(result);
							switch (jsonObj.getInt(Config.KEY_STATUS)) {
							case Config.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									successCallback.onSuccess(
											jsonObj.getString("username"),
											jsonObj.getString("password"),
											jsonObj.getString("name"),
											jsonObj.getString("qq"),
											jsonObj.getString("local"),
											jsonObj.getString("email"));

								}
								break;
							default:
								if (failCallback != null) {
									failCallback.onFail();
								}
								break;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							if (failCallback != null) {
								failCallback.onFail();
							}
						}
					}
				}, new NetConnection.FailCallback() {

					@Override
					public void onFail() {
						if (failCallback != null) {
							failCallback.onFail();
						}
					}
				}, Config.KEY_ACTION, Config.ACTION_LOGIN,
				Config.KEY_LOGIN_USERNAME, username, Config.KEY_LOGIN_PASSWORD,
				password);

	}

	public static interface SuccessCallback {
		void onSuccess(String username, String passwrod, String name,
				String qq, String local,String email);
	}

	public static interface FailCallback {
		void onFail();
	}

}
