package com.fgr.miaoxin.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * ���ݴ����ʱ��ֵ���õ�һ�����������Ե��ַ��� </br>ʱ��������ʱ��Ϊ����14:35 ---> 14:35</br>
	 * ʱ��������ʱ��Ϊ����14:35 ---> ����14:35 </br>ʱ��������ʱ��Ϊǰ��14:35 ---> ǰ��14:35
	 * </br>ʱ��������ʱ��Ϊ������ǰ��ʱ�� ---> 2016/XX/XX 14:35
	 * 
	 * @param timeStamp
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(long timeStamp) {
		String result = "";
		// ��ǰʱ��ʱ���
		long now = System.currentTimeMillis();

		// ���㵱ǰʱ����timeStamp֮����˶�����
		// ����������·�ʽ���õ�����now��timeStamp����˶��ٸ�24Сʱ
		// int day = (int) ((now - timeStamp)/1000/60/60/24);
		// �������·�ʽ���õ�����now��timeStamp����˶�����
		int day = (int) (now / 1000 / 60 / 60 / 24 - timeStamp / 1000 / 60 / 60
				/ 24);

		switch (day) {
		case 0:
			// timeStampת�ɡ�ʱ:�֡�
			result = new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		case 1:
			// timeStampת�ɡ����� ʱ:�֡�
			result = "���� " + new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		case 2:
			// timeStampת�ɡ�ǰ�� ʱ:�֡�
			result = "ǰ�� " + new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		default:
			// timeStampת�ɡ�xxxx/xx/xx ʱ:�֡�
			result = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(timeStamp);
			break;
		}

		return result;
	}

}
