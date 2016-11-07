package com.tarena.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tarena.groupon.R;
import com.tarena.utils.SPUtil;

public class SplashActivity extends Activity {
	SPUtil spUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		spUtil = new SPUtil(this, "first");
		// ������ʾ1.5�����ת
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// �ж��û��Ƿ��ǵ�һ������
				Intent intent = null;
				if (spUtil.isFirst()) {
					spUtil.setFirst(false);
					// ��һ��������ת����������
					intent = new Intent(SplashActivity.this,
							GuideActivity.class);
				} else {
					// ���ǵ�һ��������ת����ҳ��
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 1500);
	}
}
