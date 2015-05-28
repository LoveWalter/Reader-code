package com.zwt.myapp.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.zwt.myapp.bean.DownloadInfo;
import com.zwt.myapp.bean.LoadInfo;
import com.zwt.myapp.database.DownloadDao;

public class Downloader {
	private String downPath;// ����·��
	private String savePath;// ����·��
	private String musicName;// ��������
	private int threadCount;// �߳���
	private Handler mHandler;
	private DownloadDao dao;
	private Context context;
	private int fileSize;// �ļ���С
	private int range;
	private List<DownloadInfo> infos;// ���������Ϣ��ļ���
	private int state = INIT;
	private static final int INIT = 1;// �����������ص�״̬����ʼ��״̬����������״̬����ͣ״̬
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;

	/**
	 * @param downPath
	 *            ���ص�ַ
	 * @param savePath
	 *            �����ַ
	 * @param musicName
	 *            ��������
	 * @param threadcount
	 *            ʹ�õ��߳���
	 * @param context
	 *            ��������һ��DAO����
	 * **/
	public Downloader(String downPath, String savePath, String musicName,
			int threadCount, Context context, Handler mHandler) {
		this.downPath = downPath;
		this.savePath = savePath;
		this.musicName = musicName;
		this.threadCount = threadCount;
		this.context = context;
		this.mHandler = mHandler;
		dao = new DownloadDao(context);
	}

	/**
	 * �ж��Ƿ���������
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * �ж��Ƿ���ͣ
	 * **/
	public boolean isPause() {
		return state == PAUSE;
	}

	/**
	 * �õ�downloader�����Ϣ ���Ƚ����ж��Ƿ��ǵ�һ�����أ�����ǵ�һ�ξ�Ҫ���г�ʼ������������������Ϣ���浽���ݿ���
	 * ������ǵ�һ�����أ��Ǿ�Ҫ�����ݿ��ж���֮ǰ���ص���Ϣ����ʼλ�ã�����Ϊֹ���ļ���С�ȣ�������������Ϣ���ظ�������
	 */
	public LoadInfo getDownloaderInfors() {
		if (isFirst(downPath)) {
			init();// ��1������Ҫ���г�ʼ��
			System.out.println("Downloader getDownload:"+fileSize+".."+threadCount+downPath);
			range = this.fileSize / this.threadCount;// ����ÿ���߳�Ӧ�����صĳ���
			System.out.println("range is:" + range);
			infos = new ArrayList<DownloadInfo>();// List<DownloadInfo>infos
													// ��װ����ÿ���̵߳�������Ϣ
			// ��ʼ���߳���Ϣ������,��ʼ��ÿ���̵߳���Ϣ,Ϊÿ���̷߳��俪ʼ����λ�ã�����λ��
			for (int i = 0; i < this.threadCount - 1; i++) {
				// startPos��ÿ���߳�������ÿ���߳�Ӧ�����صĳ���,��0��,��0��ʼ
				// endPosҪ��ȥ1Byte����Ϊ,����1byte�ĵط�����һ���߳̿�ʼ��λ��
				DownloadInfo info = new DownloadInfo(i, i * range, (i + 1)
						* range - 1, 0, downPath);
				System.out.println("set threaid��" + info.getThreadId()
						+ "startPos:" + info.getStartPos() + "endPos:"
						+ info.getEndPos() + "CompeleteSize:"
						+ info.getCompeleteSize() + "url:" + info.getUrl());
				infos.add(info);// ��ÿ���̵߳���Ϣ���뵽infos����߳���Ϣ��������
			}
			// ����������1���̵߳���Ϣ,ֻ���Ե����ó�������Ϊ���һ���߳����صĽ���λ��Ӧ��ΪfileSize
			DownloadInfo info = new DownloadInfo(this.threadCount - 1,
					(this.threadCount - 1) * range, this.fileSize, 0, downPath);
			System.out.println("set threaid��" + info.getThreadId()
					+ "startPos:" + info.getStartPos() + "endPos:"
					+ info.getEndPos() + "CompeleteSize:"
					+ info.getCompeleteSize() + "url:" + info.getUrl());
			infos.add(info);
			// �����infos���뵽���ݿ�,����ListView�ϵ�һ��item�Ѿ���ʼ�����Ѿ����ǵ�1��������
			dao.saveInfos(infos, this.context);
			// ����һ��LoadInfo��������������ľ�����Ϣ
			LoadInfo loadInfo = new LoadInfo(this.fileSize, 0, this.downPath);
			return loadInfo;
		} else {
			// ������ǵ�1������,�õ����ݿ������е�urlstr���������ľ�����Ϣ
			infos = dao.getInfos(this.downPath);
			int size = 0;
			int completeSize = 0;
			for (DownloadInfo info : infos) {
				completeSize += info.getCompeleteSize();// ��ÿ���߳����صĳ����ۼ�����,�õ������ļ������س���
				size += info.getEndPos() - info.getStartPos() + 1;// ������ļ��Ĵ�С,��ÿ���̵߳Ľ���λ�ü�ȥ��ʼ���ص�λ��,����ÿ���߳�Ҫ���صĳ��ȣ�Ȼ���ۼ�
			}
			LoadInfo loadInfo = new LoadInfo(size, completeSize, this.downPath);
			return loadInfo;
		}
	}

	/**
	 * ��ʼ��,Ҫ�ɵ���:1.�õ������ļ��ĳ��� 2.�ڸ����ı���·�������ļ�,�����ļ��Ĵ�С
	 */
	private void init() {
		try {
			URL url = new URL(downPath);// ͨ�����������ص�ַ�õ�һ��url
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();// �õ�һ��http����
			conn.setConnectTimeout(5 * 1000);// �������ӳ�ʱΪ5����
			conn.setRequestMethod("GET");// �������ӷ�ʽΪGET
			// ���http���صĴ�����200����206��Ϊ���ӳɹ�
			if (conn.getResponseCode() == 200 || conn.getResponseCode() == 206) {
				fileSize = conn.getContentLength();// �õ��ļ��Ĵ�С
				if (fileSize <= 0)
					Toast.makeText(this.context, "�������,�޷���ȡ�ļ���С.",
							Toast.LENGTH_SHORT).show();
				File dir = new File(savePath);
				// ����ļ�������,�򴴽�һ��ָ�����ļ�,���������չһ��,����ļ��Ѿ�����,�����Ի��������û��Ƿ񸲸�
				if (!dir.exists()) {
					if (dir.mkdirs()) {
						System.out.println("mkdirs success.");
					}
				}
				File file = new File(this.savePath, this.musicName);
				RandomAccessFile randomFile = new RandomAccessFile(file, "rwd");
				randomFile.setLength(fileSize);// ���ñ����ļ��Ĵ�С
				randomFile.close();
				conn.disconnect();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * �����߳̿�ʼ��������
	 */
	public void download() {
		if (infos != null) {
			System.out.println("infos is not null");
			if (state == DOWNLOADING) {
				return;
			}
			state = DOWNLOADING;// ��״̬����Ϊ��������
			for (DownloadInfo info : infos) {
				// for ѭ�������̣߳�ʵ������
				new MyThread(info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getUrl(), this.context).start();
			}

		}
	}

	// ɾ�����ݿ���urlstr��Ӧ����������Ϣ
	public void delete(String urlstr) {
		dao.delete(urlstr);
	}

	// ������ͣ
	public void pause() {
		state = PAUSE;
	}

	// ��������״̬,������״̬����Ϊinit��ʼ��״̬
	public void reset() {
		state = INIT;
	}

	/**
	 * �ж��Ƿ��ǵ�һ�����أ�����dao��ѯ���ݿ����Ƿ������������ַ�ļ�¼
	 * 
	 * @return boolean �еĻ�����false,û�з���true
	 */
	private boolean isFirst(String urlstr) {
		return dao.isHasInfors(urlstr);
	}

	/**
	 * MyThread��һ���ڲ��࣬���̳���Thread��
	 * **/
	public class MyThread extends Thread {
		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;
		private Context context;

		/**
		 * @param threadId
		 *            �̺߳�
		 * @param startPos
		 *            �߳̿�ʼ���ص�λ��
		 * @param endPos
		 *            �߳̽������ص�λ��
		 * @param compeleteSize
		 *            ÿ���߳�������صĳ���
		 * @param urlstr
		 *            ���ص�ַ
		 * **/
		public MyThread(int threadId, int startPos, int endPos,
				int compeleteSize, String urlstr, Context context) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			this.urlstr = urlstr;
			this.context = context;
		}

		@Override
		public void run() {
			HttpURLConnection conn = null;
			RandomAccessFile randomAccessFile = null;
			InputStream inStream = null;
			File file = new File(savePath, musicName);
			try {
				URL url = new URL(this.urlstr);
				conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				// constructConn(conn);
				System.out.println("responseCode:" + conn.getResponseCode());
				if (conn.getResponseCode() == 200
						|| conn.getResponseCode() == 206) {
					randomAccessFile = new RandomAccessFile(file, "rwd");
					// ����Ĳ���ֻ����Ҫ����compeleteSize��ȫ��Ϊ�˶ϵ���������,��Ϊ�ܿ��ܲ��ǵ�1������
					randomAccessFile.seek(this.startPos + this.compeleteSize);// ���������̴߳��ĸ��ط���ʼд������,�����������ϻ�ȡ������һ����
					inStream = conn.getInputStream();
					byte buffer[] = new byte[4096];
					int length = 0;
					while ((length = inStream.read(buffer, 0, buffer.length)) != -1) {
						randomAccessFile.write(buffer, 0, length);
						compeleteSize += length;// �ۼ��Ѿ����صĳ���
						// �������ݿ��е�������Ϣ
						dao.updataInfos(threadId, compeleteSize, urlstr,
								this.context);
						// ����Ϣ��������Ϣ�������������Խ��������и���
						Message message = Message.obtain();
						message.what = 1;
						message.obj = urlstr;
						message.arg1 = length;
						mHandler.sendMessage(message);// ��DownloaderService������Ϣ
						if (state == PAUSE) {
							System.out.println("-----pause----");
							return;
						}
					}
					System.out.println("threadid:" + this.threadId + "is over");
				}
			} catch (Exception e) {
				System.out.println("threaid has exception");
				e.printStackTrace();
			} finally {
				try {
					inStream.close();
					randomAccessFile.close();
					conn.disconnect();
					dao.closeDb();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}

		/***********************************************************************
		 * ������������ʱ�Ĳ��� ���ؿ�ʼ���ص�λ��
		 * 
		 * @throws IOException
		 **********************************************************************/
		private void constructConn(HttpURLConnection conn) throws IOException {
			conn.setConnectTimeout(5 * 1000);// һ��Ҫ�������ӳ�ʱ�����ﶨΪ5��
			conn.setRequestMethod("GET");// ����GET��ʽ�ύ
			conn.setRequestProperty(
					"Accept",
					"image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
			conn.setRequestProperty("Accept-Language", "zh-CN");
			conn.setRequestProperty("Referer", this.urlstr);
			conn.setRequestProperty("Charset", "UTF-8");
			int startPosition = this.startPos + this.compeleteSize;
			// ���÷�Χ����ʽΪRange��bytes x-y;
			// ���д������ʵ�ֶ��̵߳Ĺؼ�,Range�ֶ������û��������صĿ�ʼ��ַ�ͽ�����ַ,��Ȼrange���кܶ��������÷�
			conn.setRequestProperty("Range", "bytes=" + startPosition + "-"
					+ this.endPos);// ���û�ȡʵ�����ݵķ�Χ
			conn.setRequestProperty(
					"User-Agent",
					"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.connect();
		}
	}

}
