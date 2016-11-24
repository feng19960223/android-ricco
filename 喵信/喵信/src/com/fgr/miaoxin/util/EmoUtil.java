package com.fgr.miaoxin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fgr.miaoxin.app.MyApp;

import android.text.SpannableString;
import android.text.style.ImageSpan;

public class EmoUtil {
	/**
	 * �����洢���еı���ͼƬ ������Bitmap��Integer��String���������еı���ͼƬ
	 * 
	 * Bitmap��������ͼƬתΪBitmap�洢��emos�� Integer��������ͼƬ����Դid�洢��emos��
	 * String��������ͼƬ�����ƴ洢��emos��
	 * 
	 * ע�⣺ֱ��ʹ��ͼƬ��������Ϊ����Ĵ���ʱ ��ע�⣬��������ֵ�����������ԵĽ��޷��� �������ֱ�����������������ݵĳ�ͻ
	 * ����:[emo]ue056[/emo]�ȽϺ� ue056��̫�ã������������������ݲ�����ͻ
	 * 
	 */
	public static List<String> emos = new ArrayList<String>();
	static {
		emos.add("ue056");
		emos.add("ue057");
		emos.add("ue058");
		emos.add("ue059");
		emos.add("ue105");
		emos.add("ue106");
		emos.add("ue107");
		emos.add("ue108");
		emos.add("ue401");
		emos.add("ue402");
		emos.add("ue403");
		emos.add("ue404");
		emos.add("ue405");
		emos.add("ue406");
		emos.add("ue407");
		emos.add("ue408");
		emos.add("ue409");
		emos.add("ue40a");
		emos.add("ue40b");
		emos.add("ue40c");
		emos.add("ue40d");
		emos.add("ue40e");
		emos.add("ue40f");
		emos.add("ue410");
		emos.add("ue411");
		emos.add("ue412");
		emos.add("ue413");
		emos.add("ue414");
		emos.add("ue415");
		emos.add("ue416");
		emos.add("ue417");
		emos.add("ue418");
		emos.add("ue420");
		emos.add("ue421");
		emos.add("ue422");
		emos.add("ue423");
		emos.add("ue452");
		emos.add("ue453");
		emos.add("ue454");
		emos.add("ue455");
		emos.add("ue456");
		emos.add("ue457");
		emos.add("ue403");
		emos.add("ue404");
		emos.add("ue405");
		emos.add("ue406");
		emos.add("ue407");
		emos.add("ue408");
		emos.add("ue409");
		emos.add("ue40a");
		emos.add("ue40b");
		emos.add("ue40c");
		emos.add("ue40d");
		emos.add("ue40e");
		emos.add("ue40f");
		emos.add("ue457");
	}

	public static SpannableString getSpannableString(String string) {

		SpannableString ss = new SpannableString(string);

		// hiue056ue057ue058
		Pattern pattern = Pattern.compile("ue[0-9a-z]{3}");
		Matcher matcher = pattern.matcher(ss);
		while (matcher.find()) {
			String resName = matcher.group();// ue056
			// resName--->resId
			int resId = MyApp.context.getResources().getIdentifier(resName,
					"drawable", MyApp.context.getPackageName());
			if (resId != 0) {
				// R.drawable.ue056
				int startIdx = matcher.start();
				int endIdx = matcher.end();
				ss.setSpan(new ImageSpan(MyApp.context, resId), startIdx,
						endIdx, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return ss;
	}

}
