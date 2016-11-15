package com.fgr.miaoxin.ui;

import android.app.Activity;
import android.os.Bundle;
import butterknife.ButterKnife;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.LogUtil;
import com.fgr.miaoxin.util.WindowUtil;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WindowUtil.translucent(getWindow());// ͸��״̬��
		ButterKnife.bind(this);// @Bind()ʹ��
		LogUtil.d("��λ��Ϣ:" + MyApp.lastPoint.getLatitude() + " / "
				+ MyApp.lastPoint.getLongitude());

	}
}
