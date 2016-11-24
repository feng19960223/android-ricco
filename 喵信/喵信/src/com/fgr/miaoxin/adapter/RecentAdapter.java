package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.BadgeView;

public class RecentAdapter extends MyBaseAdapter<BmobRecent> {

	public RecentAdapter(Context context, List<BmobRecent> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_recent_layout,
					parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		BmobRecent recent = getItem(position);

		// ͷ��
		setAvatar(recent.getAvatar(), vh.ivAvatar);
		// �û���
		vh.tvUsername.setText(recent.getUserName());
		// ʱ��
		vh.tvTime.setText(TimeUtil.getTime(recent.getTime() * 1000));
		// ����
		// ���ݲ�ͬ���������ͣ�������ͬ����ʾ

		int msgType = recent.getType();
		switch (msgType) {
		case 1:// �ı����͵�������Ϣ
			vh.tvContent
					.setText(EmoUtil.getSpannableString(recent.getMessage()));
			break;

		case 2:// ͼ�����͵�������Ϣ
			vh.tvContent.setText("[ͼƬ]");
			break;
		case 3:// λ�����͵�������Ϣ
			vh.tvContent.setText("[λ��]");
			break;
		case 4:// �������͵�������Ϣ
			vh.tvContent.setText("[����]");
			break;
		default:
			throw new RuntimeException("����ȷ����Ϣ��ʽ");
		}

		// δ����Ϣ����

		int count = BmobDB.create(context).getUnreadCount(recent.getTargetid());

		// ���count��Ϊ0����BadgeView�Ժ�װ��ֵ���ʽ����ʾcount
		vh.bvCount.setBadgeCount(count);

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_recent_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_recent_name)
		TextView tvUsername;
		@Bind(R.id.tv_item_recent_time)
		TextView tvTime;
		@Bind(R.id.tv_item_recent_content)
		TextView tvContent;
		@Bind(R.id.bv_item_recent_unread)
		BadgeView bvCount;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

	}

}
