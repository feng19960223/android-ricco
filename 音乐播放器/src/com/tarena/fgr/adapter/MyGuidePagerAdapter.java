package com.tarena.fgr.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyGuidePagerAdapter extends PagerAdapter {
	// �����������Ե�view
	private List<View> views = null;

	public MyGuidePagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	// ��view����Ҫ��ʱ��,���Խ�������
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	// ����view�ķ���,������listview�е�getview����
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}

	@Override
	// ���ص�ǰview������
	public int getCount() {
		return views.size();
	}

	@Override
	// �жϵ�ǰ��view�ǲ���������Ҫ�Ķ���
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
