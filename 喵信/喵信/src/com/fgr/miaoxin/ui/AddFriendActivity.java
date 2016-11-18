package com.fgr.miaoxin.ui;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.AddFriendAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

//ģ����ѯ�����кܶ�����,����Ҫʹ�÷�ҳ����
public class AddFriendActivity extends BaseActivity {
	@Bind(R.id.et_addfriend_username)
	EditText etUsername;

	@Bind(R.id.lv_addfriend_ptrlistview)
	PullToRefreshListView ptrListView;
	ListView listView;
	List<BmobChatUser> users;
	AddFriendAdapter adapter;

	List<BmobChatUser> friendList;// ��ǰ��¼�û��������б�

	// ģ����ѯʱÿҳ�����ֵ�������//��һҳ����ѯ�����û�
	private static final int PAGE_LIMIT = 20;// ��һ��ȽϺ�,���շ��ص�ʱ��,��ȥ���Ѿ��Ǻ��ѵ�
	int page;// ģ����ѯʱ��ҳ��

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_add_friend);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("��������");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initListView() {
		listView = ptrListView.getRefreshableView();
		users = new ArrayList<BmobChatUser>();
		adapter = new AddFriendAdapter(this);
		listView.setAdapter(adapter);

		ptrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				page += 1;

				queryUserByPage(page, etUsername.getText().toString(),
						new OnDatasLoadFinishListener<BmobChatUser>() {

							@Override
							public void onLoadFinish(List<BmobChatUser> datas) {

								ptrListView.onRefreshComplete();

								if (datas == null) {
									toast("û�и���������");
									ptrListView.setMode(Mode.DISABLED);
								} else if (datas.size() == 0) {
									page += 1;
									queryUserByPage(page, etUsername.getText()
											.toString(), this);
								} else {
									adapter.addItems(datas, false);
								}
							}
						});

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast("�����" + adapter.getItem(position - 1).getUsername());
				// TODO �鿴��ϸ��Ϣ
			}
		});
	}

	@OnClick(R.id.btn_addfriend_search)
	public void serarch(View v) {// ׼ȷ����
		// �ر�PullToRefreshListView��"ˢ�¹���"
		ptrListView.setMode(Mode.DISABLED);// �Ȳ�������,Ҳ��������
		String username = etUsername.getText().toString();
		if (TextUtils.isEmpty(username)) {
			return;// ��
		}
		adapter.clear();// �����ǰ
		if (username.equals(bmobUserManager.getCurrentUser().getUsername())) {
			return;// ��ѯ�Լ�
		}
		if (isFriend(username)) {
			toast(username + "�Ѿ����������");
			return;// �Ѿ��Ǻ���
		}
		queryUserByName(username);

	}

	private void queryUserByName(String username) {
		BmobQuery<BmobChatUser> query = new BmobQuery<BmobChatUser>();
		query.addWhereEqualTo("username", username);
		query.findObjects(this, new FindListener<BmobChatUser>() {

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				if (arg0.size() > 0) {
					// ��_user�����ҵ����û���Ϊusername���û�
					adapter.addItems(arg0, true);
				} else {
					toast("������,���޴��� -_-!!");
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("��ѯ����ʧ��,���Ժ�����", arg0, arg1);
			}
		});

	}

	/**
	 * �ж�username����Ӧ���û��Ƿ��Ѿ��ǵ�ǰ��¼�û��ĺ���
	 * 
	 * @param username
	 *            �û���
	 * @return true �Ѿ��Ǻ���
	 */
	private boolean isFriend(String username) {
		// �ӱ������ݿ���ѱ��л�õ�ǰ��¼�û������Եĺ����б�
		if (friendList == null) {// û��Ҫÿ�ζ�ȡ�����ݿ�
			// ��Ӻ��Ѻ�,Ӧ�ø���friendList
			friendList = bmobDB.getAllContactList();
		}
		for (BmobChatUser bmobChatUser : friendList) {
			if (bmobChatUser.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@OnClick(R.id.btn_addfriend_searchmore)
	public void searchMore(View v) {
		adapter.clear();
		// ��PullToRefreshListView��������
		ptrListView.setMode(Mode.PULL_FROM_END);
		final String username = etUsername.getText().toString();
		if (TextUtils.isEmpty(username)) {
			return;
		}
		// ���username�뵱ǰ��¼�û����û���һ�£�ҲӦ��
		// �����û���������ģ������
		// ���磺��ǰ��¼�û���abc�������username��abc
		// ��ô���������û������û���������abc������Щ�û�

		// ɸѡ��ǰ��¼�û��ĺ��ѣ�Ҳ��Ҫ�ڻ�ò�ѯ�������������
		page = 0;
		queryUserByPage(page, username,
				new OnDatasLoadFinishListener<BmobChatUser>() {

					@Override
					public void onLoadFinish(List<BmobChatUser> datas) {
						if (datas == null) {
							toast("û�а���" + username + "����");
						} else if (datas.size() == 0) {
							// ������һ�εĲ�ѯ
							page += 1;
							queryUserByPage(page, username, this);
						} else {
							adapter.addItems(datas, true);
						}
					}
				});
	}

	/**
	 * ��ҳ��ѯ ��ѯ_user���У��û�������username���û�
	 * 
	 * @param page
	 *            ҳ��
	 * @param username
	 *            Ҫ��ѯ���������û���
	 * @param listener
	 *            �����ѯ���ؽ��
	 */
	private void queryUserByPage(int page, final String username,
			final OnDatasLoadFinishListener<BmobChatUser> listener) {
		BmobQuery<BmobChatUser> query = new BmobQuery<BmobChatUser>();
		// �趨��ѯ����
		query.addWhereNotEqualTo("username",
				bmobUserManager.getCurrentUserName());
		// ���ڷ�ҳ���趨
		// ���Բ�ѯ�����ǰPAGE_LIMIT*page������
		query.setSkip(PAGE_LIMIT * page);
		// �趨һ����෵�ض��ٸ�����
		query.setLimit(PAGE_LIMIT);
		query.findObjects(this, new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("��ѯ�û�ʱ�������Ժ�����", arg0, arg1);

			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// 1)arg0.size()==0 _user���ݱ���ȷʵû�а���username���û�
				// 2)arg0.size()>0
				// 2.1 �а���username���û�,�����û�ȫ�����ǵ�ǰ��¼�û��ĺ��ѣ�����
				// 2.2 ͨ��ɾѡ��ʣ�����ݣ����Էŵ�ListView�г���
				if (arg0.size() > 0) {
					// ��arg0���ǵ�ǰ��¼�û��������û����а���username�ķ���list
					List<BmobChatUser> list = new ArrayList<BmobChatUser>();
					// ����ɾѡ
					for (BmobChatUser bcu : arg0) {
						if (!isFriend(bcu.getUsername())
								&& bcu.getUsername().contains(username)) {
							list.add(bcu);
						}
					}
					// list�������������
					// list.size()==0 �ӷ��������ص��������ݣ�û��һ��ͨ��ɾѡ
					// list.size()>0 �ӷ��������ص���������������ͨ��ɾѡ
					listener.onLoadFinish(list);

				} else {
					listener.onLoadFinish(null);
				}

			}
		});
	}
}
