package com.fgr.miaoxin.app;

import com.baidu.mapapi.SDKInitializer;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.im.BmobChat;
import cn.bmob.v3.datatype.BmobGeoPoint;
import android.app.Application;
import android.media.MediaPlayer;

public class MyApp extends Application {
	public static MyApp context;// ������
	public static MediaPlayer mediaPlayer;// ��Ч
	public static BmobGeoPoint lastPoint;// ���Դ洢��Bmob�������ϵ�λ��(��λ),��SplashActivity��ʼ��

	@Override
	public void onCreate() {
		super.onCreate();

		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		SDKInitializer.initialize(getApplicationContext());

		BmobChat.getInstance(this).init(Constant.BMOB_KEY);// һ�������3��
		// // ��ʼ��BmobSDK
		// Bmob.initialize(this, Constant.BMOB_KEY);
		// // ʹ�����ͷ���ʱ�ĳ�ʼ������
		// BmobInstallation.getCurrentInstallation(this).save();
		// // �������ͷ���
		// BmobPush.startWork(this);
		// ��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext
		// ע��÷���Ҫ��setContentView����֮ǰʵ��
		context = this;
		mediaPlayer = MediaPlayer.create(this, R.raw.notify);// ��������ļ��ر��,������ʹ��
		// ��ImageLoader��ȫ�ֳ�ʼ��
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
	}

}
