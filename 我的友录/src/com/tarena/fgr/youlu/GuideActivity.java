package com.tarena.fgr.youlu;

import java.util.ArrayList;
import java.util.List;

import com.tarena.fgr.adapter.MyGuidePagerAdapter;
import com.tarena.fgr.view.CircleIndicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * ��������ҳ������¹�����ʾ����
 * 
 * @author �����
 */
public class GuideActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	private ViewPager viewPager = null;
	private MyGuidePagerAdapter viewPagerAdapter = null;
	private List<View> views = null;
	private CircleIndicator circleIndicator = null;
	private Button goButton = null;// ��ʼ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();// ��ʼ����ͼ
		viewPager.setOnPageChangeListener(this);
		goButton.setOnClickListener(this);
	}

	private void initViews() {
		// ��ʼ���Զ����view
		circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator1);

		// ����������Ҫ����ͼ
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(layoutInflater.inflate(R.layout.guide_one, null));
		views.add(layoutInflater.inflate(R.layout.guide_two, null));
		views.add(layoutInflater.inflate(R.layout.guide_three, null));
		viewPagerAdapter = new MyGuidePagerAdapter(views);
		viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
		viewPager.setAdapter(viewPagerAdapter);

		goButton = (Button) views.get(views.size() - 1)// �õ����һ��ҳ��İ�ť
				.findViewById(R.id.button_go);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int position, float offset, int arg2) {
		circleIndicator.updateDraw(position, offset);
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public void onClick(View v) {// ��ť�¼�
		Intent intent = new Intent(GuideActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
