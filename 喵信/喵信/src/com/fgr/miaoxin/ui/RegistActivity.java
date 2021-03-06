package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.listener.SaveListener;

import com.dd.CircularProgressButton;
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
	@Bind(R.id.btn_regist_regist)
	CircularProgressButton btnRegist;

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
		setHeaderTitle("欢迎注册");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						jumpTo(LoginActivity.class, true);

					}
				});
	}

	// 注册完成后,直接登录,下次开启程序,也不需要登录

	/**
	 * 单击“注册”按钮，注册新用户
	 * 
	 * @param view
	 */
	@OnClick(R.id.btn_regist_regist)
	public void regist(View view) {

		// 1)判空
		if (isEmpty(etUsername, etPassword, etRePassword)) {
			return;
		}
		// 2)判断两次密码输入是否一致
		String password = etPassword.getText().toString();
		String rePassword = etRePassword.getText().toString();
		if (!password.equals(rePassword)) {
			toast("两次密码不一致");
			etPassword.setText("");
			etRePassword.setText("");
			return;
		}
		// 3)判断是否有网络
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("您在没有网络的二次元 -_-!!");
			return;
		}
		// CircularProgressButton进入工作状态
		btnRegist.setIndeterminateProgressMode(true);
		btnRegist.setProgress(50);

		// 4)构建实体类，并进行注册
		final MyUser user = new MyUser();

		user.setUsername(etUsername.getText().toString());
		// 用户密码不需要加密,Bmob后台就是密文......
		user.setPassword(etPassword.getText().toString());

		boolean gender = true;

		if (rgGender.getCheckedRadioButtonId() == R.id.rb_regist_girl) {
			gender = false;
		}

		user.setGender(gender);

		// 设置用户的位置
		user.setLocation(MyApp.lastPoint);

		// 设置用户的拼音名字
		user.setPyName(PinYinUtil.getPinYin(etUsername.getText().toString()));

		// 设置用户的首字母
		user.setLetter(PinYinUtil.getFirstLetter(etUsername.getText()
				.toString()));

		// 设置用户注册时所使用的设备ID
		user.setInstallId(BmobInstallation.getInstallationId(this));

		// 设置用户注册时所使用的设备类型
		user.setDeviceType("android");

		// 提交用户信息（signUp方法继承自BmobUser）
		user.signUp(this, new SaveListener() {

			@Override
			public void onSuccess() {
				// 登录成功后,再跳转会有一个Bug,Bmob服务器的原因,把他写到这里就没事了
				// 更新_installation数据表中
				// 当前设备所对应的数据记录的uid字段的值
				// 将其值改为刚刚注册并登录成功的用户的username
				// 更新Bmob数据库,让用户名和手机绑定
				bmobUserManager.bindInstallationForRegister(user.getUsername());
				// 登录成功后，界面跳转到主界面
				jumpTo(MainActivity.class, true);

				// 让用户马上进行登录操作（login方法继承自BmobUser）
				user.login(RegistActivity.this, new SaveListener() {

					@Override
					public void onSuccess() {
						btnRegist.setProgress(100);
						// 更新_installation数据表中
						// 当前设备所对应的数据记录的uid字段的值
						// 将其值改为刚刚注册并登录成功的用户的username
						// 更新Bmob数据库,让用户名和手机绑定
						// userManager.bindInstallationForRegister(user
						// .getUsername());
						// 登录成功后，界面跳转到主界面
						// jumpTo(MainActivity.class, true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						toastAndLog("登录失败", arg0, arg1);
					}
				});

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				btnRegist.setProgress(-1);
				switch (arg0) {
				case 202:
					toast("用户名重复");
					break;
				default:
					toastAndLog("注册失败", arg0, arg1);
					break;
				}
				// 注册失败,两秒恢复按钮样子
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						btnRegist.setProgress(0);
					}
				}, 2000);

			}
		});
	}

	@OnTextChanged(R.id.et_regist_username)
	public void recover(Editable s) {
		// 双恢复
		// 1.用户改变用户名的时候,恢复按钮,
		// 2.定时2秒,自动恢复
		btnRegist.setProgress(0);
	}

}
