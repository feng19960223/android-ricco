package com.tarena.fgr.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.util.TypedValue;

/**
 * ʵ��Բ��ͷ��Ĵ���
 * 
 * @author ����� 2016��10��8�� 09:21:53
 * 
 */
public class ImageManager {

	/**
	 * @param context
	 * @param bitmap
	 *            ��ϵ��ͷ��
	 * @return
	 */
	public static Bitmap circleBitmap(Context context, Bitmap bitmap) {
		int width = bitmap.getWidth();// ͷ��Ŀ��
		int height = bitmap.getHeight();// ͷ��ĸ߶�

		// �����ı���
		Bitmap backBitmap = Bitmap
				.createBitmap(width, height, Config.ARGB_8888);
		// �Դ˱�������������
		Canvas canvas = new Canvas(backBitmap);

		// �ڻ����ϻ�Բ
		// ��������
		Paint paint = new Paint();
		// ���û��Ƶ�ʱ��û�о��
		paint.setAntiAlias(true);
		// ���û��ʵ���ɫ
		paint.setColor(Color.BLACK);

		// Բ�İ뾶
		int radius = Math.min(width, height) / 2;

		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				radius, paint);

		// ��ͷ��
		// ����ǰ���ͱ����ཻʱ�Ĵ���ģʽ
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		// ��Բ�İױ�
		// ��������һ�°ױ�
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);// ���
		// ���ñ��ߵĿ��
		float strockWidth = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources()
						.getDisplayMetrics());
		paint.setStrokeWidth(strockWidth);

		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				radius, paint);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		// radius
		// - strockWidth, paint);

		return backBitmap;
	}
}
