package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.FriendAdapter;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.AddFriendActivity;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.ui.NearFriendActivity;
import com.fgr.miaoxin.ui.NewFriendActivity;
import com.fgr.miaoxin.ui.RobotActivity;
import com.fgr.miaoxin.ui.UserInfoActivity;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.PinYinUtil;
import com.fgr.miaoxin.view.MyLetterView;
import com.fgr.miaoxin.view.MyLetterView.OnTouchLetterListener;

public class FriendFragment extends BaseFragment {
	@Bind(R.id.lv_friend_listview)
	ListView listView;
	List<MyUser> users;
	FriendAdapter adapter;

	@Bind(R.id.tv_friend_letter)
	TextView tvLetter;

	@Bind(R.id.mlv_friend_letters)
	MyLetterView mlvLetters;

	View header;// listviewͷ��

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_friend, container, false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
		initView();
	}

	private void initHeaderView() {
		setHeaderTitle("����", Position.CENTER);
		setHeaderImage(Position.END, R.drawable.ic_add_newfriend, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpTo(AddFriendActivity.class, false);
					}
				});
	}

	private void initListView() {
		users = new ArrayList<MyUser>();
		adapter = new FriendAdapter(getActivity(), users);
		if (header != null) {// 4->2��������,����ʹ������
			listView.removeHeaderView(header);
		}
		header = baseActivity.getLayoutInflater().inflate(
				R.layout.header_listview_friend, listView, false);
		listView.addHeaderView(header);
		// Ϊheader�е�������Ӽ�������ת����
		TextView tvNew = (TextView) header
				.findViewById(R.id.tv_header_newfriend);
		tvNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �����ת��NewFriendActivity��ʾ�յ��ġ���Ӻ������롱
				jumpTo(NewFriendActivity.class, false);
			}
		});

		TextView tvNear = (TextView) header
				.findViewById(R.id.tv_header_nearfriend);
		tvNear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpTo(NearFriendActivity.class, false);
			}
		});
		TextView tvRobot = (TextView) header.findViewById(R.id.tv_header_robot);
		tvRobot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpTo(RobotActivity.class, false);
			}
		});
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(getActivity(),
						UserInfoActivity.class);
				intent.putExtra("from", "friend");
				// ListView��һ��header
				String username = adapter.getItem(position - 1).getUsername();
				intent.putExtra("username", username);
				jumpTo(intent, false);

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				final MyUser user = adapter.getItem(position - 1);

				DialogUtil.showDialog(getActivity(), "ɾ������",
						"ȷ��Ҫɾ��" + user.getUsername() + "��", true,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 1)�ӷ������ˣ����µ�ǰ��¼�û���contacts�ֶ�ֵ,��userȥ������
								// �ͱ������ݿ��friend���У���user�������ݼ�¼ɾ������������ѹ�ϵ
								// 2)ɾ�������¼
								// chat���а�����֮�����е������¼ȫ��ɾ��
								// recent���а�����֮��ĻỰ��¼ɾ��
								bmobUserManager.deleteContact(
										user.getObjectId(),
										new UpdateListener() {

											@Override
											public void onSuccess() {
												// 3)��FriendFragment��ʾ���ѵ�ListView����Դ��ɾ��
												adapter.removeData(user);
												// 4)����MainActivity��refreshMessageFragment����
												// ˢ��MessageFragment��ListView�Լ�MainActivity�ĽǱ�
												((MainActivity) getActivity())
														.refreshMessageFragment();
											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												toastAndLog("ɾ������ʧ��", arg0,
														arg1);
											}
										});

							}

						});

				return true;
			}
		});

	}

	private void initView() {
		tvLetter.setVisibility(View.INVISIBLE);
		// mlvLetters.setTvLetter(tvLetter);
		mlvLetters.setOnTouchLetterListener(new OnTouchLetterListener() {

			@Override
			public void onTouchLetter(String letter) {
				listView.setSelection(adapter.getPositionForSection(letter
						.charAt(0)) + 1);
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(letter);
			}

			@Override
			public void onReleaseLetter() {
				tvLetter.setVisibility(View.INVISIBLE);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		refresh();
	}

	public void refresh() {
		// �����ٵĺ�������
		// List<MyUser> list = createFakeFriends();

		List<BmobChatUser> contacts = bmobDB.getAllContactList();

		// ����List<BmobChatUser>���һ��List<MyUser>

		List<MyUser> list = getMyUserList(contacts);

		Collections.sort(list, new Comparator<MyUser>() {
			@Override
			public int compare(MyUser lhs, MyUser rhs) {
				// return lhs.getPyName().compareTo(rhs.getPyName());//(#a-z)
				// ��'#'�ƶ���'z'����(a-z#),'#'��ҲӦ�ð����ֵ�˳������
				char mychar = '#' + 128;
				String l = lhs.getPyName();
				if (l.charAt(0) == '#') {
					l = l.replace('#', mychar);
				}

				String r = rhs.getPyName();
				if (r.charAt(0) == '#') {
					r = r.replace('#', mychar);
				}

				if (l.charAt(0) == mychar && r.charAt(0) == mychar) {
					// #���������ֵ�����
					String l2 = l.replace(mychar, lhs.getUsername().charAt(0));
					String r2 = r.replace(mychar, rhs.getUsername().charAt(0));

					return l2.compareTo(r2);

				} else {
					// #�����������������ֵ�����
					return l.compareTo(r);
				}
			}
		});
		// ������õ����ݷŵ�ListView�г���
		adapter.addItems(list, true);
	}

	/**
	 * ����List<BmobChatUser>���һ��List<MyUser>
	 * 
	 * @param contacts
	 * @return
	 */

	private List<MyUser> getMyUserList(List<BmobChatUser> contacts) {
		List<MyUser> list = new ArrayList<MyUser>();

		for (BmobChatUser bcu : contacts) {
			MyUser mu = new MyUser();
			mu.setAvatar(bcu.getAvatar());
			mu.setUsername(bcu.getUsername());
			mu.setPyName(PinYinUtil.getPinYin(bcu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(bcu.getUsername()));
			// Ϊ��ɾ������ʱ�������ṩobjectId
			mu.setObjectId(bcu.getObjectId());

			list.add(mu);
		}

		return list;
	}

	@SuppressWarnings("unused")
	private List<MyUser> createFakeFriends() {
		// �����ٵĺ�������
		List<MyUser> list = new ArrayList<MyUser>();

		MyUser mu = null;
		for (int i = 0; i < 10; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(10) + 48) + "��������");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}
		for (int i = 0; i < 100; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(26) + 65) + "��������");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}

		for (int i = 0; i < 100; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(26) + 97) + "��������");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}

		return list;
	}
}
