package com.fgr.miaoxin.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.util.SPUtil;

public class MyPushMessageReceiver extends BroadcastReceiver {
	private SPUtil sputil = new SPUtil(MyApp.context, BmobUserManager
			.getInstance(MyApp.context).getCurrentUserObjectId());

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
					if ("offline".equals(tag)) {
						// ���tag��ֵΪoffline
						// ��ǰ�豸�ĵ�¼�û����ʺ�,�Ѿ�������һ̨�豸�ϵ�¼
						// �õ�ǰ�豸���û�����
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
					if ("add".equals(tag)) {
						// �յ���һ����Ӻ��ѵ�����
						// �ж���Ӻ��������Ƿ��Ƿ�����ǰ��¼�û���
						String tid = BmobJsonUtil.getString(jsonObject, "tId");
						if (tid != null) {
							handleAddFriendInvitation(context, message, tid);
						}
					}
					if ("agree".equals(tag)) {
						// �յ�һ��ͬ����ѵĻ�ִ
						String tid = BmobJsonUtil.getString(jsonObject, "tId");
						if (tid != null) {
							addFriend(context, message, tid);
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private void addFriend(final Context context, final String message,
			final String tid) {

		try {
			final String targetName = BmobJsonUtil.getString(new JSONObject(
					message), "fu");
			// 1. ����targetName�ڷ�����_user���в��Ҷ�Ӧ���û�
			// 2. ���ȷʵ���ڸ��û�����ǰ��¼�û���_user��������Ӧ�����ݼ�¼��contacts�ֶ�ֵ��������˺��ѹ�ϵ����
			// 3. �ڵ�ǰ��¼�û�����Ӧ�������ݿ�frineds������Ӻ�����Ϣ
			// 4. �����Լ��ļ������Ļص�����

			BmobUserManager.getInstance(context).addContactAfterAgree(
					targetName, new FindListener<BmobChatUser>() {

						@Override
						public void onSuccess(List<BmobChatUser> arg0) {
							// �жϸû�ִ�Ľ������ǲ��ǵ�ǰ�豸�ĵ�¼�û�
							String uid = BmobUserManager.getInstance(context)
									.getCurrentUserObjectId();
							if (tid.equals(uid)) {
								// ����ǣ���Ҫ֪ͨ��ǰ��¼�û�
								// �����߶��յ���һ��ͬ�������ӵĻ�ִ���鲢������Ȥ(��û�ж����������)
								// ����Ҫ֪ͨ��ǰ��¼�û�ֻ����MyReceiver����֪ͨ
								if (sputil.isAllowNotification()) {
									BmobNotifyManager.getInstance(context)
											.showNotify(sputil.isAllowSound(),
													sputil.isAllowVibrate(),
													R.drawable.ic_notification,
													targetName + "ͬ��������Ӻ��ѵ�����",
													"ͬ����Ӻ���",
													targetName + "ͬ��������Ӻ��ѵ�����",
													MainActivity.class);
								}

								// 1. �����յ���json�ַ���������һ��BmobMsg����
								// 2. ��1.������������BmobMsg������Ϊ������֮���һ�������¼�����浽
								// �������ݿ��chat����
								// 3.
								// ����1.������������BmobMsg������ȡ�������Թ�����һ��BmobRecent����
								// 4. ��3.��������BmobRecent���󱣴浽�˱������ݿ��Recent���ݱ���
								// 5. Ҫ���»�ִ��Ϣ��BmobMsg���ݱ���isReaded�ֶ�ֵ(��0����Ϊ1)
								BmobMsg.createAndSaveRecentAfterAgree(context,
										message);
							}
						}

						@Override
						public void onError(int arg0, String arg1) {
							// TODO Auto-generated method stub

						}
					});
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	// ��Ӻ�������
	private void handleAddFriendInvitation(Context context, String message,
			String tid) {
		// ���յ���Json�ַ�����ʽ����Ӻ������뱣�汾�����ݿ�����ݱ���
		// step1. �����յ���Json�ַ�������BmobInvitationʵ������󣬸ö���һ����Ҫ������ֵstatusΪ2��
		// statusΪ2��ζ���յ���һ����Ӻ������뵫��δ����
		// �ö�������Ҳ��saveReceiveInvite�����ķ���ֵ
		// step2. ��step1������ʵ���������������д�뵽tid����Ӧ�����ݿ��tab_new_contacts���ݱ���
		// �����ݼ�¼��status�ֶε�ֵҲ��Ϊ2
		// step3. ���յ����������������ڷ�����BmobMsg��������Ӧ���ݼ�¼��isReaded�ֶ�ֵ��0����Ϊ1
		// ��ζ�Ÿ���Ӻ��������Ѿ��յ���

		BmobInvitation bmobInvitation = BmobChatManager.getInstance(context)
				.saveReceiveInvite(message, tid);

		// �ú��������Ƿ��͸�tid����tid�ǵ�ǰ�豸�ĵ�¼�û�ʱ
		// ��֪��ǰ�豸�ϵĵ�¼�û�
		String uid = BmobUserManager.getInstance(context)
				.getCurrentUserObjectId();
		if (tid.equals(uid)) {

			if (list.size() > 0) {
				// ����ж�����
				// ���յ�����Ӻ������롱��������߶�����
				// ���ɶ����߸��ߵ�ǰ�豸��¼�û�
				for (EventListener listener : list) {
					listener.onAddUser(bmobInvitation);
				}
			} else {
				// ���û�ж�����
				// MyReceiverͨ������֪ͨ�ķ�ʽ
				// ���ߵ�ǰ�豸��¼�û�
				if (sputil.isAllowNotification()) {
					BmobNotifyManager.getInstance(context).showNotify(
							sputil.isAllowSound(), sputil.isAllowVibrate(),
							R.drawable.ic_notification,
							bmobInvitation.getFromname() + "���������Ϊ����", "��Ӻ���",
							bmobInvitation.getFromname() + "���������Ϊ����",
							MainActivity.class);
				}
			}
		}

	}

	private static List<EventListener> list = new ArrayList<EventListener>();

	public static void regist(EventListener listener) {
		list.add(listener);
	}

	public static void unRegist(EventListener listener) {
		list.remove(listener);
	}
}
