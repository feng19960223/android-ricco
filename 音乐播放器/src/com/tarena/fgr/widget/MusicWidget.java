package com.tarena.fgr.widget;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.music.R;
import com.tarena.fgr.utils.LogUtils;

/**
 * ����С���,��ʵ��һ���㲥������
 * 
 * @author �����
 */
public class MusicWidget extends AppWidgetProvider {
	// ��С�����ά��һ�����ֲ��ŵ������б�
	// ����ά����ǰ���ڲ��ŵ����ֵ�����
	// ��Ϊservice���������ڱȽ϶�,�������service,��Լ10�ͻᱻ����,���Զ���ɾ�̬
	private static List<Music> musics = null;
	private static int position = 0;

	// ������mediaplayer�ķ�������ʱ��,�첽���������б�
	// ���ҽ�������ϵ������б�,ͨ�����㲥�ķ�ʽ,����С���
	// ��С������н��յ��㲥�����ͼ�н������б����Ϣ��ȡ����,�����ø�С����е�musics
	// ��С�������ӵ�����ʱ�жϵ�ǰ�������б��Ƿ�Ϊ��,�����Ϊ��,������ϵĿؼ����г�ʼ������,����Ϊ������м�����������:
	// ������ŵ�ʱ��������Ͳ��Ź㲥...�����ͣ��ʱ�����������ͣ�㲥,�����һ�׺���һ�׵�ʱ���͹㲥..

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);// ������һ���ַ�����,����һ��Ҫд����һ��
		String action = intent.getAction();
		if (action.equals(IURL.MUSIC_LIST_ACTION)) {
			LogUtils.i("�����б�������");
			musics = (List<Music>) intent.getSerializableExtra("musiclistKey");
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		if (musics != null && musics.size() > 0) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_music);
			views.setViewVisibility(R.id.imageView_pre, View.VISIBLE);
			views.setViewVisibility(R.id.imageView_play, View.VISIBLE);
			views.setViewVisibility(R.id.imageView_next, View.VISIBLE);
			setWidgets(context, views);
			appWidgetManager.updateAppWidget(appWidgetIds, views);
		}
	}

	private void setWidgets(Context context, RemoteViews views) {
		// ��ü����еĵ�ǰ�����ŵ����ֶ���
		Music music = musics.get(position);
		LogUtils.i(music.toString());
		// Ҫ���ŵ����ֶ����url
		String songname = music.getName();
		String singer = music.getSinger();
		views.setTextViewText(R.id.textView_name, songname);
		views.setTextViewText(R.id.textView_singer, singer);

		String musicpath = IURL.ROOT + music.getMusicpath();
		// Ϊ����ϵ��ĸ���ť��Ӽ����¼�
		Intent intentPlay = new Intent(IURL.WIDGET_PLAY_ACTION);
		intentPlay.putExtra("musicpath", musicpath);
		PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,
				100, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_play, pendingIntentPlay);

		Intent intentPause = new Intent(IURL.WIDGET_PAUSE_ACTION);
		intentPause.putExtra("musicpath", musicpath);
		PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context,
				100, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageview_pause, pendingIntentPause);

		// ��һ��
		String next = IURL.ROOT + musics.get(music.getId()).getMusicpath();
		LogUtils.i("-----------" + next);
		Intent intentNext = new Intent(IURL.WIDGET_NEXT_ACTION);
		intentPause.putExtra("musicpath", next);
		PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,
				100, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_next, pendingIntentNext);

		// ��һ��
		String pre = IURL.ROOT + musics.get(music.getId()).getMusicpath();// -2
		Intent intentPre = new Intent(IURL.WIDGET_PRE_ACTION);
		intentPause.putExtra("musicpath", pre);
		PendingIntent pendingIntentPre = PendingIntent.getBroadcast(context,
				100, intentPre, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_pre, pendingIntentPre);

	}

	@Override
	public void onDisabled(Context context) {
	}

	@Override
	public void onEnabled(Context context) {
	}
}
