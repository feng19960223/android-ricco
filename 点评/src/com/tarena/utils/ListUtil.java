package com.tarena.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author �����
 */
public class ListUtil {
	/**
	 * ��ּ��� <br>
	 * ��һ��������ļ���,��ֳ�ָ�������Ķ��С����
	 * 
	 * @param resList
	 *            Ҫ��ֵļ���
	 * @param count
	 *            ÿ�����ϵļ��ϸ���
	 * @return ���ز�ֺ�ĸ�������
	 */
	public static <T> List<List<T>> splitList(List<T> resList, int count) {
		if (resList == null || count < 1) {
			return null;
		}
		List<List<T>> ret = new ArrayList<List<T>>();
		int size = resList.size();
		if (size <= count) {// ����������countָ���Ĵ�С
			ret.add(resList);
		} else {
			int pre = size / count;
			int last = size % count;
			// ǰ��pre������,ÿ����С����count��Ԫ��
			for (int i = 0; i < pre; i++) {
				List<T> itemList = new ArrayList<T>();
				for (int j = 0; j < count; j++) {
					itemList.add(resList.get(i * count + j));
				}
				ret.add(itemList);
			}
			// last�Ľ��д���
			if (last > 0) {
				List<T> itemList = new ArrayList<T>();
				for (int i = 0; i < last; i++) {
					itemList.add(resList.get(pre * count + i));
				}
				ret.add(itemList);
			}
		}
		return ret;
	}
}
