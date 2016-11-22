package com.fgr.miaoxin.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.InvitationAdapter;
import com.fgr.miaoxin.constant.Constant.Position;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobInvitation;

/**
 * ���ֵ�ǰ��¼�û����յ��ġ���Ӻ������롱
 * 
 *
 */
public class NewFriendActivity extends BaseActivity {

	@Bind(R.id.lv_newfriend_listview)
	ListView listView;
	List<BmobInvitation> invitations;
	InvitationAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_new_friend);

	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {

		setHeaderTitle("��Ӻ���", Position.START);

		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initListView() {

		invitations = new ArrayList<BmobInvitation>();

		adapter = new InvitationAdapter(this, invitations);

		listView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {

		// �ӱ������ݿ��tab_new_contacts���ݱ���ȡ������
		// ��ΪListView������Դ
		List<BmobInvitation> list = bmobDB.queryBmobInviteList();

		// "ɸѡ"
		Set<String> set = new HashSet<String>();
		List<BmobInvitation> filterList = new ArrayList<BmobInvitation>();

		for (BmobInvitation bi : list) {
			if (!set.contains(bi.getFromname())) {
				filterList.add(bi);
				set.add(bi.getFromname());
			}
		}

		adapter.addItems(filterList, true);
	}
}
