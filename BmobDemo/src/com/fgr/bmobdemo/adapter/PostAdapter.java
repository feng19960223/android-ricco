package com.fgr.bmobdemo.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.DeleteListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;
import com.fgr.bmobdemo.ui.PostActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PostAdapter extends BaseAdapter {

	Context context;
	List<MyPost> posts;
	LayoutInflater inflater;

	MyUser currentUser;

	public PostAdapter(Context context, List<MyPost> posts, MyUser currentUser) {
		super();
		this.context = context;
		this.posts = posts;
		this.inflater = LayoutInflater.from(context);
		this.currentUser = currentUser;

	}

	@Override
	public int getCount() {
		return posts.size();
	}

	@Override
	public MyPost getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_post_layout, parent,
					false);
			vh = new ViewHolder(convertView);
			// vh.ivAvatar = (ImageView)
			// convertView.findViewById(R.id.iv_item_avatar);
			// vh.tvUsername = (TextView)
			// convertView.findViewById(R.id.tv_item_username);
			// vh.tvTitle = (TextView)
			// convertView.findViewById(R.id.tv_item_title);
			// vh.tvTime = (TextView)
			// convertView.findViewById(R.id.tv_item_posttime);
			// vh.tvContent = (TextView)
			// convertView.findViewById(R.id.tv_item_content);
			// vh.tvDelete = (TextView)
			// convertView.findViewById(R.id.tv_item_delete);
			// vh.tvUpdate = (TextView)
			// convertView.findViewById(R.id.tv_item_update);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final MyPost post = getItem(position);

		MyUser user = post.getUser();

		// �趨ͷ��
		String avatar = user.getAvatar();
		if (TextUtils.isEmpty(avatar)) {
			// �û�ע��ʱδ�趨ͷ��
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		} else {
			// �û�ע��ʱ�趨��ͷ��
			ImageLoader.getInstance().displayImage(avatar, vh.ivAvatar);
		}

		// �û���
		vh.tvUsername.setText(user.getUsername());

		// ���ӵı���
		vh.tvTitle.setText(post.getTitle());

		// ���ӵ�ʱ��
		if (post.getCreatedAt().equals(post.getUpdatedAt())) {
			vh.tvTime.setText("������:" + post.getCreatedAt());
		} else {
			vh.tvTime.setText("�༭�ڣ�" + post.getUpdatedAt());
		}

		// ���ӵ�����
		vh.tvContent.setText(post.getContent());

		// ���º�ɾ��
		// �����position��λ�������ӵ������ǵ�ǰ��¼�û�
		// "����"��"ɾ��"�ſɼ�
		if (user.getUsername().equals(currentUser.getUsername())) {
			vh.tvDelete.setVisibility(View.VISIBLE);
			vh.tvUpdate.setVisibility(View.VISIBLE);
		} else {
			vh.tvDelete.setVisibility(View.INVISIBLE);
			vh.tvUpdate.setVisibility(View.INVISIBLE);
		}

		// �����ɾ����������positionλ���ϵ�����ɾ����
		vh.tvDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("��ʾ");
				builder.setIcon(android.R.drawable.ic_menu_info_details);
				builder.setMessage("��ȷʵҪɾ����������");
				builder.setNegativeButton("ȡ��", null);
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								MyPost delPost = new MyPost();

								// delPost.setObjectId(post.getObjectId());

								// ɾ������
								delPost.delete(context, post.getObjectId(),
										new DeleteListener() {

											@Override
											public void onSuccess() {
												// ���ݴӷ�������MyPost���ݱ���ɾ���ɹ�
												Toast.makeText(context, "ɾ���ɹ�",
														Toast.LENGTH_SHORT)
														.show();
												// �ӻ������������

												// ��ListView����Դ�н���Ӧ������ɾ��
												// ˢ��ListView
												remove(post);

											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												Toast.makeText(
														context,
														"ɾ��ʧ�ܣ����Ժ����ԡ�������룺"
																+ arg0 + ","
																+ arg1,
														Toast.LENGTH_SHORT)
														.show();
											}
										});
							}
						});
				builder.create().show();
			}
		});

		vh.tvUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����position��λ���ϵġ����ӡ����ݵ�PostActivtiy
				// ��PostActivity��������ӵĸ���
				Intent intent = new Intent(context, PostActivity.class);
				intent.putExtra("from", "update");
				intent.putExtra("post", post);
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	public void addAll(List<MyPost> list, boolean isClear) {
		if (isClear) {
			posts.clear();
		}
		posts.addAll(list);
		notifyDataSetChanged();
	}

	public void remove(MyPost post) {
		posts.remove(post);
		notifyDataSetChanged();
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_title)
		TextView tvTitle;
		@Bind(R.id.tv_item_posttime)
		TextView tvTime;
		@Bind(R.id.tv_item_content)
		TextView tvContent;
		@Bind(R.id.tv_item_delete)
		TextView tvDelete;
		@Bind(R.id.tv_item_update)
		TextView tvUpdate;

		public ViewHolder(View convertView) {

			ButterKnife.bind(this, convertView);
		}

	}

}
