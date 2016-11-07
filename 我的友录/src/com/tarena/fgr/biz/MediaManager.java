package com.tarena.fgr.biz;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * һ��С��Ч,���ڵ�����Ű�ť
 * 
 * @author �����
 * 
 */
public class MediaManager {
	// ������Ч�ض���
	public static SoundPool soundPool = null;

	/**
	 * ��Ч
	 * 
	 * @param context
	 * @param resId
	 *            ��Դid
	 */
	public static void playMusic(Context context, int resId) {
		if (soundPool == null) {
			// ֧�ֶ��ٸ�����,��Դ����,��Դ������(Ԥ��)[0]
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		}
		// ���ּ������ʱִ��
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// Load���ص�resid,����������,����������,���ȼ�,�Ƿ�ѭ��-1����ѭ��0��ѭ������Ϊѭ������,��������
				soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
			}
		});
		// ������,��Դid,priority ���ȼ�(Ԥ��)[1]
		soundPool.load(context, resId, 1);
	}

	// �ͷ�������Դ
	public static void release() {
		if (soundPool != null) {
			soundPool.release();
		}
	}
}
