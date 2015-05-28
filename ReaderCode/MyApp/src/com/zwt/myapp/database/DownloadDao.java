package com.zwt.myapp.database;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.zwt.myapp.bean.DownloadInfo;
import com.zwt.myapp.bean.FileState;


public class DownloadDao
{
	private DownloadDBHelper dbHelper;
	private static final String DATABASE_NAME = "down.db";
	public static String Lock = "dblock"; 
	public static String file_Lock="fileLock";
	
	 public DownloadDao(Context context) 
	 {
	        dbHelper = new DownloadDBHelper(context);
	 }
	 
	     /**
	     * �鿴���ݿ����Ƿ�������
	     * @param urlstr ���ص�ַ
	     * @return count==0 ������ݿ���û������,����true,������ݿ��������ݷ���false
	     */
	    public boolean isHasInfors(String urlstr) 
	    {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select count(*)  from download_info where url=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	        cursor.moveToFirst();
	        //getInt�������ص�0�е�ֵ
	        int count = cursor.getInt(0);
	        cursor.close();
	        database.close();
	        return count == 0;
	    }
	 
	    /**
	     * ���� ���صľ�����Ϣ
	     * ����͸��·����������Ϊͬ��
	     */
	    public  void saveInfos(List<DownloadInfo> infos,Context context)
	    {
	    	//����ҲҪ��������ķ������Ч��
	        synchronized (Lock) 
	        {
				SQLiteDatabase database = context.openOrCreateDatabase(	DATABASE_NAME, Context.MODE_PRIVATE, null);
				database.beginTransaction();
				try {
					for (DownloadInfo info : infos) {
						String sql = "insert into download_info(thread_id,start_pos, end_pos,compelete_size,url) values (?,?,?,?,?)";
						Object[] bindArgs = { info.getThreadId(),
								info.getStartPos(), info.getEndPos(),
								info.getCompeleteSize(), info.getUrl() };
						database.execSQL(sql, bindArgs);
					}
					database.setTransactionSuccessful();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					database.endTransaction();
				}
				database.close();
			}
	      
	    }
	        
	    /**
	     * ����һ���ļ���״̬��¼
	     * **/
	    public void insertFileState(FileState fileState,Context context)
	    {
	    	synchronized(file_Lock)
	    	{
	    		SQLiteDatabase database = context.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
	    		database.beginTransaction();
	    		try 
	    		{
	    			System.out.println("insert data");
					String sql="insert into localdown_info (music_name,url,completeSize,fileSize,state) values(?,?,?,?,?)";
					Object[] bindArgs={fileState.getMusicName(),
										fileState.getUrl(),
										fileState.getCompleteSize(),
										fileState.getFileSize(),
										fileState.getState()
										};
					database.execSQL(sql, bindArgs);
					database.setTransactionSuccessful();
				}
	    		catch (SQLException e) 
	    		{
					e.printStackTrace();
				}
	    		finally
	    		{
					database.endTransaction();
				}
				database.close();
	    	}
	    }
	    
	    /**
	     * �õ����ؾ�����Ϣ
	     * @return List<DownloadInfo> һ����������Ϣ������,��������ÿ���̵߳�������Ϣ
	     */
	    public List<DownloadInfo> getInfos(String urlstr) 
	    {
	        List<DownloadInfo> list = new ArrayList<DownloadInfo>();
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select thread_id, start_pos, end_pos,compelete_size,url from download_info where url=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	        while (cursor.moveToNext()) {
	            DownloadInfo info = new DownloadInfo(cursor.getInt(0),
	                    cursor.getInt(1), cursor.getInt(2), cursor.getInt(3),
	                    cursor.getString(4));
	            list.add(info);
	        }
	        cursor.close();
	        database.close();
	        return list;
	    }
	    
	    /**
	     * �õ��ļ�������״̬
	     * **/
	    public List<FileState> getFileState()
	    {
	    	List<FileState> list = new ArrayList<FileState>();
	    	SQLiteDatabase database=dbHelper.getReadableDatabase();
	    	String sql="select music_name,url,completeSize,fileSize,state from localdown_info";
	    	Cursor cursor=database.rawQuery(sql, null);
	    	while(cursor.moveToNext())
	    	{
	    		FileState fileState = new FileState(cursor.getString(0),
	    				cursor.getString(1),cursor.getInt(2),cursor.getInt(3),cursor.getInt(4));
	    		list.add(fileState);
	    	}
	    	cursor.close();
	    	database.close();
	    	return list;
	    }
	    
	    
	    /**
	     * ����������򷵻�true
	     * **/
	    public boolean isHasFile(String musicName)
	    {
	    	SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select count(*)  from localdown_info where music_name=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { musicName });
	        cursor.moveToFirst();
	        //getInt�������ص�0�е�ֵ
	        int count = cursor.getInt(0);
	        cursor.close();
	        database.close();
	        return count == 0;
	    }
	    
	    
	    
	    /**
	     * �������ݿ��е�������Ϣ,����͸��·����������Ϊͬ��
	     * @param threadId �̺߳�
	     * @param compeleteSize �Ѿ����صĳ���
	     * @param urlstr ���ص�ַ
	     */
	    public void updataInfos(int threadId, int compeleteSize, String urlstr,Context context) 
	    {
	    	synchronized (Lock) 
	    	{
				//������Ϊ��Ҫ��������,����Ҫ����д����,������ķ��������Ч��
				//SQLiteDatabase database = dbHelper.getReadableDatabase();
				String sql = "update download_info set compelete_size=? where thread_id=? and url=?";
				Object[] bindArgs = { compeleteSize, threadId, urlstr };
				//SQLiteDatabase database = dbHelper.getWritableDatabase();
				SQLiteDatabase database = context.openOrCreateDatabase(
						DATABASE_NAME, Context.MODE_PRIVATE, null);
				database.beginTransaction();
				try {
					database.execSQL(sql, bindArgs);
					database.setTransactionSuccessful();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					database.endTransaction();
				}
				database.close();
			}
	    
	    }
	    
	    /**
	     * �����ļ���״̬��1Ϊ�������أ�0Ϊ�Ѿ��������
	     * **/
	    public void updateFileState(String url)
	    {
	    	synchronized(file_Lock)
	    	{
	    		String sql = "update localdown_info set state=? where url=?";
	    		Object[] bindArgs={0,url};
	    		SQLiteDatabase database = dbHelper.getWritableDatabase();
	    		database.beginTransaction();
	    		try 
	    		{
					database.execSQL(sql, bindArgs);
					database.setTransactionSuccessful();
				} 
	    		catch (SQLException e)
	    		{
					e.printStackTrace();
				} 
	    		finally 
	    		{
					database.endTransaction();
				}
				database.close();
	    	}
	    }
	    	    
	    
	    /**
	     * �����ļ�������״̬
	     * **/
	    public void updateFileDownState(List<FileState> list)
	    {
	    	synchronized(file_Lock)
	    	{
	    		String sql = "update localdown_info set completeSize=?,state=? where url=?";
	    		SQLiteDatabase database = dbHelper.getWritableDatabase();
	    		database.beginTransaction();
	    		try 
	    		{
		    		for (FileState fileState : list) 
		    		{
		    			Object[] bindArgs={fileState.getCompleteSize(),fileState.getState(),fileState.getUrl()};
		    			database.execSQL(sql, bindArgs);
					}
					database.setTransactionSuccessful();
				} 
	    		catch (SQLException e)
	    		{
					e.printStackTrace();
				} 
	    		finally 
	    		{
					database.endTransaction();
				}
				database.close();
	    	}	
	    }
	    
	    /**
	     * �ر����ݿ�
	     */
	    public void closeDb() {
	        dbHelper.close();
	    }

	    /**
	     * ������ɺ�ɾ�����ݿ��е�����
	     */
	    public void delete(String url) 
	    {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        database.delete("download_info", "url=?", new String[] { url });
	        database.close();
	    }
	    
	    public void deleteFileState(String musicName)
	    {
	    	SQLiteDatabase database=dbHelper.getReadableDatabase();
	    	database.delete("localdown_info", "music_name=?",new String[]{musicName});
	    	database.close();
	    }

}
