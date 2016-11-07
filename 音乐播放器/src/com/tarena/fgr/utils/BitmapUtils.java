package com.tarena.fgr.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {

	/**
	 * ��SD���϶�ȡͼƬ
	 * 
	 * @param bytes
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] bytes, int width, int height) {
		Options options = new Options();
		// ����ʱ,�������ر߽�����
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		// ������Ϻ�,��ȡͼƬ�Ŀ����
		int w = options.outWidth / width;
		int h = options.outHeight / height;
		// ���ü���ʱ,��ʹ�õ���������
		int scale = w > h ? w : h;
		options.inSampleSize = scale;
		// �����ر߽�����
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}

	/**
	 * ����ͼƬ��SD��
	 * 
	 * @param file
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveBitmap(File file, Bitmap bitmap) throws IOException {
		// ��Ŀ¼�Ƿ����
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// �ļ��Ƿ����
		if (!file.exists()) {
			file.createNewFile();
		}
		// ��bitmapѹ����JPEG��ʽ���
		bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
	}
}
