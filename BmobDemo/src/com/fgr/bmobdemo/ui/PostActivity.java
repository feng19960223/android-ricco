package com.fgr.bmobdemo.ui;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;

public class PostActivity extends Activity {
	@Bind(R.id.iv_header_back)
	ImageView ivBack;
	@Bind(R.id.et_post_title)
	EditText etTitle;
	@Bind(R.id.et_post_content)
	EditText etContent;
	MyUser user;
	String from;// update new
	@Bind(R.id.btn_post_post)
	Button btnPost;
	@Bind(R.id.btn_post_update)
	Button btnUpate;
	MyPost updatePost;// ��PostAdapter׼�����µ�����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		ButterKnife.bind(this);
		user = (MyUser) getIntent().getSerializableExtra("user");
		initHeaderView();
		from = getIntent().getStringExtra("from");// update,new
		if ("new".equals(from)) {
			// ��ShowActivity���ͷ�����Ӻš���ť��ת����
			btnPost.setVisibility(View.VISIBLE);
			btnUpate.setVisibility(View.GONE);
		} else {
			// ��PostAdpater��������¡���ת����
			btnPost.setVisibility(View.GONE);
			btnUpate.setVisibility(View.VISIBLE);
			updatePost = (MyPost) getIntent().getSerializableExtra("post");
			etTitle.setText(updatePost.getTitle());
			etContent.setText(updatePost.getContent());
		}
	}

	private void initHeaderView() {
		ivBack.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
	}

	@OnClick(R.id.iv_header_back)
	public void backTo(View v) {
		finish();
	}

	@OnClick(R.id.btn_post_post)
	public void post(View v) {
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			return;
		}
		final MyPost post = new MyPost();
		post.setTitle(title);
		post.setContent(content);
		post.setUser(user);
		post.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(PostActivity.this, "�����ɹ�", Toast.LENGTH_SHORT)
						.show();
				etTitle.setText("");
				etContent.setText("");
				push(post);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(PostActivity.this,
						"����ʧ�ܣ�������룺" + arg0 + "," + arg1, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	protected void push(MyPost myPost) {
		// ������Ϣ
		BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>(
				this);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", "new");
			jsonObject.put("user", myPost.getUser().getUsername());
			jsonObject.put("time", System.currentTimeMillis());
		} catch (Exception e) {
		}
		manager.pushMessageAll(jsonObject);
	}

	/**
	 * @param v
	 */
	@OnClick(R.id.btn_post_update)
	public void updatePost(View v) {
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			return;
		}
		MyPost newPost = new MyPost();
		newPost.setObjectId(updatePost.getObjectId());
		newPost.setTitle(title);
		newPost.setContent(content);
		// updatePost.setTitle(title);
		// updatePost.setContent(content);
		newPost.update(this, new UpdateListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(PostActivity.this, "���Ӹ������", Toast.LENGTH_SHORT)
						.show();
				// TODO ���³ɹ����Ƿ����etTitle,etContent
				// �رս���ص�ShowActivity�����ݸ�����Ҫ��
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(PostActivity.this,
						"���Ӹ���ʧ�ܣ����Ժ����ԡ�������룺" + arg0 + "," + arg1,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
