package com.tarena.fgr.youlu;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tarena.fgr.adapter.MyFragmentPagerAdapter;
import com.tarena.fgr.fragment.CalllogFragment;
import com.tarena.fgr.fragment.ContactFragment;
import com.tarena.fgr.fragment.DialpadFragment;
import com.tarena.fgr.fragment.SmsFragment;

// �̳�FragmentActivity
public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {
	private ViewPager viewPager = null;
	private MyFragmentPagerAdapter pageradapter = null;// ����������
	private Fragment calllogFragment = null;// ͨ����¼
	private Fragment smsFragment = null;// ����Ϣ
	private Fragment contactFragment = null;// ��ϵ��
	private Fragment dialpadFragment = null;// ����
	private RadioGroup bottomRadioGroup = null;// ѡ��ť

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// ��ʼ�����е�Fragment
		initialFragments();
		// ��ʼ��RadioGroup
		initialRadioGroup();

		ActivityManager aManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// ����ֻ�ΪӦ�ó������Ķ��ڴ��С
		MEMORY_MAX_SIZE = aManager.getMemoryClass() * 1024 * 1024 / 8;
	}

	public static int MEMORY_MAX_SIZE = 0;

	private void initialRadioGroup() {
		bottomRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_bottom);
		// ��RadioGroup���ѡ�����仯ʱ�ļ����¼�
		// 2016��9��29�� 17:26:55
		// �����:���ü�����ʱ��,��ע��:��������,ѡ��RadioGroup���Ǹ�,�����Ǹ�С����,�������˻�ܶ��˵�...
		bottomRadioGroup.setOnCheckedChangeListener(this);
	}

	private void initialFragments() {
		// ��ʼ��viewpage
		viewPager = (ViewPager) findViewById(R.id.viewPager_main);
		// ��������������
		FragmentManager fm = getSupportFragmentManager();
		pageradapter = new MyFragmentPagerAdapter(fm);

		// ����4��Fragment����
		calllogFragment = new CalllogFragment();
		smsFragment = new SmsFragment();
		contactFragment = new ContactFragment();
		dialpadFragment = new DialpadFragment();

		// ��4��Fragment������ӵ��������ļ�����
		// 2016��9��29�� 16:43:16
		// �����:����ط�����Ϊ�ǲ����ʵ�,������˳�����,�����ҳ�治�Ե����
		pageradapter.addFragment(calllogFragment);
		pageradapter.addFragment(contactFragment);
		pageradapter.addFragment(smsFragment);
		pageradapter.addFragment(dialpadFragment);

		// ��������������viewpager��
		viewPager.setAdapter(pageradapter);
		viewPager.setCurrentItem(1);// Ĭ����ʾ��1������
		// ��viewpager���û���ʱ�ļ����¼�
		viewPager.setOnPageChangeListener(this);
	}

	/**
	 * RaidoGroup��ѡ���¼�����
	 * */
	@Override
	// RadioGroup��ѡ����ı�ʱ
	// ������ѡ��ͬ�ĵ��ťʱ,�ı�viewpager�е�ǰ��ʾ��fragment
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// 2016��9��29�� 19:22:04
		// �����:��switch�Ĵ�������ظ�����,�������µĽ���,����RadioGroup��˵,ѡ���ǲ���̫���,���Կ��Խ���switch
		// ���ѡ��ܶ�,���ǿ���ѡ��ListView��ʵ��
		switch (checkedId) {
		case R.id.radio_calllog:// ͨ����
			// viewPager.setCurrentItem(0);
			viewPager.setCurrentItem(0, false);
			// 2016��9��29�� 17:35:08
			// �����:�������Ļ�����ʽ,falseû�л�����
			// true���л�����,����Ϊ����false�Ǻõ�,û�ж���Ч��,�����������Եĸߴ���,���
			// �����г��ϴ����false*****
			break;
		case R.id.radio_contact:// ��ϵ��
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.radio_sms:// ����Ϣ
			viewPager.setCurrentItem(2, false);
			break;
		case R.id.radio_dialpad:// ����
			viewPager.setCurrentItem(3, false);
			break;
		default:
			break;
		}
		// 2016��9��29�� 19:17:10
		// �����:���ͬ�����뷨,����ͨ��RadioBtuuon��λ��,ȷ��ViewPager��λ��,���Ҵ�����ݽ׶�
		// ���ҷ���,����������¹���Button,���п��������ڴ�...������findViewById���������ڳ�����Ż�
		RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
		int index = group.indexOfChild(radioButton);
		// indexofchild��getchildat()����
		viewPager.setCurrentItem(index, false);

		// if (checkedId == R.id.radio_calllog) {// ͨ����¼
		// viewPager.setCurrentItem(0);
		// } else if (checkedId == R.id.radio_contact) {// ��ϵ��
		// viewPager.setCurrentItem(1);
		// } else if (checkedId == R.id.radio_sms) {// ����Ϣ
		// viewPager.setCurrentItem(2);
		// } else if (checkedId == R.id.radio_dialpad) {// ����
		// viewPager.setCurrentItem(3);
		// }
	}

	/**
	 * viewpager�Ļ����¼�����
	 * */
	@Override
	// ѡ��ҳ��ʱִ��
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// 2016��9��29�� 17:03:02
		// �����:�������������������������,������ֺܺ��������,ҳ������ť���޷�������
		// ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
	}

	@Override
	// �������ִ��
	// ��������viewpager��ѡ��ͬ��ҳ��ʱ,��ص��˷���
	// �������ص���ʱ����ҳ���ڼ����е�����������
	public void onPageSelected(int position) {
		// 2016��9��29�� 17:10:48
		// �����:������������д�Ļ�������,����ʦд����swith,��д��һ��,����ʦȴд�˶���,��֪��Ϊʲô?
		// �Ѷ��Ǻ��滹Ҫ�Ӵ���?�����Ժ���չ?
		// ������Ϊ�ҵĴ����������,��Ϊ�Ժ������ӻ�ɾ����radiobutton,�Ҳ���Ҫ�޸�����Ĵ���
		// ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
		switch (position) {
		case 0:
			bottomRadioGroup.check(R.id.radio_calllog);
			break;
		case 1:
			bottomRadioGroup.check(R.id.radio_contact);
			break;
		case 2:
			bottomRadioGroup.check(R.id.radio_sms);
			break;
		case 3:
			bottomRadioGroup.check(R.id.radio_dialpad);
			break;
		default:
			break;
		}
	}

	@Override
	// ���ڻ����ı�ʱִ��
	public void onPageScrollStateChanged(int state) {
	}
}
