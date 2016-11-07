package com.tarena.view;

import com.tarena.groupon.R;

import android.annotation.SuppressLint;
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

public class MyLetterView extends View {
	private Paint paint = null;

	private String[] letters = { "����", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };// �����пո�,�����޷�����
	private onTouchLetterListener listener;
	private int letter_color;

	public void setOnTouchLetterListener(onTouchLetterListener listener) {
		this.listener = listener;
	}

	public MyLetterView(Context context) {// Java���빹��,����
		super(context);
	}

	@SuppressLint("Recycle")
	public MyLetterView(Context context, AttributeSet attrs) {// �����ļ�ʹ��,����
		super(context, attrs);
		// 1.��ȡ���Զ���view���Զ�������
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyLetterView);// ȡ�й��Լ�������
		t.recycle();// ***һ��Ҫ���յ�
		letter_color = t.getColor(R.styleable.MyLetterView_letter_color,
				Color.BLACK);// û��ȡ��ʹ�ú�ɫ
		// 2.��ʼ����Ҫ������:����,����
		initPaint();
	}

	public MyLetterView(Context context, AttributeSet attrs, int defStyle) {// ��style����ʱ,����
		super(context, attrs, defStyle);
	}

	private void initPaint() {
		// ���������ɫ����Ч��,��ǲ�����
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);// �����Ч��,
		paint.setColor(letter_color);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				12, getResources().getDisplayMetrics()));// ֱ��д����,��dp,����������,��Ϊsp
		// TODO ��ɫ
	}

	@SuppressLint("DrawAllocation")
	@Override
	// �����Զ���view�೤,���,����View�Ѿ�ʵ��,���һ��û��Ҫʵ��
	// ������Ϊwrap_contentʱ��ô����....
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// ����дwrap_content������
		// TODO ���MyLetterView�ĳߴ����������View��Ĭ�ϲ�������һ��ʱ,���б�Ҫ��д�÷���
		// View��Ĭ�ϲ�������:���ָ���˾���ĳߴ�ֵ,�Ͱ������ֵָ�����
		// ���ʹ��wrap_content��match_parent,�򶼰�׿match_parentָ��
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// �Կ�����޸�
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		if (specMode == MeasureSpec.AT_MOST) {
			// ���specMode��ֵΪMeasureSpec.EXACTLY,��ζ����Ǿ����ֵ,��match-parent
			// ���specMode��ֵΪMeasureSpec.AT_MOST,��ζ���wrap_content
			// ִ��ָ�����
			// �������ֿ��+���ұ߾�
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
			int width = leftPadding + textwidth + rightPadding;
			setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
		}

	}

	@Override
	// ����Զ���view��һ������,�����ڷ���view��λ��,дһ���������view,�б�Ҫ��д
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	// �����������ڻ�/���ֲ���(preallocate������)
	@SuppressLint("DrawAllocation")
	@Override
	// �Զ���view��γ���,���û����Ĳ���,ͨ������Paint������Canvas
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float width = getWidth();
		float height = getHeight() / letters.length;
		for (int i = 0; i < letters.length; i++) {
			// ��������������,����ȫ������
			Rect bounds = new Rect();
			paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);// ���ֱ߽�,����ֵ����ŵ�bounds��,ÿһ���ֵĿ��
			float x = width / 2.0f - bounds.width() / 2.0f;
			float y = height / 2.0f + bounds.height() / 2.0f + i * (height);
			canvas.drawText(letters[i], x, y, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {// true��Ҫ��ָ�����¼�
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			setBackgroundColor(Color.GRAY);
			float y = event.getY();// ��ǰ��ָ��λ��,����(0,0)��ľ���
			int idx = (int) y * letters.length / getHeight();
			if (idx >= 0 && idx < letters.length) {
				if (listener != null) {
					listener.onTouchLetter(letters[idx]);
				}
			}
			break;
		default:
			setBackgroundColor(Color.TRANSPARENT);// ͸��ɫ
			break;
		}
		return true;
	}

	public interface onTouchLetterListener {
		public void onTouchLetter(String letter);
	}
}
