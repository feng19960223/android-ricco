package com.fgr.miaoxin.fragment;

import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.BaseActivity;

public abstract class BaseFragment extends Fragment {

	BmobUserManager bmobUserManager;
	BmobChatManager bmobChatManager;
	BmobDB bmobDB;

	View headerView;

	BaseActivity baseActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bmobUserManager = BmobUserManager.getInstance(MyApp.context);
		bmobChatManager = BmobChatManager.getInstance(getActivity());
		bmobDB = BmobDB.create(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = null;
		// SettingFragment;
		String clazzName = getClass().getSimpleName();
		if (clazzName.contains("Fragment")) {
			// "��׼"�Ĳ����ļ�����Ӧ����:fragment_setting
			String layoutName = clazzName.substring(0,
					clazzName.indexOf("Fragment")).toLowerCase(Locale.US);
			layoutName = "fragment_" + layoutName;
			// R.layout.fragment_setting
			int resId = getResources().getIdentifier(layoutName, "layout",
					getActivity().getPackageName());

			if (resId != 0) {
				view = inflater.inflate(resId, container, false);
			} else {
				view = createMyView(inflater, container, savedInstanceState);
			}
		} else {
			view = createMyView(inflater, container, savedInstanceState);
		}

		ButterKnife.bind(this, view);
		headerView = view.findViewById(R.id.headerview);
		baseActivity = (BaseActivity) getActivity();

		return view;
	}

	/**
	 * �����������д���ṩ��ͼ
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View createMyView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

	@Override
	public void onStart() {
		super.onStart();
		init();
	}

	public void init() {
		// No-OP ���ӷ���
	}

	// @Override
	// public void onActivityCreated(Bundle savedInstanceState) {
	// super.onActivityCreated(savedInstanceState);
	// init();
	// }

	// ����headerView�еı���
	public void setHeaderTitle(String title) {
		baseActivity.setHeaderView(headerView);
		baseActivity.setHeaderTitle(title);
	}

	public void setHeaderTitle(String title, int position) {
		baseActivity.setHeaderView(headerView);
		baseActivity.setHeaderTitle(title, position);
	}

	public void setHeaderTitle(String title, Position position) {
		baseActivity.setHeaderView(headerView);
		baseActivity.setHeaderTitle(title, position);
	}

	public void setHeaderImage(Position pos, int resId, boolean colorFilter,
			OnClickListener listener) {
		baseActivity.setHeaderView(headerView);
		baseActivity.setHeaderImage(pos, resId, colorFilter, listener);
	}

	// ��˾��log
	public void toast(String text) {
		baseActivity.toast(text);
	}

	public void log(String log) {
		baseActivity.log(log);
	}

	public void log(String log, int error, String msg) {
		baseActivity.log(log, error, msg);
	}

	public void toastAndLog(String text, String log) {
		baseActivity.toastAndLog(text, log);
	}

	public void toastAndLog(String text, int error, String msg) {
		baseActivity.toastAndLog(text, error, msg);
	}

	// ������ת����ط���
	public void jumpTo(Class<?> clazz, boolean isFinish) {
		Intent intent = new Intent(getActivity(), clazz);
		startActivity(intent);
		if (isFinish) {
			getActivity().finish();
		}
	}

	public void jumpTo(Intent intent, boolean isFinish) {
		startActivity(intent);
		if (isFinish) {
			getActivity().finish();
		}
	}
}
