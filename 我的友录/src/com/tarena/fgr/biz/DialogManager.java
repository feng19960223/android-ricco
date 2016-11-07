package com.tarena.fgr.biz;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.adapter.ContactBaseAdapter;
import com.tarena.fgr.adapter.ConversationBaseAdapter;
import com.tarena.fgr.adapter.SmsBaseAdapter;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.R;

/**
 * contact�ĵ��ͼƬ�����Ĳ�ͬ����
 * 
 * @author �����2016��10��8�� 11:15:22
 */
/**
 * @author anzhuo
 * 
 */
public class DialogManager {
	/**
	 * ����µ���ϵ��,����һ��dialog
	 * 
	 * @param context
	 */
	public static void showAddContactDialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		// builder.setIcon(icon);
		// builder.setTitle(title);
		// builder.setMessage(message);
		final AlertDialog dialog = builder.create();
		dialog.show();// ��ʾ
		View view = View.inflate(context, R.layout.daliog_add_contact, null);
		dialog.setContentView(view);
		// ��ʼ���Ի����ϵĿؼ�;
		ImageButton imageButton_close = (ImageButton) view
				.findViewById(R.id.imageButton_close);
		ImageButton imageButton_selected = (ImageButton) view
				.findViewById(R.id.imageButton_selected);

		// ������д�������͵绰
		final EditText editText_name = (EditText) view
				.findViewById(R.id.edit_dialogadd_name);
		final EditText editText_phone = (EditText) view
				.findViewById(R.id.edit_dialogadd_phone);
		// ������Ϣ
		final EditText editText_address = (EditText) view
				.findViewById(R.id.edit_dialogadd_address);
		final EditText editText_email = (EditText) view
				.findViewById(R.id.edit_dialogadd_email);

		imageButton_close.setOnClickListener(new OnClickListener() {
			// ȡ����ť
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		imageButton_selected.setOnClickListener(new OnClickListener() {
			// ȷ����ť
			@Override
			public void onClick(View v) {
				// 1�õ�����,2��������3dismiss4.ˢ��gridview
				String name = editText_name.getText().toString();
				String phone = editText_phone.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
					Toast.makeText(context, "��������ϵ�˵������͵绰����",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String address = editText_address.getText().toString();
				String email = editText_email.getText().toString();
				// ����ϵ�˵���Ϣ��ӵ���ϵ�˵��б���
				// ����ϵͳ����ϵ����ӹ���
				Intent intent = new Intent(
						ContactsContract.Intents.SHOW_OR_CREATE_CONTACT); // ��ʾ�򴴽���ϵ��

				Uri dataUri = Uri.parse("tel:" + phone);
				intent.setData(dataUri);
				// ������ϵ�˵�������Ϣ
				intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
				intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
				intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);

				context.startActivity(intent);
				dialog.dismiss();
				// ˢ������˭����˭ˢ��,��������������ʾˢ��
			}
		});
	}

	/**
	 * ����ǵ�һ��ʱ,�鿴��ϸ��Ϣ,�޸������Ϣ
	 * 
	 * @param context
	 */
	public static void showDetailContactDialog(final Context context,
			final Contact contact) {
		// ������ʾ��ϵ�˵���Ϣ�ĶԻ���
		// ��ʼ���Ի����ϵĿؼ�
		// ����position.�Ӽ����л����ϵ�˶���
		// �ٽ���ϵ�˵��������õ��Ի������Ӧ�ؼ���
		AlertDialog.Builder builder = new Builder(context);
		// builder.setIcon(icon);
		// builder.setTitle(title);
		// builder.setMessage(message);
		final AlertDialog dialog = builder.create();
		dialog.show();// ��ʾ
		View view = View.inflate(context, R.layout.daliog_information_contact,
				null);
		dialog.setContentView(view);
		// ��ʼ���Ի����ϵĿؼ�;
		ImageButton imageButton_close = (ImageButton) view
				.findViewById(R.id.imageButton_close);// ȡ��
		ImageButton imageButton_edit = (ImageButton) view
				.findViewById(R.id.imageButton_edit);// �޸�
		ImageView imageView_phone = (ImageView) view
				.findViewById(R.id.imageView_contact_photo);// ͷ��
		TextView textView_name = (TextView) view
				.findViewById(R.id.contact_name);// ����
		TextView textView_phone = (TextView) view
				.findViewById(R.id.contact_phone);// �绰
		final TextView textView_email = (TextView) view
				.findViewById(R.id.contact_email);
		TextView textView_address = (TextView) view
				.findViewById(R.id.contact_address);
		ImageView add_blackphone = (ImageView) view
				.findViewById(R.id.add_blackphone);
		ImageView send_email = (ImageView) view.findViewById(R.id.send_email);// �����ʼ�
		// ImageView send_or_call = (ImageView) view// �����˵�,��绰���߷�����,Ŀǰδʵ��
		// .findViewById(R.id.send_or_call);

		send_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(Uri.parse("mailto:"
						+ textView_email.getText().toString()));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "����");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "����");
				context.startActivity(emailIntent);
			}
		});
		add_blackphone.setOnClickListener(new OnClickListener() {
			// ��Ӻ������绰
			@Override
			public void onClick(View v) {
				String phone = contact.getPhone();
				new DBUtil(context).insertBlackNumber(phone);
				Toast.makeText(context, "����������ɹ�", Toast.LENGTH_SHORT).show();
				// ɾ����ϵ�˵���ʾ
				ContactManager.deleteContact(context, contact);// ɾ����ϵ��
				dialog.dismiss();
			}
		});

		// ͷ��
		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,// ��MyBaseAdapter��װ��context
				contact.getPhotoid());
		if (bitmap != null) {
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			imageView_phone.setImageBitmap(circleBitmap);
		}

		textView_name.setText(contact.getName());// ��������
		textView_phone.setText(contact.getPhone());// ���õ绰
		textView_email.setText(contact.getEmail());
		textView_address.setText(contact.getAddress());

		imageButton_close.setOnClickListener(new OnClickListener() {// ȡ��
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		// ����޸�
		imageButton_edit.setOnClickListener(new OnClickListener() {// �޸���ϵ�˵���Ϣ
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						// Ҫ�޸���ϵ�˵���Ϣ֮ǰҪ����ջ����е�����,��Ȼ����������ݻ���ԭ��������
						ContactManager.clearCache(contact);
						// ����һ������ϵͳ�༭��ϵ�˵�activity��ͼ
						// ����ϵͳ����ϵ�˱༭�����ʵ����ϵ�˵ı༭����
						Intent intent = new Intent(Intent.ACTION_EDIT);

						// �ĵ����ҵ���
						Uri dataUri = Uri.parse("content://contacts/people/"
								+ contact.getId());
						intent.setData(dataUri);
						// �����:��֮����2016��10��8�� 16:24:33
						// �����ǵ��ñ༭��ɺ�ֱ����ϵͳ��activity
						intent.putExtra("finishActivityOnSaveCompleted", true);
						context.startActivity(intent);
					}
				});

	}

	// ɾ����ϵ��
	public static void showDeleteContactDialog(final Context context,
			final Contact contact, final ContactBaseAdapter adapter) {
		// ����һ��ɾ����ϵ�˵ĶԻ���
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("ϵͳ��ʾ");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("ȷ��Ҫɾ������ϵ����?");// APIҪ��14
		builder.setNegativeButton("������?", null);
		builder.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// ִ����ϵ��ɾ������ʱ��ɾ��raw_contact��ϵ���˻�
				// ��ɾ����ϵ����data���е�����
				// contacts���е����ݲ���Ҫ�ֹ�ɾ��
				// ����ϵ�˵��˻���Ϣ��������Ϣ��ɾ����contentprovide��ͬ��ɾ��

				// ����ϵ�˴����ݿ���ɾ��
				ContactManager.deleteContact(context, contact);
				// �����ݴ���������ɾ��,����������UI
				adapter.removeData(contact);
				dialog.dismiss();
			}
		});
		// ��ʾ�Ի���
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// ɾ��ͨ����¼�Ի���
	public static void showDeleteCalllogDialog(final Context context,
			final Calllog calllog, final CalllogBaseAdapter adapter) {
		// ����һ��ɾ����ϵ�˵ĶԻ���
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("ϵͳ��ʾ");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("ȷ��Ҫɾ����ͨ����¼��?");// APIҪ��14
		builder.setNegativeButton("������?", null);
		builder.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// ����ϵ�˴����ݿ���ɾ��
				CalllogManager.deleteCalllog(context, calllog);

				// context.getContentResolver().delete(
				// Uri.parse("content://call_log/calls"), "number = ?",
				// new String[] { calllog.getPhone() });

				// �����ݴ���������ɾ��,����������UI
				adapter.removeData(calllog);
				dialog.dismiss();
			}
		});
		// ��ʾ�Ի���
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// ɾ�����ŶԻ���
	public static void showDeleteConversationDialog(final Context context,
			final Conversation conversation,
			final ConversationBaseAdapter adapter) {
		// ����һ��ɾ����ϵ�˵ĶԻ���
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("ϵͳ��ʾ");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("ȷ��Ҫɾ�������Զ��ż�¼��?");// APIҪ��14
		builder.setNegativeButton("������?", null);
		builder.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// �Ѷ��Ŵ����ݿ���ɾ��
				SMSManager.deleteConveration(context,
						conversation.getThread_id());
				// �����ݴ���������ɾ��,����������UI
				adapter.removeData(conversation);
				dialog.dismiss();
			}
		});
		// ��ʾ�Ի���
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// ɾ��һ��������ŶԻ���
	public static void showDeleteSmsDialog(final Context context,
			final Sms sms, final SmsBaseAdapter adapter) {
		// ����һ��ɾ����ϵ�˵ĶԻ���
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("ϵͳ��ʾ");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("ȷ��Ҫɾ�����������ż�¼��?");// APIҪ��14
		builder.setNegativeButton("������?", null);
		builder.setPositiveButton("ɾ��", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// �Ѷ��Ŵ����ݿ���ɾ��
				SMSManager.deleteSms(context, sms.get_id());
				// �����ݴ���������ɾ��,����������UI
				adapter.removeData(sms);
				dialog.dismiss();
			}
		});
		// ��ʾ�Ի���
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}
}
