package com.tarena.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tarena.entity.CityNameBean;

public class DBUtil {
	DBHelper dbHelper;
	private Dao<CityNameBean, String> dao;// ?=����id������

	public DBUtil(Context context) {
		DBHelper(context);
	}

	private void DBHelper(Context context) {
		dbHelper = DBHelper.getInstance(context);
		try {
			dao = dbHelper.getDao(CityNameBean.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(CityNameBean bean) {// ����
		try {
			dao.createIfNotExists(bean);
			// dao.delete(bean);//ɾ��
			// dao.update(bean);//����
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// ����һ��,Ч�ʸ���,��Լ10��,3000��700����
	public void addBatch(final List<CityNameBean> list) {
		try {
			dao.callBatchTasks(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (CityNameBean cityNameBean : list) {
						save(cityNameBean);
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveAll(List<CityNameBean> list) {// ����һ��.3000��8��
		for (CityNameBean cityNameBean : list) {
			save(cityNameBean);
		}
	}

	public List<CityNameBean> queryAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("sql��ѯȫ���쳣" + e);
		}
	}
}
