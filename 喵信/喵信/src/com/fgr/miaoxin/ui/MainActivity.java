package com.fgr.miaoxin.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatInstallation;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

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
		userManager.logout();// �ǳ�,ֻ����˷�����Session���豸�ڷ������Ľ��,�����ݱ�û���޸�
		// ���_installation���ݱ����û����豸�Ľ��
		BmobQuery<BmobChatInstallation> query = new BmobQuery<BmobChatInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<BmobChatInstallation>() {

			@Override
			public void onSuccess(List<BmobChatInstallation> arg0) {
				BmobChatInstallation bci = arg0.get(0);
				// TODO 2016��11��16�� 12:21:13
				bci.setUid("");
				bci.update(MainActivity.this, new UpdateListener() {

					@Override
					public void onSuccess() {
						jumpTo(LoginActivity.class, true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
		jumpTo(LoginActivity.class, true);
	}

}
