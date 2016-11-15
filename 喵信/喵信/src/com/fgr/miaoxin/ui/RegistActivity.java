package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.NetUtil;
import com.fgr.miaoxin.util.PinYinUtil;

public class RegistActivity extends BaseActivity {
	@Bind(R.id.et_regist_username)
	EditText etUsername;
	@Bind(R.id.et_regist_password)
	EditText etPassword;
	@Bind(R.id.et_regist_repassword)
	EditText etRePassword;
	@Bind(R.id.rg_regist_gender)
	RadioGroup rgGender;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_regist);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
	}

	private void initHeaderView() {
		setHeaderTitle("��ӭע��");
		setHeaderImage(Position.LEFT, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						jumpTo(LoginActivity.class, true);

					}
				});

	}

	// ע����ɺ�,ֱ�ӵ�¼,�´ο�������,Ҳ����Ҫ��¼

	/**
	 * ������ע�ᡱ��ť��ע�����û�
	 * 
	 * @param view
	 */
	@OnClick(R.id.btn_regist_regist)
	public void regist(View view) {

		// 1)�п�
		if (isEmpty(etUsername, etPassword, etRePassword)) {
			return;
		}
		// 2)�ж��������������Ƿ�һ��
		String password = etPassword.getText().toString();
		String rePassword = etRePassword.getText().toString();
		if (!password.equals(rePassword)) {
			toast("�������벻һ��");
			etPassword.setText("");
			etRePassword.setText("");
			return;
		}
		// 3)�ж��Ƿ�������
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("��ǰ���粻����");
			return;
		}
		// 4)����ʵ���࣬������ע��
		final MyUser user = new MyUser();

		user.setUsername(etUsername.getText().toString());
		// �û����벻��Ҫ����,Bmob��̨��������......
		user.setPassword(etPassword.getText().toString());

		boolean gender = true;

		if (rgGender.getCheckedRadioButtonId() == R.id.rb_regist_girl) {
			gender = false;
		}

		user.setGender(gender);

		// �����û���λ��
		user.setLocation(MyApp.lastPoint);

		// �����û���ƴ������
		user.setPyName(PinYinUtil.getPinYin(etUsername.getText().toString()));

		// �����û�������ĸ
		user.setLetter(PinYinUtil.getFirstLetter(etUsername.getText()
				.toString()));

		// �����û�ע��ʱ��ʹ�õ��豸ID
		user.setInstallId(BmobInstallation.getInstallationId(this));

		// �����û�ע��ʱ��ʹ�õ��豸����
		user.setDeviceType("android");

		// �ύ�û���Ϣ��signUp�����̳���BmobUser��
		user.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {

				// ���û����Ͻ��е�¼������login�����̳���BmobUser��
				user.login(RegistActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						// ����_installation���ݱ���
						// ��ǰ�豸����Ӧ�����ݼ�¼��uid�ֶε�ֵ
						// ����ֵ��Ϊ�ո�ע�Ტ��¼�ɹ����û���username
						// ����Bmob���ݿ�,���û������ֻ���
						userManager.bindInstallationForRegister(user
								.getUsername());
						// ��¼�ɹ��󣬽�����ת��������
						jumpTo(MainActivity.class, true);

					}

					@Override
					public void onFailure(int arg0, String arg1) {
						toastAndLog("��¼ʧ��", arg0, arg1);

					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {

				toastAndLog("ע��ʧ��", arg0, arg1);

			}
		});

	}

}
