package com.tarena.app;

import java.util.List;

import com.tarena.entity.CityNameBean;
import com.tarena.utils.SPUtil;

import android.app.Application;

public class MyApp extends Application {
	public static MyApp context;
	// ��������CityActivity�еĳ������Ƶ��ڴ滺��
	public static List<CityNameBean> cities;// Ӧ�ñ�֤�������ݵĶ�����

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		new SPUtil(context).setCloseBanner(false);// ÿ�������й��,�û��˳�ʱ,���β���ʾ
	}

}
