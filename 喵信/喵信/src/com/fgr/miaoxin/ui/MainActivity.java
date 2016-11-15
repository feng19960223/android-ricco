package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.LogUtil;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("��λ��Ϣ:" + MyApp.lastPoint.getLatitude() + " / "
				+ MyApp.lastPoint.getLongitude());

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {
		super.init();
	}

	@OnClick(R.id.btn_main_logout)
	public void logout(View v) {
		userManager.logout();// �ǳ�
		jumpTo(LoginActivity.class, true);
	}

}
