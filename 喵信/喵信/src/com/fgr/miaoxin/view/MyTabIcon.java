package com.fgr.miaoxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fgr.miaoxin.R;

/**
 * ��Activity�ײ��ؼ�,���Խ�����ɫ,��΢��
 * 
 * @author �����
 *
 */
public class MyTabIcon extends View {

	Drawable drawable;// �Զ���ViewҪ��ʾ��ͼƬ

	Bitmap bitmap;// ��drawableҪת��bitmap�����ܻ�������Ļ��

	String text;// �Զ���ViewҪ��ʾ������

	int color;// �Զ���View��ʹ�õ���ɫ

	int textsize;// �Զ���View��ʾ����ʱʹ�õ������С

	Paint textPaint;// �����ֵĻ���
	Paint drawPaint;// ��ͼƬ�Ļ���

	// 0~255 0ȫ͸�� 255��ȫ��͸��
	int alpha;// ������ɫ��͸���ȣ�ͨ��͸���ȵĸı����ı���ɫ����ǳ��

	public MyTabIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		initPaint();
	}

	/**
	 * ��ʼ������
	 */
	private void initPaint() {
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(textsize);
		drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * ��ȡ�����ļ���,ʹ�ø�View���û��ṩ���Զ������Ե�����ֵ
	 * 
	 * @param context
	 *            ������
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		// �Ӳ����ļ��ж�ȡ�Զ������Ե�ֵ
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyTabIcon);

		// ��ȡͼƬ
		drawable = t.getDrawable(R.styleable.MyTabIcon_tabicon_drawable);

		bitmap = ((BitmapDrawable) drawable).getBitmap();

		// �Ŵ�bitmap
		bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2,
				bitmap.getHeight() * 2, true);
		// ��ȡ����
		text = t.getString(R.styleable.MyTabIcon_tabicon_text);

		// ��ȡ��ɫ(���δָ��,Ĭ��ʹ����ɫ)
		color = t.getColor(R.styleable.MyTabIcon_tabicon_color, Color.GREEN);

		// �����С(���δָ��,Ĭ��ʹ��14sp)
		textsize = t.getDimensionPixelSize(
				R.styleable.MyTabIcon_tabicon_textsize, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
								getResources().getDisplayMetrics()));
		t.recycle();// һ��Ҫ����
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ����WRAP_CONTENT
		// ����ؼ��Ŀ�Ⱥ͸߶ȶ�ָ��Ϊwrap_contentʱ
		// ͨ������ָ���ؼ��ĳߴ�
		// ע�⣺�벻Ҫɾ��super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		// ͬʱָ���ߴ��setMeasuredDimension(widthSize, heightSize)
		// һ��Ҫд��super.onMeasure(widthMeasureSpec, heightMeasureSpec)���·�
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST
				&& MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			// ������
			// ���ڱ߾�+Max(ͼƬ��ȣ����ֿ��)+���ڱ߾�
			int leftPadding = getPaddingLeft();
			int rightPadding = getPaddingRight();
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			int myWidth = Math.max(bounds.width(), bitmap.getWidth());
			int widthSize = leftPadding + myWidth + rightPadding;
			// ����߶�
			// ���ڱ߾�+ͼƬ�߶�+���ָ߶�+���ڱ߾�
			int topPadding = getPaddingTop();
			int bottomPadding = getPaddingBottom();
			int myHeight = bitmap.getHeight() + bounds.height();
			int heightSize = topPadding + myHeight + bottomPadding;
			// ΪMyTabIconָ���Ҽ���ĳߴ�
			setMeasuredDimension(widthSize, heightSize);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// ����ͼƬ
		float left = getWidth() / 2 - bitmap.getWidth() / 2;
		float top = getHeight() / 2 - bitmap.getHeight() / 2 - 16;
		canvas.drawBitmap(bitmap, left, top, null);

		// ��������
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);

		float x = getWidth() / 2 - bounds.width() / 2;
		float y = getHeight() / 2 + bitmap.getHeight() / 2 + bounds.height()
				- 16;
		textPaint.setColor(Color.DKGRAY);
		canvas.drawText(text, x, y, textPaint);

		drawColorText(canvas, x, y);

		drawColorBitmap(canvas, left, top);
	}

	private void drawColorBitmap(Canvas canvas, float left, float top) {
		// 1)��һ����ɫ��ͼƬ
		Bitmap colorBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		// ͨ��myCanvas�����ƿհ׵�colorBitmap
		Canvas myCanvas = new Canvas(colorBitmap);
		// �Ȱѻ�ɫ��ͼƬ����colorBitmap��
		myCanvas.drawBitmap(bitmap, 0, 0, null);
		// ��һ������ɫ�ķ��鵽colorBitmap��
		Rect r = new Rect(0, 0, colorBitmap.getWidth(), colorBitmap.getHeight());
		drawPaint.setColor(color);
		drawPaint.setAlpha(alpha);
		// ����drawPaint��ɫ��ʱ������ָ����ɫ��͸����֮��
		// ���û��ģʽ����ɫ����colorBitmap���е����ݲ�������Ч��
		drawPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		myCanvas.drawRect(r, drawPaint);

		// 2)�Ѳ�ɫͼƬ������Ļ��(left,top)������ԭ�Ȼ�ɫͼƬ��
		canvas.drawBitmap(colorBitmap, left, top, null);
	}

	/**
	 * ����ɫ������
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 */
	private void drawColorText(Canvas canvas, float x, float y) {
		textPaint.setColor(color);
		textPaint.setAlpha(alpha);
		canvas.drawText(text, x, y, textPaint);
	}

	public void setPaintAlpha(int alpha) {
		this.alpha = alpha;
		// ͨ������invali�������µ���onDraw����
		invalidate();
	}
}
