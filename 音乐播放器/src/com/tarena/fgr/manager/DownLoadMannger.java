package com.tarena.fgr.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.tarena.fgr.music.R;

public class DownLoadMannger {
	// ����ǰ��һ��֪ͨ���û�
	// ���ع����з�֪ͨ���û�(��ʾ�ļ���ǰ���صİٷֱ�)
	// ������ɷ�֪ͨ���û��������
	/**
	 * @param context
	 * @param ticker
	 *            ״̬������
	 * @param title
	 *            ����
	 * @param content
	 *            ����
	 */
	public static void sendNotification(Context context, String ticker,
			String title, String content) {
		// ���֪ͨ�����ϵͳ����
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// ����֪ͨ�Ĺ�����
		Notification.Builder builder = new Notification.Builder(context);
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.build();
		manager.notify(100, notification);
	}

	public static void downloadSong(final Context context, String url,
			final String name) {
		String downloadDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		final File file = new File(downloadDir + File.separatorChar + name);
		// ʹ���첽������ʵ������
		new AsyncTask<String, Void, File>() {
			protected void onPreExecute() {
				// ��ִ���첽����֮ǰ�ȷ�һ��֪ͨ,Ҫִ���ļ�������
				sendNotification(context, "׼������" + name, "׼������", "׼����");
			};

			@Override
			protected File doInBackground(String... params) {
				String path = params[0];
				URL url;
				try {
					url = new URL(path);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setDoInput(true);
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						InputStream is = connection.getInputStream();
						BufferedInputStream bufferedInputStream = new BufferedInputStream(
								is);
						byte[] buffer = new byte[1024 * 8];
						OutputStream os = new FileOutputStream(file);
						BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
								os);
						int downloadCount = 0;// ��ǰ�Ѿ����ص����ݵĳ���
						int len = 0;// ÿ�����ص����ݵĳ���
						int connentLength = connection.getContentLength();// Ҫ�����ļ����ܴ�С
						while ((len = bufferedInputStream.read(buffer, 0,
								buffer.length)) != -1) {
							downloadCount += len;
							sendNotification(context, "������", "�Ѿ�������:",
									downloadCount / 1000 + "/" + connentLength
											/ 1000 + "  KB");
							// �Ѷ���������д��ָ��λ��
							bufferedOutputStream.write(buffer);
						}
						bufferedOutputStream.flush();
						bufferedInputStream.close();
						bufferedOutputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return file;
			}

			protected void onPostExecute(File result) {
				sendNotification(context, "�������", result.getName(), "�������");
			};

		}.execute(url);
	}

}
