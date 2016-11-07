package com.tarena.fgr.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.entity.BlackPhone;
import com.tarena.fgr.entity.BlackSMS;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;
import com.tarena.fgr.youlu.ChatActivity;

/**
 * ����������
 * 
 * @author �����
 * 
 */
public class BlackNumberService extends Service {
	private MyReceiver receiver = null;
	private DBUtil dbUtil = null;
	private TelephonyManager manager = null;// �绰�����ϵͳ����
	private PhoneStateListener phoneStateListener = null;// �绰״̬������
	private ITelephony iTelephony = null;
	// �����绰��ϵͳ�㲥
	public static final String OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
	// ����绰��ϵͳ�㲥
	public static final String INCOMING_CALL = "android.intent.action.PHONE_STATE";

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtilS.i("�������绰����ķ�������");

		receiver = new MyReceiver();
		registerReceiver();
		dbUtil = new DBUtil(this);
		// ��õ绰����
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// ͨ�����䷽�����÷����е�һ��˽�еķ������ITelephony����
		try {
			Method method = manager.getClass().getDeclaredMethod(
					"getITelephony", null);
			method.setAccessible(true);// ���ø÷������Ա���ӵ���
			// ���ø÷������ITelephony��ʵ��
			iTelephony = (ITelephony) method.invoke(manager, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// ��װһ���ҵ绰�ķ���
		endBlockCall();
	}

	private void registerReceiver() {
		// �ѹ㲥������ע����÷���
		IntentFilter filter = new IntentFilter();
		// ������ϵͳ�������յ����ŵĹ㲥
		// �ù㲥��һ������㲥
		filter.addAction(ChatActivity.RECEIVE_SMS);
		// ���Ĳ����绰ʱϵͳ�����Ĺ㲥
		filter.addAction(OUTGOING_CALL);
		// Ҫ���㲥�����������ȼ����õ��㹻��
		// ʹ�ĸ÷����ܹ�������ϵͳ�Ķ��Ź����Ӧ�ý���
		filter.setPriority(1001);// �ȻỰ�ĸ�һ��,���������յ�
		registerReceiver(receiver, filter);
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ����ж��ŷ���,�÷����ᱻ����
			String action = intent.getAction();
			if (action.equals(ChatActivity.RECEIVE_SMS)) {
				// �յ�����
				// ��ö��ŵĵ绰����
				Bundle bundle = intent.getExtras();
				Sms sms = SMSManager.getSmsByReceiver(bundle);
				String smsPhone = sms.getAddress();
				// �жϸõ绰�Ƿ��Ǻ������绰
				if (dbUtil.isBlackNumber(smsPhone)) {
					// ����Ǻ������绰���ֹ�㲥����һ������������
					abortBroadcast();
					LogUtilS.i("�ö����Ǻ������绰������");
					// �������ŵ�����,�����ݱ����ڵ�ǰӦ�õı���
					BlackSMS blackSMS = new BlackSMS();
					blackSMS.setNumber(smsPhone);
					blackSMS.setBody(sms.getBody());
					blackSMS.setDate(sms.getDate());
					dbUtil.insertSMS(blackSMS);
				}
			}
			// �Լ�Ҫ�����绰
			else if (action.equals(OUTGOING_CALL)) {
				LogUtilS.i("Ҫ�����绰");
				// ��ò����ĵ绰�ĵ绰����
				String callPhone = getResultData();
				// �жϸõ绰�Ƿ��Ǻ������绰
				if (dbUtil.isBlackNumber(callPhone)) {
					LogUtilS.i("�Ǻ������绰");
					setResultData(null);
					BlackPhone blackPhone = new BlackPhone();
					blackPhone.setNumber(callPhone);
					blackPhone.setDate(new Date().getTime());
					blackPhone.setType(2);
					dbUtil.insertPhone(blackPhone);
				}
			}
			// �е绰����,�Ҷϵ绰�Ƚ�˽��
		}
	}

	private void endBlockCall() {
		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state,
					final String incomingNumber) {
				super.onCallStateChanged(state, incomingNumber);
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE: // ����״̬

					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: //
					break;
				case TelephonyManager.CALL_STATE_RINGING: // ����״̬
					// �е绰�����
					LogUtilS.i("�е绰������");
					if (dbUtil.isBlackNumber(incomingNumber)) {
						// �Ǻ������绰
						LogUtilS.i("һ���������绰������");
						try {
							// �Ҷϵ绰
							iTelephony.endCall();
							// �Ѵ�����ĵ绰����Ϣ��ı���
							BlackPhone blackPhone = new BlackPhone();
							blackPhone.setNumber(incomingNumber);
							blackPhone.setDate(new Date().getTime());
							blackPhone.setType(2);
							dbUtil.insertPhone(blackPhone);
						} catch (RemoteException e) {
							e.printStackTrace();
						}

						// ��ǰ���߳�����һ��,����ɾ������,����û��д������
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// ɾ��ͨ����¼��
								CalllogManager
										.deleteCalllog(BlackNumberService.this,
												incomingNumber);
							}
						}, 5000);
					}
					break;
				default:
					break;
				}

			}
		};
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	// �ͷ���Դ
	public void onDestroy() {
		LogUtilS.i("������ֹͣ");
		// ����ֹͣʱע���㲥������
		super.onDestroy();
		unregisterReceiver(receiver);
		manager.listen(null, PhoneStateListener.LISTEN_CALL_STATE);
		manager = null;
	}

}
