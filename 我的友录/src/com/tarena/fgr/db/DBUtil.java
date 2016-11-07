package com.tarena.fgr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tarena.fgr.entity.BlackPhone;
import com.tarena.fgr.entity.BlackSMS;

/**
 * �����ݿ������ݵĹ���
 * 
 * @author �����
 * 
 */
public class DBUtil {// DAO
	private MyDBHelper helper = null;

	public DBUtil(Context context) {
		helper = MyDBHelper.getInstance(context);
	}

	// �����ķ�ʽ,������ķ���,���ܼ�static

	/**
	 * ��Ӻ������绰
	 * 
	 * @param number
	 */
	public void insertBlackNumber(String number) {
		// ����Ѿ����ݿ��Ѿ��м�¼,����ִ�����ݵ����
		if (isBlackNumber(number)) {
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		db.insert(MyDBHelper.TABLE_BLACKNUMBER, null, values);
		db.close();
	}

	/**
	 * ��ѯĳһ���绰��,�Ƿ��Ǻ������绰
	 * 
	 * @param number
	 * @return
	 */
	public boolean isBlackNumber(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		// Cursor cursor = db.rawQuery(
		// "select _id from blacknumber where number =?",
		// new String[] { number });
		Cursor cursor = db.query(MyDBHelper.TABLE_BLACKNUMBER, null,
				"number = ?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * �����صĶ��Ŵ����¼�������绰�Ķ������ݱ�
	 */
	public void insertSMS(BlackSMS sms) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", sms.getNumber());
		values.put("body", sms.getBody());
		values.put("date", sms.getDate());
		db.insert(MyDBHelper.TABLE_SMSLIST, null, values);
		db.close();
	}

	/**
	 * �����صĵ绰���뵽��¼�������绰�����ݱ�
	 * 
	 * @param phone
	 */
	public void insertPhone(BlackPhone phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", phone.getNumber());
		values.put("type", phone.getType());
		values.put("date", phone.getDate());
		db.insert(MyDBHelper.TABLE_PHONELIST, null, values);
		db.close();
	}

}
