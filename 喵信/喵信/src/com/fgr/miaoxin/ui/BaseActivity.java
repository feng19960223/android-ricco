package com.fgr.miaoxin.ui;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.WindowUtil;

public abstract class BaseActivity extends FragmentActivity {

	// ��������
	// BmobIMSDK�ṩ��һ��������
	// �����������û����û��ĵ�¼���ǳ�����Ӻ��ѣ�ɾ�����ѣ�
	BmobUserManager bmobUserManager;
	// BmobIMSDK�ṩ��һ��������
	// �����������������ݣ��������ݵĴ��������͡�ɾ�����洢��
	BmobChatManager bmobChatManager;
	// BmobIMSDK�ṩ��һ��������
	// �����������������ݿ�
	BmobDB bmobDB;

	Toast toast;

	View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowUtil.translucent(getWindow());
		bmobUserManager = BmobUserManager.getInstance(MyApp.context);
		bmobChatManager = BmobChatManager.getInstance(MyApp.context);
		bmobDB = BmobDB.create(MyApp.context);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		MyApp.activities.add(this);
		initLayout();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApp.activities.remove(this);
	}

	/**
	 * ������ѡ������д
	 */
	public void init() {
		// NO_OP �����ӷ�����
	}

	private void initLayout() {
		// ���Ե���setContentView(layoutId)����
		// ���Ը�������(���磺MainActivity)--->��Դ�ļ�������(activity_main)
		String clazzName = this.getClass().getSimpleName();// MainAcitivity
		if (clazzName.contains("Activity")) {
			String activityName = clazzName.substring(0,
					clazzName.indexOf("Activity")).toLowerCase(Locale.US);// main
			String resName = "activity_" + activityName;// activity_main
			// ����resName�ҵ����Ӧ��resId(����activity_main--->R.layout.activity_main)
			int resId = getResources().getIdentifier(resName, "layout",
					getPackageName());
			if (resId != 0) {
				// ȷʵ�ҵ�����ԴID(R.layout.activity_main)
				setContentView(resId);
			} else {
				setMyContentView();
			}

		} else {
			setMyContentView();
		}

		ButterKnife.bind(this);

		headerView = findViewById(R.id.headerview);

	}

	/**
	 * ������������д �ṩ������ʹ�õĲ����ļ�������
	 */
	public abstract void setMyContentView();

	// ����headerView
	// ����headerView�еı���
	public void setHeaderTitle(String title) {
		// TextView tv = (TextView)
		// headerView.findViewById(R.id.tv_headerview_title);
		// if(title==null){
		// tv.setText("");
		// }else{
		// tv.setText(title);
		// }
		setHeaderTitle(title, Constant.CENTER);
	}

	public void setHeaderTitle(String title, int position) {
		TextView tv = (TextView) headerView
				.findViewById(R.id.tv_headerview_title);
		switch (position) {
		case Constant.START:
			tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		case Constant.CENTER:
			tv.setGravity(Gravity.CENTER);
			break;
		default:
			tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			break;
		}
		if (title == null) {
			tv.setText("");
		} else {
			tv.setText(title);
		}

	}

	public void setHeaderTitle(String title, Position position) {
		switch (position) {
		case START:
			setHeaderTitle(title, Constant.START);
			break;
		case CENTER:
			setHeaderTitle(title, Constant.CENTER);
			break;
		case END:
			setHeaderTitle(title, Constant.END);
			break;
		}
	}

	/**
	 * �趨HeaderView��ImageView
	 * 
	 * @param pos
	 *            �趨���/�Ҳ��ImageView
	 * @param resId
	 *            ImageView����ʾͼƬ����ԴID
	 * @param colorFilter
	 *            �Ƿ���ҪΪImageView����ʾ��ͼ����Ӱ�ɫ��ǰ�� �������Ҫ�ʹ���false
	 * @param listener
	 *            �Ƿ���ҪΪImageView��ӵ����¼������� �������Ҫ�ʹ���null
	 */
	public void setHeaderImage(Position pos, int resId, boolean colorFilter,
			OnClickListener listener) {

		ImageView imageView;

		if (pos == Position.START) {
			imageView = (ImageView) headerView
					.findViewById(R.id.iv_headerview_left);
		} else {
			imageView = (ImageView) headerView
					.findViewById(R.id.iv_headerview_right);
		}

		imageView.setImageResource(resId);

		if (colorFilter) {
			imageView.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		}

		if (listener != null) {
			imageView.setOnClickListener(listener);
		}
	}

	// �����˿�ʹ�ӡlog��ط���
	public void toast(String text) {
		if (!TextUtils.isEmpty(text)) {
			toast.setText(text);
			toast.show();
		}
	}

	public void log(String log) {
		if (Constant.DEBUG)
			Log.d(Constant.TAG, getClass().getSimpleName() + "�������־��" + log);
	}

	public void toastAndLog(String text, String log) {
		toast(text);
		log(log);
	}

	public void log(String log, int error, String msg) {
		log(log + ",������룺" + error + ": " + msg);
	}

	public void toastAndLog(String text, int error, String msg) {
		toast(text);
		log(text + ",���ִ��󣬴�����룺" + error + ": " + msg);
	}

	// ������ת����ط���
	public void jumpTo(Class<?> clazz, boolean isFinish) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		if (isFinish) {
			this.finish();
		}
	}

	public void jumpTo(Intent intent, boolean isFinish) {
		startActivity(intent);
		if (isFinish) {
			this.finish();
		}
	}

	/**
	 * �ж��Ƿ���δ�������ݵ�EditText
	 * 
	 * @param ets
	 *            �������Ķ��EditText
	 * @return true ��δ�������ݵ�EditText false ���е�EditText������������
	 */
	public boolean isEmpty(EditText... ets) {

		for (EditText et : ets) {
			String text = et.getText().toString();
			if (TextUtils.isEmpty(text)) {
				// TODO
				// ���ֲ�������,������ǰ����ɫһ��
				et.setError("������������");
				return true;
			}
		}

		return false;
	}

	/**
	 * ���µ�ǰ�豸�ϵ�¼�û���λ��
	 */
	public void updateUserLocation(UpdateListener listener) {
		MyUser user = bmobUserManager.getCurrentUser(MyUser.class);
		if (user != null) {
			// ���µ�ǰ�豸�ϵ�¼�û���λ��
			user.setLocation(MyApp.lastPoint);
			if (listener != null) {
				user.update(this, listener);
			} else {
				user.update(this);
			}
		}
	}

	// �ֶ�ΪheaderView���Ը�ֵ�ķ���
	public void setHeaderView(View headerView) {
		this.headerView = headerView;
	}
}
