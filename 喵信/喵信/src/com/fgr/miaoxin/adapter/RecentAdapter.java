package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.BadgeView;
import com.fgr.miaoxin.view.XCRoundImageView;

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
		setAvatar(recent.getAvatar(), vh.ivAvatar);
		vh.tvUsername.setText(recent.getUserName());
		// ʱ��
		// recent.getTime�ķ���ֵ������long������ʱ��ĵ�λ����,��ֹBmob�����ը
		vh.tvTime.setText(TimeUtil.getTime(recent.getTime() * 1000));

		// TODO(���˸ɾ�����չ�Ͳ��Ǻܺ�,���������,ͨ��,�仯̫��,����д����)
		// ���� �������һ��������Ϣ�����ͣ�������vh.tvContent�г���ʲô
		// typeΪ1���ı����͵�������Ϣ vh.tvContent����ʾgetMessage()���ص�����
		// typeΪ2��ͼƬ���͵�������Ϣ vh.tvContent����ʾ[ͼƬ]
		// typeΪ3��λ�����͵�������Ϣ vh.tvContent����ʾ[λ��]
		// typeΪ4���������͵�������Ϣ vh.tvContent����ʾ[����]
		int type = recent.getType();
		switch (type) {
		case 1:
			// vh.tvContent.setText(recent.getMessage());
			vh.tvContent
					.setText(EmoUtil.getSpannableString(recent.getMessage()));
			break;
		case 2:
			vh.tvContent.setText("[ͼƬ]");
			break;
		case 3:
			vh.tvContent.setText("[λ��]");
			break;
		case 4:
			vh.tvContent.setText("[����]");
			break;
		default:
			vh.tvContent.setText("[��ȷ������Ϣ��ʽ]");
			break;
		}

		// δ����Ϣ����
		int count = BmobDB.create(context).getUnreadCount(recent.getTargetid());
		vh.bvCount.setBadgeCount(count);// ��Ϊ0��ʾ,�����0���ǲ���ʾ,���Ի�����!

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_recent_avatar)
		XCRoundImageView ivAvatar;
		@Bind(R.id.tv_item_recent_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_recent_time)
		TextView tvTime;// �Ự�����һ����Ϣ������ʱ��
		@Bind(R.id.tv_item_recent_content)
		TextView tvContent;// �Ự�����һ����Ϣ������
		@Bind(R.id.bv_item_recent_count)
		BadgeView bvCount;// �Ự������δ����Ϣ������

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
	}
}
