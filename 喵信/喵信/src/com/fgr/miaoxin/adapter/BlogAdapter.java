package com.fgr.miaoxin.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.Comment;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.bean.Zan;
import com.fgr.miaoxin.listener.OnCommentBlogListener;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;
import com.fgr.miaoxin.util.DBUtil;
import com.fgr.miaoxin.util.TimeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class BlogAdapter extends MyBaseAdapter<Blog> {

	DBUtil dbUtil;
	OnCommentBlogListener listener;

	public BlogAdapter(Context context, List<Blog> datasource,
			OnCommentBlogListener listener) {
		super(context, datasource);
		dbUtil = new DBUtil(context);
		this.listener = listener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder vh;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.item_blog_layout,
						parent, false);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			Blog blog = getItem(position);
			MyUser author = blog.getAuthor();
			setAvatar(author.getAvatar(), vh.ivAvatar);
			vh.tvUsername.setText(author.getUsername());

			vh.tvContent.setText(blog.getContent());

			vh.imageContainer.removeAllViews();

			String imgUrls = blog.getImgUrls();

			if (!TextUtils.isEmpty(imgUrls)) {
				showBlogImages(imgUrls, vh.imageContainer);
			}
			// 2016-11-28 15:45:14--TimeUtils.getTime(ʱ���)--->15:45
			String time = blog.getCreatedAt();
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
			String descTime = TimeUtil.getTime(date.getTime());
			vh.tvTime.setText(descTime);

			vh.tvShare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					shareBlog(position);

				}
			});

			vh.tvLove.setText(blog.getLove() + " ��");

			vh.tvLove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					loveBlog(position);

				}
			});

			vh.tvComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					commentBlog(position);
				}
			});

			vh.commentContainer.removeAllViews();
			showBlogComments(position, vh.commentContainer);

			return convertView;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("����ȷ��ʱ���ʽ");
		}
	}

	/**
	 * ��ʾ��ĳһ��blog��������������
	 * 
	 * @param position
	 * 
	 * @param commentContainer
	 */
	private void showBlogComments(int position,
			final LinearLayout commentContainer) {
		// �ӷ�����Comment���ݱ��У������positionλ�õ�blog���������۶���ѯ����

		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("blogId", getItem(position).getObjectId());
		// query.order("-createdAt");//Խ���������۳���ʱԽ����
		query.findObjects(context, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				// ÿһ�����۷���һ��TextView��Ȼ��TextView���뵽commentContainer��
				for (Comment comment : arg0) {
					TextView tv = new TextView(context);
					// abc���ۣ�xxxxxxx
					String username = comment.getUsername();
					String content = comment.getContent();
					// ���������Ƿ���Ҫ���ʱ�䣬�Լ�ʱ��ĸ�ʽ�Լ����Ű�
					tv.setText(username + "���ۣ�" + content);
					tv.setTextColor(Color.BLUE);
					int padding = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 3, context
									.getResources().getDisplayMetrics());
					tv.setPadding(padding, padding, padding, padding);
					tv.setGravity(Gravity.CENTER_VERTICAL);
					commentContainer.addView(tv);
				}

			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.d("TAG", "��ѯ����ʧ�ܣ�������룺" + arg0 + ":" + arg1);

			}
		});
	}

	/**
	 * ����һ��blog
	 * 
	 * @param position
	 */
	protected void commentBlog(int position) {
		Blog blog = getItem(position);
		listener.onComment(position, blog);
	}

	/**
	 * Ϊblog����
	 * 
	 * @param position
	 */
	protected void loveBlog(final int position) {
		final String blogId = getItem(position).getObjectId();
		final String userId = BmobUserManager.getInstance(context)
				.getCurrentUserObjectId();
		// ��ѯ������Zan���ݱ�
		BmobQuery<Zan> query = new BmobQuery<Zan>();
		query.addWhereEqualTo("blogId", blogId);
		query.addWhereEqualTo("userId", userId);
		query.findObjects(context, new FindListener<Zan>() {

			@Override
			public void onError(int arg0, String arg1) {

				if (arg0 == 101) {
					// ��ʱ�����������ݿ��и�����δ����Zan���ݱ�
					saveZan(position, blogId, userId);
				} else {
					Log.d("TAG", "��ѯʧ�ܣ�������룺" + arg0 + "," + arg1);
				}

			}

			@Override
			public void onSuccess(List<Zan> arg0) {
				if (arg0 != null && arg0.size() > 0) {
					// ˵����ǰ��¼�û��Ѿ�Ϊ��positionλ���ϵ�blog�������
					Toast.makeText(context, "�Ѿ��������", Toast.LENGTH_SHORT)
							.show();
				} else {
					// ���С����ޡ�����
					saveZan(position, blogId, userId);
				}
			}
		});
	}

	protected void saveZan(final int position, String blogId, String userId) {
		Zan zan = new Zan();
		zan.setBlogId(blogId);
		zan.setUserId(userId);
		zan.save(context, new SaveListener() {

			@Override
			public void onSuccess() {

				Blog blog = getItem(position);
				blog.setLove(blog.getLove() + 1);
				blog.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// ˢ��ListView
						notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						Log.d("TAG", "����Blogʧ�ܣ�������룺" + arg0 + "," + arg1);

					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.d("TAG", "������ʧ�ܣ�������룺" + arg0 + "," + arg1);

			}
		});

	}

	/**
	 * ����blog���������罻ƽ̨
	 * 
	 * @param position
	 */
	protected void shareBlog(int position) {

		Blog blog = getItem(position);

		OnekeyShare oks = new OnekeyShare();
		// �ر�sso��Ȩ
		oks.disableSSOWhenAuthorize();

		// ����ʱNotification��ͼ������� 2.5.9�Ժ�İ汾�����ô˷���
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ�ʹ��
		oks.setTitle("�������ŵķ���");
		// titleUrl�Ǳ�����������ӣ�����Linked-in,QQ��QQ�ռ�ʹ��
		oks.setTitleUrl("http://sharesdk.cn");
		// text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�

		if (TextUtils.isEmpty(blog.getContent())) {
			oks.setText("�������ŵķ���");
		} else {
			oks.setText(blog.getContent());
		}
		// ��������ͼƬ������΢����������ͼƬ��Ҫͨ����˺�����߼�д��ӿڣ�������ע�͵���������΢��
		if (!TextUtils.isEmpty(blog.getImgUrls())) {
			// ͨ��ShareSDK����ͼƬʱ��Ŀǰ���ֻ�ܷ���һ��ͼƬ
			String[] imgUrls = blog.getImgUrls().split("&");
			oks.setImageUrl(imgUrls[0]);
			// siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
			oks.setSiteUrl(imgUrls[0]);
		}
		// imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
		// oks.setImagePath("/sdcard/test.jpg");//ȷ��SDcard������ڴ���ͼƬ
		// url����΢�ţ��������Ѻ�����Ȧ����ʹ��
		oks.setUrl("http://sharesdk.cn");
		// comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
		oks.setComment("���ǲ��������ı�");
		// site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
		oks.setSite("ShareSDK");
		// ��������GUI
		oks.show(context);
	}

	/**
	 * ������ʾblog��������ͼ
	 * 
	 * @param imgUrls
	 *            ������ͼ����ַ
	 * @param imageContainer
	 *            ����װ����ʾblog��ͼ��ImageView������
	 */
	private void showBlogImages(String imgUrls, RelativeLayout imageContainer) {

		String[] urls = imgUrls.split("&");

		// ÿһ��ImageView�ĳߴ�
		// ������Ļ�Ŀ��-����߾�15dp-����߾�15dp-����ImageView֮��ľ���15dp

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

		int imgWidth = (int) ((screenWidth - TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources()
						.getDisplayMetrics())) / 2);

		int imgHeight = imgWidth;

		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources()
						.getDisplayMetrics());

		for (int i = 0; i < urls.length; i++) {

			final ImageView iv = new ImageView(context);
			iv.setId(i + 1);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					imgWidth, imgHeight);

			if (i % 2 != 0) {
				params.addRule(RelativeLayout.RIGHT_OF, i);
				params.leftMargin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources()
								.getDisplayMetrics());
			}

			if (i >= 2) {
				params.addRule(RelativeLayout.BELOW, i - 1);
				params.topMargin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources()
								.getDisplayMetrics());
			}

			iv.setLayoutParams(params);

			iv.setBackgroundResource(R.drawable.input_bg);
			iv.setPadding(padding, padding, padding, padding);
			iv.setScaleType(ScaleType.FIT_XY);

			// ImageLoader.getInstance().displayImage(urls[i], iv);

			String url = urls[i];

			if (dbUtil.isExist(url)) {
				// Log.d("TAG", "ͼƬ�����ݿ����");
				// �������ݿ����Ѿ�������url����Ӧ��ͼƬ
				dbUtil.get(url, new OnDatasLoadFinishListener<Bitmap>() {

					@Override
					public void onLoadFinish(List<Bitmap> datas) {
						Bitmap bitmap = datas.get(0);
						iv.setImageBitmap(bitmap);
					}
				});
			} else {
				// �������м���ͼƬ
				// Log.d("TAG", "ͼƬ���������");
				ImageLoader.getInstance().displayImage(url, iv,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// �����ͼƬ�ŵ��������ݿ��л���
								iv.setImageBitmap(loadedImage);
								dbUtil.save(imageUri, loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						});

			}

			imageContainer.addView(iv);
		}

	}

	class ViewHolder {
		@Bind(R.id.iv_item_blog_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_blog_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_blog_content)
		TextView tvContent;
		@Bind(R.id.rl_item_blog_imagecontainer)
		RelativeLayout imageContainer;
		@Bind(R.id.tv_item_blog_time)
		TextView tvTime;
		@Bind(R.id.tv_item_blog_share)
		TextView tvShare;
		@Bind(R.id.tv_item_blog_love)
		TextView tvLove;
		@Bind(R.id.tv_item_blog_comment)
		TextView tvComment;
		@Bind(R.id.ll_item_blog_commentcontainer)
		LinearLayout commentContainer;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
	}
}
