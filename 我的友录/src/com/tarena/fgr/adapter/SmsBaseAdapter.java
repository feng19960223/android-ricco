package com.tarena.fgr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.R;

public class SmsBaseAdapter extends MyBaseAdapter<Sms> {

	public SmsBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		// Sms sms = getItem(position);
		int type = getItemViewType(position);
		if (convertView == null) {
			// ��õ�ǰҪ��������ݲ��õĲ��ֵ�����
			// 2016��10��11�� 14:23:01
			// �����:���ﲻ�ȵõ�sms����,���ǲ���getItemViewType���õ�
			// ����û�оͽ�Adapterд��
			// int type = sms.getType();
			// if (type == 1) {// ���յ��Ķ���
			if (type == 0) {// ���յ��Ķ���
				convertView = layoutInflater.inflate(R.layout.item_left, null);
			}
			// if (type == 2) {// ���͵Ķ���
			if (type == 1) {// ���͵Ķ���
				convertView = layoutInflater.inflate(R.layout.item_right, null);
			}
			// ������������,Ԫ�ص�idһ��
			viewHolder = new ViewHolder();

			viewHolder.ImageView_photo = (ImageView) convertView
					.findViewById(R.id.imageView_photo);
			viewHolder.textView_body = (TextView) convertView
					.findViewById(R.id.textView_body);
			viewHolder.textView_time = (TextView) convertView
					.findViewById(R.id.textView_time);

			// ������������
			SMSManager.stetFontType(context, viewHolder.textView_body);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Sms sms = getItem(position);

		viewHolder.textView_body.setText(sms.getBody());
		// 2016��10��11�� 11:52:01
		// �ں���Ӧ���ж�ʱ��,���һ����֮��,��ʱ�����ص�

		if (position == 0) {
			viewHolder.textView_time.setText(sms.getFormateDate());
		} else {// 1����֮�ڲ���ʾʱ��
			Sms smsUp = getItem(position - 1);
			long time = sms.getDate() - smsUp.getDate();
			// ��ʾ��,�п�϶
			// viewHolder.textView_time
			// .setText(getTime(sms.getFormateDate(), time));
			// ����Ϊ��,������,Ӧ������
			if (time <= 1000 * 60) {
				viewHolder.textView_time.setVisibility(View.GONE);
			} else {
				viewHolder.textView_time.setVisibility(View.VISIBLE);
				viewHolder.textView_time.setText(sms.getFormateDate());
			}
		}

		// ͷ��
		Bitmap bitmap = null;
		if (sms.getType() == 1) {// ���յ��Ķ���
			// ͨ�������ͷ��id
			int photoId = CalllogManager.getPhotoidByNumber(context,
					sms.getAddress());
			// ͨ��ͷ��id��ͷ��ͼƬ
			bitmap = ContactManager.getPhotoByPhotoId(context, photoId);
		}
		if (sms.getType() == 2) {// ���͵Ķ���
			// �Լ���ͷ��
			// 2016��10��11�� 13:44:16
			// �õ��û�ֱ�ӵ�ͷ��,����û�������ͷ��,���û���,������һ��Ĭ�ϵ�
			// Ŀǰ��resĿ¼�µ���Ƭ,��Ϊ�Լ���ͷ��
			bitmap = BitmapFactory.decodeResource(convertView.getResources(),
					R.drawable.ic_contact);
		}

		if (bitmap != null) {
			// �õ�Բ��ͷ��
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			viewHolder.ImageView_photo.setImageBitmap(circleBitmap);
		}

		return convertView;
	}

	// private String getTime(String str, long time) {
	// if (time >= 1000 * 60) {
	// return str;
	// } else {
	// return "";
	// }
	// }

	@Override
	// �ı���������ʱ���õĲ��ֵ�����
	public int getItemViewType(int position) {
		// ��õ�ǰ��������ݶ���
		Sms sms = getItem(position);
		int type = sms.getType();
		// return super.getItemViewType(position);
		return type - 1;// 0��1
	}

	@Override
	// �ı���������ʱ���õĲ��ֵĸ���
	public int getViewTypeCount() {
		// return super.getViewTypeCount();
		return 2;
	}

	public class ViewHolder {
		ImageView ImageView_photo;// ͷ��
		TextView textView_time;// ʱ��
		TextView textView_body;// ����
	}

}
