package com.fgr.miaoxin.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatInstallation;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.SDKInitializer;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant;
import com.fgr.miaoxin.ui.LoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApp extends Application {
	public static MyApp context;// ������
	public static MediaPlayer mediaPlayer;// ��Ч
	public static BmobGeoPoint lastPoint;// ���Դ洢��Bmob�������ϵ�λ��(��λ),��SplashActivity��ʼ��
	public static List<Activity> activities;

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
		activities = new ArrayList<Activity>();
	}

	public static void logout() {
		BmobUserManager userManager = BmobUserManager.getInstance(context);
		userManager.logout();// �ǳ�,ֻ����˷�����Session���豸�ڷ������Ľ��,�����ݱ�û���޸�
		// ���_installation���ݱ����û����豸�Ľ��
		BmobQuery<BmobChatInstallation> query = new BmobQuery<BmobChatInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(context));
		query.findObjects(context, new FindListener<BmobChatInstallation>() {

			@Override
			public void onSuccess(List<BmobChatInstallation> arg0) {
				BmobChatInstallation bci = arg0.get(0);
				bci.setUid("");// ???
				bci.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// ɱ��(�˳�)����Activity
						for (Activity activity : activities) {
							activity.finish();
						}
						Intent intent = new Intent(context, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// ������Activity������Activity,Ҫ��������
						// ������ջ,�˳�����������,����Ҫ�������ɱ������Acitivity
						context.startActivity(intent);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}

}
