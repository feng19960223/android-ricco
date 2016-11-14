package com.fgr.bmobdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.adapter.PostAdapter;
import com.fgr.bmobdemo.app.MyApp;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;
import com.fgr.bmobdemo.receiver.MyPushMessageReceiver;
import com.fgr.bmobdemo.receiver.MyPushMessageReceiver.EventListener;

public class ShowActivity extends Activity implements EventListener {
	// ��MainActivity���ݹ����ģ���ǰ��¼�û�
	MyUser user;
	@Bind(R.id.iv_header_add)
	ImageView ivAdd;
	@Bind(R.id.iv_header_refresh)
	ImageView ivRefresh;
	@Bind(R.id.pb_header_refresh)
	ProgressBar pbLoading;
	@Bind(R.id.lv_show_listviews)
	ListView listView;
	@Bind(R.id.iv_header_newpost)
	ImageView ivNewPost;
	List<MyPost> posts;
	PostAdapter adapter;

	// ����һ ,ͨ�����㲥,��ʾ��������
	// NewPostReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		ButterKnife.bind(this);
		user = (MyUser) getIntent().getSerializableExtra("user");
		initHeaderView();
		initListView();
	}

	private void initListView() {
		posts = new ArrayList<MyPost>();
		adapter = new PostAdapter(this, posts, user);
		listView.setAdapter(adapter);
	}

	private void initHeaderView() {
		ivAdd.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		ivRefresh.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ����һ
		// receiver = new NewPostReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("com.fgr.bmobdemo.ACTION_NEW_POST");
		// registerReceiver(receiver, filter);
		// ������
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		// unregisterReceiver(receiver);// д��super֮ǰ
		MyPushMessageReceiver.unregist(this);
		super.onPause();
	}

	private void refresh() {
		ivRefresh.setVisibility(View.INVISIBLE);
		pbLoading.setVisibility(View.VISIBLE);
		ivNewPost.setVisibility(View.INVISIBLE);
		// ������MyPost���ݱ��е����ӷŵ�ListView�г���
		BmobQuery<MyPost> query = new BmobQuery<MyPost>();
		// ��ѯ����
		// �����ӵ�������Ϣ��MyUser����ȡ����
		query.include("user");
		// �������ݱ�Ļ�������������
		query.order("-createdAt");
		// �����ѯ
		query.findObjects(this, new FindListener<MyPost>() {
			@Override
			public void onSuccess(List<MyPost> arg0) {
				// TODO ˼·��
				// ��arg0�������ӵķ���ʱ���������
				// ����Ĺ��򣺽��ַ�����ʽ��ʱ��(yyyy-MM-dd HH:mm:ss)
				// ����SimpleDateFormatתΪlong��ʱ���
				// �Ƚ�ʱ����Ĵ�С�õ���������
				// ������ٽ����ݷŵ�ListView�г���
				adapter.addAll(arg0, true);
				ivRefresh.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(ShowActivity.this,
						"���Ӳ�ѯʧ�ܣ�������룺" + arg0 + "," + arg1, Toast.LENGTH_SHORT)
						.show();
				ivRefresh.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
			}
		});

	}

	@OnClick(R.id.iv_header_add)
	public void jumpTo(View v) {
		Intent intent = new Intent(this, PostActivity.class);
		intent.putExtra("from", "new");
		intent.putExtra("user", user);
		startActivity(intent);
	}

	@OnClick(R.id.iv_header_refresh)
	public void refreshPost(View v) {
		refresh();
	}

	// ������
	@Override
	public void onNewPost() {// ������
		ivNewPost.setVisibility(View.VISIBLE);
		MyApp.palyer.start();
	}

	@Override
	public void onAtone() {// @
		ivNewPost.setVisibility(View.VISIBLE);
		MyApp.palyer.start();
		Toast.makeText(this, "�ո����˷���@����", Toast.LENGTH_SHORT).show();
	}

	// ����һ
	// public class NewPostReceiver extends BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// if ("com.fgr.bmobdemo.ACTION_NEW_POST".equals(action)) {
	// // ��ʾ���
	// ivNewPost.setVisibility(View.VISIBLE);
	// // ��������
	// MyApp.palyer.start();
	// }
	// }
	// }
}
