package com.tarena.fgr.entity;

/**
 * ��װ���ŵ�����
 * 
 * @author ����� 2016��10��11�� 10:47:34
 * 
 */
public class Sms {
	private int _id;// ����
	private String address;// �绰����
	private long date;// ʱ��
	private String formateDate;// ��ʽ�����ʱ��
	private int type;// �������ͷ��ͻ��ǽ��� 2���ͳ�ȥ��,1���յ���
	private String body;// ����

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getFormateDate() {
		return formateDate;
	}

	public void setFormateDate(String formateDate) {
		this.formateDate = formateDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Sms(int _id, String address, long date, String formateDate,
			int type, String body) {
		super();
		this._id = _id;
		this.address = address;
		this.date = date;
		this.formateDate = formateDate;
		this.type = type;
		this.body = body;
	}

	public Sms() {
		super();
	}

	@Override
	public String toString() {
		return "Sms [_id=" + _id + ", address=" + address + ", date=" + date
				+ ", formateDate=" + formateDate + ", type=" + type + ", body="
				+ body + "]";
	}

}
