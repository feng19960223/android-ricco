package com.tarena.fgr.constant;

public interface IURL {
	public static final String ROOT = "http://10.8.52.92:8080/MusicServer/";
	public static final String LOADMUSICS_URL = ROOT + "loadMusics.jsp";
	// ���ſ��ƽ��淢������Ĺ㲥��ͼ����
	public static final String MUSIC_PLAY_ACTION = "com.tarena.fgr.music.action.play";// ����
	public static final String MUSIC_PAUSE_ACTION = "com.tarena.fgr.music.action.pause";// ��ͣ
	public static final String MUSIC_PROGRESS_ACTION = "com.tarena.fgr.music.action.progress";// ����
	public static final String MUSIC_UP_PROGRESS_ACTION = "com.tarena.fgr.music.action.upprogress";// ���½���
	public static final String MUSIC_NEXT_ACTION = "com.tarena.fgr.music.action.next";// ��һ��
	public static final String MUSIC_LIST_ACTION = "android.appwidget.action.MUSIC_LIST";// �����б�

	public static final String WIDGET_NEXT_ACTION = "com.tarena.widgetnext";// ������дһ��ACTION,������ͻ
	public static final String WIDGET_PRE_ACTION = "com.tarena.widgetpre";
	public static final String WIDGET_PLAY_ACTION = "com.tarena.widgetplay";
	public static final String WIDGET_PAUSE_ACTION = "com.tarena.widgetpause";

}
