package com.zwt.myapp.config;

import android.content.Context;
import android.content.ServiceConnection;

public class Config {
	public static final String IMAGEUPLOAD_URL = "http://10.0.54.128:8080/Android/FileImageUploadServlet";
	public static final String REGISTER_URL = "http://10.0.54.128:8080/Android/Register.jsp";
	public static final String SERVER_URL = "http://10.0.54.128:8080/Android/api.jsp";
	public static final String LOGIN_URL = "http://10.0.54.128:8080/Android/LoginServlet";
	public static final String UPLOAD_FILE = "http://10.0.54.128:8080/Android/ConventPdfToText";
	public static final String GET_FILE_URL = "http://10.0.54.128:8080/Android/GetTxtFile?username=zwt";
	public static final String BOOK_URL = "http://www.qb5.com";
	public static final String APP_ID = "com.walter.app";
	public static final String CHARSET = "utf-8";
	public static final String KEY_ACTION = "action";
	public static final String KEY_LOGIN_USERNAME = "username";
	public static final String KEY_LOGIN_PASSWORD = "password";
	public static final String KEY_STATUS = "status";

	public static final String KEY_NAME = "name";
	public static final String KEY_BIR_DATE = "birDate";
	public static final String KEY_QQ = "qq";
	public static final String KEY_REGISTER_DATE = "registerDate";
	public static final String KEY_REGISTER_USERNAME = "username";
	public static final String KEY_REGISTER_PASSWORD = "password";

	public static final int RESULT_STATUS_SUCCESS = 1;
	public static final int RESULT_STATUS_FAIL = 0;
	public static final int RESULT_STATUS_INVALID_TOKEN = 0;

	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_REGISTER = "register";
	public static final String USER_XML_PATH = "user";
	public static final String BOOK_XML_PATH = "book";
	
	
	
	public static final String DATABASE_TABKE = "mybook";
	public static final String APP_KEY = "com.zwt.myapp";
	public static final String FLAG = "zwt";
	public static final String KEY_RES = "res";
	public static final String KEY_EMAIL = "email";
	public static final String KEY_LOCAL = "local";
	public static long tme;
	public static ServiceConnection mConnection;
	public static boolean isTrue;
	public static Context context;
	public static boolean isFirst;

}
