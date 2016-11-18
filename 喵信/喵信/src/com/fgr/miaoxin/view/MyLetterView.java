package com.fgr.miaoxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fgr.miaoxin.R;

/**
 * ListView���� #a-z
 * 
 * @author �����
 */

public class MyLetterView extends View {

	Paint paint;// ����

	OnTouchLetterListener listener;

	int fontColor;

	// TextView tvLetter;

	// public void setTvLetter(TextView tvLetter) {
	// this.tvLetter = tvLetter;
	// }

	private String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	public MyLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyLetterView);
		fontColor = t
				.getColor(R.styleable.MyLetterView_font_color, Color.BLACK);
		t.recycle();

		initPaint();

	}

	/**
	 * ��listener���Ը�ֵ
	 * 
	 * @param listener
	 */
	public void setOnTouchLetterListener(OnTouchLetterListener listener) {
		this.listener = listener;
	}

	/**
	 * ��ʼ������
	 */
	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				12, getResources().getDisplayMetrics()));
		paint.setColor(fontColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// �����Ĳ���ԭ���밲׿Ĭ�ϵĲ���ԭ��ͬ��ʱ��
		// ����Ҫ��д
		// Ĭ��ԭ�� 1)���ָ������ȷ�ĳߴ磨����60dp��
		// ��ô�ͽ��������ĳߴ���ΪView�ĳߴ�
		// 2)���ָ����match_parent��wrap_content
		// һ�ɶ�����match_parent���д���
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// Ĭ��ԭ�������ԭ���г�ͻ
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			// ������ʹ�õ���wrap_content
			// �ֶ����㲢�趨view�Ŀ��ֵ
			// 1)�ֶ�����
			// ���=���ڱ߾�+�������ֿ��+���ڱ߾�
			int leftPadding = getPaddingLeft();
			int rightPadding = getPaddingRight();
			int textwidth = 0;
			for (int i = 0; i < letters.length; i++) {
				Rect bounds = new Rect();
				paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);
				if (bounds.width() > textwidth) {
					textwidth = bounds.width();
				}
			}

			int size = leftPadding + textwidth + rightPadding;
			// 2)�ֶ��趨
			setMeasuredDimension(size, MeasureSpec.getSize(heightMeasureSpec));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// �Զ���View�Ŀ�����ڷ���Ŀ����
		int width = getWidth();
		// �Զ���View�ĸ߶ȳ���Ҫ��ʾ���ݵ��������ڷ���ĸ߶ȣ�
		int height = getHeight() / letters.length;

		for (int i = 0; i < letters.length; i++) {
			Rect bounds = new Rect();
			paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);
			int w = bounds.width();// "�췽��"�Ŀ��
			int h = bounds.height();// "�췽��"�ĸ߶�

			int x = width / 2 - w / 2;
			int y = height / 2 + h / 2 + i * height;

			canvas.drawText(letters[i], x, y, paint);

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// action��Ҫ������������
		// ���£�DOWN�����ƶ���MOVE����̧��UP��
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			// 1)����ɫ��ɻ�ɫ
			setBackgroundColor(Color.GRAY);
			// 2)ListView��������Ӧ�ķ���

			// ��ǰ��ָ�ĸ߶�
			float y = event.getY();
			// ���ݵ�ǰ��ָλ�ã������һ�����Ƶ��±�ֵ
			int idx = (int) ((y * letters.length) / getHeight());

			if (idx >= 0 && idx < letters.length) {
				String letter = letters[idx];
				if (listener != null) {
					listener.onTouchLetter(letter);
					// if(tvLetter!=null){
					// tvLetter.setVisibility(View.VISIBLE);
					// tvLetter.setText(letter);
					// }

				}
			}

			break;

		default:
			// ����ɫ���͸��ɫ
			setBackgroundColor(Color.TRANSPARENT);
			// if(tvLetter!=null){
			// tvLetter.setVisibility(View.INVISIBLE);
			// //tvLetter.setText("");
			// }
			if (listener != null) {
				listener.onReleaseLetter();
			}

			break;
		}

		return true;
	}

	public interface OnTouchLetterListener {
		void onTouchLetter(String letter);

		void onReleaseLetter();
	}

}
