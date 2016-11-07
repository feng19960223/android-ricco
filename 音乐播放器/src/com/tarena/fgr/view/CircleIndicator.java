package com.tarena.fgr.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * �Զ���Ŀؼ�ָʾ��������,С����˶��켣������Ӳ ,����ֱ�ӵ�����ڲ�����ʹ�á�
 * 
 * @author �����
 * 
 */
public class CircleIndicator extends View {

	public CircleIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleIndicator(Context context) {
		super(context);
	}

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int radius = 10;
	private float width;
	private float height;
	private float varWidth;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// Ҫ����Բ�ͼ�����
		width = w / 2 - 4 * radius;
		varWidth = w / 2 - 4 * radius;
		height = h / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);
		// Ҫ������ѭ������
		for (int i = 0; i < 4; i++) {
			canvas.drawCircle(width + 3 * i * radius, height, radius, paint);
		}
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		canvas.drawCircle(varWidth, height, radius - 3, paint);
	}

	public void updateDraw(int position, float offset) {
		varWidth = width + (position + offset) * 3 * radius;
		invalidate();
	}
}
