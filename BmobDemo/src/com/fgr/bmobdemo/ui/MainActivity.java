package com.fgr.bmobdemo.ui;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyUser;

/**
 * ʹ��ע�͵ķ���,���������Բ�һ��,���Ƿ���ֵ�Ͳ����б����һ��
 *
 */
public class MainActivity extends Activity {
	@Bind(R.id.et_main_username)
	EditText etUsername;
	@Bind(R.id.et_main_password)
	EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	public void regist(View view) {
		Intent intent = new Intent(this, RegistActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_main_login)
	public void login(View v) {
		String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return;
		}

		// �����û������username�ڷ�����MyUser���ݱ��в����Ƿ����������û�
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		// ����һ����ѯ����
		query.addWhereEqualTo("username", username);
		// �����ѯ
		query.findObjects(this, new FindListener<MyUser>() {
			@Override
			public void onSuccess(final List<MyUser> arg0) {
				// �������Բ�ѯ��������������Ӧ
				// ����ѯ�����Ϊ�������뵽onSuccess������
				if (arg0 != null && arg0.size() > 0) {
					// ��ζ�ŷ�������MyUser���ݱ��д���"username"
					// Ϊ�û�����username�������û�
					// ��������ıȶ�
					// �û���¼ʱ���������
					String md5 = new String(Hex.encodeHex(DigestUtils
							.sha(password))).toUpperCase();
					// �û�ע��ʱ�����ڷ������ϵ�����
					String pwd = arg0.get(0).getPassword();
					if (md5.equals(pwd)) {
						// ����һ�£���¼�ɹ�

						// ------------------------
						// ����һ����Ϣ�������豸,��֪���,xxx��¼��
						// BmobPushManager<BmobInstallation> manager = new
						// BmobPushManager<BmobInstallation>(
						// MainActivity.this);
						// manager.pushMessageAll(arg0.get(0).getUsername()
						// + "������");
						// Ȼ��receiver��,�ж�,������Լ�����,����,��������
						// ------------------------

						BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>(
								MainActivity.this);
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("tag", "online");
							jsonObject.put("user", arg0.get(0).getUsername()
									+ "������");
							jsonObject.put("tiem", System.currentTimeMillis());
						} catch (Exception e) {
						}
						manager.pushMessageAll(jsonObject);
						// �����豸id
						MyUser user = arg0.get(0);
						user.setInstallationId(BmobInstallation
								.getInstallationId(MainActivity.this));
						user.update(MainActivity.this, new UpdateListener() {

							@Override
							public void onSuccess() {
								// ��ת����
								Intent intent = new Intent(MainActivity.this,
										ShowActivity.class);
								intent.putExtra("user", arg0.get(0));
								startActivity(intent);
								finish();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(MainActivity.this,
										"�����豸IDʧ��,���Ժ�����", Toast.LENGTH_SHORT)
										.show();
							}
						});

					} else {
						Toast.makeText(MainActivity.this, "�û������������",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "�û������������",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(MainActivity.this,
						"��ѯʧ�ܣ����Ժ����ԡ�������룺" + arg0 + "," + arg1,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
