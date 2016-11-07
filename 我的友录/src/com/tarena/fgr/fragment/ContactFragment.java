package com.tarena.fgr.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tarena.fgr.adapter.ContactBaseAdapter;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.R;

/**
 * ��ϵ��
 * 
 * @author �����
 * 
 */
public class ContactFragment extends BaseFragment implements
		OnItemClickListener, OnItemLongClickListener {
	private GridView gridView = null;
	private ContactBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ����Ϊfalse,container�����Ǹ��ڵ�,���������Ĳ���
		contentView = inflater.inflate(R.layout.fragment_contact, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// ��ʼ�����Ե�UI�ؼ�
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		gridView = (GridView) contentView.findViewById(R.id.gridView_contact);

		// ����һ����ѯ��СͼƬ
		initialActionbar(R.drawable.ic_add_icon, "��ϵ��", -1);
		ImageView imageView_add = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_left);
		imageView_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogManager.showAddContactDialog(getActivity());
			}
		});

		// 2016��9��30�� 14:36:54
		// GridView�����Ա�������Ϊ
		// android:layout_height="match_parent"
		// �����Ż�û������,ԭ��:ÿ�ζ�ȥ���¼��㲼��

		adapter = new ContactBaseAdapter(getActivity());
		gridView.setAdapter(adapter);

		refreshContact();// ��ϵ������
		// �����¼�
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);// ����ɾ��
	}

	private void refreshContact() {
		// ���鵽������,��ӵ�������������
		// 2016��9��30�� 14:59:52
		// �����:����˼·��ĺܰ�,�����ҾͿ��������д�첽��
		// ���Էŵ��첽������м���,���ݼ��ز���Ҫ֪ͨ����,��MyBaseAdapter��,�ײ�,�������Ǿ��Ѿ�֪ͨ������
		List<Contact> list = ContactManager.getContacts(getActivity());
		// ��һ��
		// �������ǵ�һ�������ϵ�˰�ť
		// adapter.addItem(new Contact(-1, "�����ϵ��", "", "", "", 0));
		// ���Ե���ϵ������
		// adapter.addItems(list, false);// ��ӵ�ͬʱ,�����ǰ��������ϵ��

		// �ڶ���
		// 2016��10��8�� 14:11:05�Ľ����ݵ����
		list.add(0, new Contact(-1, "�����ϵ��", "", "", "", 0));
		adapter.addItems(list, true);// ��ӵ�ͬʱ,�����ǰ��������ϵ��
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {// �����ϵ��
			DialogManager.showAddContactDialog(getActivity());
			// refreshContact();//��������
			// 2016��10��8�� 14:06:16
			// ��Activity����������,д��onResume��
			// ˢ������
			// adapter.notifyDataSetChanged();
			// �����2016��10��8�� 12:37:32
			// ˢ������û��

		} else {
			// ��ʾ��ϵ�˵���ϸ��Ϣ
			Contact contact = adapter.getItem(position);
			DialogManager.showDetailContactDialog(getActivity(), contact);
		}
	}

	@Override
	// ������������ɺ�,ˢ�����ݿ���д������
	// dialog������ʱ���ִ��
	public void onResume() {
		super.onResume();
		// adapter.addItems(null, true);//ˢ�µ�ʱ��,��Ҫɾ��ǰ�������
		// 2016��10��8�� 14:13:46
		// �����:ʹ�õ�һ��ʱ,��Ϊ����ֵ��falseû�����ԭ��������,����Ҫ���һ������,�ٸ�����ʾ
		// �ڶ���,����һ�������ϵ�˵�������ӵ�list��,����true,Ĭ�����,ֻ��Ҫˢ�¾ͺ���
		refreshContact();// ˢ����ϵ��UI����
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (position != 0) {// ����ɾ����һ�������ϵ��
			Contact contact = adapter.getItem(position);
			DialogManager.showDeleteContactDialog(getActivity(), contact,
					adapter);
		}
		return true;// ����ִ�е����¼�,�¼��ַ�����
	}
}
