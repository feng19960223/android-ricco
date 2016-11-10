package com.tarena.utils;

import com.baidu.mapapi.model.LatLng;

public class DistanceUtil {

	private static double EARTH_RADIUS = 6378.137 * 1000;// ��

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * ��������λ�õľ�γ�ȣ����������صľ��루��λΪKM�� ����ΪString����
	 * 
	 * @param lat1
	 *            �û�����
	 * @param lng1
	 *            �û�γ��
	 * @param lat2
	 *            �̼Ҿ���
	 * @param lng2
	 *            �̼�γ��
	 * @return
	 */
	public static double getDistance(double lat1, double lng1, double lat2,
			double lng2) {// ������ļ���
		double radLat1 = lat1;
		double radLat2 = lat2;
		double difference = radLat1 - radLat2;
		double mdifference = rad(lng1) - rad(lng2);
		double distance = 2 * Math.asin(Math.sqrt(Math.pow(
				Math.sin(difference / 2), 2)
				+ Math.cos(radLat1)
				* Math.cos(radLat2)
				* Math.pow(Math.sin(mdifference / 2), 2)));
		distance = distance * EARTH_RADIUS;
		distance = Math.round(distance * 10000) / 10000;
		return distance;
	}

	public double getDistance(LatLng l1, LatLng l2) {
		return getDistance(l1.latitude, l1.longitude, l2.latitude, l2.longitude);
	}
}