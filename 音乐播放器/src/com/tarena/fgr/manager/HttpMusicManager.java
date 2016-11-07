package com.tarena.fgr.manager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.tarena.fgr.entity.Music;
import com.tarena.fgr.utils.JSONParserUtils;
import com.tarena.fgr.utils.LogUtils;
import com.tarena.fgr.utils.StreamUtils;

public class HttpMusicManager {
	/**
	 * GET��ʽʵ�����ּ���
	 * 
	 * @param path
	 * @return
	 */
	private static List<Music> loadMusics(String path) {
		List<Music> musics = new ArrayList<Music>();
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {// 200
				InputStream is = connection.getInputStream();
				String jsonStr = StreamUtils.streamToString(is);
				JSONObject jsonObject = new JSONObject(jsonStr);
				musics = JSONParserUtils.parseMusics(jsonObject);
			} else {
				LogUtils.i("" + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Music music : musics) {
			LogUtils.i(music.toString());
		}
		LogUtils.i("�������");
		return musics;
	}

	public static LoadMusicListener loadMusicListener = null;

	/**
	 * �첽����ʵ�ּ�������
	 * 
	 * @param path
	 */
	public static void asyncLoadMusics(String path, LoadMusicListener listener) {
		loadMusicListener = listener;
		MyAsyncTask asyncTask = new MyAsyncTask();
		asyncTask.execute(path);
	}

	// execute����ֵ,���صĽ���,��������
	public static class MyAsyncTask extends
			AsyncTask<String, Void, List<Music>> {

		@Override
		// �÷����ں�ִ̨��,ִ�е��ǹ����̵߳�����
		// �Ѽ�����ɵ����ݷ��ظ�UI���߳�
		protected List<Music> doInBackground(String... params) {
			return loadMusics(params[0]);// onPostExecute();
		}

		@Override
		protected void onPostExecute(List<Music> result) {
			loadMusicListener.onMusicsLoadEnd(result);
			super.onPostExecute(result);
		}

		@Override
		// ��doInBackground֮ǰִ��,Ԥ�������
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		// ���ȸ���
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	public interface LoadMusicListener {
		public void onMusicsLoadEnd(List<Music> musics);
	}

}
