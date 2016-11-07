package com.tarena.fgr.entity;

/**
 * ��װ��ϵ�˵���Ϣ��ʵ����
 * 
 * @author ����� 2016��9��30�� 09:19:32
 * 
 */
public class Contact {
	private int id;// ��ϵ�˵ı��
	private String name;// ����
	private String phone;// �绰����
	private String address;// ͨѶ��ַ
	private String email;// ��������
	private int photoid;// ��ϵ�˵�ͷ����

	// ���ݿ��п���ͨ�����,���ҵ�ͼƬ
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}

	/**
	 * @param id��ϵ�˵ı��
	 * @param name����
	 * @param phone�绰����
	 * @param addressͨѶ��ַ
	 * @param email��������
	 * @param photoid��ϵ�˵�ͷ����
	 */
	public Contact(int id, String name, String phone, String address,
			String email, int photoid) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.photoid = photoid;
	}

	public Contact() {
		super();
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", address=" + address + ", email=" + email + ", photoid="
				+ photoid + "]";
	}

}
