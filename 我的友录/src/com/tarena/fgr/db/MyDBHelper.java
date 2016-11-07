package com.tarena.fgr.db;

import com.tarena.fgr.utils.LogUtilS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <h1>����ģʽ���</h1></br> ���ݿ�(phone.db)</br>--�������绰��(blacknumber) id:primary key
 * integer ������ number:text</br>--��¼�������绰�����ݱ�(phonelist) id:���� ������ number:text
 * type:integer date:integer</br>--��¼�������绰�Ķ������ݱ�(smslist) id:���� ������ number:text
 * body:text date:integer
 * 
 * @author ����� 2016��10��13�� 09:19:12
 * 
 */
public class MyDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "phone.db";// ���ݿ���
	public static final String TABLE_BLACKNUMBER = "blacknumber";// �������绰��
	public static final String TABLE_PHONELIST = "phonelist";// ��¼�������绰�����ݱ�
	public static final String TABLE_SMSLIST = "smslist";// ��¼�������绰�Ķ������ݱ�
	public static final int VERSION = 1;

	// ����һ��˽�о�̬�������͵�����
	private static MyDBHelper myDBHelper = null;

	// ��װһ�����еľ�̬�ķ��ر���ʵ���ķ���
	public synchronized static MyDBHelper getInstance(Context context) {
		if (myDBHelper == null) {
			myDBHelper = new MyDBHelper(context);
		}
		return myDBHelper;
	}

	/**
	 * @param context
	 * @param name
	 *            ���������ݿ��ļ����ļ���
	 * @param factory
	 * @param version
	 *            ���ݿ�İ汾��
	 */
	private MyDBHelper(Context context) {
		// db��д�ɲ�д
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	// ���������ݿ���ɺ�,ִ�д˷���,������д����������
	public void onCreate(SQLiteDatabase db) {
		// ��д���,�������ű�
		String blacknumberTable = "CREATE TABLE " + TABLE_BLACKNUMBER + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL)";
		String phonelistTable = "CREATE TABLE "
				+ TABLE_PHONELIST
				+ "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL,type INTEGER NOT NULL,date INTEGER NOT NULL)";
		String smslistTable = "CREATE TABLE "
				+ TABLE_SMSLIST
				+ "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL,body TEXT NOT NULL,date INTEGER NOT NULL)";

		db.execSQL(blacknumberTable);
		db.execSQL(phonelistTable);
		db.execSQL(smslistTable);

		LogUtilS.i("TAG", "tabel create ok!");
	}

	@Override
	// �汾��ŷ��ͱ仯ʱ,���ݿ����ʱ��ִ��
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
