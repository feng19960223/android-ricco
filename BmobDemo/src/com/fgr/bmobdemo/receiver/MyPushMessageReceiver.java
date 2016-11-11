package com.fgr.bmobdemo.receiver;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
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
						Intent intent2 = new Intent(
								"com.fgr.bmobdemo.ACTION_NEW_POST");
						context.sendBroadcast(intent2);
					}
				}
			} catch (Exception e) {
			}
		}
	}
}