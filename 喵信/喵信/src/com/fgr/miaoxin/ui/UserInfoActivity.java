package com.fgr.miaoxin.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

import com.dd.CircularProgressButton;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.view.XCRoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfoActivity extends BaseActivity {
	@Bind(R.id.iv_userinfo_avatar)
	XCRoundImageView ivAvatar;
	@Bind(R.id.iv_userinfo_avatareditor)
	ImageView ivAvatarEditor;

	@Bind(R.id.tv_userinfo_nickname)
	TextView tvNickname;
	@Bind(R.id.iv_userinfo_nicknameeditor)
	ImageView ivNicknameEditor;
	@Bind(R.id.ll_userinfo_editnicknamecontainer)
	LinearLayout llNicknameContainer;
	@Bind(R.id.et_userinfo_nickname)
	EditText etNickname;
	@Bind(R.id.ib_userinfo_confirm)
	ImageButton ibConfirm;
	@Bind(R.id.ib_userinfo_cancel)
	ImageButton ibCancel;

	@Bind(R.id.tv_userinfo_username)
	TextView tvUsername;

	@Bind(R.id.iv_userinfo_gender)
	ImageView ivGender;
	// ���Ҫ�޸��Ա���ο��޸��ǳƵ�����

	@Bind(R.id.btn_userinfo_update)
	CircularProgressButton btnUpdate;
	@Bind(R.id.btn_userinfo_chat)
	Button btnChat;
	@Bind(R.id.btn_userinfo_black)
	Button btnBlack;

	// me����SettingFragment��ת����
	// friend: ��FriendFragmetn��ת����
	// stranger: ��AddFriendActivity����NewFriendActivity��ת����
	String from;
	String username;

	MyUser user;// ����username���Ի�����Ӧ���û�

	String cameraPath;// ����ͷ����ƬʱSD����·��
	String avatarUrl;// �ϴ�ͷ����Ƭ��Ϻ�ͷ����Ƭ�ڷ������ϵ���ַ

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_user_info);

	}

	@Override
	public void init() {
		super.init();
		from = getIntent().getStringExtra("from");
		if ("me".equals(from)) {
			username = bmobUserManager.getCurrentUserName();
			// log(from+" / "+username);
		} else {
			username = getIntent().getStringExtra("username");
			// log(from+" / "+username);
		}

		initHeaderView();

		initView();

	}

	private void initView() {
		bmobUserManager.queryUser(username, new FindListener<MyUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("��ѯ������Ϣʧ��", arg0, arg1);
			}

			@Override
			public void onSuccess(List<MyUser> arg0) {
				user = arg0.get(0);
				// ����user�е������趨����
				// �趨�û�ͷ��
				String avatar = user.getAvatar();
				if (TextUtils.isEmpty(avatar)) {
					ivAvatar.setImageResource(R.drawable.ic_launcher);
				} else {
					// TODO ͷ��ͼƬӦ��ʹ�ö�������
					// ÿ�ζ�Ҫ���¼���ͼƬ...
					ImageLoader.getInstance().displayImage(avatar, ivAvatar);
				}

				// ͷ���Ǧ��
				if ("me".equals(from)) {
					ivAvatarEditor.setVisibility(View.VISIBLE);
				} else {
					ivAvatarEditor.setVisibility(View.INVISIBLE);
				}

				// user���ǳ�
				String nickname = user.getNick();
				tvNickname.setText(nickname);
				llNicknameContainer.setVisibility(View.INVISIBLE);

				// �ǳƵ�Ǧ��
				if ("me".equals(from)) {
					ivNicknameEditor.setVisibility(View.VISIBLE);
				} else {
					ivNicknameEditor.setVisibility(View.INVISIBLE);
				}

				// user���û���
				String username = user.getUsername();
				tvUsername.setText(username);

				// user���Ա�
				Boolean gender = user.getGender();
				if (gender) {
					ivGender.setImageResource(R.drawable.boy);
				} else {
					ivGender.setImageResource(R.drawable.girl);
				}

				// ���ð�ť
				if ("me".equals(from)) {
					btnUpdate.setVisibility(View.VISIBLE);
				} else {
					btnUpdate.setVisibility(View.GONE);
				}

				if ("friend".equals(from)) {
					btnChat.setVisibility(View.VISIBLE);
					btnBlack.setVisibility(View.VISIBLE);
				} else {
					btnChat.setVisibility(View.INVISIBLE);
					btnBlack.setVisibility(View.INVISIBLE);
				}
			}
		});

	}

	private void initHeaderView() {
		String title = "";
		if ("me".equals(from)) {
			title = "�ҵ�����";
		} else if ("friend".equals(from)) {
			title = "��������";
		} else {
			title = username + "������";
		}
		setHeaderTitle(title);

		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	@OnClick(R.id.iv_userinfo_avatareditor)
	public void setAvatar(View view) {

		Intent intent1 = new Intent(Intent.ACTION_PICK);
		intent1.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");

		Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File file = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				System.currentTimeMillis() + ".jpg");
		cameraPath = file.getAbsolutePath();
		Uri uri = Uri.fromFile(file);
		intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);

		Intent chooser = Intent.createChooser(intent1, "ѡ��ͷ��...");
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { intent2 });
		startActivityForResult(chooser, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		try {
			super.onActivityResult(arg0, arg1, arg2);
			if (arg1 == RESULT_OK) {

				if (arg0 == 101) {

					String filePath = "";

					if (arg2 != null) {
						// ͷ��ͼƬ�Ǵ�ͼ��ѡ��
						Uri uri = arg2.getData();
						Cursor cursor = getContentResolver().query(uri,
								new String[] { Media.DATA }, null, null, null);
						cursor.moveToNext();
						filePath = cursor.getString(0);
						cursor.close();
					} else {
						// �������
						filePath = cameraPath;
					}

					crop(filePath);

				}

				if (arg0 == 102) {
					// �����ϵͳ��ͼ���򷵻صĽ�ȡ���ͼƬ
					final Bitmap bitmap = arg2.getParcelableExtra("data");
					// �ϴ�ǰ��Ҫ��bitmap���浽SD��
					// ��ñ���·�������ϴ�
					File file = new File(
							Environment
									.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
							System.currentTimeMillis() + ".jpg");
					OutputStream stream = new FileOutputStream(file);
					bitmap.compress(CompressFormat.JPEG, 100, stream);
					final BmobFile bf = new BmobFile(file);
					bf.uploadblock(this, new UploadFileListener() {

						@Override
						public void onSuccess() {
							avatarUrl = bf.getFileUrl(UserInfoActivity.this);
							log("avatarUrl:" + avatarUrl);
							ivAvatar.setImageBitmap(bitmap);
						}

						@Override
						public void onFailure(int arg0, String arg1) {
							toastAndLog("ͷ���ϴ�ʧ���Ժ�����", arg0, arg1);
						}
					});

				}

			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���ð�׿��ͼƬ���ó�����û�ѡ���ͷ����м���
	 * 
	 * @param filePath
	 *            �û�ѡȡ��ͷ����SD�ϵĵ�ַ
	 */
	private void crop(String filePath) {
		// ��ʽintent
		Intent intent = new Intent("com.android.camera.action.CROP");

		Uri data = Uri.fromFile(new File(filePath));

		intent.setDataAndType(data, "image/*");

		intent.putExtra("crop", true);
		intent.putExtra("return-data", true);
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);

		startActivityForResult(intent, 102);
	}

	@OnClick(R.id.iv_userinfo_nicknameeditor)
	public void setNickname(View v) {
		String nickname = tvNickname.getText().toString();
		tvNickname.setVisibility(View.INVISIBLE);
		llNicknameContainer.setVisibility(View.VISIBLE);
		if (TextUtils.isEmpty(nickname)) {
			etNickname.setHint("�������ǳ�...");
		} else {
			etNickname.setText(nickname);
		}
		ivNicknameEditor.setVisibility(View.GONE);
	}

	@OnClick(R.id.ib_userinfo_confirm)
	public void saveNickname(View v) {
		String nickname = etNickname.getText().toString();
		etNickname.setText("");
		tvNickname.setVisibility(View.VISIBLE);
		tvNickname.setText(nickname);
		llNicknameContainer.setVisibility(View.INVISIBLE);
		ivNicknameEditor.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.ib_userinfo_cancel)
	public void cancelNickname(View v) {
		etNickname.setText("");
		tvNickname.setVisibility(View.VISIBLE);
		llNicknameContainer.setVisibility(View.INVISIBLE);
		ivNicknameEditor.setVisibility(View.VISIBLE);
	}

	@OnClick(R.id.btn_userinfo_update)
	public void update(View v) {
		if (avatarUrl != null) {
			user.setAvatar(avatarUrl);
		}

		user.setNick(tvNickname.getText().toString());

		btnUpdate.setProgress(50);

		user.update(this, new UpdateListener() {

			@Override
			public void onSuccess() {
				btnUpdate.setProgress(100);
				toast("���ϸ������");
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						btnUpdate.setProgress(0);

					}
				}, 1500);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				btnUpdate.setProgress(-1);
				log("", arg0, arg1);
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						btnUpdate.setProgress(0);

					}
				}, 1500);

			}
		});
	}

	@OnClick(R.id.btn_userinfo_chat)
	public void chat(View v) {
		// ��ת��ChatActivity����תʱ������ͨ��intent���ݹ�ȥ
		Intent intent = new Intent(this, ChatActivity.class);
		intent.putExtra("user", user);
		jumpTo(intent, true);
	}

}