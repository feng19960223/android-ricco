package com.fgr.miaoxin.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class MyUser extends BmobChatUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// �Ա�λ�ã�ƴ�����ƺ�ƴ������ĸ
	Boolean gender;// true ���� false Ů��
	BmobGeoPoint location;// �û�λ��
	String pyName;// username���Ե�ƴ����ʽ
	Character letter;// ƴ�����ֵ�����ĸ

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public BmobGeoPoint getLocation() {
		return location;
	}

	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}

	public String getPyName() {
		return pyName;
	}

	public void setPyName(String pyName) {
		this.pyName = pyName;
	}

	public Character getLetter() {
		return letter;
	}

	public void setLetter(Character letter) {
		this.letter = letter;
	}

	@Override
	public String toString() {
		return "MyUser [gender=" + gender + ", location=" + location
				+ ", pyName=" + pyName + ", letter=" + letter + "]";
	}

}
