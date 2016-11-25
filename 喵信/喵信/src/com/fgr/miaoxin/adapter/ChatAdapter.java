package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.LocationActivity;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.XCRoundImageView;

public class ChatAdapter extends MyBaseAdapter<BmobMsg> {

	private static final int RECEIVE_TEXT_MSG = 0;
	private static final int SEND_TEXT_MSG = 1;
	private static final int RECEIVE_IMAGE_MSG = 2;
	private static final int SEND_IMAGE_MSG = 3;
	private static final int RECEIVE_LOC_MSG = 4;
	private static final int SEND_LOC_MSG = 5;
	private static final int RECEIVE_VOICE_MSG = 6;
	private static final int SEND_VOICE_MSG = 7;

	public ChatAdapter(Context context, List<BmobMsg> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;

		BmobMsg msg = getItem(position);

		int msgType = msg.getMsgType();

		int itemType = getItemViewType(position);

		if (convertView == null) {

			switch (msgType) {
			case 1:

				if (itemType == RECEIVE_TEXT_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_text_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_text_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);

				convertView.setTag(vh);

				break;

			case 2:

				if (itemType == RECEIVE_IMAGE_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_img_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_img_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				// Ϊvh.pbSending��ֵʱ�����convertView�Ǵ�item_chat_image_left�õ�����vh.pbSendingΪnull
				vh.pbSending = (ProgressBar) convertView
						.findViewById(R.id.pb_item_chat_sending);
				convertView.setTag(vh);

				break;

			case 3:

				if (itemType == RECEIVE_LOC_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_location_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_location_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);
				convertView.setTag(vh);

				break;

			case 4:

				if (itemType == RECEIVE_VOICE_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_voice_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_voice_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);
				vh.pbSending = (ProgressBar) convertView
						.findViewById(R.id.pb_item_chat_sending);
				convertView.setTag(vh);

				break;

			default:

				throw new RuntimeException("����ȷ����Ϣ��ʽ");
			}

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvTime
				.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime()) * 1000));

		setAvatar(msg.getBelongAvatar(), vh.ivAvatar);

		switch (msgType) {
		case 1:

			vh.tvContent.setText(EmoUtil.getSpannableString(msg.getContent()));

			break;

		case 2:

			String imageUrl = msg.getContent();
			// ���ڽ��շ���˵����ַ
			// ���ڷ��ͷ���˵��onStartʱ file:///filePath
			// onSuccessʱ file:///filePath&��ַ
			if (imageUrl.contains("&")) {
				// ��file:///filePath&��ַȡ��filePath
				String address = imageUrl.split("&")[0];
				address = address.split("///")[1];
				Bitmap bm = BitmapFactory.decodeFile(address);
				vh.ivContent.setImageBitmap(bm);
			} else {
				if (getItemViewType(position) == RECEIVE_IMAGE_MSG) {
					setAvatar(imageUrl, vh.ivContent);
				} else {
					String address = imageUrl.split("///")[1];
					Bitmap bm = BitmapFactory.decodeFile(address);
					vh.ivContent.setImageBitmap(bm);
				}
			}

			if (vh.pbSending != null) {
				if (msg.getStatus() == 0) {
					vh.pbSending.setVisibility(View.VISIBLE);
				} else {
					vh.pbSending.setVisibility(View.INVISIBLE);
				}
			}

			break;

		case 3:
			// ��ַ&ͼƬ���ص�ַ&ͼƬ�����ַ&γ��&����
			String info = msg.getContent();
			final String[] infos = info.split("&");
			vh.tvContent.setText(infos[0]);
			if (getItemViewType(position) == SEND_LOC_MSG) {
				// ���ͳ�ȥ��λ������������Ϣ
				// ��ʾ��ͼ��ͼʱʹ�ñ��ص�ַ
				Bitmap bm = BitmapFactory.decodeFile(infos[1]);
				vh.ivContent.setImageBitmap(bm);
			} else {
				// ���յ���λ������������Ϣ
				// ��ʾ��ͼ��ͼʱʹ�������ַ
				setAvatar(infos[2], vh.ivContent);
			}

			vh.ivContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// ��ת��LocationActivity���ڰٶȵ�ͼ����ʾ��Ŀ�еĵ�ַ
					Intent intent = new Intent(context, LocationActivity.class);
					intent.putExtra("from", "showaddress");
					intent.putExtra("address", infos[0]);
					intent.putExtra("lat", Double.parseDouble(infos[3]));
					intent.putExtra("lng", Double.parseDouble(infos[4]));
					context.startActivity(intent);
				}
			});

			break;

		case 4:

			// ����ǽ��յ���������Ϣ:�����ļ���������ַ&�����ļ��ĳ���
			// ����Ƿ��͵�������Ϣ��
			// ��һ��ˢ�£������ļ����ص�ַ&�����ļ��ĳ���
			// �ڶ���ˢ�£������ļ����ص�ַ&�����ļ���������ַ&�����ļ��ĳ���

			final String voiceInfo = msg.getContent();

			if (getItemViewType(position) == RECEIVE_VOICE_MSG) {
				vh.tvContent.setText(voiceInfo.split("&")[1] + "'");
			} else {
				String[] voiceInfos = voiceInfo.split("&");
				if (voiceInfos.length == 2) {
					vh.tvContent.setText(voiceInfos[1] + "'");
				} else {
					vh.tvContent.setText(voiceInfos[2] + "'");
				}
			}

			if (vh.pbSending != null) {
				if (msg.getStatus() == 1) {
					// �����ļ��Ѿ����ͳɹ���
					vh.pbSending.setVisibility(View.INVISIBLE);
					vh.tvContent.setVisibility(View.VISIBLE);
				} else {
					// �����ļ���δ��ʼ����
					vh.pbSending.setVisibility(View.VISIBLE);
					vh.tvContent.setVisibility(View.INVISIBLE);
				}
			}

			vh.ivContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// �����ʼ���������ļ�
					String voiceUrl;
					Position pos;
					if (getItemViewType(position) == RECEIVE_VOICE_MSG) {
						voiceUrl = voiceInfo.split("&")[0];
						pos = Position.START;
					} else {
						voiceUrl = voiceInfo.split("&")[0];
						pos = Position.END;
					}

					playVoice(voiceUrl, pos, vh.ivContent);
				}
			});

			break;

		default:

			throw new RuntimeException("����ȷ����Ϣ����");

		}

		return convertView;
	}

	protected void playVoice(String voiceUrl, final Position pos,
			final ImageView iv) {

		try {
			MediaPlayer mp = new MediaPlayer();
			mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					if (pos == Position.START) {
						iv.setImageResource(R.drawable.play_voice_left_anim);
					} else {
						iv.setImageResource(R.drawable.play_voice_right_anim);
					}
					((AnimationDrawable) iv.getDrawable()).start();

				}
			});

			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();
					if (pos == Position.START) {
						iv.setImageResource(R.drawable.voice_right3);
					} else {
						iv.setImageResource(R.drawable.voice_left3);
					}
					mp.release();
				}
			});
			mp.setDataSource(voiceUrl);
			mp.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getViewTypeCount() {

		return 8;
	}

	@Override
	public int getItemViewType(int position) {

		BmobMsg msg = getItem(position);

		int msgType = msg.getMsgType();

		String belongId = msg.getBelongId();

		switch (msgType) {
		case 1:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_TEXT_MSG;
			} else {
				return RECEIVE_TEXT_MSG;
			}

		case 2:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_IMAGE_MSG;
			} else {
				return RECEIVE_IMAGE_MSG;
			}

		case 3:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_LOC_MSG;
			} else {
				return RECEIVE_LOC_MSG;
			}

		case 4:
			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_VOICE_MSG;
			} else {
				return RECEIVE_VOICE_MSG;
			}

		default:

			throw new RuntimeException("����ȷ��������Ϣ��ʽ");

		}

	}

	public class ViewHolder {

		TextView tvTime;

		XCRoundImageView ivAvatar;

		TextView tvContent;

		ImageView ivContent;

		ProgressBar pbSending;

	}

}
