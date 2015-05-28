package com.zwt.myapp.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.zwt.myapp.config.Config;


public class RegisterNet {
	public RegisterNet(final String username, final String password,
			final String name, final String email, final String qq,final String res,final String local,
			final SuccessCallback successCallback,
			final FailCallback failCallback) {
		new NetConnection(Config.REGISTER_URL, HttpMethod.POST,
				new NetConnection.SuccessCallback() {

					@Override
					public void onSuccess(String result) {
						try {
							JSONObject jsonObj = new JSONObject(result);
							switch (jsonObj.getInt(Config.KEY_STATUS)) {
							case Config.RESULT_STATUS_SUCCESS:
								if (successCallback != null) {
									successCallback.onSuccess();
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
				}, Config.KEY_ACTION, Config.ACTION_REGISTER,
				Config.KEY_REGISTER_USERNAME, username,
				Config.KEY_REGISTER_PASSWORD, password, Config.KEY_NAME, name,Config.KEY_EMAIL,email,
				 Config.KEY_QQ, qq,Config.KEY_RES,res,Config.KEY_LOCAL,local);
	}

	public static interface SuccessCallback {
		void onSuccess();
	}

	public static interface FailCallback {
		void onFail();
	}
}
