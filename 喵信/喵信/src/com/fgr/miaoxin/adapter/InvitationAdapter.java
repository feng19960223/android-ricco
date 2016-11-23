package com.fgr.miaoxin.adapter;

import java.util.List;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.DialogUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

public class InvitationAdapter extends MyBaseAdapter<BmobInvitation> {

	public InvitationAdapter(Context context, List<BmobInvitation> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.item_newfriend_layout, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final BmobInvitation invitation = getItem(position);
		// ����ͷ��
		setAvatar(invitation.getAvatar(), vh.ivAvatar);
		// ���÷��͸���Ӻ���������û���
		vh.tvUsername.setText(invitation.getFromname());

		vh.ibAgree.setVisibility(View.VISIBLE);
		vh.ibReject.setVisibility(View.VISIBLE);
		vh.tvAdd.setVisibility(View.INVISIBLE);

		vh.ibAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ͬ����Ӻ���
				// 1. ���ݡ���Ӻ������롱�����ߵ�username���ڷ�������_user���в�ѯ��ȷ��ȷ���и��û�
				// 2. ���µ�ǰ��¼�û���_user���е����ݣ�����contacts�ֶ�ֵ��������Ӻ������롱��������Ϊ��ǰ��¼�û��ĺ���
				// 3.
				// ���µ�ǰ��¼�û�����Ӧ�ı������ݿ���tab_new_contacts���ݱ���username�����͵�������Ӻ����������ݼ�¼��statusֵ���ӳ�ʼ��2����Ϊ����1
				// 4. ��ǰ��¼�û�����Ӻ������롱�����ߵ��豸����һ����Ϣ������Ϣ��tagֵΪ"agree"
				// 5. ��ǰ��¼�û�����Ӧ�ı���������friends���в���һ�����ݼ�¼
				// 6. �ص������Լ�д�ļ������еĻص�����
				BmobUserManager.getInstance(context).agreeAddContact(
						invitation, new UpdateListener() {

							@Override
							public void onSuccess() {
								// ��Ӻ������
								vh.ibAgree.setVisibility(View.INVISIBLE);
								vh.ibReject.setVisibility(View.INVISIBLE);
								vh.tvAdd.setVisibility(View.VISIBLE);

							}

							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(context, "��Ӻ���ʧ�ܣ����Ժ�����",
										Toast.LENGTH_SHORT).show();
								Log.d("TAG", "��Ӻ���ʧ�ܣ�������룺" + arg0 + "," + arg1);

							}
						});

			}
		});

		vh.ibReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// �ܾ���Ӻ���
				// ������Ӻ������롱��tab_new_contacts���ݱ���ɾ��������
				DialogUtil.showDialog(context, "ɾ��֪ͨ",
						"��ȷ��Ҫɾ��" + invitation.getFromname() + "����Ӻ��������",
						true, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								List<BmobInvitation> list = BmobDB.create(
										context).queryBmobInviteList();

								for (BmobInvitation bi : list) {
									if (bi.getFromname().equals(
											invitation.getFromname())) {
										BmobDB.create(context).deleteInviteMsg(
												bi.getFromid(),
												String.valueOf(bi.getTime()));
									}
								}
								// ������Դ��ɾ����Ӧ������
								removeData(invitation);
							}
						});

			}
		});

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_newfriend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_newfriend_name)
		TextView tvUsername;
		@Bind(R.id.ib_item_newfriend_agree)
		ImageButton ibAgree;
		@Bind(R.id.ib_item_newfriend_reject)
		ImageButton ibReject;
		@Bind(R.id.tv_item_newfriend_add)
		TextView tvAdd;

		public ViewHolder(View convertView) {
			super();
			ButterKnife.bind(this, convertView);
		}

	}

}
