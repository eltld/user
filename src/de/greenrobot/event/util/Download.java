package de.greenrobot.event.util;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.ememed.user2.entity.ChatHistoryEntry;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class Download implements Serializable {
	private static final int START = 1; // 开始下载
	private static final int PUBLISH = 2; // 更新进度
	private static final int PAUSE = 3; // 暂停下载
	private static final int CANCEL = 4; // 取消下载
	private static final int ERROR = 5; // 下载错误
	private static final int SUCCESS = 6; // 下载成功
	private static final int GOON = 7; // 继续下载

	private static ExecutorService mThreadPool; // 线程池

	static {
		mThreadPool = Executors.newFixedThreadPool(5); // 默认5个
	}

	private int mDownloadId; // 下载id
	private String mFileName; // 本地保存文件名
	private String mUrl; // 下载地址
	private String mLocalPath = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ "/Android/data/net.ememed.user2/ememed#ememeduserofficial"; // 本地存放目录

	private boolean isPause = false; // 是否暂停
	private boolean isCanceled = false; // 是否手动停止下载

	private OnDownloadListener mListener; // 监听器

	public static Download download = null;


	/**
	 * 配置下载线程池的大小
	 * 
	 * @param maxSize
	 *            同时下载的最大线程数
	 */
	public static void configDownloadTheadPool(int maxSize) {
		mThreadPool = Executors.newFixedThreadPool(10);
	}

	public Download(String userid) {
		mLocalPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath()
				+ "/Android/data/net.ememed.user2/ememed#ememeduserofficial/"+userid;
		
	}

	public void setParameters(OnDownloadListener listener) {
		// setFileName(entry, myID);
		setOnDownloadListener(listener);
	}

	/**
	 * 添加下载任务
	 * 
	 * @param downloadId
	 *            下载任务的id
	 * @param url
	 *            下载地址
	 * @param localPath
	 *            本地存放地址
	 */
	public Download(int downloadId, String url, String localPath) {
		if (!new File(localPath).exists()) {
			new File(localPath).mkdirs();
		}

		mDownloadId = downloadId;
		mUrl = url;
		String[] tempArray = url.split("/");
		// mFileName = tempArray[tempArray.length - 1];
		mLocalPath = localPath;
	}

	/**
	 * 设置监听器
	 * 
	 * @param listener
	 *            设置下载监听器
	 * @return this
	 */
	public Download setOnDownloadListener(OnDownloadListener listener) {
		mListener = listener;
		return this;
	}

	// /**
	// * 获取文件名
	// *
	// * @return 文件名
	// */
	// public String getFileName() {
	// return mFileName;
	// }

	/**
	 * 开始下载 params isGoon是否为继续下载
	 */
	// public void start(final boolean isGoon, ChatHistoryEntry entry) {
	public void start(final boolean isGoon,final ChatHistoryEntry entry,final String myID) {
		// 处理消息

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case ERROR:
					mListener.onError(mDownloadId);
					break;
				case CANCEL:
					mListener.onCancel(mDownloadId);
					break;
				case PAUSE:
					mListener.onPause(mDownloadId);
					break;
				case PUBLISH:
					mListener.onPublish(mDownloadId,
							Long.parseLong(msg.obj.toString()));
					break;
				case SUCCESS:
					
					mListener.onSuccess(mDownloadId, (String)msg.obj);

					
					System.out.println("getFileName(entry, myID) = "+getFileName(entry, myID));
					
					break;
				case START:
					mListener.onStart(mDownloadId,
							Long.parseLong(msg.obj.toString()));
					break;
				case GOON:
					mListener.onGoon(mDownloadId,
							Long.parseLong(msg.obj.toString()));
					break;
				}
			}
		};

		String fileName = getFileName(entry, myID);
		System.out.println("fileName = "+ fileName);
		File file = new File(fileName);
		if (file.exists()) {
			System.out.println("文件已存在...");
			 Message message = new Message();
			 message.what = SUCCESS;
			 message.obj = fileName;
			 handler.sendMessage(message);
			return;
		}
		// 真正开始下载
		final String url = entry.getCONTENT_EXT().get(0).getUrl();
		mThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				download(isGoon, handler, url,mFileName);
			}
		});
	}

	/**
	 * 下载方法
	 * 
	 * @param handler
	 *            消息处理器
	 */
	private void download(boolean isGoon, Handler handler, String mUrl,String mFileName) {
		Message msg = null;
		System.out.println("开始下载。。。");
		try {
			RandomAccessFile localFile = new RandomAccessFile(new File(
					mLocalPath + File.separator + mFileName), "rwd");
			
			
			
			DefaultHttpClient client = new DefaultHttpClient();
			client.setParams(getHttpParams());
			HttpGet get = new HttpGet(mUrl);

			// long localFileLength = getLocalFileLength();
			final long remoteFileLength = getRemoteFileLength();
			long downloadedLength = 0;

			// 远程文件不存在
			if (remoteFileLength == -1l) {
				System.out.println("!!下载文件不存在...");
				localFile.close();
				handler.sendEmptyMessage(ERROR);
				return;
			}
			// 本地文件存在
			// if (localFileLength > -1l && localFileLength < remoteFileLength)
			// {
			// System.out.println("本地文件存在...");
			// localFile.seek(localFileLength);
			// get.addHeader("Range", "bytes=" + localFileLength + "-"
			// + remoteFileLength);
			// }
			msg = Message.obtain();
			// 如果不是继续下载
			if (!isGoon) {
				// 发送开始下载的消息并获取文件大小的消息
				msg.what = START;
				msg.obj = remoteFileLength;
			} else {
				msg.what = GOON;
				// msg.obj = localFileLength;
			}

			handler.sendMessage(msg);
			HttpResponse response = client.execute(get);
			int httpCode = response.getStatusLine().getStatusCode();
			if (httpCode >= 200 && httpCode <= 300) {
				InputStream in = response.getEntity().getContent();
				byte[] bytes = new byte[1024];
				int len = -1;
				while (-1 != (len = in.read(bytes))) {
					localFile.write(bytes, 0, len);
					downloadedLength += len;
					// System.out.println((int)(downloadedLength/(float)remoteFileLength
					// * 100));
					if ((int) (downloadedLength / (float) remoteFileLength * 100) % 10 == 0) {
						// 发送更新进度的消息
						msg = Message.obtain();
						msg.what = PUBLISH;
						msg.obj = downloadedLength;
						handler.sendMessage(msg);
						// System.out.println(mDownloadId + "已下载" +
						// downloadedLength);
					}

					// 暂停下载， 退出方法
					if (isPause) {
						// 发送暂停的消息
						handler.sendEmptyMessage(PAUSE);
						System.out.println("下载暂停...");
						break;
					}

					// 取消下载， 删除文件并退出方法
					if (isCanceled) {
						System.out.println("手动关闭下载。。");
						localFile.close();
						client.getConnectionManager().shutdown();
						new File(mLocalPath + File.separator + mFileName)
								.delete();
						// 发送取消下载的消息
						handler.sendEmptyMessage(CANCEL);
						return;
					}
				}

				localFile.close();
				client.getConnectionManager().shutdown();
				// 发送下载完毕的消息
				if (!isPause) {
					Message msg1  = new Message();
					msg1.what = SUCCESS;
					msg1.obj = mLocalPath + File.separator + mFileName;
					handler.sendMessage(msg1);
				}
			}

		} catch (Exception e) {
			// 发送下载错误的消息
			e.printStackTrace();
			handler.sendEmptyMessage(ERROR);
		}
	}

	/**
	 * 暂停/继续下载 param pause 是否暂停下载 暂停 return true 继续 return false
	 */
	public synchronized boolean pause(boolean pause) {
		if (!pause) {
			System.out.println("继续下载");
			isPause = false;
			start(true, null, null); // 开始下载
		} else {
			System.out.println("暂停下载");
			isPause = true;
		}
		return isPause;
	}

	/**
	 * 关闭下载， 会删除文件
	 */
	public synchronized void cancel() {
		isCanceled = true;
		if (isPause) {
			new File(mLocalPath + File.separator + mFileName).delete();
		}
	}

	// /**
	// * 获取本地文件大小
	// *
	// * @return 本地文件的大小 or 不存在返回-1
	// */
	// public synchronized long getLocalFileLength() {
	// long size = -1l;
	// File localFile = new File(mLocalPath + File.separator + mFileName);
	// if (localFile.exists()) {
	// size = localFile.length();
	// }
	// System.out.println("本地文件大小" + size);
	// return size <= 0 ? -1l : size;
	// }

	public synchronized boolean fileExists(String fileName) {

		File localFile = new File(fileName);
		if (localFile.exists()) {
			return true;
		}
		return false;
	}

	/**
	 * 获取远程文件打下 or 不存在返回-1
	 * 
	 * @return
	 */
	public synchronized long getRemoteFileLength() {
		long size = -1l;
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			client.setParams(getHttpParams());
			HttpGet get = new HttpGet(mUrl);

			HttpResponse response = client.execute(get);
			int httpCode = response.getStatusLine().getStatusCode();
			if (httpCode >= 200 && httpCode <= 300) {
				size = response.getEntity().getContentLength();
			}

			client.getConnectionManager().shutdown();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("远程文件大小" + size);
		return size;
	}

	/**
	 * 设置http参数 不能设置soTimeout
	 * 
	 * @return HttpParams http参数
	 */
	private static HttpParams getHttpParams() {
		HttpParams params = new BasicHttpParams();

		HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
		HttpProtocolParams.setUseExpectContinue(params, true);
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2041.4 Safari/537.36");
		ConnManagerParams.setTimeout(params, 4000);
		HttpConnectionParams.setConnectionTimeout(params, 4000);

		return params;
	}

	/**
	 * 关闭下载线程池
	 */
	public static void closeDownloadThread() {
		if (null != mThreadPool) {
			mThreadPool.shutdownNow();
		}
	}

	/**
	 * 设置文件名
	 */
	public String getFileName(ChatHistoryEntry entry, String id) {

		mDownloadId = 1;
		mUrl = entry.getCONTENT_EXT().get(0).getUrl();

		String filenName = null;

		if (entry.getTYPE().equals("img")) {
			mLocalPath = mLocalPath + "/image";
			if (id.equals(entry.getRECEIVER().trim())) {
				String pathName = entry.getCONTENT_EXT().get(0).getUrl();
				pathName = pathName.substring(pathName.lastIndexOf("/"),
						pathName.length());
				mFileName = pathName;
				filenName = mLocalPath  + pathName;
			} else {
				mFileName = entry.getCONTENT_EXT().get(0).getFilename();
				filenName = mLocalPath  + File.separator + entry.getCONTENT_EXT().get(0).getFilename();
			}
			
		} else if (entry.getTYPE().equals("audio")) {
			mLocalPath = mLocalPath + "/voice";
			if (id.equals(entry.getRECEIVER().trim())) {
				String pathName = entry.getCONTENT_EXT().get(0).getUrl();
				pathName = pathName.substring(pathName.lastIndexOf("/"),
						pathName.length());
				 mFileName = pathName;
			} else {
				 mFileName = entry.getCONTENT_EXT().get(0)
						.getFilename();
			}
		}
		return filenName;

	}

	public interface OnDownloadListener {
		public void onStart(int downloadId, long fileSize); // 回调开始下载

		public void onPublish(int downloadId, long size); // 回调更新进度

		public void onSuccess(int downloadId, String fileName); // 回调下载成功

		public void onPause(int downloadId); // 回调暂停

		public void onError(int downloadId); // 回调下载出错

		public void onCancel(int downloadId); // 回调取消下载

		public void onGoon(int downloadId, long localSize); // 回调继续下载
	}
}