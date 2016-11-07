package com.tarena.fgr.youlu;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.fgr.adapter.SmsBaseAdapter;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.fragment.SmsFragment;

/**
 * �����������
 * 
 * @author �����
 * 
 */
public class ChatActivity extends Activity implements OnScrollListener,
		OnItemLongClickListener {
	private ImageView imageView_left = null;// ���˶��Ž���
	private ImageView imageView_right = null;// ����
	private TextView textView_title = null;// ��ϵ��������绰
	private ListView listView_chat = null;// ��������
	private ImageView imageView_plus = null;// ���½ǵļӺ�
	private EditText editText_sms = null;// ���ű༭����
	private Button button_send = null;// ���Ͱ�ť
	private InputMethodManager IMM = null;// �����
	private SmsBaseAdapter adapter = null;
	private List<Sms> list = null;
	private String phone = null;// ���ϸ�ҳ�洫�ݹ����ĵ绰����
	private SMSReceiver smsReceiver = null;
	// �յ����ŵ�ϵͳ�㲥
	public final static String RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initUI();// ��ʼ��UI
		initIntent();// �ϸ�ҳ�洫�ݹ���������
		hideIMM();// Ĭ�����������
		initListener();
		initData();
		// �㲥��������ע�᷽��
		// 1.��̬ע��(�㲥�������ǺͶ��������������һ���)
		// ���Ĺ㲥�������������������,�㲥�������ſ��Խ��չ㲥
		// 2.��̬ע��(�㲥������ע�����嵥�ļ���,ֻҪ���䶩�ĵĹ㲥�����������յ��㲥,
		// ���ǹ㲥���������������ڴ��ֻ��10������)
		registReceiver();
	}

	private IntentFilter intentFilter;

	private void registReceiver() {
		// �����㲥����������
		smsReceiver = new SMSReceiver();
		// ���Ĺ㲥�յ�����ʱϵͳ����������㲥
		intentFilter = new IntentFilter();
		intentFilter.addAction(RECEIVE_SMS);

		// *********************************************
		// *********************************************
		// *********************************************
		intentFilter.addAction(SMSManager.SEND_SMS);
		// ����̫TM��Ҫ��,�м�
		// *********************************************
		// *********************************************
		// *********************************************

		intentFilter.setPriority(1000);// һ�����ȼ������ȼ�Ϊ-1000��1000
		registerReceiver(smsReceiver, intentFilter);
	}

	private void initIntent() {
		Intent intent = getIntent();
		int intentThreadId = intent.getIntExtra(SmsFragment.ACTION_THREAD_ID,
				-1);
		phone = intent.getStringExtra(SmsFragment.ACTION_PHONE);
		String name = intent.getStringExtra(SmsFragment.ACTION_NAME);
		thread_id = intentThreadId;
		textView_title.setText(name);
	}

	private void initUI() {
		IMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imageView_left = (ImageView) findViewById(R.id.imageView_actionbar_left);
		imageView_right = (ImageView) findViewById(R.id.imageView_actionbar_right);
		textView_title = (TextView) findViewById(R.id.textView_actionbar_title);
		listView_chat = (ListView) findViewById(R.id.listView_chat);
		imageView_plus = (ImageView) findViewById(R.id.imageView_plus);
		editText_sms = (EditText) findViewById(R.id.editText_sms);
		button_send = (Button) findViewById(R.id.button_send);
		imageView_right.setVisibility(View.INVISIBLE);// ����
		adapter = new SmsBaseAdapter(this);
	}

	private int thread_id;

	private void initData() {
		// �������������
		refreshChat();
		listView_chat.setAdapter(adapter);
		listView_chat.setSelection(list.size() - 1);// Ĭ����ʾ,�����һ������
	}

	private void refreshChat() {
		list = SMSManager.getSMSes(this, thread_id);
		adapter.addItems(list, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshChat();
		registerReceiver(smsReceiver, intentFilter);
	}

	private void initListener() {

		// ���edittextΪ����ʾ��ɫ,��������ʾ��ɫ
		editText_sms.addTextChangedListener(new MyTextWatcher());
		// ����Ӻ���ʾ�����������
		imageView_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				autoIMM();
			}
		});
		// ����ı��򵯳������
		editText_sms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showIMM();
			}
		});
		// ���˵����Ž���
		imageView_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 2016��10��12�� 09:07:16
				// �����:����Ϊ��ʵ�ֹ���д��һ����bug�ķ���
				// �޴��bug,����û�з��ͳɹ�Ҳ�ᱣ�浽���ݿ�
				// 2016��10��12�� 10:50:47
				// �����:ͨ���㲥����,Ȼ����һ���Ƿ��ͳɹ�,������
				// ȥ��ǰ��ո�
				if (!TextUtils
						.isEmpty(editText_sms.getText().toString().trim())) {// ��Ϊ�յ�ʱ����
					// ǿ�йر������
					closeIMM();
					// ���Ͷ���
					// �����ķ���,���������ݿ�д����ż�¼
					String message = editText_sms.getText().toString();

					SMSManager.sendSms(ChatActivity.this, message, phone);

					// 2016��10��12�� 10:54:22
					// ���Ӽ�¼��ˢ������,���㲥����ŷ��ͳɹ�

					// ���ݿ����Ӽ�¼
					// ֱ�Ӽ�,�Ự�б��������???
					// SmsManager.insertSms(ChatActivity.this, phone, body);

					// itemͬʱ��Ҫ����
					// ���¼�������
					// refreshChat();
					// Ĭ����ʾ,�����һ������
					// ���Ҵ򿪵�ʱ��,��ʾ���һ��,���ϻ�,Ȼ��ֱ�ӷ��Ͷ���,���ŷ��ͳɹ�,Ӧ����ʾ���ͳ�ȥ�Ķ���
					// listView_chat.setSelection(list.size() - 1);

					// ���edit
					editText_sms.setText("");
				}
			}
		});

		// �鿴�����¼,�����¼�,��Ҫǿ�ƹر������
		listView_chat.setOnScrollListener(this);
		listView_chat.setOnItemLongClickListener(this);
	}

	public class SMSReceiver extends BroadcastReceiver {
		@Override
		// �������ĵ���Щ���͵Ĺ㲥������ʱ���ص�����
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(RECEIVE_SMS)) {// �յ�����
				// ����140���ֽ�
				// LogUtil.i("TAG:", action);
				// ��ö��ŵ�����,�Զ������ݽ��в��
				Bundle bundle = intent.getExtras();

				Sms sms = SMSManager.getSmsByReceiver(bundle);

				// LogUtil.i("TAG:sms", sms.toString());
				// �ж�һ���ǲ��Ƿ�����ǰ�Ự�Ķ���
				// ����ǵĻ������ű��浽�ռ��䲢�����յ����ŵ�
				// ����㲥 ��������ϵͳ�Ķ����շ�Ӧ�ô���
				if (phone.equals(sms.getAddress())) {
					// �����ű������ռ�����
					SMSManager.saveReciveMessage(context, sms, thread_id);
					// ���Ե�ǰ������㲥
					// ��ֹ�ö��ŵĹ㲥����һ���㲥����,ʹϵͳ�Ķ���Ӧ�ò��ٽ��ոö���
					// ������ʾϵͳ���ŵ�Notification
					abortBroadcast();
					refreshChat();
				}
			} else if (action.equals(SMSManager.SEND_SMS)) {
				// ���ŷ��ͳɹ�

				// ���ݿ����Ӽ�¼
				String phone = intent.getStringExtra("phone");
				String body = intent.getStringExtra("body");

				SMSManager.insertSms(context, phone, body);

				Toast.makeText(ChatActivity.this, "���ͳɹ�", Toast.LENGTH_SHORT)
						.show();

				// itemͬʱ��Ҫ����
				// ���¼�������
				refreshChat();
				// Ĭ����ʾ,�����һ������
				// ���Ҵ򿪵�ʱ��,��ʾ���һ��,���ϻ�,Ȼ��ֱ�ӷ��Ͷ���,���ŷ��ͳɹ�,Ӧ����ʾ���ͳ�ȥ�Ķ���
				listView_chat.setSelection(list.size() - 1);

			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (smsReceiver != null) {
			// ע���㲥������,�ͷ���Դ
			unregisterReceiver(smsReceiver);
		}
	}

	// ������Զ�,�����,����
	private void autoIMM() {
		// �ж�������Ƿ��
		boolean isOpen = getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		if (isOpen) {
			showIMM();
		} else {
			hideIMM();
		}
	}

	// ��ʾ�����
	private void showIMM() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	// ���������
	private void hideIMM() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// ǿ�ƹر������
	private void closeIMM() {
		IMM.hideSoftInputFromWindow(editText_sms.getWindowToken(), 0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {// �����¼�Ļ����¼�
		closeIMM();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	// ����ɾ��һ������
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		DialogManager.showDeleteSmsDialog(this, adapter.getItem(position),
				adapter);
		return false;
	}

	public class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() == 0) {
				// button_send.setBackgroundColor(Color.WHITE);
				button_send.setBackgroundResource(R.drawable.button_bg);
			} else {
				// button_send.setBackgroundColor(Color.BLUE);
				button_send.setBackgroundResource(R.drawable.button_bg_ok);
			}
		}

	}
}
