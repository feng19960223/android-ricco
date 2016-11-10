package com.tarena.entity;

import java.util.Arrays;

/**
 * ����ʵ����
 * 
 * @author �����
 * 
 */
public class Comment {
	private String avatar;// ͷ��
	private String username;// �û���
	private String rating;// ���
	private String avgPrice;// �˾��۸�,(������������������ʱ���ṩ�˼۸�)
	private String content;// ��������
	private String[] imgs;// ������ͼ(������������ҳ�ṩ����ͼ,����ṩ����ͼ���ȡ����)

	public Comment(String avatar, String username, String rating,
			String avgPrice, String content, String[] imgs) {
		super();
		this.avatar = avatar;
		this.username = username;
		this.rating = rating;
		this.avgPrice = avgPrice;
		this.content = content;
		this.imgs = imgs;
	}

	public Comment() {
		super();
	}

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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getImgs() {
		return imgs;
	}

	public void setImgs(String[] imgs) {
		this.imgs = imgs;
	}

	@Override
	public String toString() {
		return "Comment [avatar=" + avatar + ", username=" + username
				+ ", rating=" + rating + ", avgPrice=" + avgPrice
				+ ", content=" + content + ", imgs=" + Arrays.toString(imgs)
				+ "]";
	}

}
