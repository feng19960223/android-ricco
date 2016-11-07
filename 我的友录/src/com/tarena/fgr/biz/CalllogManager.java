package com.tarena.fgr.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.PhoneLookup;

import com.tarena.fgr.entity.Calllog;

/**
 * ͨ����¼ҵ������
 * 
 * @author �����
 * 
 */
public class CalllogManager {
	public static List<Calllog> getCalllogs(Context context) {
		List<Calllog> calllogs = new ArrayList<Calllog>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = CallLog.Calls.CONTENT_URI;// ע������
		// ��Calls�����ṩ�����޷�ֱ�Ӳ�ѯ��ϵ�˵�ͷ��
		// ��ѯ���ֶ�ֱ�Ӳ鵽����5����
		// id,����,�绰,����,����
		String[] projection = new String[] { Calls._ID, Calls.CACHED_NAME,
				Calls.NUMBER, Calls.DATE, Calls.TYPE };
		// ����ʱ�併������,������asc
		// �����ͨ����¼����ǰ��
		String sortOrder = Calls.DATE + " desc ";// �ո�

		Cursor cursor = resolver.query(uri, projection, null, null, sortOrder);

		while (cursor.moveToNext()) {

			Calllog calllog = new Calllog();

			int _id = cursor.getInt(cursor.getColumnIndex(Calls._ID));// id
			String name = cursor.getString(cursor
					.getColumnIndex(Calls.CACHED_NAME));// ����
			String phone = cursor
					.getString(cursor.getColumnIndex(Calls.NUMBER));// �绰
			long date = cursor.getLong(cursor.getColumnIndex(Calls.DATE));// ʱ��
			int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));// ����

			calllog.setId(_id);
			calllog.setName(name);
			calllog.setPhone(phone);
			calllog.setType(type);
			calllog.setCallTime(date);
			calllog.setPhotoid(getPhotoidByNumber(context, phone));// ����ͷ��
			calllog.setFormatCallTimeString(convertTime(date));// ��ʽ��ʱ��
			// ��ͨ����¼��ӵ�������
			calllogs.add(calllog);
		}

		cursor.close();

		return calllogs;
	}

	// ͨ���¼��ĸ�ʽ����˼·
	// 1.���ͨ��ʱ���ǵ���Ļ�,ʱ���ʽ��Ϊ-->HH:mm
	// 2.�����һ����ǰ,ʱ����ʾΪ-->����
	// 3.�����һ������,ʱ����ʾΪ-->���ڼ�
	// 4.�����һ����ǰ,ʱ����ʾΪ-->yyyy-MM-dd

	// ���ڸ�ʽ��
	@SuppressLint("SimpleDateFormat")
	public static String convertTime(long stamp) {
		String strFormat = "";
		int daydiff = dayDiff(stamp);

		if (daydiff == 0) {
			// 2016��10��9�� 16:21:07
			// �����:����������д,�ж��Ƿ��Ǹո�.60����֮��,��Сʱ֮��,���������...
			// ͨ��ʱ���ǵ���
			Date date = new Date();
			date.setTime(stamp);
			strFormat = new SimpleDateFormat("HH:mm").format(date);
		} else if (daydiff == 1) {
			// ͨ��ʱ��������
			strFormat = "����";
		} else if (daydiff <= 7) {
			// ͨ��ʱ����һ�����ڵ�
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(stamp);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case Calendar.MONDAY:
				strFormat = "��һ";
				break;
			case Calendar.TUESDAY:
				strFormat = "�ܶ�";
				break;
			case Calendar.WEDNESDAY:
				strFormat = "����";
				break;
			case Calendar.THURSDAY:
				strFormat = "����";
				break;
			case Calendar.FRIDAY:
				strFormat = "����";
				break;
			case Calendar.SATURDAY:
				strFormat = "����";
				break;
			case Calendar.SUNDAY:
				strFormat = "����";
				break;
			default:
				break;
			}
		} else {
			// һ����ǰ��ͨ��
			strFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
					stamp));
		}
		return strFormat;
	}

	/**
	 * ���ݿ�ʱ��͵�ǰʱ����
	 * 
	 * @param stamp
	 *            ���ݿ��ʱ��
	 * @return
	 */
	public static int dayDiff(long stamp) {
		// ��õ�ǰϵͳʱ�����������
		Calendar calendar1 = Calendar.getInstance();
		// ͨ��ʱ�����������
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(stamp);
		// ����ʱ����������
		int daydiff = calendar1.get(Calendar.DAY_OF_YEAR)// һ���еĵڼ���
				- calendar2.get(Calendar.DAY_OF_YEAR);
		return daydiff;
	}

	/**
	 * ͨ���绰�����ѯͷ��id
	 * 
	 * @param context
	 * @param number
	 *            �绰����
	 * @return ͷ��id
	 */
	public static int getPhotoidByNumber(Context context, String number) {
		int photoid = 0;
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
		String[] projection = new String[] { PhoneLookup.PHOTO_ID };
		Cursor cursor = resolver.query(uri, projection, null, null, null);
		if (cursor.moveToNext()) {
			photoid = cursor.getInt(0);
		}
		cursor.close();
		return photoid;
	}

	/**
	 * ͨ���绰�����ѯ����
	 * 
	 * @param context
	 * @param number
	 *            �绰����
	 * @return ͷ��id
	 */
	public static String getNameByNumber(Context context, String number) {
		String name = "";
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
		String[] projection = new String[] { PhoneLookup.DISPLAY_NAME };
		Cursor cursor = resolver.query(uri, projection, null, null, null);
		if (cursor.moveToNext()) {
			name = cursor.getString(0);
		}
		cursor.close();
		return name;
	}

	/**
	 * ��ͨ����¼�����ݿ���ɾ��
	 * 
	 * @param context
	 * @param calllog
	 *            ��ϵ��
	 */
	public static void deleteCalllog(Context context, Calllog calllog) {
		ContentResolver resolver = context.getContentResolver();
		// Uri uri = Uri.parse("content://call_log/calls");
		Uri uri = CallLog.Calls.CONTENT_URI;
		String where = Calls.NUMBER + "=?";
		String[] args = new String[] { calllog.getPhone() };
		resolver.delete(uri, where, args);
	}

	public static void deleteCalllog(Context context, String phone) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = CallLog.Calls.CONTENT_URI;
		String where = Calls.NUMBER + "=?";
		String[] args = new String[] { phone };
		resolver.delete(uri, where, args);
	}
}
