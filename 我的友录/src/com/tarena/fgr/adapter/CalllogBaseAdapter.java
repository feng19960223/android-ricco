package com.tarena.fgr.adapter;

import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CallLog.Calls;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CalllogBaseAdapter extends MyBaseAdapter<Calllog> {

	public CalllogBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_calllog, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView_photo = (ImageView) convertView
					.findViewById(R.id.imageView_photo);
			viewHolder.imageView_warning = (ImageView) convertView
					.findViewById(R.id.imageView_warning);
			viewHolder.imageView_call = (ImageView) convertView
					.findViewById(R.id.imageView_call);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			viewHolder.textView_phone = (TextView) convertView
					.findViewById(R.id.textView_phone);
			viewHolder.textVIew_date = (TextView) convertView
					.findViewById(R.id.textVIew_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// ���Ҫ��������ݶ���
		Calllog calllog = getItem(position);
		// �����İ��������
		// 2016��10��9�� 15:18:04
		// �����:����������ϵ������,�о���һ���ظ�,����֪����ô����,����������ظ�д,�ͻ����bug...
		if (TextUtils.isEmpty(calllog.getName())) {// û������
			viewHolder.textView_name.setTextColor(Color.RED);
			viewHolder.textView_name.setText("δ֪��ϵ��");
			// ��ʾ��̾��
			viewHolder.imageView_warning.setVisibility(View.VISIBLE);
		} else {// �����ͨѶ¼����
			viewHolder.textView_name.setTextColor(Color.BLACK);
			viewHolder.imageView_warning.setVisibility(View.INVISIBLE);
			viewHolder.textView_name.setText(calllog.getName());
		}

		viewHolder.textView_phone.setText(calllog.getPhone());
		viewHolder.textVIew_date.setText(calllog.getFormatCallTimeString());
		if (calllog.getType() == Calls.OUTGOING_TYPE) {// ����ȥ�ĵ绰
			// Calls.INCOMING_TYPE;������ĵ绰
			// ����Ǵ��ȥ�ĵ绰,��ʾСͼƬ
			viewHolder.imageView_call.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageView_call.setVisibility(View.INVISIBLE);
		}

		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,
				calllog.getPhotoid());// �õ�ͷ��
		if (bitmap != null) {
			// �õ�Բ��ͷ��
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			viewHolder.imageView_photo.setImageBitmap(circleBitmap);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView_photo = null;// ͷ��
		ImageView imageView_warning = null;// δ֪��ϵ��
		ImageView imageView_call = null;// ͨ������
		TextView textView_name = null;// ��ϵ������
		TextView textView_phone = null;// �绰
		TextView textVIew_date = null;// ʱ��
	}

}
