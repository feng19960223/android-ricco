package com.tarena.fgr.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * ����ҵ������
 * 
 * @author ����� 2016��10��10�� 15:48:26
 * 
 */
public class SMSManager {
	// ���ŻỰUri
	public static final Uri CONVERSATION_URI = Uri
			.parse("content://mms-sms/conversations");
	// ����Uri ��Ӧ��ContentProvider��Э��������ŵ��ռ���ͷ�����
	public static final Uri SMS_URI = Uri.parse("content://sms");
	// ���ŷ�����:
	// ע��sent �� send
	public static final Uri SMS_SEND_URI = Uri.parse("content://sms/sent");
	// �����ռ���:
	public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");

	public static List<Conversation> getConversations(Context context) {
		List<Conversation> conversations = new ArrayList<Conversation>();
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[] { "body", "date", "read",
				"thread_id", "address" };
		String sortOrder = "date" + " desc";// �ո�
		Cursor cursor = resolver.query(CONVERSATION_URI, projection, null,
				null, sortOrder);
		while (cursor.moveToNext()) {
			Conversation conversation = new Conversation();
			String body = cursor.getString(cursor.getColumnIndex("body"));
			long date = cursor.getLong(cursor.getColumnIndex("date"));
			int read = cursor.getInt(cursor.getColumnIndex("read"));
			int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
			String phone = cursor.getString(cursor.getColumnIndex("address"));
			conversation.setBody(body);
			conversation.setDate(date);
			conversation.setRead(read);
			conversation.setThread_id(thread_id);
			conversation.setPhone(phone);

			int photoId = CalllogManager.getPhotoidByNumber(context, phone);
			conversation.setPhotoid(photoId);
			String formatDate = CalllogManager.convertTime(date);
			conversation.setFormatdate(formatDate);
			String name = CalllogManager.getNameByNumber(context, phone);
			if (TextUtils.isEmpty(name)) {// �����İ������ʾ�绰��
				conversation.setName(phone);
			} else {// ��Ϥ������ʾ��ϵ������
				conversation.setName(name);
			}
			conversations.add(conversation);
		}
		cursor.close();
		return conversations;
	}

	// ��װ���Ų�ѯ��ҵ����ķ���
	public static List<Sms> getSMSes(Context context, int thread_id) {
		List<Sms> smses = new ArrayList<Sms>();
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "date", "type",
				"body" };
		// ��������
		String sortOrder = "date" + " asc";// �ո�
		String selection = "thread_id=?";
		Cursor cursor = resolver.query(SMS_URI, projection, selection,
				new String[] { String.valueOf(thread_id) }, sortOrder);
		while (cursor.moveToNext()) {

			Sms sms = new Sms();

			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String address = cursor.getString(cursor.getColumnIndex("address"));
			long date = cursor.getLong(cursor.getColumnIndex("date"));
			int type = cursor.getInt(cursor.getColumnIndex("type"));
			String body = cursor.getString(cursor.getColumnIndex("body"));

			sms.set_id(_id);
			sms.setAddress(address);
			sms.setDate(date);
			sms.setType(type);
			sms.setBody(body);
			// ��ʽ��ʱ��
			// String formateDate = CalllogManager.convertTime(date);
			// sms.setFormateDate(formateDate);
			// ��ʽ�����ں�ͨ����¼��������һЩ��һ��,�Ժ�����޸�
			String formateDate = chatTime(date);
			sms.setFormateDate(formateDate);

			smses.add(sms);
		}
		cursor.close();
		return smses;
	}

	/**
	 * ����ʱ��ĸ�ʽ��
	 * 
	 * @param stamp
	 *            ʱ�����ֵ
	 * @return ��ʽ���õ�ʱ��
	 */
	@SuppressLint("SimpleDateFormat")
	public static String chatTime(long stamp) {
		// ʱ���ǵ����ʽ��Ϊ HH:mm
		// �����ʽ��Ϊ ���� HH:mm
		// ����ʱ���ʽ��Ϊ yyyy-
		int daydiff = CalllogManager.dayDiff(stamp);
		String strdate = "";
		// ����ʱ���ǵ���
		if (daydiff == 0) {
			strdate = new SimpleDateFormat("HH:mm").format(new Date(stamp));
		} else if (daydiff == 1) {
			strdate = "���� "
					+ new SimpleDateFormat("HH:mm").format(new Date(stamp));
		} else {
			strdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(
					stamp));
		}
		return strdate;
	}

	// ���Է���
	// ��������Щ����,���������ݿ�,�п��ܲ���Ӧ
	public static void getConversationColumn(Context context) {
		// ���ŻỰ������
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver
				.query(CONVERSATION_URI, null, null, null, null);
		if (cursor.moveToNext()) {
			int count = cursor.getColumnCount();
			for (int i = 0; i < count; i++) {
				LogUtilS.i("Tag:",
						cursor.getColumnName(i) + "-->" + cursor.getString(i));
			}
		}
		cursor.close();
	}

	// ���Է���
	// ��������Щ����,���������ݿ�,�п��ܲ���Ӧ
	public static void getSMSColumn(Context context) {
		// ��������
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(SMS_URI, null, null, null, null);
		if (cursor.moveToNext()) {
			int count = cursor.getColumnCount();
			for (int i = 0; i < count; i++) {
				LogUtilS.i("Tag:",
						cursor.getColumnName(i) + "-->" + cursor.getString(i));
			}
		}
		cursor.close();
	}

	// ɾ���Ự����
	public static void deleteConveration(Context context, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		String where = "thread_id=?";
		resolver.delete(CONVERSATION_URI, where,
				new String[] { String.valueOf(thread_id) });
	}

	// ɾ���������[chatActivity]
	public static void deleteSms(Context context, int _id) {
		ContentResolver resolver = context.getContentResolver();
		String where = "_id = ?";
		resolver.delete(SMS_URI, where, new String[] { String.valueOf(_id) });
	}

	// �޸Ķ���������Զ���δ��״̬
	public static void updateConverationType(Context context, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		// ���¶����ռ������ݿ�
		ContentValues values = new ContentValues();
		values.put("read", 1);// ��δ����Ϊ�Զ�,Ҫ�õ�����
		String where = "thread_id = ?";// �޸ĵ�����
		String[] selectionArgs = new String[] { String.valueOf(thread_id) };// ������ֵ
		resolver.update(SMS_INBOX_URI, values, where, selectionArgs);
	}

	/**
	 * �Լ������˷�, �����Ų��뵽���ݿ���,
	 * 
	 * @param context
	 * @param phone
	 *            �绰����
	 * @param body
	 *            ����
	 */
	public static void insertSms(Context context, String phone, String body) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("date", new Date().getTime());// ����ʱ��
		values.put("read", 1);// �Ķ�״̬
		values.put("type", 2);// 1��2��
		values.put("address", phone);// ���͵ĵ绰����
		values.put("body", body);// ���͵�����
		resolver.insert(SMS_SEND_URI, values);
		LogUtilS.i(">>>>>>>>>>>>>>>>", "insertSms");
	}

	// �ڹ㲥�еõ�����
	public static Sms getSmsByReceiver(Bundle bundle) {
		Object[] pdus = (Object[]) bundle.get("pdus");
		// Android�豸���յ���SMS����pdu��ʽ��(protocol description unit)��
		// ���Դ�intent��ȡ����ʱ�ͻ�����pdus��
		SmsMessage[] messages = new SmsMessage[pdus.length];// ע���
		for (int i = 0; i < pdus.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		}
		StringBuilder builder = new StringBuilder();// ��Ч,����ȫ
		long date = 0;
		String address = "";
		for (int i = 0; i < messages.length; i++) {
			if (i == 0) {// �绰�����ʱ����һ����,����ȡһ�ξͿ�����
				// ��ö��ŵĵ绰����
				// address = messages[i].getOriginatingAddress();
				address = messages[i].getDisplayOriginatingAddress();
				// ��ö��ŵķ���ʱ��
				date = messages[i].getTimestampMillis();
			}
			builder.append(messages[i].getDisplayMessageBody());
		}
		// LogUtilS.i("Tag:address", address);
		// LogUtilS.i("Tag:date", "" + date);
		// LogUtilS.i("Tag:body", builder.toString());
		Sms sms = new Sms();
		sms.setAddress(address);
		sms.setDate(date);
		sms.setBody(builder.toString());
		sms.setType(1);// �յ��Ķ���
		return sms;
	}

	// �����˷����Ķ��ű������ռ�����,chatActivity
	public static void saveReciveMessage(Context context, Sms sms, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();

		values.put("thread_id", thread_id);
		values.put("body", sms.getBody());
		values.put("address", sms.getAddress());
		values.put("date", sms.getDate());
		values.put("read", 1);// ����ʾС�̵�
		values.put("type", sms.getType());

		resolver.insert(SMS_INBOX_URI, values);
	}

	// �����˷����Ķ��ű������ռ�����,�Ự��
	public static void save(Context context, Sms sms, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();

		// 2016��10��12�� 09:29:16
		// �����:�����thread_id ��ȫ��ָ����0,Ҳû���κ�����,Ҳû���κδ���
		values.put("thread_id", thread_id);
		values.put("body", sms.getBody());
		values.put("address", sms.getAddress());
		values.put("date", sms.getDate());
		values.put("read", 0);// ��ʾС�̵�
		values.put("type", sms.getType());

		resolver.insert(SMS_INBOX_URI, values);
	}

	public static final String SEND_SMS = "com.tarena.fgr.youlu.SEND_SMS";

	/**
	 * ʵ�ַ��Ͷ��Ź��� </br>�����ķ���,���������ݿ�д����ż�¼
	 * 
	 * @param context
	 * @param body
	 *            ��������
	 * @param phone
	 *            ���ŵ绰��
	 */
	public static void sendSms(Context context, String body, String phone) {
		// ֱ��ʹ�÷��Ͷ��ŵ�api����������ϵͳ�Ķ���Ӧ��
		SmsManager smsManager = SmsManager.getDefault();
		// �ѳ����Žس��������̶���
		ArrayList<String> sms = smsManager.divideMessage(body);
		for (String string : sms) {
			Intent intent = new Intent(SEND_SMS);
			intent.putExtra("body", body);
			intent.putExtra("phone", phone);
			// ������ͨ���㲥���͵Ķ���
			// ���Ի�ִʹ��getBroadcast,������getService,getActivity
			PendingIntent sentIntent = PendingIntent.getBroadcast(context, 100,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// arg0:���շ����ֻ�,Ŀ�����
			// arg1:�������ĺ��룬null��ʾʹ��Ĭ��,(���ͷ����ֻ�����???)
			// arg2:��������
			// arg3:�Լ������Ƿ�ɹ��Ļ�ִ
			// arg4:�Է������Ƿ�ɹ��Ļ�ִ
			smsManager.sendTextMessage(phone, null, string, sentIntent, null);
		}
	}

	public static Typeface Fonts;

	// �Զ��������
	public static void stetFontType(Context context, TextView textView) {
		if (Fonts == null) {
			AssetManager manager = context.getAssets();
			String path = "fonts/customfont.ttf";// �Զ��������
			Fonts = Typeface.createFromAsset(manager, path);
		}
		// ������Ӧ�õ��ؼ���
		textView.setTypeface(Fonts);
	}

}
