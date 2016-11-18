package com.fgr.miaoxin.fragment;

import java.lang.reflect.Method;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.SPUtil;

public class SettingFragment extends BaseFragment {
	private static final String NOTIFICATION = "notification";
	private static final String SOUND = "sound";
	private static final String VIBRATE = "vibrate";

	private static final int SWITCH_ON = 0;
	private static final int SWITCH_OFF = 1;

	@Bind(R.id.tv_setting_username)
	TextView tvUsername;

	@Bind(R.id.tv_setting_notification)
	TextView tvNotification;
	@Bind(R.id.tv_setting_sound)
	TextView tvSound;
	@Bind(R.id.tv_setting_vibrate)
	TextView tvVibrate;

	@Bind(R.id.iv_setting_editornotification)
	ImageView ivNotification;
	@Bind(R.id.iv_setting_editorsound)
	ImageView ivSound;
	@Bind(R.id.iv_setting_editorvibrate)
	ImageView ivVibrate;

	SPUtil sputil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		sputil = new SPUtil(getActivity(),
				bmobUserManager.getCurrentUserObjectId());
		initHeaderView();
		initView();
	}

	private void initHeaderView() {
		setHeaderTitle("����", Position.CENTER);
	}

	private void initView() {
		// ��ǰ��¼�û����û���
		tvUsername.setText(bmobUserManager.getCurrentUserName());
		// ���ݵ�ǰ��¼�û���ƫ�������ļ�����TextView��ImageView����ʾ
		if (sputil.isAllowNotification()) {
			switcher(NOTIFICATION, SWITCH_ON);
			ivSound.setClickable(true);
			ivVibrate.setClickable(true);
		} else {
			switcher(NOTIFICATION, SWITCH_OFF);
			ivSound.setClickable(false);
			ivVibrate.setClickable(false);
		}

		if (sputil.isAllowSound()) {
			switcher(SOUND, SWITCH_ON);
		} else {
			switcher(SOUND, SWITCH_OFF);
		}
		if (sputil.isAllowVibrate()) {
			switcher(VIBRATE, SWITCH_ON);
		} else {
			switcher(VIBRATE, SWITCH_OFF);
		}

	}

	@OnClick(R.id.iv_setting_editornotification)
	public void setNotification(View view) {
		if (!sputil.isAllowNotification()) {
			switcher(NOTIFICATION, SWITCH_ON);
			ivSound.setClickable(true);
			ivVibrate.setClickable(true);
		} else {
			switcher(NOTIFICATION, SWITCH_OFF);
			ivSound.setClickable(false);
			ivVibrate.setClickable(false);

			switcher(SOUND, SWITCH_OFF);

			switcher(VIBRATE, SWITCH_OFF);

		}
	}

	@OnClick(R.id.iv_setting_editorsound)
	public void setSound(View view) {
		if (!sputil.isAllowSound()) {
			switcher(SOUND, SWITCH_ON);
		} else {
			switcher(SOUND, SWITCH_OFF);
		}
	}

	@OnClick(R.id.iv_setting_editorvibrate)
	public void setVibrate(View view) {
		if (!sputil.isAllowVibrate()) {
			switcher(VIBRATE, SWITCH_ON);
		} else {
			switcher(VIBRATE, SWITCH_OFF);
		}
	}

	@OnClick(R.id.btn_setting_logout)
	public void logout(View v) {
		MyApp.logout();
	}

	/**
	 * ����</br> �趨ImageView�е�ͼƬ TextView�е��ı� ƫ������
	 * 
	 * @param name
	 *            NOTIFICATION SOUND VIBRATE
	 * @param state
	 *            SWITCH_ON SWITCH_OFF
	 */
	private void switcher(String name, int state) {
		try {
			// "notification"
			// ImageView��Ӧ��id
			String ivResName = "iv_setting_editor" + name;
			int ivResId = getResources().getIdentifier(ivResName, "id",
					getActivity().getPackageName());

			// TextView��Ӧ��id
			String tvResName = "tv_setting_" + name;
			int tvResId = getResources().getIdentifier(tvResName, "id",
					getActivity().getPackageName());

			if (ivResId == 0 || tvResId == 0) {
				throw new RuntimeException("δ���ҵ���ȷ����ͼ");
			}

			ImageView iv = (ImageView) getView().findViewById(ivResId);

			if (state == SWITCH_ON) {
				iv.setImageResource(R.drawable.ic_switch_on);
			} else {
				iv.setImageResource(R.drawable.ic_switch_off);
			}

			TextView tv = (TextView) getView().findViewById(tvResId);

			tv.setText((state == SWITCH_ON ? "����" : "��ֹ")
					+ (NOTIFICATION.equals(name) ? "֪ͨ"
							: (SOUND.equals(name) ? "����" : "��")));

			// ����sputil�еķ������趨ƫ�������ļ�
			// "notification"--->"setNotification"
			char[] chars = name.toCharArray();
			chars[0] -= 32;
			String methodName = "set" + new String(chars);

			Method method = sputil.getClass().getDeclaredMethod(methodName,
					boolean.class); // boolean.class��Boolean.TYPE�������Ϊboolean,Boolean.class������ĸ�ΪBoolean

			method.invoke(sputil, state == SWITCH_ON ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
