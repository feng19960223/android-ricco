package com.tarena.fgr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.HttpMusicManager;
import com.tarena.fgr.manager.HttpMusicManager.LoadMusicListener;
import com.tarena.fgr.utils.LogUtils;

public class MusicService extends Service {
	MediaPlayer player = null;
	MusicReceiver musicReceiver = null;
	int seekTime;// ��¼����������ͣλ��
	public static boolean isPause = false;// ��¼���ֵ����ڲ���״̬

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i("���ֲ��ŷ�������");
		player = new MediaPlayer();
		// Ϊ���������ü�����
		player.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				LogUtils.i("���ּ������,�ص��˷���");
				mp.start();
			}
		});
		player.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				LogUtils.i("���ּ��س���ʱִ��,����״̬��:" + what);
				return false;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				LogUtils.i("���ֲ������");
				mp.stop();
				// ������һ�׸��ɿ��ƽ���Ľ�����SeekBar����,����������
				// ��������һ���µĹ㲥,����һ���µ�����
				// ������������ͣ��ʱ�򲥷���һ��
				isPause = false;
				// ���㲥,�����ƽ���,seekber��0
				Intent intent = new Intent();
				intent.setAction(IURL.MUSIC_NEXT_ACTION);
				sendBroadcast(intent);
			}
		});
		// ע�Ქ�ſ��ƽ��淢���Ĺ㲥
		registMusicReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();// �߳�ֹͣ
		player.release();
		player = null;
		unregisterReceiver(musicReceiver);
	}

	Thread thread = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (player.isPlaying()) {
						// ���㲥,������
						Intent intent = new Intent();
						// ��ǰ����ʱ��
						int currentPosition = player.getCurrentPosition();
						intent.putExtra("currentPositionKey", currentPosition);
						intent.setAction(IURL.MUSIC_UP_PROGRESS_ACTION);
						sendBroadcast(intent);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();

		HttpMusicManager.asyncLoadMusics(IURL.LOADMUSICS_URL,
				new LoadMusicListener() {

					@Override
					public void onMusicsLoadEnd(List<Music> musics) {
						// �Ѽ�����ϵ����ֵ���Ϣͨ�����㲥�ķ�ʽת��С���
						Intent intent = new Intent();
						intent.setAction(IURL.MUSIC_LIST_ACTION);
						intent.putExtra("musiclistKey",
								(ArrayList<Music>) musics);
						sendBroadcast(intent);
					}
				});

		return super.onStartCommand(intent, flags, startId);
	}

	private void registMusicReceiver() {
		musicReceiver = new MusicReceiver();
		IntentFilter filter = new IntentFilter();// ������
		filter.addAction(IURL.MUSIC_PLAY_ACTION);
		filter.addAction(IURL.MUSIC_PAUSE_ACTION);
		filter.addAction(IURL.MUSIC_PROGRESS_ACTION);
		filter.addAction(IURL.WIDGET_NEXT_ACTION);
		filter.addAction(IURL.WIDGET_PAUSE_ACTION);
		filter.addAction(IURL.WIDGET_PLAY_ACTION);
		filter.addAction(IURL.WIDGET_PRE_ACTION);
		registerReceiver(musicReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MusicReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// �����Ҫ�������ֵĹ㲥
			if (IURL.MUSIC_PLAY_ACTION.equals(action)) {
				// ��ñ����ŵ����ֵ�URL
				String musicPath = intent.getStringExtra("musicPathKey");
				// ��������ʵ�����ֲ���
				play(musicPath);
			} else if (IURL.MUSIC_PAUSE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicPathKey");
				pause(musicPath);
			} else if (IURL.MUSIC_PROGRESS_ACTION.equals(action)) {
				int progress = intent.getIntExtra("progressKey", 0);
				// ���Ĳ��Ž���
				seekToProgress(progress);
			} else if (IURL.WIDGET_NEXT_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			} else if (IURL.WIDGET_PAUSE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				pause(musicPath);
			} else if (IURL.WIDGET_PLAY_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			} else if (IURL.WIDGET_PRE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			}
		}

	}

	public void play(String musicPath) {// ���Ŵ���·��������
		try {
			if (isPause) {
				player.seekTo(seekTime);// �Ӽ�¼ʱ�䳪
				player.start();
				seekTime = 0;
			} else {
				// �������ֲ�����
				player.reset();
				// ������������Դ
				player.setDataSource(musicPath);
				// �첽����������Դ
				player.prepareAsync();
			}
			isPause = false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void seekToProgress(int progress) {
		seekTime = player.getDuration() * progress / 100;
		// ��ת���Ž���
		player.seekTo(seekTime);
		// player.start();
	}

	public void pause(String musicPath) {// ��ͣ���ڲ��ŵ�����
		if (player.isPlaying()) {
			isPause = true;
			seekTime = player.getCurrentPosition();
			player.pause();
		}
	}

}
