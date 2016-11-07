package com.tarena.fgr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author ����� 2016��9��30�� 14:08:04
 * 
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	private List<T> datas = new ArrayList<T>();
	protected Context context = null;
	protected LayoutInflater layoutInflater = null;

	public MyBaseAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	// �������
	/**
	 * @param t���һ������
	 */
	public void addItem(T t) {
		if (t != null) {
			datas.add(t);
		}
		notifyDataSetChanged();// ֪ͨ��������
	}

	/**
	 * ���isCleanΪtrue,��Ҫ���ԭ��������
	 * 
	 * @param ts
	 *            ��Ӷ������
	 * @param isClean
	 *            �Ƿ���Ҫ��ԭ�������������
	 */
	public void addItems(List<T> ts, boolean isClean) {
		if (isClean) {
			datas.clear();
		}
		datas.addAll(ts);
		notifyDataSetChanged();
	}

	/**
	 * ɾ��һ������
	 * 
	 * @param tҪɾ��������
	 */
	public void removeData(T t) {
		if (t != null) {
			datas.remove(t);
		}
		notifyDataSetChanged();
	}

	/**
	 * ������Ե�����
	 * 
	 * @return list
	 */
	public List<T> getDatas() {
		return this.datas;
	}

	@Override
	/** �������ݼ���item���ݵĸ��� */
	public int getCount() {
		return datas.size();
	}

	@Override
	/** ���ض�Ӧλ�õ�item�е����� */
	public T getItem(int position) {
		return datas.get(position);
	}

	@Override
	/** ����ֱ�ӷ���position */
	public long getItemId(int position) {
		return position;
	}

	@Override
	/** �˷������ڹ���item,ÿ��ʾһ��item��Ҫִ��һ�δ˷��� */
	// ֻ�м̳д���,��Ҫ��д�˷���
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

}
