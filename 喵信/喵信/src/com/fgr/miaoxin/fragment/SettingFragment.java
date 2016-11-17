package com.fgr.miaoxin.fragment;

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
	@Bind(R.id.tv_setting_username)
	TextView tvUsername;
	@Bind(R.id.tv_setting_notification)
	TextView tvNotification;
	@Bind(R.id.tv_setting_sound)
	TextView tvSound;
	@Bind(R.id.tv_setting_vibrate)
	TextView tvVibrate;

	@Bind(R.id.iv_setting_editornotification)
	ImageView ivEditorNotification;
	@Bind(R.id.iv_setting_editorsound)
	ImageView ivEditorSound;
	@Bind(R.id.iv_setting_editorvibrate)
	ImageView ivEditorVibrate;

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
		sputil = new SPUtil(bmobUserManager.getCurrentUserObjectId());
		initHeaderView();
		initView();
	}

	private void initHeaderView() {
		setHeaderTitle("����", Position.CENTER);
	}

	private void initView() {
		tvUsername.setText(bmobUserManager.getCurrentUserName());
		if (sputil.isAllowNotification()) {
			tvNotification.setText("����֪ͨ");
			ivEditorNotification.setImageResource(R.drawable.ic_switch_on);
		} else {
			tvNotification.setText("�ܾ�֪ͨ");
			ivEditorNotification.setImageResource(R.drawable.ic_switch_off);
			ivEditorSound.setClickable(false);
			ivEditorVibrate.setClickable(false);
		}
		if (sputil.isAllowSound()) {
			tvSound.setText("��������");
			ivEditorSound.setImageResource(R.drawable.ic_switch_on);
		} else {
			tvSound.setText("��ֹ����");
			ivEditorSound.setImageResource(R.drawable.ic_switch_off);
		}
		if (sputil.isAllowVibrate()) {
			tvVibrate.setText("������");
			ivEditorVibrate.setImageResource(R.drawable.ic_switch_on);
		} else {
			tvVibrate.setText("��ֹ��");
			ivEditorVibrate.setImageResource(R.drawable.ic_switch_off);
		}

	}

	@OnClick(R.id.iv_setting_editornotification)
	public void setNotification(View v) {
		if (!sputil.isAllowNotification()) {
			sputil.setNotification(true);

			tvNotification.setText("����֪ͨ");
			ivEditorNotification.setImageResource(R.drawable.ic_switch_on);

			ivEditorSound.setClickable(true);
			ivEditorVibrate.setClickable(true);

		} else {
			sputil.setNotification(false);

			tvNotification.setText("�ܾ�֪ͨ");
			ivEditorNotification.setImageResource(R.drawable.ic_switch_off);

			tvSound.setText("��ֹ����");
			ivEditorSound.setImageResource(R.drawable.ic_switch_off);
			ivEditorSound.setClickable(false);
			sputil.setSound(false);

			tvVibrate.setText("��ֹ��");
			ivEditorVibrate.setImageResource(R.drawable.ic_switch_off);
			ivEditorVibrate.setClickable(false);
			sputil.setVibrate(false);
		}
	}

	@OnClick(R.id.iv_setting_editorsound)
	public void setSound(View v) {
		if (!sputil.isAllowSound()) {
			sputil.setSound(true);
			tvSound.setText("��������");
			ivEditorSound.setImageResource(R.drawable.ic_switch_on);
		} else {
			sputil.setSound(false);
			tvSound.setText("��ֹ����");
			ivEditorSound.setImageResource(R.drawable.ic_switch_off);
		}
	}

	@OnClick(R.id.iv_setting_editorvibrate)
	public void setVibrate(View v) {
		if (!sputil.isAllowVibrate()) {
			sputil.setVibrate(true);
			tvVibrate.setText("������");
			ivEditorVibrate.setImageResource(R.drawable.ic_switch_on);
		} else {
			sputil.setVibrate(false);
			tvVibrate.setText("��ֹ��");
			ivEditorVibrate.setImageResource(R.drawable.ic_switch_off);
		}
	}

	@OnClick(R.id.btn_setting_logout)
	public void logout(View v) {
		MyApp.logout();
	}
}
