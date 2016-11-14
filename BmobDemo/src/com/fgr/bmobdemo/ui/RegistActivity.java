package com.fgr.bmobdemo.ui;

import java.io.File;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RegistActivity extends Activity {
	@Bind(R.id.et_regist_username)
	EditText etUsername;
	@Bind(R.id.et_regist_password)
	EditText etPassword;
	@Bind(R.id.rg_regist_gender)
	RadioGroup rgGender;
	@Bind(R.id.iv_regist_avatar)
	ImageView ivAvatar;

	// ����ͷ����Ƭ��洢��Ƭ��·��
	String photoPath;
	// ͷ��ͼƬ�ϴ��ɹ���ͼƬ����ĵ�ַ
	String avatarUrl;
	// ���ϴ�ͷ��ͼƬʱ���û���ʾ
	ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);
		ButterKnife.bind(this);
	}

	@OnClick(R.id.btn_regist_regist)
	public void regist(View v) {
		registUser();
	}

	protected void registUser() {
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return;
		}
		MyUser user = new MyUser();
		user.setUsername(username);
		// ��password����MD5ת��
		String md5 = new String(Hex.encodeHex(DigestUtils.sha(password)))
				.toUpperCase();
		user.setPassword(md5);
		// ͷ��
		if (!TextUtils.isEmpty(avatarUrl)) {
			user.setAvatar(avatarUrl);
		} else {
			user.setAvatar("");
		}
		boolean gender = true;
		if (rgGender.getCheckedRadioButtonId() == R.id.rb_regist_girl) {
			gender = false;
		}
		user.setGender(gender);
		// ���û����󱣴浽Bmob������
		// ��ӵ�ǰ��ʹ���豸���豸id
		user.setInstallationId(BmobInstallation.getInstallationId(this));

		user.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(RegistActivity.this, "����ɹ�", Toast.LENGTH_SHORT)
						.show();
				avatarUrl = null;
				ivAvatar.setImageResource(R.drawable.ic_launcher);
				etUsername.setText("");
				etPassword.setText("");
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				switch (arg0) {
				case 401:
					Toast.makeText(RegistActivity.this, "�û����ظ�",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(RegistActivity.this,
							"����ʧ�ܣ�������룺" + arg0 + ":" + arg1,
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	@OnClick(R.id.iv_regist_avatar)
	public void setAvatar(View view) {
		// ����Intentѡ����(IntentChooser)ʵ�ֵ����Ի���
		// ���û��������ջ���ѡͼ����Ϊͷ��
		// ������ͼ���intent
		Intent intent1 = new Intent(Intent.ACTION_PICK);
		intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				"image/*");
		// ������ϵͳ���ճ����intnet
		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// ����ϵͳ���ճ���������Ϻ���Ƭ�����λ��
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				System.currentTimeMillis() + ".jpg");
		photoPath = file.getAbsolutePath();
		Uri value = Uri.fromFile(file);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, value);

		// ����Intentѡ����(IntentChooser)
		Intent intent = Intent.createChooser(intent1, "ѡ��ͷ��...");
		intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent2 });

		startActivityForResult(intent, 101);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 101) {
			// ��data���ҵ���Ϊͷ��ͼƬ�ı��ص�ַ
			String filePath;
			if (data != null) {
				// ͷ��ͼƬ���û���ͼ��ѡ���ͼƬ
				// uri���û�ѡȡ��ͼƬ��MeidaStroe���ݱ��λ��
				Uri uri = data.getData();
				// ����uri�ҵ���ͼƬ��SD�ϵ���ʵλ��
				Cursor cursor = getContentResolver().query(uri,
						new String[] { MediaStore.Images.Media.DATA }, null,
						null, null);
				cursor.moveToNext();
				filePath = cursor.getString(0);
				cursor.close();
			} else {
				// ͷ��ͼƬ���û���������������
				filePath = photoPath;
			}
			// ��ʾһ����ʾ�򣬸����û������ϴ�
			pd = ProgressDialog.show(this, "", "�ϴ���...");
			// ����BmobSDK�ṩ��BmobFile���ϴ�ͼƬ
			final BmobFile bf = new BmobFile(new File(filePath));
			bf.uploadblock(this, new UploadFileListener() {
				@Override
				public void onSuccess() {
					// �ϴ��ļ���ͼƬ���ɹ�
					// ��ȡ���ļ���ͼƬ���ڷ������ϵĵ�ַ
					avatarUrl = bf.getFileUrl(RegistActivity.this);
					// ����ImageLoader���û�ѡȡ��ͼƬ�ŵ�ivAvatar����ʾ
					ImageLoader.getInstance().displayImage(avatarUrl, ivAvatar);
					// ����ʾ����ʧ
					pd.dismiss();
				}

				@Override
				public void onFailure(int arg0, String arg1) {
					Toast.makeText(RegistActivity.this,
							"�ϴ�ͷ��ʧ�ܣ����Ժ����ԡ�������룺" + arg0 + "," + arg1,
							Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
