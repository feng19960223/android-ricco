package com.fgr.miaoxin.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnReceiveListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.util.LogUtil;
import com.fgr.miaoxin.util.SPUtil;

public class MyPushMessageReceiver extends BroadcastReceiver {
	private SPUtil sputil = new SPUtil(MyApp.context, BmobUserManager
			.getInstance(MyApp.context).getCurrentUserObjectId());

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (PushConstants.ACTION_MESSAGE.equals(action)) {
			try {
				String message = intent
						.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
				LogUtil.d("TAG", "MyReceiver�յ������ݣ�" + message);
				// {"tag":"offline"}
				JSONObject jsonobj = new JSONObject(message);
				String tag = jsonobj.getString("tag");
				if ("offline".equals(tag)) {
					// ��ǰ�豸�ϴ��ڵ�¼״̬���û��������豸�ϵ�¼��
					// Ӧ���õ�ǰ�豸�ϴ��ڵ�¼״̬���û�����
					if (list.size() > 0) {
						for (EventListener listener : list) {
							// MyReceiverͨ�����ö����ߵ�onOffline����
							// ���߶������յ�������֪ͨ���붩����������
							listener.onOffline();
						}
					} else {
						// ���û�ж����ߣ���MyReceiver���д�������֪ͨ
						// ʹ��ǰ�豸��¼�û�����
						MyApp.logout();
					}

				}

				if ("add".equals(tag)) {
					// �ж���Ӻ��������Ƿ��Ƿ�����ǰ��¼�û���
					String tid = BmobJsonUtil.getString(
							new JSONObject(message), "tId");
					if (tid != null) {
						handleAddFriend(context, message, tid);
					}
				}

				if ("agree".equals(tag)) {
					// �Է�ͨ������֮ǰ���͵ĺ�������
					String tid = BmobJsonUtil.getString(
							new JSONObject(message), "tId");
					if (tid != null) {
						addFriend(context, message, tid);
					}
				}

				if ("".equals(tag)) {
					// �յ��˶Է����͵�һ��������Ϣ
					String tid = BmobJsonUtil.getString(
							new JSONObject(message), "tId");
					if (tid != null) {
						saveMsg(context, message, tid);
					}

				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private void saveMsg(final Context context, String message, final String tid) {

		// ��Json�ַ�����ʽ��������Ϣ���浽�������ݿ���

		// 1. �����յ���json�ַ�������JSonObject���󣬲��ж���Ϣ�ķ��ͷ��Ƿ��ǵ�ǰ��¼�û��ĺ���
		// 2. ����JsonObject���󴴽�BmobMsg����BmobMsg�����isreaded����ֵΪ2��status����ֵΪ3
		// 3. ���ݵ�ǰ�豸�Ƿ��е�¼�û����Լ�����е�¼�û��Ƿ�����Ϣ�Ľ��շ���������ͬ�Ĵ���
		// ֻ���ڵ�ǰ��¼�û�����Ϣ���շ�������£��Żᱣ��BmobMsg�����������Ǵ���ļ������е���Ӧ����
		// �������������BmobMsg����
		// 4. �����ǰ��¼�û�����Ϣ�Ľ��շ����ڱ���ǰ�����ж��£�����Ϣ�Ƿ����������
		// 5. ����BmobMsg����ʱ��������Ϣ�Ľ��շ�����Ӧ�����ݿ��chat���recent���б���
		// 6. BmobMsg���󱣴���ϣ�����Ϣ�ķ��ͷŷ���һ����ִ��tag����ֵΪreaded
		// 7. ��ִ������Ϻ󣬻���½��յ���������Ϣ��BmobMsg���ݱ��б�������ݼ�¼��isreaded�ֶ�ֵ��0--->1
		// 8. ���������Լ�д�ļ�����
		// ע�⣺ֻ�е�ǰ�豸��¼�û�����Ϣ���շ���ͬһ��ʱ���Լ�д�ļ������Żᱻ����
		// ����ʱ������BmobMsg���󱣴����֮��ִ�У����п����ڵ�6,7��ִ��֮ǰ�ͱ�ִ��

		BmobChatManager.getInstance(context).createReceiveMsg(message,
				new OnReceiveListener() {

					@Override
					public void onSuccess(BmobMsg msg) {
						// ����������Ϣ�ɹ����������Ǹ���Json�ַ�������õ�BmobMsg����
						// ���������Ϣ�Ƿ��͸���ǰ��¼�û��Ļ�
						// ��֪ͨ��ǰ��¼�û�
						String uid = BmobUserManager.getInstance(context)
								.getCurrentUserObjectId();
						if (tid.equals(uid)) {
							if (list.size() > 0) {
								for (EventListener listener : list) {
									listener.onMessage(msg);
								}
							} else {
								if (sputil.isAllowNotification()) {
									String ticker = "";
									switch (msg.getMsgType()) {
									case 1:
										ticker = msg.getBelongUsername() + "˵��"
												+ msg.getContent();
										break;
									case 2:
										ticker = msg.getBelongUsername()
												+ "������һ����[ͼƬ]";
										break;
									case 3:
										ticker = msg.getBelongUsername()
												+ "������һ���� [λ��]";
										break;
									case 4:
										ticker = msg.getBelongUsername()
												+ "������һ����[����]";
										break;
									default:
										throw new RuntimeException("�������Ϣ����");
									}
									BmobNotifyManager.getInstance(context)
											.showNotify(sputil.isAllowSound(),
													sputil.isAllowVibrate(),
													R.drawable.ic_notification,
													ticker, "��������", ticker,
													MainActivity.class);
								}
							}
						}
					}

					@Override
					public void onFailure(int code, String arg1) {
						switch (code) {
						case 1002:
							LogUtil.d("һ����֮���յ�������ͬһ�û��Ķ���������Ϣ");
							break;

						default:
							LogUtil.d("����������ʧʱ���ִ��󣬴�����룺" + code + "," + arg1);
							break;
						}
					}
				});

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
								// ����һ��֪ͨ,��ʱˢ�º����б�(FriendFragment--ListView)
								// �Լ�ˢ�»Ự�б�(MessageFragment--ListView)
								// ˢ��MainActivity����δ����Ϣ����
								Intent intent2 = new Intent(Constant.ADD_FRIEND);
								context.sendBroadcast(intent2);
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
	private void handleAddFriend(Context context, String message, String tid) {
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
