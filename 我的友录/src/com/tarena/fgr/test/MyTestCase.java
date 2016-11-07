package com.tarena.fgr.test;

import java.util.List;

import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.db.MyDBHelper;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

/**
 * ��Ԫ���԰���
 * 
 * @author ����� 2016��9��30�� 09:59:23
 * 
 */
public class MyTestCase extends AndroidTestCase {
	// �Ҽ���������.Android JUnit test
	// ������ϵ������
	public void contactTest() {
		List<Contact> contacts = ContactManager.getContacts(getContext());
		for (Contact contact : contacts) {
			LogUtilS.i("TAG", "" + contact);
		}
	}

	// ����ͨ����¼����
	public void calllogTest() {
		List<Calllog> calllogs = CalllogManager.getCalllogs(getContext());
		for (Calllog calllog : calllogs) {
			LogUtilS.i("TAG", "" + calllog);
		}
	}

	// ���ԻỰ�����ṩ���ṩ�������ֶε�����
	public void conversationColumnTest() {
		SMSManager.getConversationColumn(getContext());
	}

	// ���Զ��ŻỰ��¼����
	public void conversationTest() {
		List<Conversation> list = SMSManager.getConversations(getContext());
		for (Conversation conversation : list) {
			LogUtilS.i("TAG", "" + conversation);
		}
	}

	// ���Զ��������ṩ���ṩ�������ֶε�����
	public void smsColumnTest() {
		SMSManager.getSMSColumn(getContext());
	}

	// ���Զ��������¼������
	public void smsTest() {
		// ģ�⴫��һ������,ģ���������ݿ��7��
		List<Sms> list = SMSManager.getSMSes(getContext(), 7);
		for (Sms sms : list) {
			LogUtilS.i("TAG", "" + sms);
		}
	}

	// ���ݿⴴ������
	public void DBonCreateTest() {
		MyDBHelper helper = MyDBHelper.getInstance(getContext());
		SQLiteDatabase db = helper.getWritableDatabase();
		db.close();
	}

	// ���ݿ⹦�ܲ���
	public void DBUtilTest() {
		DBUtil dbUtil = new DBUtil(getContext());
		dbUtil.insertBlackNumber("18812341234");
		boolean isHave = dbUtil.isBlackNumber("18812341234");
		LogUtilS.i("" + isHave);
	}
}
