package com.tarena.fgr.music;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.DownLoadMannger;
import com.tarena.fgr.manager.ImageLoader;
import com.tarena.fgr.service.MusicService;
import com.tarena.fgr.utils.LogUtils;
import com.tarena.fgr.view.MyDiskView;
import com.tarena.fgr.view.XCRoundImageView;

public class PlayActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {
	private ImageView imageView_back = null;// ����
	private TextView textView_name = null;// ����,ͷ����
	private ImageView imageView_music_playing = null;// �������ڲ��Ŷ���
	private XCRoundImageView xCRoundImageView1 = null;// ��ƬͼƬ
	private ImageView imageView_favcount = null;// ����
	private ImageView imageView_download = null;// ����
	private SeekBar seekBar1 = null;// ���Ž���
	private TextView textView_start = null;// ��ʼʱ��
	private TextView textView_end = null;// ����ʱ��
	private ImageView imageView_pre = null;// ��һ��
	private ImageView imageView_play = null;// ������ͣ
	private ImageView imageView_next = null;// ��һ��
	private ArrayList<Music> musics = null;
	private AnimationDrawable animationDrawable = null;// ���ֲ�����
	private MyDiskView myDiskView = null;// ��Ƭ�͵�Ƭ��ָ�붯��
	private Music music = null;
	private int position;// ���ŵڼ�������
	private int progress;// ��ǰ���ֲ��Ž���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		getActionBar().hide();
		initViews();
		initListener();
		registUpProgress();
	}

	private void registUpProgress() {
		myReceiver = new UpProgressReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IURL.MUSIC_UP_PROGRESS_ACTION);
		filter.addAction(IURL.MUSIC_NEXT_ACTION);
		registerReceiver(myReceiver, filter);
	}

	private UpProgressReceiver myReceiver = null;

	class UpProgressReceiver extends BroadcastReceiver {
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (IURL.MUSIC_UP_PROGRESS_ACTION.equals(action)) {
				// ��ǰ���ֲ��ŵ��ڼ�����
				int upProgress = intent.getIntExtra("currentPositionKey", 0);
				SimpleDateFormat format = new SimpleDateFormat("mm:ss");
				String time = format.format(upProgress);
				textView_start.setText(time);
				try {
					long up = upProgress * 100
							/ format.parse(music.getDurationtime()).getTime();
					seekBar1.setProgress((int) up);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (IURL.MUSIC_NEXT_ACTION.equals(action)) {
				nextMusic();
			}
		}
	}

	private void initListener() {// ���ü����¼�
		imageView_back.setOnClickListener(this);
		imageView_favcount.setOnClickListener(this);
		imageView_download.setOnClickListener(this);
		imageView_pre.setOnClickListener(this);
		imageView_play.setOnClickListener(this);
		imageView_next.setOnClickListener(this);
		seekBar1.setOnSeekBarChangeListener(this);
	}

	@SuppressWarnings("unchecked")
	private void initDatas() {// ��ʼ������
		musics = (ArrayList<Music>) getIntent().getSerializableExtra(
				"musicsKey");
		position = getIntent().getIntExtra("positionKey", 0);
		music = musics.get(position);
		// music = (Music) getIntent().getExtras().get("music");
		// music = getIntent().getParcelableExtra("musicKey");
		initMusicDatas();
		// imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
		// Mode.SRC_ATOP);
		imageView_download.setColorFilter(Color.parseColor("#0099CC"),
				Mode.SRC_ATOP);
	}

	public static final String MUSIC_PLAY = "com.tarena.fgr.music.musicplay.action";

	@Override
	protected void onResume() {// ��������Ϳ�ʼ����
		super.onResume();
		initDatas();
		// ()()()(()()()()()()()()()()()()()()()()()()())
		// ������ŵ��ǵ�ǰ����,��Ӧ�����²���
		goPlayMusic();
		// Ĭ�Ͽ�ʼ����
		startAnim();
	}

	private void initViews() {// ��ʼ��view
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textView_name = (TextView) findViewById(R.id.textView_name);
		imageView_music_playing = (ImageView) findViewById(R.id.imageView_music_playing);
		xCRoundImageView1 = (XCRoundImageView) findViewById(R.id.xCRoundImageView1);
		imageView_favcount = (ImageView) findViewById(R.id.imageView_favcount);
		imageView_download = (ImageView) findViewById(R.id.imageView_download);
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		textView_start = (TextView) findViewById(R.id.textView_start);
		textView_end = (TextView) findViewById(R.id.textView_end);
		imageView_pre = (ImageView) findViewById(R.id.imageView_pre);
		imageView_play = (ImageView) findViewById(R.id.imageView_play);
		imageView_next = (ImageView) findViewById(R.id.imageView_next);
		myDiskView = (MyDiskView) findViewById(R.id.myDiskView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:// ����
			finish();
			break;
		case R.id.imageView_favcount:// ����
			isFavorite();
			break;
		case R.id.imageView_download:// ����
			download();
			break;
		case R.id.imageView_pre:// ��һ��
			preMusic();
			break;
		case R.id.imageView_play:// ������,��ͣ
			playMusic();
			break;
		case R.id.imageView_next:// ��һ��
			nextMusic();
			break;
		default:
			break;
		}

	}

	private void download() {
		// ����ǰӦ����ȥ��ѯһ�±����ļ���,�Ƿ��Ѿ�����,�����������
		final String path = IURL.ROOT + music.getMusicpath();
		final String name = music.getName();

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle("ϵͳ��ʾ");
		builder.setIcon(R.drawable.download);
		builder.setMessage("ȷ��Ҫ����" + name + "��?");// APIҪ��14
		builder.setNegativeButton("������?", null);
		builder.setPositiveButton("����", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownLoadMannger.downloadSong(PlayActivity.this, path, name);
			}
		});
		// ��ʾ�Ի���
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	private boolean isPlaying = true;

	private void playMusic() {
		if (isPlaying) {
			imageView_play.setImageResource(R.drawable.selector_play);
			animationDrawable.stop();
			myDiskView.stopRotation();
			// ������ͣ
			goPauseMusic();
		} else {
			startAnim();
			// ��������
			goPlayMusic();
		}
		isPlaying = !isPlaying;
	}

	private void startAnim() {
		imageView_play.setImageResource(R.drawable.selector_pause);
		animationDrawable.start();
		myDiskView.startRotation();
	}

	private void goPauseMusic() {
		// �����ַ����͹㲥,����������ͣ����
		Intent intent = new Intent();
		intent.setAction(IURL.MUSIC_PAUSE_ACTION);
		String musicPath = IURL.ROOT + music.getMusicpath();
		LogUtils.i(musicPath);
		intent.putExtra("musicPathKey", musicPath);
		sendBroadcast(intent);
	}

	private void goPlayMusic() {
		// �����ֲ��ŵķ��񷢹㲥��ָ��(���㲥)
		Intent intent = new Intent();
		// Ҫ���Ÿ�����URL
		String musicPath = IURL.ROOT + music.getMusicpath();
		LogUtils.i(musicPath);
		intent.putExtra("musicPathKey", musicPath);
		intent.setAction(IURL.MUSIC_PLAY_ACTION);
		sendBroadcast(intent);
	}

	private void nextMusic() {
		position++;
		if (position == musics.size()) {// �������û�и���,���ص�һ��
			position = 0;
		}
		music = musics.get(position);
		MusicService.isPause = false;
		isPlaying = true;
		goPlayMusic();
		initMusicDatas();
		startAnim();
		initFavorite();
	}

	private void preMusic() {
		// position��������һ����ť,��ѭ��,˳��,�������position�Ϳ���ʵ�ֲ��ŵ�ģʽ
		// ˳�򲥷�
		position--;
		// �������
		// position = new Random().nextInt(musics.size());
		if (position == -1) {// �������û�и���,����������ĸ���
			position = musics.size() - 1;
		}
		music = musics.get(position);
		isPlaying = true;
		MusicService.isPause = false;
		goPlayMusic();
		initMusicDatas();
		startAnim();
		initFavorite();
	}

	private void initMusicDatas() {
		initFavorite();
		textView_name.setText(music.getName() + "-" + music.getSinger() + "-"
				+ music.getAlbum());
		textView_start.setText("00:00");// ?�õ���ǰ���ŵ�ʱ��
		textView_end.setText(music.getDurationtime());
		if (music.getDurationtime().length() == 4) {
			textView_end.setText("0" + music.getDurationtime());
		}
		animationDrawable = (AnimationDrawable) imageView_music_playing
				.getDrawable();
		String imageviewUrl = IURL.ROOT + music.getAlbumpic();
		ImageLoader.setBitmapFromCache(this, xCRoundImageView1, imageviewUrl);
	}

	private void isFavorite() {// ϲ���Ǻ�Ļ��Ǻڵ�??,����¼���
		SharedPreferences sharedPreferences = getSharedPreferences("favorite",
				MODE_PRIVATE);
		isFavorite = sharedPreferences.getBoolean(
				"isFavorite" + music.getName(), false);
		Editor editor = sharedPreferences.edit();
		if (isFavorite) {
			imageView_favcount.setColorFilter(Color.parseColor("#00000000"),
					Mode.SRC_ATOP);
			editor.putBoolean("isFavorite" + music.getName(), false);
		} else {
			imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
					Mode.SRC_ATOP);
			editor.putBoolean("isFavorite" + music.getName(), true);
		}
		editor.commit();// �ύ�޸�
	}

	private void initFavorite() {// ��ʼ��,�������л�ʱ��
		SharedPreferences sharedPreferences = getSharedPreferences("favorite",
				MODE_PRIVATE);
		isFavorite = sharedPreferences.getBoolean(
				"isFavorite" + music.getName(), false);
		if (isFavorite) {
			imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
					Mode.SRC_ATOP);
		} else {
			imageView_favcount.setColorFilter(Color.parseColor("#00000000"),
					Mode.SRC_ATOP);
		}
	}

	// ͨ��isFirstIn�ж�Ҫ��������Ϣ,��ֵҪ�洢����
	private boolean isFavorite = false;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		this.progress = progress;
		String duration = textView_end.getText().toString();
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		Date dateDuration;
		try {
			// �����ڶ���ת����ʱ���
			dateDuration = format.parse(duration);
			long currentTime = dateDuration.getTime() * progress / 100;
			textView_start.setText(format.format(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// ����֪ͨ������
		Intent intent = new Intent();
		intent.putExtra("progressKey", progress);
		intent.setAction(IURL.MUSIC_PROGRESS_ACTION);
		sendBroadcast(intent);
	}
}
