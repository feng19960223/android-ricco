package com.fgr.bmobdemo.bean;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String avatar;// �û�ͷ��ͼƬ�ڷ������ϵĵ�ַ
	private String username;
	private String password;
	// MyUser���������Ҫ���浽Bmob�����������ݱ���
	// Bmob���������ݱ�֧��Java��8�ֻ�����������
	// ʹ��ʱҪ��������������תΪ��װ����
	private Boolean gender;// true ��,false Ů

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

}
