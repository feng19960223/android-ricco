package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;

/**
 * ���ŵĻ�ӭҳ��
 * 
 * 1)��λ 2)����Ч�� 3)������ת
 * 
 * @author pjy
 *
 */
public class SplashActivity extends BaseActivity {
	@Bind(R.id.tv_splash_miao)
	TextView tvMiao;// "��"

	@Bind(R.id.tv_splash_xin)
	TextView tvXin;// "��"

	LocationClient client;// �ٶȵ�ͼ��λ�ͻ���
	BDLocationListener listener;// �ٶȵ�ͼ��λ������

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_splash);
		// ButterKnife.bind(this);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void init() {
		super.init();
		getLocation();
	}

	/**
	 * ����λ
	 */
	private void getLocation() {
		client = new LocationClient(getApplicationContext()); // ����LocationClient��
		listener = new MyLocationListener();
		client.registerLocationListener(listener); // ע���������
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000 * 60 * 5;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ
		client.setLocOption(option);
		// ����λ����
		client.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			double lat = -1;
			double lng = -1;
			if (code == 61 || code == 66 || code == 161) {
				// ��λ�ɹ�
				lat = location.getLatitude();
				lng = location.getLongitude();
			} else {
				// ��λ���ɹ�
				// �ֶ�ָ��һ����γ�ȱ����˼�԰��ҵԷд��¥
				lat = 39.876402;
				lng = 116.465049;
			}
			// ��ö�λ�����ΪMyApp.lastPoint���Ը�ֵ
			MyApp.lastPoint = new BmobGeoPoint(lng, lat);

			// ��������
			initAnim();

			// ֹͣ��������λ����
			if (client.isStarted()) {
				client.unRegisterLocationListener(listener);
				client.stop();
			}

		}

	}

	/**
	 * ���Ŷ���Ч��
	 */
	public void initAnim() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
		tvMiao.startAnimation(anim);
		tvXin.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tvMiao.setVisibility(View.VISIBLE);
				tvXin.setVisibility(View.VISIBLE);
				// ������������ת����
				// ���ݵ�ǰ�豸�Ƿ��д��ڵ�¼״̬���û�
				// BmobUserManager userManager =
				// BmobUserManager.getInstance(SplashActivity.this);
				BmobChatUser user = userManager.getCurrentUser();
				if (user != null) {
					// TODO ����λ��

					// ����У���MainActivity��ת
					// Intent intent = new
					// Intent(SplashActivity.this,MainActivity.class);
					// startActivity(intent);
					// finish();
					jumpTo(MainActivity.class, true);
				} else {
					// ���û�У���LoginActivity��ת
					// Intent intent = new
					// Intent(SplashActivity.this,LoginActivity.class);
					// startActivity(intent);
					// finish();
					jumpTo(LoginActivity.class, true);
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (client != null) {
			client.stop();
			client = null;
		}

	}

}
