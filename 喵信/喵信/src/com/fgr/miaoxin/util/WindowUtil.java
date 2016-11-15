package com.fgr.miaoxin.util;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * ͸��״̬��,����ʽ״̬��</br>ÿ��Activity�����ļ����д���</br>android:clipToPadding="true" </br>
 * android:fitsSystemWindows="true"
 * 
 * @author �����
 */
public class WindowUtil {
	/**
	 * ͸��״̬��
	 * 
	 * @param window
	 */
	public static void translucent(Window window) {// getWindow();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 ȫ͸��ʵ��
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(
					View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
							| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4ȫ͸��״̬��
			window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
	}
}
