package com.tarena.fgr.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tarena.fgr.utils.StreamUtils;

/**
 * �Զ��������ͼƬ������
 * 
 * @author �����
 * 
 */
public class ImageLoader {
	/*
	 * ������ʹ��һ��ͼƬ��ʱ�� 1.�ȴ��ڴ滺���в��ң��ڴ滺���������ʹ�õ�ͼƬ ��ֱ��ʹ�� 2.�ڴ滺�������û�У��ٴ��ļ������в��� ,�����
	 * ֱ��ʹ�� 3.����ļ�������Ҳû�У��ٴ������ϼ���ͼƬ ������Ͻ�ͼƬ�ֱ𻺴����ڴ���ļ��С�
	 */
	// ��������ͼƬ���ڴ�
	static LruCache<String, Bitmap> lruCache = null;
	static {
		int maxsize = 1024 * 1024 * 4;// 4mb�����ڴ�����Ļ���ռ�
		lruCache = new LruCache<String, Bitmap>(maxsize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * Ϊÿһ�����ֶ�������ר��ͼƬ
	 */
	public static void setBitmapFromCache(Context context, ImageView imageview,
			String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Bitmap bitmap = null;
		// �ȴ��ڴ滺���в���ͼƬ
		bitmap = getBitmapFromMemoryCache(url);
		if (bitmap != null) {
			imageview.setImageBitmap(bitmap);
			return;
		}
		// �ٴ��ļ������в���ͼƬ
		bitmap = getBitmapFromFileCache(context, url);
		if (bitmap != null) {
			imageview.setImageBitmap(bitmap);
			return;
		}
		// �ٴ������ϼ���
		loadBitmapAsync(context, imageview, url);
	}

	/**
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param imageview
	 *            ʹ��ͼƬ�Ŀؼ�
	 * @param url
	 *            ͼƬ��·��
	 */
	private static void loadBitmapAsync(Context context, ImageView imageview,
			String url) {
		// ʹ���첽�ķ�ʽ�����ϼ���
		MyAsyncTask task = new MyAsyncTask(context, imageview);
		task.execute(url);
	}

	public static class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		Context context = null;
		ImageView imageview = null;

		public MyAsyncTask(Context context, ImageView imageview) {
			this.context = context;
			this.imageview = imageview;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String path = params[0];
			Bitmap bitmap = null;
			try {
				URL url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5000);
				connection.setDoInput(true);

				int statuscode = connection.getResponseCode();
				if (statuscode == 200) {
					InputStream is = connection.getInputStream();
					// bitmap = BitmapFactory.decodeStream(is);
					// ѹ��ͼƬ
					bitmap = compressBitmap(is);
					if (bitmap != null) {
						// ��ͼƬͬʱ�����ڴ滺����
						lruCache.put(path, bitmap);
						// ��ͼƬ���浽�ļ���
						saveBitmapCacheFile(context, bitmap, path);
						return bitmap;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			// ��ͼƬ���õ�imagevie�ؼ���
			imageview.setImageBitmap(result);
		}
	}

	private static Bitmap compressBitmap(InputStream is) {
		Bitmap bitmap = null;
		// �����ֽ�������ת����һ���ֽ����͵�����
		byte[] data = StreamUtils.streamToByteArray(is);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// ����ͼƬ���߽�Ľ���
		// ��仰һ��Ҫ��
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int outHeight = opts.outHeight;// ��ñ߽�ĸ�
		int outWidth = opts.outWidth;// ��ñ߽�Ŀ�
		// ��������ŵı���
		// Ŀ��߶�
		int targetHeight = 64;
		// Ŀ����
		int targetWidth = 64;
		// �߶ȷ����ϵ�ѹ������
		int blh = outHeight / targetHeight;
		// ��ȷ����ϵ�ѹ������
		int blw = outWidth / targetWidth;
		int inSampleSize = blh > blw ? blh : blw;
		if (inSampleSize <= 0) {
			inSampleSize = 1;
		}
		opts.inSampleSize = inSampleSize;
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		return bitmap;
	}

	private static Bitmap getBitmapFromFileCache(Context context, String url) {
		// http://172.60.50.208:8080/MusicServer/image/byebyedisc.jpg
		Bitmap bitmap = null;
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		File cachedir = context.getCacheDir();
		if (cachedir != null) {
			// ��øû���Ŀ¼�µ����е��ļ����ɵ�����
			File[] files = cachedir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					// �Ѹû����ļ�ת����һ��bitmap����
					bitmap = BitmapFactory.decodeFile(files[i]
							.getAbsolutePath());
					return bitmap;
				}
			}
		}
		return null;
	}

	/**
	 * ��������سɹ����ͼƬ���浽�ļ���
	 * 
	 * @param context
	 * @param bitmap
	 * @param path
	 */
	private static void saveBitmapCacheFile(Context context, Bitmap bitmap,
			String path) {
		try {
			// ��û���Ŀ¼
			File cacheFile = context.getCacheDir();
			if (!cacheFile.exists()) {
				cacheFile.mkdirs();
			}
			// �Ա�������ļ����ļ������д���
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			// ������������ļ�����
			File file = new File(cacheFile, fileName);
			// ͼƬѹ��,�Ż�
			// ����һ���ļ������
			OutputStream os = new FileOutputStream(file);
			// ��ͼƬ�ļ��浽����Ŀ¼
			bitmap.compress(CompressFormat.JPEG, 100, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ���ڴ滺���в���ͼƬ
	private static Bitmap getBitmapFromMemoryCache(String url) {
		Bitmap bitmap = null;
		// ���ڴ��л��ͼƬ
		bitmap = lruCache.get(url);
		if (bitmap != null) {
			return bitmap;
		}
		return null;
	}
}
