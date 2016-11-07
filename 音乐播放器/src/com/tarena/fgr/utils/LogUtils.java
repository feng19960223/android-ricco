package com.tarena.fgr.utils;

import android.util.Log;

/**
 * ֻ��Ҫ�޸�LEVEL�ĳ�����ֵ���Ϳ������ɵؿ�����־�Ĵ�ӡ��Ϊ�ˡ� ������LEVEL����VERBOSE�Ϳ��԰����е���־����ӡ������
 * ��LEVEL����WARN�Ϳ��԰�������־�����ε���
 * 
 * �ڿ����׶ν�LEVELָ����VERBOSE ������Ŀ��ʽ���ߵ�ʱ��LEVELָ����NOTHING�Ϳ����ˡ� </br>2016��10��13��
 * 10:35:47 ���� ����true��flase�޸�,���Ӷ������
 * 
 * @author anzhuo
 * 
 */
public class LogUtils {
	// public static final int VERBOSE = 1;
	// public static final int DEBUG = 2;
	// public static final int INFO = 3;
	// public static final int WARN = 4;
	// public static final int ERROR = 5;
	// public static final int NOTHING = 6;
	// public static int LEVEL = VERBOSE;// ��־�ĵȼ��������ԣ�ͨ���ı�ֵ��������־�������

	public static boolean isDebug = true;// �Ƿ���Ҫ��ӡbug��������application��onCreate���������ʼ��
	private static final String TAG = "-------------->"; // Ĭ�ϵ�Tag

	private LogUtils() {
		/* cannot be instantiated,���ܱ�ʵ���� */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// ���������Ĭ��tag�ĺ���
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	public static void w(String msg) {
		// if (LEVEL <= WARN) {
		if (isDebug) {
			Log.w(TAG, msg);
		}
	}

	// �����Ǵ����Զ���tag�ĺ���
	public static void v(String tag, String msg) {
		// if (LEVEL <= VERBOSE) {
		if (isDebug) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		// if (LEVEL <= DEBUG) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		// if (LEVEL <= INFO) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		// if (LEVEL <= ERROR) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
}
