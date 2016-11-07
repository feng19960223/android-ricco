package com.tarena.fgr.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tarena.fgr.adapter.ConversationBaseAdapter;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.ChatActivity;
import com.tarena.fgr.youlu.R;

/**
 * ����Ϣ
 * 
 * @author �����
 * 
 */
public class SmsFragment extends BaseFragment implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView listView = null;
	private ConversationBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ����Ϊfalse,container�����Ǹ��ڵ�,���������Ĳ���
		contentView = inflater.inflate(R.layout.fragment_sms, container, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// ��ʼ�����Ե�UI�ؼ�
		registReceiver();
		return contentView;
	}

	private IntentFilter intentFilter;

	private void registReceiver() {
		conversationReceiver = new ConversationReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(ChatActivity.RECEIVE_SMS);
		intentFilter.setPriority(1000);
		getActivity().registerReceiver(conversationReceiver, intentFilter);
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_sms);
		// listView.setScrollBarXXX();//���ù������ĸ�����ʽ
		// �ı��������λ��
		// listView.scrollTo(listView.getWidth() + 24, 0);
		// 2016��10��12�� 09:04:19 ���н����һ���޴�ĺڱ�...��ʧ��

		initialActionbar(R.drawable.add_sms, "����Ϣ", -1);
		ImageView imageView_send_sms = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_left);
		imageView_send_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_SENDTO, Uri
						.parse("smsto:")));
			}
		});
		adapter = new ConversationBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		refreshSms();
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	private void refreshSms() {
		// ��ѯ���ԻỰ
		List<Conversation> list = SMSManager.getConversations(getActivity());
		// ����UI
		adapter.addItems(list, true);
	}

	@Override
	// ҳ��������ʾ��ʱ��ִ��
	public void onResume() {
		super.onResume();
		refreshSms();
		getActivity().registerReceiver(conversationReceiver, intentFilter);
	}

	public static final String ACTION_THREAD_ID = "thread_id";
	public static final String ACTION_PHONE = "phone";
	public static final String ACTION_NAME = "name";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// ��õ�ǰѡ�еĻỰ����
		Conversation conversation = adapter.getItem(position);

		String phone = conversation.getPhone();
		int thread_id = conversation.getThread_id();
		String name = conversation.getName();

		if (conversation.getRead() == 0) {
			// �޸ĻỰ״̬,�����Ѷ�
			SMSManager.updateConverationType(getActivity(), thread_id);
		}

		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra(ACTION_THREAD_ID, thread_id);
		intent.putExtra(ACTION_PHONE, phone);
		intent.putExtra(ACTION_NAME, name);
		getActivity().unregisterReceiver(conversationReceiver);
		startActivity(intent);
	}

	@Override
	// ����ɾ������
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		DialogManager.showDeleteConversationDialog(getActivity(),
				adapter.getItem(position), adapter);
		return false;
	}

	private ConversationReceiver conversationReceiver = null;

	// �յ�����,ˢ��ҳ��,��ʾС�̵�
	public class ConversationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// �յ�����
			if (action.equals(ChatActivity.RECEIVE_SMS)) {
				Bundle bundle = intent.getExtras();
				Sms sms = SMSManager.getSmsByReceiver(bundle);
				SMSManager.save(context, sms, 0);// ��������������Ĳ�֪��дʲô...������0û�г��ִ���...
				// ˢ�½���
				refreshSms();
				abortBroadcast();// ����ϵͳ��֪ͨ
			}
		}
	}

}
