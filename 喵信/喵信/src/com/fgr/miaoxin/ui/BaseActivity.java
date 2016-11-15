package com.fgr.miaoxin.ui;

import java.util.Locale;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.WindowUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;

public abstract class BaseActivity extends Activity {

	// ��������
	// BmobIMSDK�ṩ��һ��������
	// �����������û����û��ĵ�¼���ǳ�����Ӻ��ѣ�ɾ�����ѣ�
	BmobUserManager userManager;
	// BmobIMSDK�ṩ��һ��������
	// �����������������ݣ��������ݵĴ��������͡�ɾ�����洢��
	BmobChatManager chatManager;
	// BmobIMSDK�ṩ��һ��������
	// �����������������ݿ�
	BmobDB bmobDB;

	Toast toast;

	View headerView;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowUtil.translucent(getWindow());// ͸��״̬��????�̳�����Activity��͸����???
		userManager = BmobUserManager.getInstance(MyApp.context);
		chatManager = BmobChatManager.getInstance(MyApp.context);
		bmobDB = BmobDB.create(MyApp.context);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		initLayout();
		init();
	}

	/**
	 * ������ѡ������д
	 */
	public void init() {
		// NO_OP �����ӷ�����
	}

	private void initLayout() {
		// ���Ե���setContentView(layoutId)����
		// ���Ը�������(���磺MainActivity)--->��Դ�ļ�������(activity_main)
		String clazzName = this.getClass().getSimpleName();// MainAcitivity
		if (clazzName.contains("Activity")) {
			String activityName = clazzName.substring(0,
					clazzName.indexOf("Activity")).toLowerCase(Locale.US);// main
			String resName = "activity_" + activityName;// activity_main
			// ����resName�ҵ����Ӧ��resId(����activity_main--->R.layout.activity_main)
			int resId = getResources().getIdentifier(resName, "layout",
					getPackageName());
			if (resId != 0) {
				// ȷʵ�ҵ�����ԴID(R.layout.activity_main)
				setContentView(resId);
			} else {
				setMyContentView();
			}

		} else {
			setMyContentView();
		}

		ButterKnife.bind(this);

		headerView = findViewById(R.id.headerview);

	}

	/**
	 * ������������д �ṩ������ʹ�õĲ����ļ�������
	 */
	public abstract void setMyContentView();

}
