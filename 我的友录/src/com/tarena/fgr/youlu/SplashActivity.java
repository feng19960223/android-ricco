package com.tarena.fgr.youlu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tarena.fgr.service.BlackNumberService;

/**
 * @author �����
 * 
 *         ʱ�� 2016��9��29�� 11:52:31
 * 
 *         ����ҳ�� ��Ҫ�õ���֪ʶ����: 1.���䶯������ 2.ҳ�����ʱ�Ľ�������
 * 
 *         ������1.5��,
 * 
 *         �� ---------�� [����alpha]
 * 
 *         50%-------->-----100% [�Ŵ�scale]
 * 
 *         ��ʼ0------1>-----1.5-------->3����
 * 
 */
// 2016��9��29�� 18:20:48
// ���ҳ��
// ��ƷLogoչʾ����
// չ�ֲ�Ʒlogo
// ��ʼ��Ӧ�ó���(��ʼ�����ݿ�,�����ռ�,����service�ȼ�������)
// ���Ӧ�ó���İ汾����
// У��Ӧ���̵߳ĺϷ���(�Ƿ�����...)
public class SplashActivity extends Activity implements AnimationListener {
	private RelativeLayout relativeLayout = null;
	// private ImageView imageView_splash = null;
	// ��ȡ�汾��
	private PackageManager packageManager = null;
	private TextView textView_version = null;

	// ͨ��isFirstIn�ж�Ҫ��������Ϣ,��ֵҪ�洢����
	private boolean isFirstIn = false;
	// ��ʱ��ʱ��
	private static final int TIME = 3000;
	// ͨ���������������Ǹ�����
	// �����������ҳ��
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	@SuppressLint("HandlerLeak")
	// ��Ӧ�������̳߳�˯,������handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// ��������̬
		startService(new Intent(this, BlackNumberService.class));

		// 2016��9��29�� 18:51:36
		// �����:��imageView�Ӷ�������ֱ�Ӹ����ּӶ���,������׼����һ��Activity�Ӷ���
		// ʵ�ַ���:��SplashActivity�ĸ����ּӶ���
		// ������һ������BUG,�Ҹ�ȫ��Activity�����˶���,��������ҵ�APP����һ���ܴ�İ�ɫ����,
		// ��û���������������,͸�����س�һ��Activity
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_splash_root);
		// imageView_splash = (ImageView) findViewById(R.id.imageView_bg);
		textView_version = (TextView) findViewById(R.id.textView_version);

		packageManager = getPackageManager();
		try {
			// AndroidManifest.xml�����ļ���,manifest�ڵ��package����
			// package="com.tarena.fgr.youlu"
			// �����:�����ֽ�versionName��ֵ��Ϊ:�ſ��ը��汾��һ�Եĳ嶯...(�����Ĳ����Ǳ������)
			// ����:�ڲ��,������...
			// android:versionName="1.0"
			// һ��Ӧ�õİ���Ϣ
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			textView_version.setText("�汾��:" + versionName);
		} catch (NameNotFoundException e) {// ����û���ҵ��쳣
			// ���ᷢ������쳣 can't reach
			e.printStackTrace();
		}

		// 2016��9��29�� 18:17:20
		// �����:������ʦд��,����׼������java����дһ������
		// ����xml
		Animation animationXML = AnimationUtils.loadAnimation(this,
				R.anim.anim_splash);

		// imageView_splash.setAnimation(animationXML);
		relativeLayout.setAnimation(animationXML);

		// 2016��9��29�� 19:01:28,��ʼд����Java��ɵĶ���
		// // ����
		// ScaleAnimation animScale = new ScaleAnimation(0.5f, 1, 0.5f, 1, 0.5f,
		// 0.5f);
		// animScale.setDuration(1000);
		// animScale.setFillAfter(true);
		// // ����
		// AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
		// animAlpha.setDuration(1500);
		// animAlpha.setFillAfter(true);
		// // ��������
		// AnimationSet animSet = new AnimationSet(true);
		// ������ô�ҳ���ͣ��,���Ը�������������ʱ��,�Ͼ��еĹ����һ��ʱ��Ÿ�ǰ...
		// animSet.setDuration(2000);
		// animSet.addAnimation(animScale);
		// animSet.addAnimation(animAlpha);
		// relativeLayout.setAnimation(animSet);

		// ����������ʱ��ת����
		// 2016��9��29�� 20:35:17
		// ʵ�ִ��ڵ���ת
		// ����Handlerʵ��
		// ���������¼�
		// animationXML.setAnimationListener(this);

		init();

	}

	private void init() {

		SharedPreferences sharedPreferences = getSharedPreferences("first",
				MODE_PRIVATE);
		isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
		if (!isFirstIn) {
			handler.sendEmptyMessageDelayed(GO_HOME, TIME);
		} else {
			handler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
			// ��������������Ժ�,�ͽ���ֵ�洢����
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();// �ύ�޸�
		}
	}

	private void goHome() {
		// ������ҳ��
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void goGuide() {
		// ������������ҳ��
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	// ��������ʼ��ʱ��ִ��
	public void onAnimationStart(Animation animation) {
	}

	@Override
	// �������ظ�ִ�е�ʱ��,������Repeat����
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	// ������������ʱ��ִ��
	public void onAnimationEnd(Animation animation) {

		// Intent intent = new Intent(SplashActivity.this,
		// MainActivity.class);
		// startActivity(intent);

		// �볡�ͳ�������,�л�����
		// overridePendingTransition(enterAnim, exitAnim);
		// 2016��9��29�� 13:16:39
		// �����:���������ը��,�ҽ��ղ���,����ȡ������
		// overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

		// finish();// ������ǰҳ��
	}
}
