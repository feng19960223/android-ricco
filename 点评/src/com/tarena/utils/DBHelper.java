package com.tarena.utils;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.tarena.entity.CityNameBean;

public class DBHelper extends OrmLiteSqliteOpenHelper {
	private static DBHelper instance;

	public static DBHelper getInstance(Context context) {
		if (instance == null) {
			synchronized (DBHelper.class) {
				if (instance == null) {
					instance = new DBHelper(context);
				}
			}
		}
		return instance;
	}

	private DBHelper(Context context) {
		super(context, "city.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		try {
			TableUtils.createTableIfNotExists(arg1, CityNameBean.class);// ��������ڴ���
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("���ݿⴴ������" + e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		try {
			TableUtils.dropTable(arg1, CityNameBean.class, true);// �Ƿ�ǿ��ɾ��
			onCreate(arg0, arg1);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("���ݿ���������" + e);
		}
	}

}
