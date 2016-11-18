package com.fgr.miaoxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgr.miaoxin.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	private List<T> datasource = new ArrayList<T>();
	protected Context context = null;
	protected LayoutInflater layoutInflater = null;

	public MyBaseAdapter(Context context, List<T> datasource) {
		super();
		this.context = context;
		this.datasource = datasource;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyBaseAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);// from�ڲ����������һ��
	}

	@Override
	/** �������ݼ���item���ݵĸ��� */
	public int getCount() {
		return datasource.size();
	}

	@Override
	/** ���ض�Ӧλ�õ�item�е����� */
	public T getItem(int position) {
		return datasource.get(position);
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

	// �������
	/**
	 * @param t���һ������
	 */
	public void addItem(T t) {
		if (t != null) {
			datasource.add(t);
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
	public void addItems(List<T> ts, boolean isClear) {
		if (isClear) {
			datasource.clear();
		}
		datasource.addAll(ts);
		notifyDataSetChanged();
	}

	/**
	 * ɾ��һ������
	 * 
	 * @param tҪɾ��������
	 */
	public void removeData(T t) {
		if (t != null) {
			datasource.remove(t);
		}
		notifyDataSetChanged();
	}

	/**
	 * �����������
	 */
	public void clear() {
		datasource.clear();
		notifyDataSetChanged();
	}

	/**
	 * ������Ե�����
	 * 
	 * @return list
	 */
	public List<T> getDatas() {
		return this.datasource;
	}

	/**
	 * ����ͼƬ
	 * 
	 * @param url
	 * @param iv
	 */
	public void setAvatar(String url, ImageView iv) {
		if (TextUtils.isEmpty(url)) {
			iv.setImageResource(R.drawable.ic_launcher);
		} else {
			ImageLoader.getInstance().displayImage(url, iv);
		}
	}

	// �Զ��������
	Typeface Fonts;

	public void stetFontType(Context context, TextView textView) {
		if (Fonts == null) {
			AssetManager manager = context.getAssets();
			String path = "fonts/customfont.ttf";// �Զ��������
			Fonts = Typeface.createFromAsset(manager, path);
		}
		// ������Ӧ�õ��ؼ���
		textView.setTypeface(Fonts);
	}
}
