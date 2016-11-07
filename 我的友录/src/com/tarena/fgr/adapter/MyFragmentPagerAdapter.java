package com.tarena.fgr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * ������
 * 
 * @author �����
 * 
 *         2016��9��29�� 16:00:20
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	// �������ڱ��������viewpager�����Ե�fragment
	private List<Fragment> fragments = new ArrayList<Fragment>();

	// FragmentPagerAdapterû���޲ι��췽��
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	// ͨ���˷�����fragment��ӵ�������
	public void addFragment(Fragment fragment) {
		if (fragment != null) {
			this.fragments.add(fragment);
		}
	}
}
