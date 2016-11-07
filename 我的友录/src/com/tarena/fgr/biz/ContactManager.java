package com.tarena.fgr.biz;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.util.LruCache;
import android.util.SparseArray;

import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.MainActivity;
import com.tarena.fgr.youlu.R;

/**
 * ��ϵ��ҵ������
 * 
 * @author ����� 2016��9��30�� 09:27:55
 * 
 */
// 2016��9��30�� 10:31:08
// �����:��ص����ű�[�ĵ�����ContactsContract]
// ContactsContract.Data
// ContactsContract.RawContacts
// ContactsContract.Contacts
public class ContactManager {
	// ͨ��ContactsContract API��ѯ���е���ϵ�˵���Ϣ
	// �ٷ�Ϊ�˷�ֹ�����˿�����Ա,���Զ����ݽ�����ɾ�Ĳ�,�Ӷ�Ӱ��������

	// ȥmimetype��ճ��
	public static final String MIMETYPE_NAME = "vnd.android.cursor.item/name";// ��������
	public static final String MIMETYPE_EMAIL = "vnd.android.cursor.item/email_v2";// ��������
	public static final String MIMETYPE_PHONE = "vnd.android.cursor.item/phone_v2";// �绰����
	public static final String MIMETYPE_ADDRESS = "vnd.android.cursor.item/postal-address_v2";// ��ַ����

	// �������HashMap�ĸ���Ч�����ݼ���
	// �����Ͳ���Ҫÿ�ζ�ȥ��ѯ���ݿ���
	// ǿ����,���ɱ���,Ҳ���ᱻ�������������յ�
	public static SparseArray<Contact> cacheContact = new SparseArray<Contact>();// ���浽����,����hashmap

	// ��ƻ��������һ���ռ�,���ﵽ��,���Ƴ����ȷ��������,�����ȳ�
	// ���Զ�̬���
	// ����ֻ�ΪӦ�ó������Ķ��ڴ��С
	// һ��ȡ���ڴ�1/8Ϊ����Ĵ�С
	// ģ������VM Heap��ֵΪ32
	// public static int maxSize = 1024 * 1024 * 4;// 4MB
	// 2016��10��9�� 10:29:20
	// �����:������ﲻ������ȷ��ֵ,���Ǵ�Activity��ȡֵ,����ʱ(test)����ִ���
	// 2016��9��30�� 16:29:03
	// �����:�����ķ�����֪�����Բ�?�û���
	public static int maxSize = MainActivity.MEMORY_MAX_SIZE;
	/**
	 * LruCache����������ڲ��洢�����ݶ���ǿ���ã�����ƵĻ���ռ䲻����ʱ�� ���ݲ��ᱻ���գ�
	 * 
	 * ���洢�ռ����ˣ�������������ʹ�õĴ洢�ռ�
	 * */
	// ����һ������LruCache����
	public static LruCache<Integer, Bitmap> cachePhoto = new LruCache<Integer, Bitmap>(
			maxSize) {
		// ����ÿһ��������ͼƬ�Ĵ�С
		protected int sizeOf(Integer key, Bitmap value) {
			// ��д�÷�����ͼƬ������ʱ����ͼƬ�Ĵ�С
			// return value.getRowBytes() * value.getHeight();
			return value.getByteCount();
		};
	};

	/**
	 * @param context
	 * @return������ϵ������
	 * 
	 */
	// ��һ����С��׿12Ҫ��
	public static List<Contact> getContacts(Context context) {
		List<Contact> contacts = new ArrayList<Contact>();
		// ����һ�����ݼ�����,�����������ṩ���ṩ������
		ContentResolver resolver = context.getContentResolver();

		Uri uri = ContactsContract.Contacts.CONTENT_URI;// ����uri���ʵ����ݿ���Դ

		// String[] projection = new String[]{"_id","photo_id"};
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.PHOTO_ID };

		Cursor cursor = resolver.query(uri, projection,// Ҫ��ѯ������
				null,// ����������ֵ
				null,// �����е�"?"��ֵ
				null);// ����ʽ

		while (cursor.moveToNext()) {
			Contact contact = new Contact();
			int id = cursor.getInt(cursor.getColumnIndex(projection[0]));// _id
			int photoid = cursor.getInt(cursor.getColumnIndex(projection[1]));// photo_id
			contact.setId(id);
			contact.setPhotoid(photoid);

			// �жϻ������Ƿ���ڸ���ϵ��
			if (cacheContact.get(id) != null) {
				// ����ڻ����б�������ϵ�˵���Ϣ,ֱ�Ӵӻ����л�ø���ϵ�˶���,��������ϵ�˵ļ���
				// ֱ����������ѭ��,����ִ����������
				contacts.add(cacheContact.get(id));
				continue;
			}

			// ������ϵ�˵��˻�����ϵ�˵���ϸ������Ϣ
			// name,phone,address,email
			// ���data��
			Uri dataUri = ContactsContract.Data.CONTENT_URI;
			String[] projection2 = new String[] { Data.MIMETYPE, Data.DATA1 };
			String selection = Data.RAW_CONTACT_ID + "=?";// ��ѯ����
			Cursor cursor2 = resolver.query(dataUri, projection2, selection,
					new String[] { String.valueOf(id) }, null);
			while (cursor2.moveToNext()) {
				String mimetype = cursor2.getString(cursor2
						.getColumnIndex(Data.MIMETYPE));// mimetype
				String data1 = cursor2.getString(cursor2
						.getColumnIndex(Data.DATA1));// data1
				if (MIMETYPE_NAME.equals(mimetype)) {
					contact.setName(data1);
				} else if (MIMETYPE_PHONE.equals(mimetype)) {
					contact.setPhone(data1);
				} else if (MIMETYPE_EMAIL.equals(mimetype)) {
					contact.setEmail(data1);
				} else if (MIMETYPE_ADDRESS.equals(mimetype)) {
					contact.setAddress(data1);
				}
			}
			cursor2.close();

			// ����ϵ�˵���Ϣ���л���
			cacheContact.put(id, contact);

			// ����ϵ�˶���ӵ���ϵ�˼�����
			contacts.add(contact);
		}
		cursor.close();
		return contacts;
	}

	/**
	 * ����ͷ��id����ϵ��ͷ�� 2016��9��30�� 15:33:38
	 * 
	 * @param context
	 * @param photoId��ϵ��ͷ��ͼƬid
	 * @return ��ϵ��ͷ��ͼƬ
	 */
	public static Bitmap getPhotoByPhotoId(Context context, int photoId) {
		Bitmap bitmap = cachePhoto.get(photoId);
		if (bitmap == null) {// û��ͷ��,����û�д���
			if (photoId == 0) {// û��ͷ��
				// Ĭ��ͷ��
				bitmap = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ic_contact);
				// ����1:2016��9��30�� 16:30:50
				// ���ڲ����ļ���������ͼƬ,�����б�Ҫ��������?
				// ����2:2016��10��8�� 08:42:52
				// ����ӵ�һ��"�����ϵ��",����bug,�����д��仰,�����ϵ�˵�ͷ�񲻻���ʾ
			} else {// û�л���

				ContentResolver resolver = context.getContentResolver();

				Uri uri = Data.CONTENT_URI;
				String[] projection = new String[] { Data.DATA15 };// ��ѯ����
				String selection = Data._ID + "=?";// ��ѯ����
				Cursor cursor = resolver.query(uri, projection, selection,
						new String[] { String.valueOf(photoId) }, null);

				// ֻ��һ��ͷ��,���Բ���Ҫwhileѭ����
				if (cursor.moveToNext()) {// ������,˵����ͷ��
					byte[] blob = cursor.getBlob(0);// ֻ��һ�����Եõ�0�Ϳ�����
					// ͨ������,��������,ת��ΪͼƬ
					bitmap = BitmapFactory
							.decodeByteArray(blob, 0, blob.length);
					if (bitmap != null) {
						// ��ͼƬ���绺��
						cachePhoto.put(photoId, bitmap);
					}
				}
				cursor.close();
			}
		}

		return bitmap;
	}

	/**
	 * �޸ĵ�ʱ��ʹ�ô˷���,�������Ҫ�޸ĵ����������,���ӻ����ж�ȡ,���Ǿ�����
	 * 
	 * @param contact
	 */
	public static void clearCache(Contact contact) {
		// ����ϵ�˵���Ϣ��SparseArray�������
		cacheContact.remove(contact.getId());
		// ����ϵ�˵�ͷ���LruCache�������
		cachePhoto.remove(contact.getPhotoid());
	}

	// ����ϵ�˴����ݿ���ɾ��
	public static void deleteContact(Context context, Contact contact) {
		ContentResolver resolver = context.getContentResolver();
		// ��ϵ�˵��˻���
		resolver.delete(ContactsContract.RawContacts.CONTENT_URI,
				RawContacts.CONTACT_ID + "=?",
				new String[] { String.valueOf(contact.getId()) });

		// ��ϵ�˵��˻����ݱ�
		Uri uri = Data.CONTENT_URI;
		String where = Data.CONTACT_ID + "=?";
		String[] args = new String[] { String.valueOf(contact.getId()) };
		resolver.delete(uri, where, args);
	}

}
