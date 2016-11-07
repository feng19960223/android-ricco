package com.tarena.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.tarena.adapter.MyGuidePagerAdapter;
import com.tarena.groupon.R;
import com.viewpagerindicator.CirclePageIndicator;

public class GuideActivity extends FragmentActivity {
	private ViewPager viewPager = null;
	private MyGuidePagerAdapter adapter = null;
	private CirclePageIndicator circlePageIndicator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViewPager();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewPager_guide);
		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

		adapter = new MyGuidePagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		circlePageIndicator.setViewPager(viewPager);

		// ͨ����������
		// final float density = getResources().getDisplayMetrics().density;
		// circlePageIndicator.setBackgroundColor(0xFFCCCCCC);
		// circlePageIndicator.setRadius(10 * density);

		// �뾶ֵ10dp�ڵ�ǰ�豸������Ӧ������ֵ
		// density ��Ļ��ʾ�ܶ� linch�ڿ�����ʾ���ٸ����ص�
		// dp�Ǿ��Գ��ȣ�1dp=1/160inch 160dp=linch=25.4mm
		// ���ܶ� ldpi 120px/inch 1dp=0.75px
		// ���ܶ� mdpi 160px/inch 1dp=1px
		// ���ܶ� hdpi 240px/inch 1dp=1.5px
		// �ܸ��ܶ� xhdpi 320px/inch
		// �ܸܺ��ܶ�
		// circlePageIndicator.setPageColor(0x880000FF);
		// circlePageIndicator.setFillColor(0xFF888888);
		// circlePageIndicator.setStrokeColor(0xFF000000);
		// circlePageIndicator.setStrokeWidth(2 * density);

		// ���ΪViewPagerָ����һ��PagerIndicator��ʱ��,
		// ��ΪViewPager�󶨻���������ʱ��,�����PagerIndicator��
		circlePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// ��4������û��,ָʾ��
				if (arg0 == 3) {
					circlePageIndicator.setVisibility(View.INVISIBLE);
				} else {
					circlePageIndicator.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
