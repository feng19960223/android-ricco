package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.dd.CircularProgressButton;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.NetUtil;

public class LoginActivity extends BaseActivity {
	@Bind(R.id.et_login_password)
	EditText etPassword;
	@Bind(R.id.et_login_username)
	EditText etUsername;
	@Bind(R.id.btn_login_login)
	CircularProgressButton btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_login);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
	}

	private void initHeaderView() {
		setHeaderTitle("��ӭʹ��");
		setHeaderImage(Position.START, R.drawable.ic_launcher, false, null);
	}

	@OnClick(R.id.tv_login_regist)
	public void regist(View v) {// ע��
		// Intent intent = new Intent(this,RegistActivity.class);
		// startActivity(intent);
		// finish();
		jumpTo(RegistActivity.class, true);
	}

	@OnClick(R.id.btn_login_login)
	public void login(View v) {// ��¼
		// 1)�п�
		if (isEmpty(etUsername, etPassword)) {
			return;
		}
		// 2)�ж��Ƿ�������
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("����û������Ķ���Ԫ -_-!!");
			return;
		}
		// 3)��¼����
		// CircularProgressButton���빤��״̬
		btnLogin.setIndeterminateProgressMode(true);
		btnLogin.setProgress(50);

		// BmobChatUser user = null;
		BmobChatUser user = new BmobChatUser();
		user.setUsername(etUsername.getText().toString());
		user.setPassword(etPassword.getText().toString());

		// ��¼�ɹ������ε�������������������
		// checkAndBindInstallation
		// ���û��ڵ�ǰ�豸��¼�󣬻�ȥ_installation���ݱ��м��
		// ���û��Ƿ��������豸����Ȼ�����ŵ�¼״̬������ǣ���ӵ�ǰ�豸
		// �������豸����һ����Ϣ{"tag":"offline"}
		// �����豸���յ�����Ϣʱ��Ӧ�����õ�ǰ��¼�û�ǿ�����ߵĴ���
		// ��ǰ�豸�ڷ�����Ϣ��Ϻ󣬸��µ�ǰ�豸��_installation���ݱ��ж�Ӧ���ݼ�¼��uid�ֶ�ֵ
		// ����Ϊ��ǰ��¼�û����û���
		// updateInstallIdForUser
		// ���µ�ǰ��¼�û���_user���ж�Ӧ�����ݼ�¼��installId��deviceType�����ֶε�ֵ
		// ����Ϊ��ǰ��ʹ���豸���豸ID��deviceType��Ϊ��android��

		// userManager.login�ڲ���ɵ���������:��ǰ�ʺ����������豸��¼,Ȼ������һ�����...,(������һ���ʺ�ͬʱ��¼�����豸)
		bmobUserManager.login(user, new SaveListener() {
			@Override
			public void onSuccess() {
				// ��¼�ɹ�
				btnLogin.setProgress(100);
				// ���µ�¼�û���λ��
				updateUserLocation(new UpdateListener() {
					@Override
					public void onSuccess() {
						jumpTo(MainActivity.class, true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						toastAndLog("�޷���ȡ����λ��", arg0, arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				switch (arg0) {
				case 101:
					toast("�û������������");
					break;
				default:
					btnLogin.setProgress(-1);
					toastAndLog("��¼ʧ��,�Ժ�����", arg0, arg1);
					break;
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						btnLogin.setProgress(0);
					}
				}, 2000);
			}
		});
	}

	@OnTextChanged(R.id.et_login_password)
	public void recover1(Editable s) {
		btnLogin.setProgress(0);
	}

	@OnTextChanged(R.id.et_login_username)
	public void recover2(Editable s) {
		btnLogin.setProgress(0);
	}

}
