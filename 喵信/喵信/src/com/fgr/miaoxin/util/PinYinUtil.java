package com.fgr.miaoxin.util;

import java.util.Locale;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

	/**
	 * �������е�����תΪƴ����ʽ
	 * 
	 * ���� -- LAOWANG
	 * 
	 * ����a -- LAOWANGA
	 * 
	 * ����8 -- LAOWANG8
	 * 
	 * 8���� -- #LAOWANG
	 * 
	 * 
	 * @param string
	 * @return
	 */
	public static String getPinYin(String string) {
		try {
			String result = "";
			// 1)�趨ƴ����ʽ
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			// 2)�����趨�õĸ�ʽ�����ֽ���ת��
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				// �жϵ�i��λ���ϵ����ǲ�������
				// "��"
				String s = string.substring(i, i + 1);
				if (s.matches("[\u4e00-\u9fff]")) {
					// ���������
					char c = string.charAt(i);
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c,
							format);
					sb.append(pinyin[0]);
				} else if (s.matches("[a-zA-Z]")) {
					// �������������Ӣ����ĸ
					sb.append(s.toUpperCase(Locale.US));
				} else {
					// �����������ֲ���Ӣ����ĸ
					if (i == 0) {
						sb.append("#");
					} else {
						sb.append(s);
					}
				}
			}

			result = sb.toString();

			return result;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			throw new RuntimeException("ƴ����ʽ�쳣");
		}
	}

	public static char getFirstLetter(String string) {
		return getPinYin(string).charAt(0);
	}

}
