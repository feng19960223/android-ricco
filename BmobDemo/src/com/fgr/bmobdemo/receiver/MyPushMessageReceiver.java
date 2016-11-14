package com.fgr.bmobdemo.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.fgr.bmobdemo.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

/**
 * �۲���ģʽ
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	private static List<EventListener> list = new ArrayList<EventListener>();

	public static void regist(EventListener listener) {
		list.add(listener);
	}

	public static void unregist(EventListener listener) {
		list.remove(listener);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// ��ȡ���������͵�����
			String message = intent.getStringExtra("msg");
			Log.d("bmob", "�ͻ����յ��������ݣ�" + intent.getStringExtra("msg"));// json
			try {

				JSONObject jsonObject = new JSONObject(message);
				if (jsonObject.has("tag")) {
					String tag = jsonObject.getString("tag");
					if ("new".equals(tag)) {
						// ���˷�����������,Ҫ��֪�û�
						// ��֪��ʽ,��ShowActivity�����Ͻǳ���һ�����
						// ͬʱ������ʾ��
						// ����һ
						// Intent intent2 = new Intent(
						// "com.fgr.bmobdemo.ACTION_NEW_POST");
						// context.sendBroadcast(intent2);
						// ������
						if (list.size() > 0) {// ���ü�����ÿһ���ӿ�
							// ֪ͨÿһ���۲���
							// ���۲�����鷢����
							for (EventListener eventListener : list) {
								// ͨ�����ù۲��ߵķ��������߹۲���
								eventListener.onNewPost();
							}
						}
					}
					if ("one".equals(tag)) {// @
						if (list.size() > 0) {
							for (EventListener eventListener : list) {
								eventListener.onAtone();
							}
						} else {// û�ж�����
							NotificationManager manager = (NotificationManager) context
									.getSystemService(Context.NOTIFICATION_SERVICE);
							Notification.Builder builder = new Notification.Builder(
									context);
							builder.setSmallIcon(R.drawable.ic_launcher);
							builder.setTicker("���˸ոշ���@����");
							builder.setContentTitle("@֪ͨ");
							builder.setContentText("���˸ոշ���@����");
							builder.setContentInfo("֪ͨ");
							builder.setAutoCancel(true);// �������ʧ
							Notification notification = builder.build();
							manager.notify(1, notification);
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// ÿһ���ӿڵ�ʵ�������һ���۲���
	public interface EventListener {
		void onNewPost();

		void onAtone();
	}
}