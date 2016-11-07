package com.tarena.fgr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.ImageLoader;
import com.tarena.fgr.music.R;
import com.tarena.fgr.view.XCRoundImageView;

public class MusicAdapter extends BaseAdapter {
	private List<Music> musics = new ArrayList<Music>();
	private LayoutInflater layoutInflater = null;
	private Context context = null;

	public MusicAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	public void addMusics(List<Music> musics) {
		if (musics != null) {
			this.musics.addAll(musics);
		}
		notifyDataSetChanged();
	}

	public List<Music> getMusics() {
		return musics;
	}

	@Override
	public int getCount() {
		return musics.size();
	}

	@Override
	public Music getItem(int position) {
		return musics.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_music, null);
			viewHolder = new ViewHolder();
			viewHolder.myXCRoundImageView = (XCRoundImageView) convertView
					.findViewById(R.id.xCRoundImageView1);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			viewHolder.textView_singer = (TextView) convertView
					.findViewById(R.id.textView_singer);
			viewHolder.textView_author = (TextView) convertView
					.findViewById(R.id.textView_author);
			viewHolder.textView_composer = (TextView) convertView
					.findViewById(R.id.textView_composer);
			viewHolder.textView_durationtime = (TextView) convertView
					.findViewById(R.id.textView_durationtime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Music music = getItem(position);

		// 2016��10��20�� 16:18:58ͼƬ��������Ĭ��
		// viewHolder.myXCRoundImageView.setImageResource(R.drawable.ic_launcher);

		// 2016��10��21�� 10:55:10
		String imageviewUrl = IURL.ROOT + music.getAlbumpic();
		ImageLoader.setBitmapFromCache(context, viewHolder.myXCRoundImageView,
				imageviewUrl);

		viewHolder.textView_name.setText(music.getName());
		viewHolder.textView_singer.setText("- " + music.getSinger() + " -");
		viewHolder.textView_author.setText("��:" + music.getAuthor());
		viewHolder.textView_composer.setText("��:" + music.getComposer());
		viewHolder.textView_durationtime.setText(music.getDurationtime());
		return convertView;
	}

	public class ViewHolder {
		private XCRoundImageView myXCRoundImageView = null;// Բ������ͷ��ؼ�
		private TextView textView_name = null;// ��������
		private TextView textView_singer = null;// �ݳ���
		private TextView textView_author = null;// ����
		private TextView textView_composer = null;// ����
		private TextView textView_durationtime = null;// ����ʱ��
	}

}
