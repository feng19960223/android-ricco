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
				LogUtil.d("TAG", "MyReceiver收到的内容：" + message);
				// {"tag":"offline"}
				JSONObject jsonobj = new JSONObject(message);
				String tag = jsonobj.getString("tag");
				if ("offline".equals(tag)) {
					// 当前设备上处于登录状态的用户在其它设备上登录了
					// 应该让当前设备上处于登录状态的用户下线
					if (list.size() > 0) {
						for (EventListener listener : list) {
							// MyReceiver通过调用订阅者的onOffline方法
							// 告诉订阅者收到了下线通知，请订阅者来处理
							listener.onOffline();
						}
					} else {
						// 如果没有订阅者，则MyReceiver自行处理下线通知
						// 使当前设备登录用户下线
						MyApp.logout();
					}

				}

				if ("add".equals(tag)) {
					// 判断添加好友申请是否是发给当前登录用户的
					String tid = BmobJsonUtil.getString(
							new JSONObject(message), "tId");
					if (tid != null) {
						handleAddFriend(context, message, tid);
					}
				}

				if ("agree".equals(tag)) {
					// 对方通过了我之前发送的好友申请
					String tid = BmobJsonUtil.getString(
							new JSONObject(message), "tId");
					if (tid != null) {
						addFriend(context, message, tid);
					}
				}

				if ("".equals(tag)) {
					// 收到了对方发送的一个聊天消息
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

		// 将Json字符串格式的聊天消息保存到本地数据库中

		// 1. 根据收到的json字符串创建JSonObject对象，并判断消息的发送方是否是当前登录用户的好友
		// 2. 根据JsonObject对象创建BmobMsg对象，BmobMsg对象的isreaded属性值为2，status属性值为3
		// 3. 根据当前设备是否有登录用户，以及如果有登录用户是否是消息的接收方来做出不同的处理
		// 只有在当前登录用户是消息接收方的情况下，才会保存BmobMsg对象后调用咱们传入的监听器中的相应方法
		// 其余情况仅保存BmobMsg对象
		// 4. 如果当前登录用户是消息的接收方，在保存前还会判断下，该消息是否曾经保存过
		// 5. 保存BmobMsg对象时，是向消息的接收方所对应的数据库的chat表和recent表中保存
		// 6. BmobMsg对象保存完毕，向消息的放送放发送一个回执，tag属性值为readed
		// 7. 回执发送完毕后，会更新接收到的聊天消息在BmobMsg数据表中保存的数据记录的isreaded字段值从0--->1
		// 8. 调用咱们自己写的监听器
		// 注意：只有当前设备登录用户与消息接收方是同一人时，自己写的监听器才会被调用
		// 调用时间是在BmobMsg对象保存完毕之后执行，它有可能在第6,7步执行之前就被执行

		BmobChatManager.getInstance(context).createReceiveMsg(message,
				new OnReceiveListener() {

					@Override
					public void onSuccess(BmobMsg msg) {
						// 保存聊天消息成功，参数就是根据Json字符串所获得的BmobMsg对象
						// 如果聊天消息是发送给当前登录用户的话
						// 则通知当前登录用户
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
										ticker = msg.getBelongUsername() + "说："
												+ msg.getContent();
										break;
									case 2:
										ticker = msg.getBelongUsername()
												+ "发送了一个：[图片]";
										break;
									case 3:
										ticker = msg.getBelongUsername()
												+ "发送了一个： [位置]";
										break;
									case 4:
										ticker = msg.getBelongUsername()
												+ "发送了一个：[语音]";
										break;
									default:
										throw new RuntimeException("错误的消息类型");
									}
									BmobNotifyManager.getInstance(context)
											.showNotify(sputil.isAllowSound(),
													sputil.isAllowVibrate(),
													R.drawable.ic_notification,
													ticker, "聊天内容", ticker,
													MainActivity.class);
								}
							}
						}
					}

					@Override
					public void onFailure(int code, String arg1) {
						switch (code) {
						case 1002:
							LogUtil.d("一秒钟之内收到了来自同一用户的多条聊天信息");
							break;

						default:
							LogUtil.d("保存聊天消失时出现错误，错误编码：" + code + "," + arg1);
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
			// 1. 根据targetName在服务器_user表中查找对应的用户
			// 2. 如果确实存在该用户，则当前登录用户在_user表中所对应的数据记录的contacts字段值，完成两人好友关系建立
			// 3. 在当前登录用户所对应本地数据库frineds表中添加好友信息
			// 4. 调用自己的监听器的回调方法

			BmobUserManager.getInstance(context).addContactAfterAgree(
					targetName, new FindListener<BmobChatUser>() {

						@Override
						public void onSuccess(List<BmobChatUser> arg0) {
							// 判断该回执的接收人是不是当前设备的登录用户
							String uid = BmobUserManager.getInstance(context)
									.getCurrentUserObjectId();
							if (tid.equals(uid)) {
								// 如果是，则要通知当前登录用户
								// 订阅者对收到了一个同意好友添加的回执事情并不感兴趣(并没有订阅这个事情)
								// 所以要通知当前登录用户只能由MyReceiver发送通知
								if (sputil.isAllowNotification()) {
									BmobNotifyManager.getInstance(context)
											.showNotify(sputil.isAllowSound(),
													sputil.isAllowVibrate(),
													R.drawable.ic_notification,
													targetName + "同意了您添加好友的申请",
													"同意添加好友",
													targetName + "同意了您添加好友的申请",
													MainActivity.class);
								}

								// 1. 根据收到的json字符串创建了一个BmobMsg对象
								// 2. 将1.所创建出来的BmobMsg对象，作为两个人之间的一条聊天记录，保存到
								// 本地数据库的chat表中
								// 3.
								// 根据1.所创建出来的BmobMsg对象，提取部分属性构建了一个BmobRecent对象
								// 4. 将3.所创建的BmobRecent对象保存到了本地数据库的Recent数据表中
								// 5. 要更新回执信息在BmobMsg数据表中isReaded字段值(从0更新为1)
								BmobMsg.createAndSaveRecentAfterAgree(context,
										message);
								// 发送一个通知,及时刷新好友列表(FriendFragment--ListView)
								// 以及刷新会话列表(MessageFragment--ListView)
								// 刷新MainActivity的总未读消息个数
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

	// 添加好友申请
	private void handleAddFriend(Context context, String message, String tid) {
		// 将收到的Json字符串格式的添加好友申请保存本地数据库的数据表中
		// step1. 根据收到的Json字符串创建BmobInvitation实体类对象，该对象一个重要的属性值status为2。
		// status为2意味着收到了一条添加好友申请但尚未处理
		// 该对象最终也是saveReceiveInvite方法的返回值
		// step2. 将step1创建的实体类对象的相关内容写入到tid所对应的数据库的tab_new_contacts数据表中
		// 该数据记录的status字段的值也是为2
		// step3. 将收到的这条好友申请在服务器BmobMsg表中所对应数据记录的isReaded字段值从0更新为1
		// 意味着该添加好友申请已经收到了

		BmobInvitation bmobInvitation = BmobChatManager.getInstance(context)
				.saveReceiveInvite(message, tid);

		// 该好友申请是发送给tid，那tid是当前设备的登录用户时
		// 告知当前设备上的登录用户
		String uid = BmobUserManager.getInstance(context)
				.getCurrentUserObjectId();
		if (tid.equals(uid)) {

			if (list.size() > 0) {
				// 如果有订阅者
				// 将收到“添加好友申请”的事情告诉订阅者
				// 再由订阅者告诉当前设备登录用户
				for (EventListener listener : list) {
					listener.onAddUser(bmobInvitation);
				}
			} else {
				// 如果没有订阅者
				// MyReceiver通过发送通知的方式
				// 告诉当前设备登录用户
				if (sputil.isAllowNotification()) {
					BmobNotifyManager.getInstance(context).showNotify(
							sputil.isAllowSound(), sputil.isAllowVibrate(),
							R.drawable.ic_notification,
							bmobInvitation.getFromname() + "请求添加您为好友", "添加好友",
							bmobInvitation.getFromname() + "请求添加您为好友",
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
