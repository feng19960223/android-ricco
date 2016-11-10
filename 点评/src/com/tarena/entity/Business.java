package com.tarena.entity;

import java.io.Serializable;
import java.util.List;

public class Business {
	private String status;// ����API����״̬������ɹ�����"OK"�������ؽ���ֶΣ����ʧ�ܷ���"ERROR"�������ش���˵��
	private int total_count;// ����API��������ȡ���̻�����
	private int count;// ����ҳ���̻����������Ϊ40��
	private List<Businesses> businesses;

	public static class Businesses implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int business_id;// �̻�ID
		private String name;// �̻���
		private String branch_name;// �ֵ���
		private String address;// ��ַ
		private String telephone;// �����ŵĵ绰
		private String city;// ���ڳ���
		private List<String> regions;// ����������Ϣ�б���[���������һ�]
		private List<String> categories;// ����������Ϣ�б���[�����ˣ�����Ƶ�]
		private float latitude;// γ������
		private float longitude;// ��������
		private float avg_rating;// �Ǽ����֣�5.0�������ǣ�4.5�������ǰ룬��������
		private String rating_img_url;// �Ǽ�ͼƬ����
		private String rating_s_img_url;// С�ߴ��Ǽ�ͼƬ����
		private int product_grade;// ��Ʒ/ʳƷ��ζ���ۣ�1:һ�㣬2:�пɣ�3:�ã�4:�ܺã�5:�ǳ���
		private int decoration_grade;// �������ۣ�1:һ�㣬2:�пɣ�3:�ã�4:�ܺã�5:�ǳ���
		private int service_grade;// �������ۣ�1:һ�㣬2:�пɣ�3:�ã�4:�ܺã�5:�ǳ���
		private float product_score;// ��Ʒ/ʳƷ��ζ���۵���֣���ȷ��С�����һλ��ʮ���ƣ�
		private float decoration_score;// �������۵���֣���ȷ��С�����һλ��ʮ���ƣ�
		private float service_score;// �������۵���֣���ȷ��С�����һλ��ʮ���ƣ�
		private int avg_price;// �˾��۸񣬵�λ:Ԫ����û���˾�������-1
		private int review_count;// ��������
		private String review_list_url;// ����ҳ��URL����
		private int distance;// �̻����������ľ��룬��λΪ�ף��粻���뾭γ�����꣬���Ϊ-1
		private String business_url;// �̻�ҳ������
		private String photo_url;// ��Ƭ���ӣ���Ƭ���ߴ�700��700
		private String s_photo_url;// С�ߴ���Ƭ���ӣ���Ƭ���ߴ�278��200
		private int photo_count;// ��Ƭ����
		private String has_coupon;// ��Ƭҳ��URL����
		private int coupon_id;// �Ż�ȯID
		private String coupon_description;// �Ż�ȯ����
		private String coupon_url;// �Ż�ȯҳ������
		private int has_deal;// �Ƿ����Ź���0:û�У�1:��
		private int deal_count;// �̻���ǰ�����Ź�����
		private List<Deals> deals;

		public static class Deals implements Serializable {// �Ź��б�
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			private String id;// �Ź�ID
			private String description;// �Ź�����
			private String url;// �Ź�ҳ������

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getDescription() {
				return description;
			}

			public void setDescription(String description) {
				this.description = description;
			}

			public String getUrl() {
				return url;
			}

			public void setUrl(String url) {
				this.url = url;
			}

			public Deals(String id, String description, String url) {
				super();
				this.id = id;
				this.description = description;
				this.url = url;
			}

			public Deals() {
				super();
			}

			@Override
			public String toString() {
				return "Deals [id=" + id + ", description=" + description
						+ ", url=" + url + "]";
			}

		}

		private int has_online_reservation;// �Ƿ�������Ԥ����0:û�У�1:��
		private String online_reservation_url;// ����Ԥ��ҳ�����ӣ�Ŀǰ������HTML5վ������

		public int getBusiness_id() {
			return business_id;
		}

		public void setBusiness_id(int business_id) {
			this.business_id = business_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getBranch_name() {
			return branch_name;
		}

		public void setBranch_name(String branch_name) {
			this.branch_name = branch_name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
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

		public float getAvg_rating() {
			return avg_rating;
		}

		public void setAvg_rating(float avg_rating) {
			this.avg_rating = avg_rating;
		}

		public String getRating_img_url() {
			return rating_img_url;
		}

		public void setRating_img_url(String rating_img_url) {
			this.rating_img_url = rating_img_url;
		}

		public String getRating_s_img_url() {
			return rating_s_img_url;
		}

		public void setRating_s_img_url(String rating_s_img_url) {
			this.rating_s_img_url = rating_s_img_url;
		}

		public int getProduct_grade() {
			return product_grade;
		}

		public void setProduct_grade(int product_grade) {
			this.product_grade = product_grade;
		}

		public int getDecoration_grade() {
			return decoration_grade;
		}

		public void setDecoration_grade(int decoration_grade) {
			this.decoration_grade = decoration_grade;
		}

		public int getService_grade() {
			return service_grade;
		}

		public void setService_grade(int service_grade) {
			this.service_grade = service_grade;
		}

		public float getProduct_score() {
			return product_score;
		}

		public void setProduct_score(float product_score) {
			this.product_score = product_score;
		}

		public float getDecoration_score() {
			return decoration_score;
		}

		public void setDecoration_score(float decoration_score) {
			this.decoration_score = decoration_score;
		}

		public float getService_score() {
			return service_score;
		}

		public void setService_score(float service_score) {
			this.service_score = service_score;
		}

		public int getAvg_price() {
			return avg_price;
		}

		public void setAvg_price(int avg_price) {
			this.avg_price = avg_price;
		}

		public int getReview_count() {
			return review_count;
		}

		public void setReview_count(int review_count) {
			this.review_count = review_count;
		}

		public String getReview_list_url() {
			return review_list_url;
		}

		public void setReview_list_url(String review_list_url) {
			this.review_list_url = review_list_url;
		}

		public int getDistance() {
			return distance;
		}

		public void setDistance(int distance) {
			this.distance = distance;
		}

		public String getBusiness_url() {
			return business_url;
		}

		public void setBusiness_url(String business_url) {
			this.business_url = business_url;
		}

		public String getPhoto_url() {
			return photo_url;
		}

		public void setPhoto_url(String photo_url) {
			this.photo_url = photo_url;
		}

		public String getS_photo_url() {
			return s_photo_url;
		}

		public void setS_photo_url(String s_photo_url) {
			this.s_photo_url = s_photo_url;
		}

		public int getPhoto_count() {
			return photo_count;
		}

		public void setPhoto_count(int photo_count) {
			this.photo_count = photo_count;
		}

		public String getHas_coupon() {
			return has_coupon;
		}

		public void setHas_coupon(String has_coupon) {
			this.has_coupon = has_coupon;
		}

		public int getCoupon_id() {
			return coupon_id;
		}

		public void setCoupon_id(int coupon_id) {
			this.coupon_id = coupon_id;
		}

		public String getCoupon_description() {
			return coupon_description;
		}

		public void setCoupon_description(String coupon_description) {
			this.coupon_description = coupon_description;
		}

		public String getCoupon_url() {
			return coupon_url;
		}

		public void setCoupon_url(String coupon_url) {
			this.coupon_url = coupon_url;
		}

		public int getHas_deal() {
			return has_deal;
		}

		public void setHas_deal(int has_deal) {
			this.has_deal = has_deal;
		}

		public int getDeal_count() {
			return deal_count;
		}

		public void setDeal_count(int deal_count) {
			this.deal_count = deal_count;
		}

		public List<Deals> getDeals() {
			return deals;
		}

		public void setDeals(List<Deals> deals) {
			this.deals = deals;
		}

		public int getHas_online_reservation() {
			return has_online_reservation;
		}

		public void setHas_online_reservation(int has_online_reservation) {
			this.has_online_reservation = has_online_reservation;
		}

		public String getOnline_reservation_url() {
			return online_reservation_url;
		}

		public void setOnline_reservation_url(String online_reservation_url) {
			this.online_reservation_url = online_reservation_url;
		}

		public Businesses(int business_id, String name, String branch_name,
				String address, String telephone, String city,
				List<String> regions, List<String> categories, float latitude,
				float longitude, float avg_rating, String rating_img_url,
				String rating_s_img_url, int product_grade,
				int decoration_grade, int service_grade, float product_score,
				float decoration_score, float service_score, int avg_price,
				int review_count, String review_list_url, int distance,
				String business_url, String photo_url, String s_photo_url,
				int photo_count, String has_coupon, int coupon_id,
				String coupon_description, String coupon_url, int has_deal,
				int deal_count, List<Deals> deals, int has_online_reservation,
				String online_reservation_url) {
			super();
			this.business_id = business_id;
			this.name = name;
			this.branch_name = branch_name;
			this.address = address;
			this.telephone = telephone;
			this.city = city;
			this.regions = regions;
			this.categories = categories;
			this.latitude = latitude;
			this.longitude = longitude;
			this.avg_rating = avg_rating;
			this.rating_img_url = rating_img_url;
			this.rating_s_img_url = rating_s_img_url;
			this.product_grade = product_grade;
			this.decoration_grade = decoration_grade;
			this.service_grade = service_grade;
			this.product_score = product_score;
			this.decoration_score = decoration_score;
			this.service_score = service_score;
			this.avg_price = avg_price;
			this.review_count = review_count;
			this.review_list_url = review_list_url;
			this.distance = distance;
			this.business_url = business_url;
			this.photo_url = photo_url;
			this.s_photo_url = s_photo_url;
			this.photo_count = photo_count;
			this.has_coupon = has_coupon;
			this.coupon_id = coupon_id;
			this.coupon_description = coupon_description;
			this.coupon_url = coupon_url;
			this.has_deal = has_deal;
			this.deal_count = deal_count;
			this.deals = deals;
			this.has_online_reservation = has_online_reservation;
			this.online_reservation_url = online_reservation_url;
		}

		public Businesses() {
			super();
		}

		@Override
		public String toString() {
			return "Businesses [business_id=" + business_id + ", name=" + name
					+ ", branch_name=" + branch_name + ", address=" + address
					+ ", telephone=" + telephone + ", city=" + city
					+ ", regions=" + regions + ", categories=" + categories
					+ ", latitude=" + latitude + ", longitude=" + longitude
					+ ", avg_rating=" + avg_rating + ", rating_img_url="
					+ rating_img_url + ", rating_s_img_url=" + rating_s_img_url
					+ ", product_grade=" + product_grade
					+ ", decoration_grade=" + decoration_grade
					+ ", service_grade=" + service_grade + ", product_score="
					+ product_score + ", decoration_score=" + decoration_score
					+ ", service_score=" + service_score + ", avg_price="
					+ avg_price + ", review_count=" + review_count
					+ ", review_list_url=" + review_list_url + ", distance="
					+ distance + ", business_url=" + business_url
					+ ", photo_url=" + photo_url + ", s_photo_url="
					+ s_photo_url + ", photo_count=" + photo_count
					+ ", has_coupon=" + has_coupon + ", coupon_id=" + coupon_id
					+ ", coupon_description=" + coupon_description
					+ ", coupon_url=" + coupon_url + ", has_deal=" + has_deal
					+ ", deal_count=" + deal_count + ", deals=" + deals
					+ ", has_online_reservation=" + has_online_reservation
					+ ", online_reservation_url=" + online_reservation_url
					+ "]";
		}

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getTotal_count() {
		return total_count;
	}

	public void setTotal_count(int total_count) {
		this.total_count = total_count;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Businesses> getBusinesses() {
		return businesses;
	}

	public void setBusinesses(List<Businesses> businesses) {
		this.businesses = businesses;
	}

	public Business(String status, int total_count, int count,
			List<Businesses> businesses) {
		super();
		this.status = status;
		this.total_count = total_count;
		this.count = count;
		this.businesses = businesses;
	}

	public Business() {
		super();
	}

	@Override
	public String toString() {
		return "Business [status=" + status + ", total_count=" + total_count
				+ ", count=" + count + ", businesses=" + businesses + "]";
	}

}
