package com.zwt.myapp.bean;



import android.os.Parcel;
import android.os.Parcelable;

public class FileState implements Parcelable
{
	private String musicName;//歌曲名字
	private String url;//下载地址
	private int state;//歌曲当前状态 1为正在下载 0为已下载
	private int completeSize;//已下载的长度
	private int fileSize;//文件长度
	
	public FileState()
	{
		
	}

	public FileState(String musicName, String url,int completeSize,int fileSize,int state )
	{
		super();
		this.musicName = musicName;
		this.url = url;
		this.state = state;
		this.completeSize = completeSize;
		this.fileSize = fileSize;
	}
	public String getMusicName() 
	{
		return musicName;
	}
	public void setMusicName(String musicName) {
		this.musicName = musicName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getCompleteSize() {
		return completeSize;
	}
	public void setCompleteSize(int completeSize) {
		this.completeSize = completeSize;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	/**
	 * Constructs a <code>String</code> with all attributes
	 * in name = value format.
	 *
	 * @return a <code>String</code> representation 
	 * of this object.
	 */
	public String toString()
	{
	    final String TAB = "    ";
	    
	    String retValue = "";
	    
	    retValue = "FileState ( "
	        + super.toString() + TAB
	        + "musicName = " + this.musicName + TAB
	        + "url = " + this.url + TAB
	        + "state = " + this.state + TAB
	        + "completeSize = " + this.completeSize + TAB
	        + "fileSize = " + this.fileSize + TAB
	        + " )";
	
	    return retValue;
	}
	
	public static final Parcelable.Creator<FileState> CREATOR =
		new Parcelable.Creator<FileState>()
		{

			public FileState createFromParcel(Parcel in)
			{
				FileState fileState = new FileState();
				fileState.musicName=in.readString();
				fileState.url=in.readString();
				fileState.completeSize=in.readInt();
				fileState.fileSize=in.readInt();
				fileState.state=in.readInt();
				return fileState;
			}

			public FileState[] newArray(int size)
			{
				
				return new FileState[size];
			}
		
		};
	
	//这个方法不用管
	public int describeContents() 
	{
		return 0;
	}
	public void writeToParcel(Parcel parcel, int flags)
	{
		parcel.writeString(musicName);
		parcel.writeString(url);
		parcel.writeInt(completeSize);
		parcel.writeInt(fileSize);
		parcel.writeInt(state);
	}

	
	
	
	
}
