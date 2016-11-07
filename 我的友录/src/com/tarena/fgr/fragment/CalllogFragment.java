package com.tarena.fgr.fragment;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;

/**
 * ͨ����¼
 * 
 * @author ����� 2016��9��29�� 16:36:42
 * 
 */
public class CalllogFragment extends BaseFragment implements
		OnItemLongClickListener, OnItemClickListener {
	private ListView listView = null;
	private CalllogBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ����Ϊfalse,container�����Ǹ��ڵ�,���������Ĳ���
		contentView = inflater.inflate(R.layout.fragment_calllog, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// ��ʼ�����Ե�UI�ؼ�
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_calllog);

		initialActionbar(-1, "ͨ����¼", -1);
		adapter = new CalllogBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(this);
		listView.setOnItemClickListener(this);
		refreshCalllog();
	}

	private void refreshCalllog() {
		// ͨ����¼������
		List<Calllog> list = CalllogManager.getCalllogs(getActivity());
		adapter.addItems(list, true);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshCalllog();// �������±�����ʱ,ˢ������
	}

	@Override
	// ����ɾ��ͨ����¼
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Calllog calllog = adapter.getItem(position);
		DialogManager.showDeleteCalllogDialog(getActivity(), calllog, adapter);
		return true;
	}

	@Override
	// �����б���
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String phone = adapter.getItem(position).getPhone();
		Intent intent = new Intent();
		// ֱ�Ӳ���
		// intent.setAction(Intent.ACTION_CALL);
		// ��ת�����Ž���
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone));
		// ��ת�����Ž���
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// �绰�������ǿ�Ƶ�ϵͳ��ͨ����¼...
		// intent.putExtra("finishActivityOnSaveCompleted", true);
		// �����ǵ��ñ༭��ɺ�ֱ����ϵͳ��activity
		// �������Ĵ���û��������
		startActivity(intent);
	}
}
