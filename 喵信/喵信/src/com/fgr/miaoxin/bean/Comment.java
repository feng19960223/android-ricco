package com.fgr.miaoxin.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String blogId;// �����������һ��blog��
	String content;// ���۵��ı�����
	String username;// �������۵��˵��û���

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
