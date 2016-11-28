package com.fgr.miaoxin.ui;

import java.util.ArrayList;
import java.util.List;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.view.NumberProgressBar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PostBlogActivity extends BaseActivity {

	@Bind(R.id.et_postblog_content)
	EditText etContent;// ������������

	@Bind(R.id.ll_postblog_imagecontainer)
	LinearLayout llImageContainer;

	@Bind(R.id.iv_postblog_del1)
	ImageView ivBlogDel1;
	@Bind(R.id.iv_postblog_del2)
	ImageView ivBlogDel2;
	@Bind(R.id.iv_postblog_del3)
	ImageView ivBlogDel3;
	@Bind(R.id.iv_postblog_del4)
	ImageView ivBlogDel4;
	@Bind(R.id.iv_postblog_img1)
	ImageView ivBlogImg1;
	@Bind(R.id.iv_postblog_img2)
	ImageView ivBlogImg2;
	@Bind(R.id.iv_postblog_img3)
	ImageView ivBlogImg3;
	@Bind(R.id.iv_postblog_img4)
	ImageView ivBlogImg4;

	@Bind(R.id.tv_postblog_imagenumber)
	TextView tvImageNumber;

	@Bind(R.id.npb_postblog_progress)
	NumberProgressBar npbProgressBar;

	@Bind(R.id.iv_postblog_plus)
	ImageView ivPlus;
	@Bind(R.id.iv_postblog_picture)
	ImageView ivPicture;
	@Bind(R.id.iv_postblog_camera)
	ImageView ivCamera;
	@Bind(R.id.iv_postblog_location)
	ImageView ivLocation;

	List<ImageView> blogImages;// 4��������ʾblogͼƬ��ImageView
	List<ImageView> blogDels;// 4���û���ɾ��blog��ͼ��С���

	boolean isExpaned;// �ײ���������ť�Ƿ�ɼ�true �ɼ� false ���ɼ�
	boolean isPosting;// ��ǰ�Ƿ���blog�������ϴ���״̬: true����blog�����ϴ���false��û��blog�����ϴ�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_post_blog);

	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initView();

	}

	private void initHeaderView() {
		setHeaderTitle("");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});

		setHeaderImage(Position.END, R.drawable.ic_upload, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						// ����û���û����������Ҳû���κ���ͼ
						String content = etContent.getText().toString();
						if (TextUtils.isEmpty(content)
								&& blogImages.get(0).getVisibility() == View.INVISIBLE) {
							return;
						}
						if (isPosting) {
							return;
						}
						isPosting = true;
						// ���ϴ�Blog����ͼ
						// �����ͼ�ĵ�ַ���ڿ�ʼ�ϴ�Blog
						postBlogImages();

					}
				});
	}

	/**
	 * �ϴ�blog����ͼ
	 */
	protected void postBlogImages() {
		// �����ʱ�û���û��Ϊ������blog��ͼ
		if (blogImages.get(0).getVisibility() == View.INVISIBLE) {
			postBlog("");
		} else {
			// ����BmobFile�������ϴ������ϴ���ͼ�����������������ͼ�ڷ������ϵĵ�ַ
			// Ҫ�ϴ���ͼƬ·���ͱ�������ʾͼƬ��ImageView��tag������

			List<String> list = new ArrayList<String>();
			for (int i = 0; i < blogImages.size(); i++) {
				ImageView iv = blogImages.get(i);
				if (iv.getVisibility() == View.VISIBLE) {
					String tag = (String) iv.getTag();
					list.add(tag);
				}
			}

			final String[] filePaths = list.toArray(new String[list.size()]);
			npbProgressBar.setVisibility(View.VISIBLE);
			npbProgressBar.setProgress(0);
			BmobFile.uploadBatch(this, filePaths, new UploadBatchListener() {

				@Override
				public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
					// �ϴ��ɹ�

					if (arg0.size() == filePaths.length) {
						// ��ʱͼƬȫ���ϴ��ɹ�
						StringBuilder sb = new StringBuilder();
						for (String string : arg1) {
							sb.append(string).append("&");
						}

						npbProgressBar.setVisibility(View.INVISIBLE);

						postBlog(sb.substring(0, sb.length() - 1));

					}

				}

				@Override
				public void onProgress(int arg0, int arg1, int arg2, int arg3) {
					// arg3 �ϴ����ܽ���
					npbProgressBar.setProgress(arg3);

				}

				@Override
				public void onError(int arg0, String arg1) {
					toastAndLog("blog����ʧ�ܣ����Ժ�����", arg0, arg1);
					// TODO

				}
			});
		}

	}

	/**
	 * ��blog��ͼ�ϴ���Ϻ󣬿�ʼ�ϴ�blog
	 * 
	 * @param imgUrls
	 *            blog��ͼ�ĵ�ַ
	 */
	private void postBlog(String imgUrls) {

		Blog blog = new Blog();
		blog.setAuthor(bmobUserManager.getCurrentUser(MyUser.class));
		blog.setContent(etContent.getText().toString());
		blog.setImgUrls(imgUrls);
		blog.setLove(0);
		// ��blog�ϴ���������
		blog.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toast("���ͷ����ɹ�");
				etContent.setText("");
				for (int i = 0; i < blogImages.size(); i++) {
					blogImages.get(i).setVisibility(View.INVISIBLE);
					blogDels.get(i).setVisibility(View.INVISIBLE);
				}
				tvImageNumber.setText("");
				isPosting = false;
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("���ͷ���ʧ�ܣ��Ժ�����", arg0, arg1);
				isPosting = false;

			}
		});

	}

	private void initView() {

		blogImages = new ArrayList<ImageView>();
		blogImages.add(ivBlogImg1);
		blogImages.add(ivBlogImg2);
		blogImages.add(ivBlogImg3);
		blogImages.add(ivBlogImg4);

		blogDels = new ArrayList<ImageView>();
		blogDels.add(ivBlogDel1);
		blogDels.add(ivBlogDel2);
		blogDels.add(ivBlogDel3);
		blogDels.add(ivBlogDel4);

		llImageContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						// llImageContainer�Ŀ��=��Ļ���-30dp
						// ���ߵ���llImageContainer��getWidth����
						int containerWidth = llImageContainer.getWidth();
						// framelayout�Ŀ��=(llImageContainer�Ŀ��-30dp)/4
						int rightMargin = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
										.getDisplayMetrics());
						int frameWidth = (containerWidth - 3 * rightMargin) / 4;
						// framelayout�ĸ߶�=framelayout�Ŀ��
						int frameHeight = frameWidth;
						// ����llImageContainer��д�ٽ���һ�β���
						for (int i = 0; i < llImageContainer.getChildCount(); i++) {
							View frame = llImageContainer.getChildAt(i);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									frameWidth, frameHeight);
							if (i != llImageContainer.getChildCount() - 1) {
								params.setMargins(0, 0, rightMargin, 0);
							}
							frame.setLayoutParams(params);
						}

						llImageContainer.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

						llImageContainer.requestLayout();
					}
				});

	}

	@OnClick(R.id.iv_postblog_plus)
	public void setButtonsVisible(View view) {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_press_anim);
		ivPlus.startAnimation(anim);
		if (isExpaned) {
			// ���صײ�������ť
			closeButtons();
		} else {
			// ��ʾ�ײ�������ť
			expandButtons();
		}
	}

	/**
	 * ��ʾ�ײ�������ť
	 */
	private void expandButtons() {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_expand_anim);
		ivPicture.startAnimation(anim);
		ivCamera.startAnimation(anim);
		ivLocation.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivPicture.setVisibility(View.VISIBLE);
				ivCamera.setVisibility(View.VISIBLE);
				ivLocation.setVisibility(View.VISIBLE);
				isExpaned = true;

			}
		});
	}

	private void closeButtons() {

		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_close_anim);
		ivPicture.startAnimation(anim);
		ivCamera.startAnimation(anim);
		ivLocation.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivPicture.setVisibility(View.INVISIBLE);
				ivCamera.setVisibility(View.INVISIBLE);
				ivLocation.setVisibility(View.INVISIBLE);
				isExpaned = false;

			}
		});

	}

	/**
	 * Ϊblog������ͼ
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_postblog_picture)
	public void selectPicture(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			if (arg0 == 101) {
				// ����û�ѡ���ͼƬ��SD�ϵĵ�ַ
				Uri uri = arg2.getData();
				Cursor cursor = getContentResolver().query(uri,
						new String[] { Images.Media.DATA }, null, null, null);
				cursor.moveToNext();
				String filePath = cursor.getString(0);
				cursor.close();
				showBlogImage(filePath);
			}
		}
	}

	/**
	 * "Ԥ��"���͵���ͼ
	 * 
	 * @param filePath
	 *            blog��ͼ��SD���ϵĵ�ַ
	 */

	private void showBlogImage(String filePath) {
		Bitmap bm = BitmapFactory.decodeFile(filePath);
		for (int i = 0; i < blogImages.size(); i++) {
			ImageView iv = blogImages.get(i);
			if (iv.getVisibility() == View.INVISIBLE) {
				iv.setImageBitmap(bm);
				iv.setTag(filePath);
				iv.setVisibility(View.VISIBLE);
				blogDels.get(i).setVisibility(View.VISIBLE);

				tvImageNumber.setText((i + 1) + " / 4");

				return;
			}
		}

		toast("���ֻ������ķ�ͼƬ");
	}

	@OnClick({ R.id.iv_postblog_del1, R.id.iv_postblog_del2,
			R.id.iv_postblog_del3, R.id.iv_postblog_del4 })
	public void deletBlogImages(View view) {
		switch (view.getId()) {
		case R.id.iv_postblog_del1:
			removeBlogImage(0);
			break;
		case R.id.iv_postblog_del2:
			removeBlogImage(1);
			break;
		case R.id.iv_postblog_del3:
			removeBlogImage(2);
			break;
		default:
			removeBlogImage(3);
			break;
		}
	}

	/**
	 * "ɾ��"�Ѿ���ӵ�blog��ͼ
	 * 
	 * @param i
	 */
	private void removeBlogImage(int idx) {
		// 1)һ���ж��ٷ���ͼ
		int count = 0;
		for (int i = 0; i < blogImages.size(); i++) {
			if (blogImages.get(i).getVisibility() == View.VISIBLE) {
				count += 1;
			}
		}
		// 2)�����û�������Ƿ������һ����ͼ�ֱ���
		if (idx == count - 1) {
			// 2.1)����ǣ�ֱ���������һ����ͼ
			blogImages.get(idx).setVisibility(View.INVISIBLE);
			blogDels.get(idx).setVisibility(View.INVISIBLE);
		} else {
			// 2.2)������ǣ��ͽ��еݲ�������ٽ����һ��ͼ����
			for (int i = idx; i < count; i++) {
				if (i == count - 1) {
					// ����Ѿ��������һ��ͼ
					// �����һ��ͼ����
					blogImages.get(i).setVisibility(View.INVISIBLE);
					blogDels.get(i).setVisibility(View.INVISIBLE);
				} else {
					// �ݲ�
					// ����i+1��ImageView�е�ͼƬȡ�����ŵ���i��ImageView��
					Drawable drawable = blogImages.get(i + 1).getDrawable();
					blogImages.get(i).setImageDrawable(drawable);
					// ����i+1��ImageView�е�ͼƬ��SD���ϵ�·��ȡ�����ŵ���i��ImageView��tag������
					String filePath = (String) blogImages.get(i + 1).getTag();
					blogImages.get(i).setTag(filePath);

				}
			}
		}

		if (count == 1) {
			tvImageNumber.setText("");
		} else {
			tvImageNumber.setText((count - 1) + " / 4");
		}

	}
}
