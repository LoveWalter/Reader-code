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
	     * 查看数据库中是否有数据
	     * @param urlstr 下载地址
	     * @return count==0 如果数据库里没有数据,返回true,如果数据库里有数据返回false
	     */
	    public boolean isHasInfors(String urlstr) 
	    {
	        SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select count(*)  from download_info where url=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { urlstr });
	        cursor.moveToFirst();
	        //getInt方法返回第0列的值
	        int count = cursor.getInt(0);
	        cursor.close();
	        database.close();
	        return count == 0;
	    }
	 
	    /**
	     * 保存 下载的具体信息
	     * 保存和更新方法最好设置为同步
	     */
	    public  void saveInfos(List<DownloadInfo> infos,Context context)
	    {
	    	//这里也要采用事务的方法提高效率
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
	     * 插入一条文件的状态记录
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
	     * 得到下载具体信息
	     * @return List<DownloadInfo> 一个下载器信息集合器,里面存放了每条线程的下载信息
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
	     * 得到文件的下载状态
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
	     * 如果不存在则返回true
	     * **/
	    public boolean isHasFile(String musicName)
	    {
	    	SQLiteDatabase database = dbHelper.getReadableDatabase();
	        String sql = "select count(*)  from localdown_info where music_name=?";
	        Cursor cursor = database.rawQuery(sql, new String[] { musicName });
	        cursor.moveToFirst();
	        //getInt方法返回第0列的值
	        int count = cursor.getInt(0);
	        cursor.close();
	        database.close();
	        return count == 0;
	    }
	    
	    
	    
	    /**
	     * 更新数据库中的下载信息,保存和更新方法最好设置为同步
	     * @param threadId 线程号
	     * @param compeleteSize 已经下载的长度
	     * @param urlstr 下载地址
	     */
	    public void updataInfos(int threadId, int compeleteSize, String urlstr,Context context) 
	    {
	    	synchronized (Lock) 
	    	{
				//这里因为是要更新数据,所以要采用写操作,和事务的方法来提高效率
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
	     * 更新文件的状态，1为正在下载，0为已经下载完成
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
	     * 更新文件的下载状态
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
	     * 关闭数据库
	     */
	    public void closeDb() {
	        dbHelper.close();
	    }

	    /**
	     * 下载完成后删除数据库中的数据
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
