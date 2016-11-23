package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.RecentAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.ChatActivity;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.LogUtil;

public class MessageFragment extends BaseFragment {
	@Bind(R.id.lv_message_listview)
	ListView listView;
	// BmobRecent��BmobIMSDK���ṩ��ʵ����
	// ������װ�ӱ������ݿ�recent���в�ѯ���������ݼ�¼
	List<BmobRecent> recents;
	RecentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container,
				false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("����", Position.CENTER);
	}

	private void initListView() {
		recents = new ArrayList<BmobRecent>();
		adapter = new RecentAdapter(getActivity(), recents);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// �����ת���������
				// �ı䵱ǰ�Ự��δ����Ϣ��״̬����Ϊ�Ѷ�
				String toId = adapter.getItem(position).getTargetid();
				bmobDB.resetUnread(toId);
				// ����MainActiviyt�е�setBadgeCount����
				// �����ܵ�δ����Ϣ����
				FragmentActivity act = getActivity();
				((MainActivity) act).setBadgeCount();
				// �������ת����ת��ChatActivity
				// Ҫ�ѻỰ(recent)�жԷ��û����ݵ�ChatActivity
				String username = adapter.getItem(position).getUserName();

				bmobUserManager.queryUserByName(username,
						new FindListener<BmobChatUser>() {

							@Override
							public void onSuccess(List<BmobChatUser> arg0) {
								BmobChatUser user = arg0.get(0);
								LogUtil.i("user" + user);
								Intent intent = new Intent(getActivity(),
										ChatActivity.class);
								intent.putExtra("user", user);
								jumpTo(intent, false);
							}

							@Override
							public void onError(int arg0, String arg1) {
								toastAndLog("��ѯ�û�ʧ��", arg0, arg1);

							}
						});

				// jumpTo(ChatActivity.class, false);

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final BmobRecent recent = adapter.getItem(position);
				DialogUtil.showDialog(getActivity(), "ɾ���Ự",
						"ȷʵҪɾ����" + recent.getUserName() + "֮���ȫ������������", true,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 1)�������ϵ���������
								// ˵���������г���˫��ȫ����������ֻ��chat���в�ѯ
								// ���Է������ϵ���������ɾ����ɾ���Գ���������Ϣû��Ӱ��
								// ������Ų�ɾ���������ϵ���������
								// �������Ϊ��Ҫɾ���������������Ӧ���߼�
								// 2)�������ݿ�chat���е���������
								bmobDB.deleteMessages(recent.getTargetid());
								// 3)�������ݿ�recent���еĻỰ
								bmobDB.deleteRecent(recent.getTargetid());
								// 4)ɾ��ListView����Դ�е�����
								adapter.removeData(recent);
								// 5)����MainActivity������δ����Ϣ������
								((MainActivity) getActivity()).setBadgeCount();

							}
						});

				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh() {
		// ��ȡ�Ự��Ϣ
		// �Ự��Ϣ�������ڱ������ݿ��recent����
		List<BmobRecent> list = bmobDB.queryRecents();
		adapter.addItems(list, true);
	}

}
