package com.tarena.fgr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tarena.fgr.music.R;

public class MyDiskView extends RelativeLayout {
	private ImageView imageView_disc = null;// ��ͼ
	private ImageView imageView_pin = null;// ��Ƭָ��
	private XCRoundImageView xCRoundImageView1 = null;// ר��ͼƬ
	private Context context;

	public MyDiskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.diskview, this);
		imageView_disc = (ImageView) view.findViewById(R.id.imageView_disc);
		imageView_pin = (ImageView) view.findViewById(R.id.imageView_pin);
		xCRoundImageView1 = (XCRoundImageView) view
				.findViewById(R.id.xCRoundImageView1);
	}

	// 2016��10��21�� 13:12:26
	// �����:Ϊʲô���������õ���Դһ��,ȴҪ��������?
	// ��Ȼ�õ���Դ��һ��,��������ͼƬȴ����һ��,�Ӷ�ȷ���˿�߲�һ��,�������ĵ�ľ���Ҳ�Ͳ�һ��.
	// ����Aͼ��100,��100,������ת�����50,50.��Bͼ��10��10
	// ����������ʱ(new) ,����ѡ�������(50,50).Aͼ������ת,��BͼҲ��Χ��(50,50)��ת,���Ծͻ����ƫ��

	private Animation animation1 = null;// ��Ƭ��ת����
	private Animation animation2 = null;// ��ƬƬ��ת����

	// ���ó�Ƭ�Ŀ�ʼ��ת�Ķ���
	public void startRotation() {
		animation1 = AnimationUtils.loadAnimation(context,
				R.anim.imageview_rotate);
		animation2 = AnimationUtils.loadAnimation(context,
				R.anim.imageview_rotate);
		imageView_disc.setAnimation(animation1);
		xCRoundImageView1.setAnimation(animation2);
		animation1.start();
		animation2.start();

		// Matrix matrix = new Matrix();
		// imageView_pin.setScaleType(ScaleType.MATRIX);
		// matrix.postRotate(15, 50, 50);
		// imageView_pin.setImageMatrix(matrix);

		RotateAnimation rotateAnimation = new RotateAnimation(0, 15,
				RotateAnimation.RELATIVE_TO_SELF, 0f,
				RotateAnimation.RELATIVE_TO_SELF, 0f);

		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		imageView_pin.setAnimation(rotateAnimation);

	}

	public void stopRotation() {
		animation1.cancel();
		animation2.cancel();
		// Matrix matrix = new Matrix();
		// imageView_pin.setScaleType(ScaleType.MATRIX);
		// matrix.postRotate(0);
		// imageView_pin.setImageMatrix(matrix);
		RotateAnimation rotateAnimation = new RotateAnimation(15, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0f,
				RotateAnimation.RELATIVE_TO_SELF, 0f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		imageView_pin.setAnimation(rotateAnimation);
	}

	public void setAlbumpic(int resId) {
		xCRoundImageView1.setImageResource(resId);
	}

	public void setAlbumpic(Bitmap bitmap) {
		xCRoundImageView1.setImageBitmap(bitmap);
	}

}
