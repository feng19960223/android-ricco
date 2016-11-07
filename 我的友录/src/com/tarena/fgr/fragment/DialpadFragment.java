package com.tarena.fgr.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.MediaManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;

/**
 * ����
 * 
 * @author �����
 * 
 */
public class DialpadFragment extends BaseFragment implements
		OnItemClickListener, OnScrollListener {
	private ListView listView = null;
	private RelativeLayout relativeLayout = null;
	private CalllogBaseAdapter adapter = null;
	private ImageView imageView_show = null;// ��ʾ���ż��̰�ť
	private TextView textView_phone = null;// �绰����,actionbar�Ĳ���绰

	// 2016��10��10�� 14:15:20
	// ����Ч�ŵ���MediaManager����

	// private SoundPool mySoundPool = null;// �������ʱ,��Ч����
	// private int loadid;// ��Чplay��Ҫ��һ������ֵ

	// һ��ʧ�ܵĶ���Ч��,��������û��ͬʱִ��
	// boolean isAnim = false;// ���ö�����ʼ�ı�ʶ

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// ����Ϊfalse,container�����Ǹ��ڵ�,���������Ĳ���
		contentView = inflater.inflate(R.layout.fragment_dialpad, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// ��ʼ�����Ե�UI�ؼ�
		// initSP(); // ��ʼ����Ч
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_calllog);
		// ���ż���
		relativeLayout = (RelativeLayout) contentView
				.findViewById(R.id.relativeLayout_dailpad);
		// ��ʾ���ż��̵İ�ť
		imageView_show = (ImageView) contentView
				.findViewById(R.id.imageView_show);
		// ������ŵ����ְ�ť,���ֻ���ʾ������
		textView_phone = (TextView) contentView
				.findViewById(R.id.textView_actionbar_title);
		ImageView imageView_backspace = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_right);
		// ���Ű�ť
		ImageView imageView_call = (ImageView) contentView
				.findViewById(R.id.imageView_call);

		// ɾ��СͼƬ
		initialActionbar(-1, "����", R.drawable.ic_backspace);

		// ��ʾ���ż��̵ļ����¼�,���ز��ż���,��ʾ��ť
		imageView_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeLayout.setVisibility(View.VISIBLE);
				imageView_show.setVisibility(View.GONE);
			}
		});

		// actionbar��ɾ��ͼ��ĵ����¼�,ɾ��һ��
		imageView_backspace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = textView_phone.getText().toString();// ������ı�
				int phoneLength = phone.length();// �����ı��ĳ���
				if (phone.equals("����")) {// �����޷�ɾ��
					// ע��:�ַ�����equlas�Ƚ�
					return;
				} else if (phoneLength == 1) {
					textView_phone.setText("����");// ���һ������ɾ����,��ʾ����
				} else if (phoneLength == 5 || phoneLength == 10) {
					String newphone = phone.substring(0, phoneLength - 2);// ɾ�����ֵ�ʱ��,ͬʱɾ��"-"
					textView_phone.setText(newphone);
				} else {
					String newphone = phone.substring(0, phoneLength - 1);
					textView_phone.setText(newphone);
				}
			}
		});
		// actionbar��ɾ��ͼ��ĳ����¼�,ɾ������
		imageView_backspace.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				textView_phone.setText("����");
				return false;
			}
		});

		// ���Ű�ť�ľ�̬�¼�
		imageView_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = textView_phone.getText().toString();
				if (phone.equals("����")) {
					return;
				} else {
					// ����һ����Ч
					// Load���ص�resid,����������,����������,���ȼ�,�Ƿ�ѭ��-1����ѭ��0��ѭ������Ϊѭ������,��������
					// play(soundID, leftVolume, rightVolume, priority, loop,
					// rate)
					// mySoundPool.play(loadid, 1.0f, 1.0f, 1, 0, 1.0f);
					MediaManager.playMusic(getActivity(), R.raw.a);
					// ����
					call(phone);
				}
			}
		});

		adapter = new CalllogBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		// 2016��10��9�� 18:43:49
		// �����:�����ļ����¼�,����ʵ������Ҫ��Ч��,��������ʧ,��������ʱ��ʾ
		// �����û�����Ҳ����̫��,���ǻ���list���������������ϵ��,����������ڻ���
		// ������ʱ��,��������,��ô�û��Ļ���û���κ�����
		// �����������һ�����Ե�����ť
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		refreshDialpad();// ͨ����¼������

		initialDailpad();// ���ż���

		textView_phone.addTextChangedListener(new MyTextWatcher());

	}

	// private void initSP() {// ������Ч

	// SoundPool��������һЩС�������ļ�
	// res/rawĿ¼
	// ֧�ֶ��ٸ�����,��Դ����,��Դ������(Ԥ��)[0]
	// new SoundPool(maxStreams, streamType, srcQuality)

	// ������,��Դid,priority ���ȼ�(Ԥ��)[1]
	// load(context, resId, priority)

	// mySoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	// loadid = mySoundPool.load(getActivity(), R.raw.b, 1);
	// }

	// Ϊ�����һ������
	// ��������������ݳ��ȷ����仯��ʱ��
	// �ж��䳤�ȣ��������13��ɾ��13֮�������
	public class MyTextWatcher implements TextWatcher {
		@Override
		// �ı��仯֮ǰ
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		// �ı��仯
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		// �ı��仯֮��
		public void afterTextChanged(Editable s) {
			// ���绰����ĳ��ȳ���13ʱ����������������
			if (s.length() > 13) {
				s.delete(13, s.length());
			}
		}

	}

	private void refreshDialpad() {
		// ͨ����¼������
		List<Calllog> list = CalllogManager.getCalllogs(getActivity());
		adapter.addItems(list, true);
	}

	private static String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "*", "0", "#" };

	// ���ż��̵ĳ�ʼ��
	private void initialDailpad() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		// ���㰴���Ŀ��
		int width = metrics.widthPixels / 3;
		// ���㰴���ĸ߶�
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 59, getActivity().getResources()
						.getDisplayMetrics());
		// �߶�59dp

		for (int i = 0; i < keys.length; i++) {
			TextView key = new TextView(getActivity());
			key.setId((i + 1));
			key.setText(keys[i]);
			key.setTextSize(40);
			key.setGravity(Gravity.CENTER);
			// Ĭ��ʹ��sp
			// key.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
			// ����һ����Բ��ֵĲ��ֲ���
			LayoutParams params = new LayoutParams(width, height);
			if (i % 3 != 0) {// �����Ҷ���
				params.addRule(RelativeLayout.RIGHT_OF, i);
			}
			if (i >= 3) {// �����¶���
				params.addRule(RelativeLayout.BELOW, i - 2);
			}
			relativeLayout.addView(key, params);
			// �����ĵ���¼�,�޷�ʵ����ɫ�ı��,����ʹ�ô����¼�
			// key.setOnClickListener(this);

			// Ϊ����ע�������
			// ʹ�ô����¼�,�ǳ��򵥵�ʵ������ɫ�仯
			// 2016��10��10�� 10:36:38
			// ��ɫ�仯��һ��Сbug,����סһֱ�ѵ�ʱ��,�п���һֱ����ɫ,���ᷢ���仯,���п�����ģ������ԭ��
			// ���Լ�һ����ָ��������,����ȥ��ʱ��,��֤���Ϊ��ɫ
			key.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// ��ť��Ч
						MediaManager.playMusic(getActivity(), R.raw.b);

						// if (isAnim) {
						// return true;
						// }
						// ������´���
						// ��ñ��������
						((TextView) v).setTextColor(Color.BLUE);
						String title = textView_phone.getText().toString();
						String k = ((TextView) v).getText().toString();
						if (title.equals("����")) {
							textView_phone.setText(k);
						} else if (title.length() == 3 || title.length() == 8) {
							textView_phone.append("-");
							textView_phone.append(k);
						} else {
							textView_phone.append(k);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						((TextView) v).setTextColor(Color.BLUE);
						break;
					case MotionEvent.ACTION_UP:
						((TextView) v).setTextColor(Color.BLACK);
						break;
					default:
						break;
					}
					return true;
				}
			});
		}
	}

	/**
	 * ����
	 * 
	 * @param phone
	 *            �绰����
	 */
	private void call(String phone) {
		Intent intent = new Intent();
		// ֱ�Ӳ���
		intent.setAction(Intent.ACTION_CALL);
		// ��ת�����Ž���
		// intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone));
		// ��ת�����Ž���
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// �绰�������ǿ�Ƶ�ϵͳ��ͨ����¼...
		// intent.putExtra("finishActivityOnSaveCompleted", true);
		// �����ǵ��ñ༭��ɺ�ֱ����ϵͳ��activity
		// �������Ĵ���û��������
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshDialpad();// �������±�����ʱ,ˢ������
		// loadDailpadAnim();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// mySoundPool.release();// ������Ч
		MediaManager.release(); // �ͷ�������Դ
	}

	// 2016��10��10�� 12:32:18
	// �����:���˼������ֵĶ���,��������Ͳ�����
	// public void loadDailpadAnim() {
	// Animation animation = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.dailpad_out);
	// animation.setAnimationListener(new AnimationListener() {
	// @Override
	// public void onAnimationStart(Animation animation) {
	// isAnim = true;
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// isAnim = false;
	// }
	// });
	// relativeLayout.setAnimation(animation);
	// }

	@Override
	// ����list���ز��ż���,��ʾ��ť,ͬʱ����
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// �����ʱ��,��ť��ʾ,���ż�������
		relativeLayout.setVisibility(View.GONE);
		imageView_show.setVisibility(View.VISIBLE);

		String phone = adapter.getItem(position).getPhone();
		call(phone);
	}

	@Override
	// ����ʱִ��
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// relativeLayout.setVisibility(View.VISIBLE);
		// ������ʱ��,��ť��ʾ,���ż�������
		relativeLayout.setVisibility(View.GONE);
		imageView_show.setVisibility(View.VISIBLE);
	}

	@Override
	// ��������ʱִ��
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	// 1.�������ı�������Ϊ"����",ֱ�ӻ�ð����ı������ø�����
	// 2.�������ı��ĳ���Ϊ3��8��ʱ��,�ڱ�������׷�ӷָ�����׷�Ӱ������ı�
	// ���ȵ���13,�޷������Ч
	// 3.����Ļ���һ�ΰ���,��ð����ı�,׷�ӵ��ı��ĺ���

	// 2016��10��10�� 10:28:52
	// �����:�����ĵ���¼�,�޷�ʵ����ɫ�ı��,����ʹ�ô����¼�
	// @Override
	// public void onClick(View v) {
	// int phoneLength = textView_phone.getText().toString().length();// �����ı��ĳ���
	// String str = ((TextView) v).getText().toString();// ���Ű�ť���ı�
	// if (textView_phone.getText().toString().equals("����")) {
	// textView_phone.setText(str);
	// } else if (phoneLength == 3 || phoneLength == 8) {
	// textView_phone.append("-");
	// textView_phone.append(str);
	// } else if (phoneLength >= 13) {
	// return;
	// } else {
	// textView_phone.append(str);
	// }
	// }
}
