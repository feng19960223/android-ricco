package com.tarena.fgr.entity;

/**
 * ��װͨ����¼��ʵ����
 * 
 * @author �����
 * 
 */
public class Calllog {

	private int id;// ͨ����¼�ı��
	private String name;// ��ϵ�˵�����
	private int type;// ͨ��������
	private String phone;// �绰����
	private int photoid;// ͷ��id
	private long callTime;// ʱ���,���ݿ���洢����1970/1/1--��ǰ��ʱ��ĺ���ֵ;
	private String formatCallTimeString;// ��ʽ�����ʱ��

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}

	public long getCallTime() {
		return callTime;
	}

	public void setCallTime(long callTime) {
		this.callTime = callTime;
	}

	public String getFormatCallTimeString() {
		return formatCallTimeString;
	}

	public void setFormatCallTimeString(String formatCallTimeString) {
		this.formatCallTimeString = formatCallTimeString;
	}

	/**
	 * @param id
	 *            ͨ����¼�ı��
	 * @param name
	 *            ��ϵ�˵�����
	 * @param type
	 *            ͨ��������
	 * @param phone
	 *            �绰����
	 * @param photoid
	 *            ͷ��id
	 * @param callTime
	 *            ʱ��� ���ݿ���洢����1970/1/1--��ǰ��ʱ��ĺ���ֵ;
	 * @param formatCallTimeString
	 *            ��ʽ�����ʱ��
	 */
	public Calllog(int id, String name, int type, String phone, int photoid,
			long callTime, String formatCallTimeString) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.phone = phone;
		this.photoid = photoid;
		this.callTime = callTime;
		this.formatCallTimeString = formatCallTimeString;
	}

	public Calllog() {
		super();
	}

	@Override
	public String toString() {
		return "Calllog [id=" + id + ", name=" + name + ", type=" + type
				+ ", phone=" + phone + ", photoid=" + photoid + ", callTime="
				+ callTime + ", formatCallTimeString=" + formatCallTimeString
				+ "]";
	}

}
