package com.fgr.miaoxin.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.text.Editable;
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
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.receiver.MyPushMessageReceiver;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.NetUtil;
import com.viewpagerindicator.CirclePageIndicator;

public class ChatActivity extends BaseActivity implements EventListener {
	// �뵱ǰ��¼�û�����������û�
	// ��MessageFragment���UserInfoActivity���ݹ�����
	BmobChatUser targetUser;
	String targetUsername;// targetUser��username
	String targetId;// targetUser��objectId
	String myId;// ��ǰ��¼�û���objectId

	// ���ı�������ص���ͼ���
	@Bind(R.id.lv_chat_listview)
	ListView listView;
	List<BmobMsg> messages;
	ChatAdapter adapter;

	@Bind(R.id.et_chat_content)
	EditText etContent;
	@Bind(R.id.btn_chat_add)
	Button btnAdd;
	@Bind(R.id.btn_chat_send)
	Button btnSend;

	// ����鲼����ص�����
	@Bind(R.id.ll_chat_morelayoutcontainer)
	LinearLayout moreContainer;

	RelativeLayout emoLayout;

	ViewPager emoViewPager;
	CirclePageIndicator emoCpi;
	EmoPagerAdapter emoPagerAdapter;

	// �뷢��ͼƬ�Լ�λ��������ص�����
	LinearLayout addLayout;
	String cameraPath;

	// ������������Ϣ��ص�����
	@Bind(R.id.ll_chat_textinputcontainer)
	LinearLayout textinputContainer;

	@Bind(R.id.ll_chat_voiceinputcontainer)
	LinearLayout voiceinputContainer;

	@Bind(R.id.ll_chat_voicecontainer)
	LinearLayout voiceContainer;

	@Bind(R.id.iv_chat_voicevolum)
	ImageView ivVoiceVolum;
	@Bind(R.id.tv_chat_voicetip)
	TextView tvVoiceTip;

	@Bind(R.id.btn_chat_speak)
	Button btnSpeak;

	int[] volumImages;// ¼��ʱ��ʾ������С��ͼƬ

	// BmobIMSDK���߷�װ��¼��������
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
		targetUsername = targetUser.getUsername();
		targetId = targetUser.getObjectId();
		myId = bmobUserManager.getCurrentUserObjectId();
		initHeaderView();
		initView();
		initListView();

	}

	private void initHeaderView() {
		setHeaderTitle(targetUsername, Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {
		initContentInput();
		initEmoLayout();
		initAddLayout();
		initVoiceLayout();

	}

	private void initVoiceLayout() {
		volumImages = new int[] { R.drawable.chat_icon_voice1,
				R.drawable.chat_icon_voice2, R.drawable.chat_icon_voice3,
				R.drawable.chat_icon_voice4, R.drawable.chat_icon_voice5,
				R.drawable.chat_icon_voice6 };

		recordManager = BmobRecordManager.getInstance(this);

		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				// ������¼�����������������仯
				// ���ݴ���ı�ʾ������С��value����ѡ��ͼƬ������ʾ
				ivVoiceVolum.setImageResource(volumImages[value]);

			}

			@Override
			public void onTimeChanged(int value, String localPath) {
				// ������¼��������ʱ�䷢���仯����λ���룩
				// value�����ʱһ��¼�Ƶ�ʱ��
				// localPath��ʱ¼�Ƶ������ļ���SD�ϵĴ洢·��
				if (value >= 60) {
					// ֹͣ¼��
					// ���Ѿ�¼�ƺõ�60����������ͳ�ȥ
					btnSpeak.setEnabled(false);
					btnSpeak.setClickable(false);
					btnSpeak.setPressed(false);
					voiceContainer.setVisibility(View.INVISIBLE);
					recordManager.stopRecording();
					sendVoiceMessage(value, localPath);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							btnSpeak.setEnabled(true);
							btnSpeak.setClickable(true);

						}
					}, 1000);

				}

			}
		});

	}

	/**
	 * ����һ���������͵�������Ϣ
	 * 
	 * @param value
	 *            ������Ϣ��ʱ��
	 * @param localPath
	 *            ������Ϣ��SD���ϵĴ洢λ��
	 */
	protected void sendVoiceMessage(int value, String localPath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}

		bmobChatManager.sendVoiceMessage(targetUser, localPath, value,
				new UploadListener() {
					// �����ļ��Ѿ��������
					// �ڵ���onStart����ʱ������BmobMsg�����Ѿ������浽�˱������ݿ��chat����
					// ͨ������refresh����ˢ��ListView
					// ��ʱchat����BmobMsg��������Ӧ�����ݼ�¼�Ĳ����ֶ�ֵ��BmobMsg���󴴽�֮�������Է����˱仯
					// content �����ļ���·��&����&�����ļ��ڷ������ϵĵ�ַ
					// status 1
					// isreaded 1
					@Override
					public void onSuccess() {
						refresh();

					}

					// ������������Ϣ����֮ǰ
					// ����ݴ���������ļ���·���ͳ���
					// ����һ��BmobMsg����
					// ��ʱ��BmobMsg���������Ϊ��
					// tag ""
					// content �����ļ���·��&����
					// msgType 4
					// status 0
					// isreaded 0
					// ���ü�������onStart����������BmobMsg������
					@Override
					public void onStart(BmobMsg msg) {
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						toastAndLog("��������������Ϣ����ʧ��", error, arg1);

					}
				});
	}

	/**
	 * ��ʼ��addLayout
	 * 
	 */
	private void initAddLayout() {
		addLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.add_layout, moreContainer, false);

		TextView tvPicture = (TextView) addLayout
				.findViewById(R.id.tv_addlayout_picture);
		tvPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
				startActivityForResult(intent, 101);

			}
		});

		TextView tvCamera = (TextView) addLayout
				.findViewById(R.id.tv_addlayout_camera);
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
				.findViewById(R.id.tv_addlayout_location);
		tvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ��ת����ͼ������ж�λ
				Intent intent = new Intent(ChatActivity.this,
						LocationActivity.class);
				intent.putExtra("from", "mylocation");
				startActivityForResult(intent, 103);

			}
		});
	}

	/**
	 * ��ʼ��emoLayout ��ʾ��ͼ����ɫ���ֵ����ݣ�
	 */
	private void initEmoLayout() {
		emoLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.emo_layout, moreContainer, false);
		emoViewPager = (ViewPager) emoLayout
				.findViewById(R.id.vp_emolayout_viewpager);
		emoCpi = (CirclePageIndicator) emoLayout
				.findViewById(R.id.cpi_emolayout_indicator);

		List<View> views = new ArrayList<View>();
		// ��views��������ʾ�������ͼ
		// views�а�������view����ʾ���飬ȡ���ڱ����������
		// view����������������%21==0?����������/21:����������/21+1
		int pageno = EmoUtil.emos.size() % 21 == 0 ? EmoUtil.emos.size() / 21
				: EmoUtil.emos.size() / 21 + 1;
		for (int i = 0; i < pageno; i++) {
			View view = getLayoutInflater().inflate(
					R.layout.emo_gridview_layout, emoViewPager, false);

			GridView gridView = (GridView) view
					.findViewById(R.id.gv_emo_gridview);
			// ����Դ
			// ���еı�������EmoUtil.emos
			// ÿһ��gridView���Ǵ�EmoUtil.emosȡ���ɸ�����
			int startPos = 21 * i;
			int endPos = Math.min(21 * (i + 1), EmoUtil.emos.size());
			List<String> list = EmoUtil.emos.subList(startPos, endPos);
			// ������
			final EmoGridViewAdapter emoGridViewAdapter = new EmoGridViewAdapter(
					this, list);
			gridView.setAdapter(emoGridViewAdapter);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String resName = emoGridViewAdapter.getItem(position);

					etContent.append(EmoUtil.getSpannableString(resName));
				}
			});

			views.add(view);
		}

		emoPagerAdapter = new EmoPagerAdapter(views);
		emoViewPager.setAdapter(emoPagerAdapter);
		emoCpi.setViewPager(emoViewPager);
		emoCpi.setFillColor(Color.DKGRAY);

	}

	private void initContentInput() {
		// etContent���һ��������
		// ����etContent�����ݱ仯
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
					// һ��etContent�������ݣ�btnAdd���ɼ���btnSend�ɼ�
					btnAdd.setVisibility(View.INVISIBLE);
					btnSend.setVisibility(View.VISIBLE);
				} else {
					// һ��etContent�������ݣ�btnAdd�ɼ���btnSend���ɼ�
					btnAdd.setVisibility(View.VISIBLE);
					btnSend.setVisibility(View.INVISIBLE);
				}

			}
		});

	}

	private void initListView() {
		messages = new ArrayList<BmobMsg>();
		adapter = new ChatAdapter(this, messages);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyPushMessageReceiver.unRegist(this);
	}

	private void refresh() {
		// ��ȡListView����������Դ
		// ��ǰ��¼�û�����Ӧ���ݿ��chat���ݱ�
		List<BmobMsg> list = bmobDB.queryMessages(targetId, 0);
		adapter.addItems(list, true);
		// ��ListView�������һ�����ݽ��г���
		listView.setSelection(adapter.getCount() - 1);
	}

	@OnClick(R.id.btn_chat_send)
	public void sendTextMessage(View view) {
		String content = etContent.getText().toString();

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}
		// �ı����͵�������Ϣmsg
		// tag ""
		// content �����content����
		// msgType 1
		// isreaded 0 δ��
		// status 1
		final BmobMsg msg = BmobMsg.createTextSendMsg(this, targetId, content);
		// 1. ȥ������_user���в���targetUser�û�
		// 2. ����У��͸���msg���󴴽�һ��JsonObject����Ȼ������BmobPushManager��������
		// ����ʱ���豸ID��targetUser���һ�ε�¼ʱ��ʹ�õ��豸ID
		// 3. ������Ϻ󣬽�msg���󱣴浽��������BmobMsg���ݱ��У���ʱ�������ݼ�¼��isreadedֵΪ0
		// 4. ��msg��isreaded����ֵ��0����Ϊ1����ʼ���浽�������ݿ��chat���recent����
		// 5. ������Ϻ󣬵����Լ�д�ļ���������Ӧ����
		// ע�⣺��3���У���������Ϻ󣬱��浽������֮ǰ���Ⱥ������趨msg�����statusΪ1��

		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);
				etContent.setText("");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("����ʧ�ܣ��Ժ�����", arg0, arg1);

			}
		});
	}

	@OnClick(R.id.btn_chat_emo)
	public void addEmoLayout(View view) {
		if (moreContainer.getChildCount() > 0) {
			// moreContainer������ͼ
			if (moreContainer.getChildAt(0) == addLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(emoLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			// moreContainer��û������ͼ
			moreContainer.addView(emoLayout);
		}
	}

	@OnClick(R.id.btn_chat_add)
	public void addAddLayout(View view) {
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
				textinputContainer.setVisibility(View.VISIBLE);
				voiceinputContainer.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case 101:
				// ͼ��ѡͼ
				Uri uri = arg2.getData();
				Cursor cursor = getContentResolver().query(uri,
						new String[] { Images.Media.DATA }, null, null, null);
				cursor.moveToNext();
				String filePath = cursor.getString(0);
				cursor.close();
				sendImageMessage(filePath);
				break;
			case 102:
				// �������
				sendImageMessage(cameraPath);
				break;
			case 103:
				// ��λ���ؽ��
				String address = arg2.getStringExtra("address");
				String localFilePath = arg2.getStringExtra("localFilePath");
				String url = arg2.getStringExtra("url");

				// log("��õĵ�ַ�ǣ�"+address+",��ͼ�ı���·����"+localFilePath+",��ͼ�ķ�������ַ��"+url);

				sendLocationMessage(MyApp.lastPoint.getLatitude(),
						MyApp.lastPoint.getLongitude(), address, url,
						localFilePath);

				break;
			}
		}
	}

	/**
	 * ����λ�����͵�������Ϣ
	 * 
	 * @param lat
	 *            γ��
	 * @param lng
	 *            ����
	 * @param address
	 *            ��ַ
	 * @param url
	 *            ��ͼ�������ַ
	 * @param localFilePath
	 *            ��ͼ�ı��ص�ַ
	 */
	private void sendLocationMessage(double lat, double lng, String address,
			String url, String localFilePath) {

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}
		// ������λ������������Ϣ��ָ����msg����msgTypeΪ3��
		// Ȼ�󽫵�ַ��γ�ȣ�����ƴ��Ϊ��ַ&γ��&���ȴ��ַ�����
		// ʣ�µĲ�����뷢���ı����͵�������Ϣһ��
		// �����޸ĺ�õ���msg��content���ǣ���ַ&���ؽ�ͼ��ַ&��ͼ�����ַ&γ��&����
		final BmobMsg msg = BmobMsg.createLocationSendMsg(this, targetId,
				address + "&" + localFilePath + "&" + url, lat, lng);
		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("����λ��������Ϣʧ�ܣ��Ժ�����", arg0, arg1);

			}
		});

	}

	/**
	 * ����ͼ�����͵�������Ϣ
	 * 
	 * @param filePath
	 *            ͼ����SD���ϵĴ洢��ַ
	 */
	private void sendImageMessage(String filePath) {

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}

		// log("ͼƬ��·���ǣ�"+filePath);
		// sendImageMessage:
		// 1. ����BmobMsg����
		// tag ""
		// content file:///+filePath
		// status 0
		// msgType 2
		// isreaded 0
		// 2. ����UploadListener onStart����������1������������BmobMsg������Ϊ��������
		// 3. �ϴ�����ͼƬ(filePath����Ӧ��ͼƬ)��������
		// 4. ��ͼƬ�ڷ������ϵĳ���ַת��Ϊ�̵�ַ(��BmobSDK3.4.6��ʼ�����ϣ�Դ�����߼����޸ģ�)
		// 5. �ѵ�һ��������BmobMsg�����content���ԣ���file:///+filePath����Ϊ�˵��Ĳ��õ��������ַ
		// 6. ��������������Ϣ�������շ��յ�����ͼ�����͵�������Ϣʱ����content����ֵ����ͼƬ�ڷ������ϵ�һ����ַ��
		// ���ͳɹ��󣬽���һ��msg����status����ֵ��0--->1
		// ��msg���󱣴浽��������BmobMsg���ݱ�isreaded 0
		// ��msg�����isreaded�ֶ�ֵ��0��Ϊ1֮�󣬱��浽�������ݿ��chat���recent��
		// 7.
		// ��������Ϣ������Ϻ��ֽ���һ��������BmobMsg�����content���Դ������ַ��Ϊ��file:///+filePath&�����ַ
		// ͬʱ�޸���isreaded����ֵ��1��Ϊ��2��Ȼ�󽫸���Ϣ���浽�������ݿ��chat��
		// ��Ϊ�ڵ������У���BmobMsg�����Ѿ���������ˣ���˵��߲��ٴ�������ʱ�����Ḳ��ԭ�е����ݣ����������ԭ�����ݵ�
		// 3���ֶ�ֵ��content��status��belongavatar�ֶΣ����������isreaded�ֶ�
		// ���ԣ��������߲����������ݼ�¼��content file:///+filePath&�����ַ
		// ����isreaded�ֶ�ֵ��Ȼά��1����δ������
		// 8. ����UploadListener onSuccess����
		bmobChatManager.sendImageMessage(targetUser, filePath,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// ��ʱ��onStart��������ʱ�������BmobMsg�����Ѿ������浽�˱������ݿ��chat��
						// ���ǲ�������ֵ�Ѿ������˸ı�
						// content file:///+filePath&ͼ���ڷ������ĵ�ַ
						// status 1
						// isreaded 1
						// �÷���ͼ��������Ϣ��Ŀ��ProgressBar����
						refresh();

					}

					@Override
					public void onStart(BmobMsg msg) {
						// ��ʱ��ListView�����������ݣ�����msg��δ����
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						toastAndLog("ͼ��������Ϣ����ʧ�ܣ��Ժ�����", error, arg1);

					}
				});

	}

	@OnClick(R.id.btn_chat_voice)
	public void showVoiceInputContainer(View view) {
		textinputContainer.setVisibility(View.INVISIBLE);
		voiceinputContainer.setVisibility(View.VISIBLE);
		moreContainer.removeAllViews();
		btnAdd.setVisibility(View.VISIBLE);
		btnSend.setVisibility(View.INVISIBLE);
	}

	@OnClick(R.id.btn_chat_keyboard)
	public void showTextInputContainer(View view) {
		textinputContainer.setVisibility(View.VISIBLE);
		voiceinputContainer.setVisibility(View.INVISIBLE);
	}

	@OnTouch(R.id.btn_chat_speak)
	public boolean speak(View view, MotionEvent event) {

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// ¼����ʼ
			voiceContainer.setVisibility(View.VISIBLE);
			recordManager.startRecording(targetId);
			break;

		case MotionEvent.ACTION_MOVE:
			btnSpeak.setPressed(true);
			float y = event.getY();
			if (y < 0) {
				// ��ָ�ڰ�ť֮��
				tvVoiceTip.setText("�ɿ���ָ��ȡ������");
			} else {
				// ��ָ�ڰ�ť֮��
				tvVoiceTip.setText("��ָ�ϻ���ȡ������");
			}

			break;

		default:
			// ¼��������
			btnSpeak.setPressed(false);
			voiceContainer.setVisibility(View.INVISIBLE);
			if (event.getY() < 0) {
				// �ڰ�ť֮��̧�����ָ
				// Ӧ��ȡ��¼�Ƶ�����
				recordManager.cancelRecording();
			} else {
				// ��¼�Ƶ�������Ϊ�������͵�������Ϣ���ͳ�ȥ
				int value = recordManager.stopRecording();
				String localPath = recordManager.getRecordFilePath(targetId);
				sendVoiceMessage(value, localPath);
			}

			break;
		}

		return true;

	}

	@Override
	public void onMessage(BmobMsg message) {
		// ��Ϊ�����ߣ���MyReceiver�յ��������������Ϣ
		// �ŵ�ListView�г���
		if (message.getBelongId().equals(targetId)) {
			adapter.addItem(message);
			listView.setSelection(adapter.getCount() - 1);
			// ��ListView�г��ֵ���Ϣ���ǡ��Ѷ���
			// ���ԣ�ÿ����һ����Ϣ��Ҫȥ�޸�chat����
			// ��message����isreaded�ֶ�ֵ��2--����Ϊ-->1
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
		// ���յ�����֪ͨʱ���÷����ᱻMyReceiver����
		DialogUtil.showDialog(this, "����֪ͨ", "��⵽�����˺�����һ̨�豸��¼���������µ�¼��", false,
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyApp.logout();
					}
				});

	}

}
