package com.fgr.miaoxin.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fgr.miaoxin.app.MyApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobInstallation;

public class MyPushMessageReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

			String message = intent
					.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			Log.d("TAG", BmobInstallation.getInstallationId(context) + "�յ������ݣ�"
					+ message);
			try {
				JSONObject jsonObject = new JSONObject(message);
				if (jsonObject.has("tag")) {
					String tag = jsonObject.getString("tag");
					// ���tag��ֵΪoffline
					// ��ǰ�豸�ĵ�¼�û����ʺ�,�Ѿ�������һ̨�豸�ϵ�¼
					// �õ�ǰ�豸���û�����
					if ("offline".equals(tag)) {
						// ������
						if (list.size() > 0) {
							// MyPushMessageReceiverͨ�����ö������offline������֪������
							// ���ж�������崦��,�õ�ǰ��¼�û�����
							for (EventListener eventListener : list) {
								eventListener.onOffline();
							}
						} else {
							// ���д������û�����
							MyApp.logout();
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<EventListener> list = new ArrayList<EventListener>();

	public void regist(EventListener listener) {
		list.add(listener);
	}

	public void unRegist(EventListener listener) {
		list.remove(listener);
	}
}
