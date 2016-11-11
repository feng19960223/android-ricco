package com.fgr.bmobdemo.app;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.constant.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;

public class MyApp extends Application {
	public static Context context;
	public static MediaPlayer palyer;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		// ��һ��Ĭ�ϳ�ʼ��
		Bmob.initialize(this, Constant.BOMB_KEY);
		// ʹ�����ͷ���ʱ�ĳ�ʼ������
		BmobInstallation.getCurrentInstallation(this).save();
		// �������ͷ���
		BmobPush.startWork(this);
		// ��ImageLoader��ȫ�ֳ�ʼ��
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		palyer = MediaPlayer.create(this, R.raw.newpost);// ��������ļ��ر��,������ʹ��
	}

}
