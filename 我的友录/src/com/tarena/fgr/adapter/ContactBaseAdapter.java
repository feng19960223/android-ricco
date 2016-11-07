package com.tarena.fgr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.R;

/**
 * ���񲼾�,��ϵ��������,����һ��ͷ���һ����ϵ������
 * 
 * @author anzhuo 2016��9��30�� 14:28:37
 * 
 */
public class ContactBaseAdapter extends MyBaseAdapter<Contact> {

	public ContactBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		// �Ż�
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.item_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView_header = (ImageView) convertView
					.findViewById(R.id.imageView_contact_photo);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_contact_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Contact contact = getItem(position);
		viewHolder.textView_name.setText(contact.getName());

		// ����ͷ��id���ͷ�����Ϣ
		// 2016��9��30�� 15:48:33
		// �����:����һֱ��Ѱ���ݿ�Ҳ����,Ӧ���Ż�һ��,����ѯ�����ͼƬ����
		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,// ��MyBaseAdapter��װ��context
				contact.getPhotoid());
		if (bitmap != null) {
			// ��ӵ�һ��
			if (position == 0) {
				viewHolder.textView_name.setText(contact.getName());
				viewHolder.imageView_header
						.setImageResource(R.drawable.ic_add_contact);
			} else {
				// ��Ӻ�����
				// ��ͷ�񷵻�ͷ��,���򷵻�Ĭ��ͼƬ
				Bitmap circleBitmap = ImageManager
						.circleBitmap(context, bitmap);
				viewHolder.imageView_header.setImageBitmap(circleBitmap);
			}
		}
		// 2016��9��30�� 15:56:28
		// �����:��inflate_contact.xml��,ֱ�Ӹ�imageView������ͼƬ,�����Ӳ���Ҫ���ж���û��ͼƬ��ʱ����
		// else {
		// viewHolder.imageView_header.setImageResource(R.drawable.ic_contact);//
		// Ĭ�Ϸ���һ��ͼƬ
		// }
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView_header = null;
		TextView textView_name = null;
	}

}
