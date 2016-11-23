package com.fgr.miaoxin.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;
import cn.bmob.v3.listener.PushListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.ChatAdapter;
import com.fgr.miaoxin.adapter.EmoGridViewAdapter;
import com.fgr.miaoxin.adapter.EmoPagerAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.receiver.MyPushMessageReceiver;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.NetUtil;
import com.viewpagerindicator.CirclePageIndicator;

public class ChatActivity extends BaseActivity implements EventListener {

	BmobChatUser targetUser;
	String targetId;// targetUser��objectId
	String targetName;// targetUser��username
	String myId;// ��ǰ��¼�û���objectId

	// ���ı�����������ص�����

	@Bind(R.id.lv_chat_listview)
	ListView listView;
	List<BmobMsg> messages;
	ChatAdapter adapter;

	@Bind(R.id.et_chat_content)
	EditText etContent;// �����ı�����
	@Bind(R.id.btn_chat_add)
	Button btnAdd;// �ӺŰ�ť
	@Bind(R.id.btn_chat_send)
	Button btnSend;// ���Ͱ�ť

	// �������ص�����
	@Bind(R.id.ll_chat_morecontainer)
	LinearLayout moreContainer;

	RelativeLayout emoLayout;// ʾ��ͼ��������ɫ�Ŀ�
	ViewPager emoViewPager;
	CirclePageIndicator emoCpi;
	// PagerAdapter��emoViewPager���ʹ��
	EmoPagerAdapter emoPagerAdapter;

	// �뷢��ͼ������������Ϣ��ص�����
	LinearLayout addLayout;
	String cameraPath;

	// �뷢����������������Ϣ��ص�����
	@Bind(R.id.ll_chat_textinputcontainer)
	LinearLayout textinputContainer;
	@Bind(R.id.ll_chat_voiceinputcontainer)
	LinearLayout voiceinputContainer;
	@Bind(R.id.ll_chat_voicecontainer)
	LinearLayout voiceContainer;
	@Bind(R.id.iv_chat_voiceVolume)
	ImageView ivVolume;
	@Bind(R.id.tv_chat_voicetip)
	TextView tvTip;

	@Bind(R.id.btn_chat_speak)
	Button btnSpeak;

	int[] volumeIamges;
	// BmobIMSDK�����ṩ��һ����װ��
	// MediaRecorder����¼�������Ĺ�����
	BmobRecordManager recordManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_chat);
	}

	@Override
	public void init() {
		super.init();
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		targetId = targetUser.getObjectId();
		targetName = targetUser.getUsername();
		myId = bmobUserManager.getCurrentUserObjectId();

		initHeaderView();

		// log(targetId+":"+targetName);
		initView();
		initListView();

	}

	private void initView() {
		// etContent���TextWatcher������
		// ��etContent���� / ������ʱ���л�btnAdd��btnSend�Ŀɼ���
		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.length() > 0) {
					btnAdd.setVisibility(View.INVISIBLE);
					btnSend.setVisibility(View.VISIBLE);
				} else {
					btnAdd.setVisibility(View.VISIBLE);
					btnSend.setVisibility(View.INVISIBLE);
				}

			}
		});

		initEmoLayout();
		initAddLayout();
		initVoiceLayout();
	}

	private void initVoiceLayout() {
		volumeIamges = new int[] { R.drawable.chat_icon_voice1,
				R.drawable.chat_icon_voice2, R.drawable.chat_icon_voice3,
				R.drawable.chat_icon_voice4, R.drawable.chat_icon_voice5,
				R.drawable.chat_icon_voice6 };

		recordManager = BmobRecordManager.getInstance(this);

		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				// ��¼�������У�������С�����仯ʱ
				// �ص��������÷�������������С��ֵ����ص�������
				ivVolume.setImageResource(volumeIamges[value]);

			}

			@Override
			public void onTimeChanged(int value, String localPath) {
				// ��¼�������У�����¼��ʱ��ĸı�
				// �ص��÷���(ÿһ���ӵ���һ��)
				// �������¼���Ѿ�����60�룬��ǿ�н������ļ����ͳ�ȥ
				if (value > 60) {
					btnSpeak.setPressed(false);
					btnSpeak.setEnabled(false);
					voiceContainer.setVisibility(View.INVISIBLE);
					sendVoiceMsg(value, localPath);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							btnSpeak.setEnabled(true);
						}
					}, 1000);
				}
			}
		});

	}

	/**
	 * �����������͵�������Ϣ
	 * 
	 * @param value
	 * @param localPath
	 */
	protected void sendVoiceMsg(int value, String localPath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}
		bmobChatManager.sendVoiceMessage(targetUser, localPath, value,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// ��ʱmsg��content����ֵ��file:///�����ļ����ص�ַ&�����ļ��������ַ&�������ļ��ĳ���
						refresh();

					}

					@Override
					public void onStart(BmobMsg msg) {
						// ��msg��content������file:///�����ļ����ص�ַ&�����ļ��ĳ���
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initAddLayout() {
		addLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.add_layout, moreContainer, false);

		TextView tvPicture = (TextView) addLayout
				.findViewById(R.id.tv_add_picture);
		tvPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 101);

			}
		});

		TextView tvCamera = (TextView) addLayout
				.findViewById(R.id.tv_add_photo);
		tvCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						System.currentTimeMillis() + ".jpg");
				cameraPath = file.getAbsolutePath();
				Uri imgUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
				startActivityForResult(intent, 102);

			}
		});

		TextView tvLocation = (TextView) addLayout
				.findViewById(R.id.tv_add_location);
		tvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ת����ͼ��ؽ���
				Intent intent = new Intent(ChatActivity.this,
						LocationActivity.class);
				intent.putExtra("from", "mylocation");
				startActivityForResult(intent, 103);

			}
		});

	}

	/**
	 * ��ʼ��������صĲ���
	 */
	private void initEmoLayout() {
		emoLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.emo_layout, moreContainer, false);
		emoViewPager = (ViewPager) emoLayout
				.findViewById(R.id.vp_emolayout_viewpager);
		emoCpi = (CirclePageIndicator) emoLayout
				.findViewById(R.id.cpi_emolayout_indicator);

		// emoViewPager���ʹ�õ�PagerAdapter
		List<View> views = new ArrayList<View>();
		// ����ʾ�����һ����GridView�ŵ�views������on��

		int pageno = EmoUtil.emos.size() % 21 == 0 ? EmoUtil.emos.size() / 21
				: EmoUtil.emos.size() / 21 + 1;

		for (int i = 0; i < pageno; i++) {
			View view = getLayoutInflater().inflate(
					R.layout.emo_gridview_layout, emoLayout, false);
			GridView gridView = (GridView) view
					.findViewById(R.id.gv_emo_gridview);

			int end = Math.min((i + 1) * 21, EmoUtil.emos.size());
			List<String> emos = EmoUtil.emos.subList(i * 21, end);
			final EmoGridViewAdapter emoGridViewAdpater = new EmoGridViewAdapter(
					this, emos);
			gridView.setAdapter(emoGridViewAdpater);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String emoname = emoGridViewAdpater.getItem(position);
					etContent.append(EmoUtil.getSpannableString(emoname));
				}
			});

			views.add(view);
		}

		emoPagerAdapter = new EmoPagerAdapter(views);
		emoViewPager.setAdapter(emoPagerAdapter);
		emoCpi.setViewPager(emoViewPager);
		// emoCpiԲ��Ĵ�С����ɫ�Ƿ���Ҫ�ı�
		emoCpi.setFillColor(Color.DKGRAY);

	}

	private void initListView() {
		messages = new ArrayList<BmobMsg>();
		adapter = new ChatAdapter(this, messages);
		listView.setAdapter(adapter);

	}

	private void initHeaderView() {
		setHeaderTitle(targetName, Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		MyPushMessageReceiver.unRegist(this);
		super.onPause();
	}

	private void refresh() {
		// ������Ϣ�����������ط�
		// 1)Bmob��������BmobMsg���ݱ���
		// 2)�����ڱ������ݿ��chat����
		// ListView������Դ���Ա������ݿ��chat��
		// �����ǣ�����ǰ��¼�û���targetUser֮������������¼ȡ��������
		List<BmobMsg> list = bmobDB.queryMessages(targetId, 0);
		adapter.addItems(list, true);
		// ���������Ϣ����������һ��������ʾ�ķ�Χ
		// ��������ʾ������Ϣ
		listView.setSelection(adapter.getCount() - 1);

	}

	@OnClick(R.id.btn_chat_send)
	public void sendTextMessage(View v) {
		String message = etContent.getText().toString();
		if (TextUtils.isEmpty(message)) {
			return;
		}

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}

		// �����ı����͵�������Ϣ
		// msg������5����Ҫ���Ե�����ֵʲô��
		// tag:""
		// content:ͨ���������������etContent�е��ı�����
		// msgType: 1 �ı�����
		// isreaded: 0 δ��
		// status: 1 Success
		final BmobMsg msg = BmobMsg.createTextSendMsg(this, targetId, message);

		// sendTextMessage��������ʲô

		// 1)�ڷ�����_user���в���targetUser����Ӧ�����ݼ�¼���豸ID��ֵ
		// 2)����BmobMsg��������ݴ���JsonObject������BmobPushManager���1����ѯ�������豸ID��������
		// 3)�����ͳɹ��󣬽�BmobMsg�����status����ֵ����Ϊ1
		// ע�⣺�ò����Է����ı�����������Ϣ��˵���岻����Ϊ��BmobMsg�����ڴ���ʱstatus������ֵ����1
		// 4)��BmobMsg���󱣴浽������BmobMsg���ݱ��У���ʱisReaded�ֶ�ֵΪ0��status�ֶ�ֵΪ1
		// 5)��BmobMsg�����isReaded����ֵ�趨Ϊ1(��0��Ϊ1)
		// 6)��BmobMsg���󱣴����µ��������ݿ��chat���ݱ���
		// 7)����BmobMsg������ȡ�������Թ���һ��BmobRecent���󣬲�����BmobRecent���󱣴浽�������ݿ��recent���ݱ���
		// 8)���������Լ�д�ļ������е���Ӧ����

		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				// �����ı�������Ϣ�ɹ���
				etContent.setText("");
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("����������Ϣʧ�ܣ��Ժ�����", arg0, arg1);
			}
		});

	}

	@OnClick(R.id.btn_chat_voice)
	public void showVoiceInputContaienr(View v) {
		textinputContainer.setVisibility(View.INVISIBLE);
		voiceinputContainer.setVisibility(View.VISIBLE);

		moreContainer.removeAllViews();
	}

	@OnTouch(R.id.btn_chat_speak)
	public boolean speak(View v, MotionEvent event) {

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			voiceContainer.setVisibility(View.VISIBLE);
			recordManager.startRecording(targetId);
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getY() < 0) {
				// ��ָ�ڰ�ť���
				tvTip.setText("��ָ�ɿ���ȡ������");
			} else {
				tvTip.setText("��ָ�ϻ���ȡ������");
			}
			break;

		default:

			voiceContainer.setVisibility(View.INVISIBLE);

			if (event.getY() < 0) {
				// ȡ������
				recordManager.cancelRecording();
			} else {
				int value = recordManager.stopRecording();
				String localPath = recordManager.getRecordFilePath(targetId);
				sendVoiceMsg(value, localPath);
			}

			break;
		}

		return true;

	}

	@OnClick(R.id.btn_chat_keyboard)
	public void showTextInputContainer(View v) {
		textinputContainer.setVisibility(View.VISIBLE);
		voiceinputContainer.setVisibility(View.INVISIBLE);

	}

	@OnClick(R.id.btn_chat_emo)
	public void addEmoLayout(View v) {
		if (moreContainer.getChildCount() > 0) {
			if (moreContainer.getChildAt(0) == addLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(emoLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			moreContainer.addView(emoLayout);
		}
	}

	@OnClick(R.id.btn_chat_add)
	public void addAddLayout(View v) {
		if (moreContainer.getChildCount() > 0) {
			if (moreContainer.getChildAt(0) == emoLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(addLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			moreContainer.addView(addLayout);
			if (voiceinputContainer.getVisibility() == View.VISIBLE) {
				voiceinputContainer.setVisibility(View.INVISIBLE);
				textinputContainer.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case 101:
				Uri uri = arg2.getData();
				Cursor c = getContentResolver().query(uri,
						new String[] { Media.DATA }, null, null, null);
				c.moveToNext();
				String filePath = c.getString(0);
				c.close();
				// log("filePath:--->"+filePath);
				sendImageMessage(filePath);
				break;

			case 102:
				// log("filePath:--->"+cameraPath);
				sendImageMessage(cameraPath);
				break;
			case 103:
				// ��LocationActivity�ش����������

				double lat = arg2.getDoubleExtra("lat", -1);
				double lng = arg2.getDoubleExtra("lng", -1);
				String address = arg2.getStringExtra("address");
				String url = arg2.getStringExtra("url");
				String path = arg2.getStringExtra("path");

				// log("lat:"+lat+", lng:"+lng+",address:"+address+",path:"+path+",url:"+url);
				sendLocationMessage(lat, lng, address, url, path);
				break;
			}
		}
	}

	/**
	 * ���͵���λ�����͵�������Ϣ
	 * 
	 * @param lat
	 * @param lng
	 * @param address
	 * @param url
	 * @param path
	 */
	private void sendLocationMessage(double lat, double lng, String address,
			String url, String path) {
		// ��ν��λ�����족��Ϣʵ�ʾ����ı����͵�������Ϣ
		// msg��content����ֵ"�ֵ�&���ص�ַ&�����ַ&γ��&����"
		final BmobMsg msg = BmobMsg.createLocationSendMsg(this, targetId,
				address + "&" + path + "&" + url, lat, lng);
		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * ����ͼ�����͵�������Ϣ
	 * 
	 * @param filePath
	 */
	private void sendImageMessage(String filePath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}

		bmobChatManager.sendImageMessage(targetUser, filePath,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// �ڵ���onStart����ʽ�����msg����
						// ��ʱ�Ѿ��������˱������ݿ���
						// ��ʱ��chat���б����msg����һЩ��Ҫ������
						// content file:///ͼ��ı��ص�ַ&ͼ���ڷ������ĵ�ַ
						// status 1
						// isreaed 1
						// msgType 2
						refresh();
					}

					@Override
					public void onStart(BmobMsg msg) {
						// msg���������
						// tag ""
						// cotent file:///+ͼ��ı��ص�ַ
						// msgType 2
						// status 0
						// isreaded 0
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);
					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	public void onMessage(BmobMsg message) {
		// ȷ��message�ķ����˾��ǵ�ǰ���������������
		if (message.getBelongUsername().equals(targetName)) {
			adapter.addItem(message);
			listView.setSelection(adapter.getCount() - 1);
			// ��������֮�������¼��Ϊ���Ѷ���
			bmobDB.resetUnread(targetId);
		}
	}

	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAddUser(BmobInvitation message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub

	}

}
