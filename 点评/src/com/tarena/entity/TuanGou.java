package com.tarena.entity;

import java.util.List;

public class TuanGou {
	private String status;
	// ����API����״̬������ɹ�����"OK"�������ؽ���ֶΣ����ʧ�ܷ���"ERROR"�������ش���˵��
	private int count;// ����API��������ȡ�ĵ�ҳ�Ź�����
	private List<Deals> deals;// �Ź���Ϣ

	public static class Deals {
		private String deal_id;// �Ź���ID
		private String title;// �Ź�����
		private String description;// �Ź�����
		private String city;// �������ƣ�cityΪ��ȫ������ʾȫ����������Ϊ���ص������з�Χ�����API���ؽ��
		private float list_price;// �Ź�������Ʒԭ��ֵ
		private float current_price;// �Ź��۸�
		private List<String> regions;// �Ź������̻���������
		private List<String> categories;// �Ź���������
		private int purchase_count;// �Ź���ǰ�ѹ�����
		private String purchase_deadline;// �Ź����Ľ�ֹ��������
		private String publish_date;// �Ź�������������
		private String details;// �Ź�����
		private String image_url;// �Ź�ͼƬ���ӣ����ͼƬ�ߴ�450��28
		private String s_image_url;// С�ߴ��Ź�ͼƬ���ӣ����ͼƬ�ߴ�160��100
		private List<String> more_image_urls;// �����ߴ�ͼƬ
		private List<String> more_s_image_urls;// ����С�ߴ�ͼƬ
		private int is_popular;// �Ƿ�Ϊ�����Ź���0�����ǣ�1����
		private Restrictions restrictions;// �Ź���������

		public static class Restrictions {
			private int is_reservation_required;// �Ƿ���ҪԤԼ��0�����ǣ�1����
			private int is_refundable;// �Ƿ�֧����ʱ�˿0�����ǣ�1����
			private String special_tips;// �ر���ʾ(һ��Ϊ�Ź���������Ϣ)

			public Restrictions() {
				super();
			}

			public Restrictions(int is_reservation_required, int is_refundable,
					String special_tips) {
				super();
				this.is_reservation_required = is_reservation_required;
				this.is_refundable = is_refundable;
				this.special_tips = special_tips;
			}

			public int getIs_reservation_required() {
				return is_reservation_required;
			}

			public void setIs_reservation_required(int is_reservation_required) {
				this.is_reservation_required = is_reservation_required;
			}

			public int getIs_refundable() {
				return is_refundable;
			}

			public void setIs_refundable(int is_refundable) {
				this.is_refundable = is_refundable;
			}

			public String getSpecial_tips() {
				return special_tips;
			}

			public void setSpecial_tips(String special_tips) {
				this.special_tips = special_tips;
			}

			@Override
			public String toString() {
				return "Restrictions [is_reservation_required="
						+ is_reservation_required + ", is_refundable="
						+ is_refundable + ", special_tips=" + special_tips
						+ "]";
			}

		}

		private String notice;// ��Ҫ֪ͨ(һ��Ϊ�Ź���Ϣ����ʱ���)
		private String deal_url;// �Ź�Webҳ�����ӣ���������ҳӦ��
		private String deal_h5_url;// �Ź�HTML5ҳ�����ӣ��������ƶ�Ӧ�ú���������Ӧ��
		private float commission_ratio;// ��ǰ�ŵ���Ӷ�����
		private List<Businesses> businesses;// �Ź������õ��̻��б�

		public static class Businesses {
			private String name;// �̻���
			private int id;// �̻�ID
			private String city;// �̻�����
			private String address;// �̻���ַ
			private float latitude;// �̻�γ��
			private float longitude;// �̻�����
			private String url;// �̻�ҳ����
			private String h5_url;

			public Businesses() {
				super();
			}

			public Businesses(String name, int id, String city, String address,
					float latitude, float longitude, String url, String h5_url) {
				super();
				this.name = name;
				this.id = id;
				this.city = city;
				this.address = address;
				this.latitude = latitude;
				this.longitude = longitude;
				this.url = url;
				this.h5_url = h5_url;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getCity() {
				return city;
			}

			public void setCity(String city) {
				this.city = city;
			}

			public String getAddress() {
				return address;
			}

			public void setAddress(String address) {
				this.address = address;
			}

			public float getLatitude() {
				return latitude;
			}

			public void setLatitude(float latitude) {
				this.latitude = latitude;
			}

			public float getLongitude() {
				return longitude;
			}

			public void setLongitude(float longitude) {
				this.longitude = longitude;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public String getH5_url() {
				return h5_url;
			}

			public void setH5_url(String h5_url) {
				this.h5_url = h5_url;
			}

			@Override
			public String toString() {
				return "Businesses [name=" + name + ", id=" + id + ", city="
						+ city + ", address=" + address + ", latitude="
						+ latitude + ", longitude=" + longitude + ", url="
						+ url + ", h5_url=" + h5_url + "]";
			}

		}

		public Deals() {
			super();
		}

		public Deals(String deal_id, String title, String description,
				String city, float list_price, float current_price,
				List<String> regions, List<String> categories,
				int purchase_count, String purchase_deadline,
				String publish_date, String details, String image_url,
				String s_image_url, List<String> more_image_urls,
				List<String> more_s_image_urls, int is_popular,
				Restrictions restrictions, String notice, String deal_url,
				String deal_h5_url, float commission_ratio,
				List<Businesses> businesses) {
			super();
			this.deal_id = deal_id;
			this.title = title;
			this.description = description;
			this.city = city;
			this.list_price = list_price;
			this.current_price = current_price;
			this.regions = regions;
			this.categories = categories;
			this.purchase_count = purchase_count;
			this.purchase_deadline = purchase_deadline;
			this.publish_date = publish_date;
			this.details = details;
			this.image_url = image_url;
			this.s_image_url = s_image_url;
			this.more_image_urls = more_image_urls;
			this.more_s_image_urls = more_s_image_urls;
			this.is_popular = is_popular;
			this.restrictions = restrictions;
			this.notice = notice;
			this.deal_url = deal_url;
			this.deal_h5_url = deal_h5_url;
			this.commission_ratio = commission_ratio;
			this.businesses = businesses;
		}

		public String getDeal_id() {
			return deal_id;
		}

		public void setDeal_id(String deal_id) {
			this.deal_id = deal_id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public float getList_price() {
			return list_price;
		}

		public void setList_price(float list_price) {
			this.list_price = list_price;
		}

		public float getCurrent_price() {
			return current_price;
		}

		public void setCurrent_price(float current_price) {
			this.current_price = current_price;
		}

		public List<String> getRegions() {
			return regions;
		}

		public void setRegions(List<String> regions) {
			this.regions = regions;
		}

		public List<String> getCategories() {
			return categories;
		}

		public void setCategories(List<String> categories) {
			this.categories = categories;
		}

		public int getPurchase_count() {
			return purchase_count;
		}

		public void setPurchase_count(int purchase_count) {
			this.purchase_count = purchase_count;
		}

		public String getPurchase_deadline() {
			return purchase_deadline;
		}

		public void setPurchase_deadline(String purchase_deadline) {
			this.purchase_deadline = purchase_deadline;
		}

		public String getPublish_date() {
			return publish_date;
		}

		public void setPublish_date(String publish_date) {
			this.publish_date = publish_date;
		}

		public String getDetails() {
			return details;
		}

		public void setDetails(String details) {
			this.details = details;
		}

		public String getImage_url() {
			return image_url;
		}

		public void setImage_url(String image_url) {
			this.image_url = image_url;
		}

		public String getS_image_url() {
			return s_image_url;
		}

		public void setS_image_url(String s_image_url) {
			this.s_image_url = s_image_url;
		}

		public List<String> getMore_image_urls() {
			return more_image_urls;
		}

		public void setMore_image_urls(List<String> more_image_urls) {
			this.more_image_urls = more_image_urls;
		}

		public List<String> getMore_s_image_urls() {
			return more_s_image_urls;
		}

		public void setMore_s_image_urls(List<String> more_s_image_urls) {
			this.more_s_image_urls = more_s_image_urls;
		}

		public int getIs_popular() {
			return is_popular;
		}

		public void setIs_popular(int is_popular) {
			this.is_popular = is_popular;
		}

		public Restrictions getRestrictions() {
			return restrictions;
		}

		public void setRestrictions(Restrictions restrictions) {
			this.restrictions = restrictions;
		}

		public String getNotice() {
			return notice;
		}

		public void setNotice(String notice) {
			this.notice = notice;
		}

		public String getDeal_url() {
			return deal_url;
		}

		public void setDeal_url(String deal_url) {
			this.deal_url = deal_url;
		}

		public String getDeal_h5_url() {
			return deal_h5_url;
		}

		public void setDeal_h5_url(String deal_h5_url) {
			this.deal_h5_url = deal_h5_url;
		}

		public float getCommission_ratio() {
			return commission_ratio;
		}

		public void setCommission_ratio(float commission_ratio) {
			this.commission_ratio = commission_ratio;
		}

		public List<Businesses> getBusinesses() {
			return businesses;
		}

		public void setBusinesses(List<Businesses> businesses) {
			this.businesses = businesses;
		}

		@Override
		public String toString() {
			return "Deals [deal_id=" + deal_id + ", title=" + title
					+ ", description=" + description + ", city=" + city
					+ ", list_price=" + list_price + ", current_price="
					+ current_price + ", regions=" + regions + ", categories="
					+ categories + ", purchase_count=" + purchase_count
					+ ", purchase_deadline=" + purchase_deadline
					+ ", publish_date=" + publish_date + ", details=" + details
					+ ", image_url=" + image_url + ", s_image_url="
					+ s_image_url + ", more_image_urls=" + more_image_urls
					+ ", more_s_image_urls=" + more_s_image_urls
					+ ", is_popular=" + is_popular + ", restrictions="
					+ restrictions + ", notice=" + notice + ", deal_url="
					+ deal_url + ", deal_h5_url=" + deal_h5_url
					+ ", commission_ratio=" + commission_ratio
					+ ", businesses=" + businesses + "]";
		}

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Deals> getDeals() {
		return deals;
	}

	public void setDeals(List<Deals> deals) {
		this.deals = deals;
	}

	@Override
	public String toString() {
		return "TuanGou [status=" + status + ", count=" + count + ", deals="
				+ deals + "]";
	}

}
