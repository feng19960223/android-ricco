package com.fgr.miaoxin.util;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.fgr.miaoxin.bean.BlogImage;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

public class DBUtil {

	private DBHelper helper;

	private Dao<BlogImage,String> dao;

	public DBUtil(Context context) {
		try {
			helper = DBHelper.getInstance(context);
			dao = helper.getDao(BlogImage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���첽����ʽ����һ��ͼƬ��blog_image.db��BlogImage���ݱ���
	 * 
	 * @param url Ҫ����ͼƬ����ַ
	 * @param bitmap Ҫ�����ͼƬ
	 * 
	 */
	public void save(final String url,final Bitmap bitmap){

		new Thread(){
			public void run() {
				try {
					BlogImage bi = new BlogImage();
					bi.setImgUrl(url);
					//bitmap--->byte[]
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 100, stream);
					byte[] bytes = stream.toByteArray();
					//byte[]--->String(Base64����)
					String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
					bi.setBitmap(base64);
					//��bi������Ϊһ�����ݼ�¼���浽���ݱ���
					dao.createIfNotExists(bi);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
		}.start();


	}
	
	/**
	 * ��ͬ���ķ�ʽ�����ݱ��л�ȡһ��ͼƬ
	 * 
	 * @param url ͼƬ����Ӧ�������ַ
	 * 
	 * @return bitmap ���ݱ�������Ӧ��ͼƬ
	 *         null   ���ݱ���û�ж�Ӧ��ͼƬ
	 */
	public Bitmap get(String url){
		try {
			//��url��Ϊ��ѯ�����������ݱ��н��в�ѯ
			List<BlogImage> list = dao.queryForEq("imgUrl",url);
			if(list!=null&&list.size()>0){
				BlogImage bi = list.get(0);
				String base64 = bi.getBitmap();
				//string--Base64����-->byte[]--BitmapFactory-->bitmap
				byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				return bitmap;
			}else{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("���ݿ��ѯʱ���ִ���");
		}
	}
	
	/**
	 * 
	 * ���첽�ķ�ʽ�����ݱ��л��ͼƬ
	 * 
	 * @param url ͼƬ����Ӧ����ַ
	 * 
	 * @param listener ���������������ݱ��л�ȡ������Ϻ�
	 *                 ���ü�������onLoadFinish�ص����������������Ϊ��������
	 */
	public void get(final String url,final OnDatasLoadFinishListener<Bitmap> listener){

		new Thread(){
			public void run() {
				try {
					List<BlogImage> list = dao.queryForEq("imgUrl", url);
					if(list!=null&&list.size()>0){

						BlogImage bi = list.get(0);

						String base64 = bi.getBitmap();

						byte[] bytes = Base64.decode(base64, Base64.DEFAULT);

						Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

						final List<Bitmap> bms = new ArrayList<Bitmap>();

						bms.add(bitmap);

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								listener.onLoadFinish(bms);
							}
						});

					}else{

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								listener.onLoadFinish(null);
							}
						});

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}
	
	/**
	 * 
	 * �ж����ݱ����Ƿ���url����Ӧ��ͼƬ
	 * 
	 * @param url ͼƬ����Ӧ����ַ
	 * @return true �ж�Ӧ��ͼƬ
	 *         false û�ж�Ӧ��ͼƬ
	 */
	public boolean isExist(String url){
		try {
			List<BlogImage> list = dao.queryForEq("imgUrl", url);
			if(list!=null&&list.size()>0){
				return true;
			}else{
				return false;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("���ݿ��ѯ����");
		}
	}

}
