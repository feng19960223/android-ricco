package com.tarena.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {
	/**
	 * ��������ĸ
	 * 
	 * @param string
	 * @return
	 */
	public static char getFirstLetter(String string) {
		return getPinYin(string).charAt(0);
	}

	/**
	 * �������е�����תΪƴ����ʽ <br>
	 * ����"����"-->"BEIJING"
	 * 
	 * @param string
	 * @return
	 */
	public static String getPinYin(String string) {
		try {
			String result = "";
			// 1.����ƴ��ת����ʽ
			// 2.�����趨�õĸ�ʽ,���ֽ���ת��
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// ��д
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// û���������
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				// �жϵ�i��λ���ϵ����Ƿ�������,�еĵ���,�����������ַ�
				String s = string.substring(i, i + 1);
				if (s.matches("[\u4e00-\u9fff]")) {// ���ı���������
					char c = string.charAt(i);
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c,
							format);// Ϊʲô��������?������
					// '��'-->[ZHONG,CHONG]//�����ֿ��ܱ��ŵ����
					sb.append(pinyin[0]);
				} else {
					sb.append(s);
				}
			}
			result = sb.toString();
			return result;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			throw new RuntimeException("ƴ����ʽ�쳣");
		}
	}
}
