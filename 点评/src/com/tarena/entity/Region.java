package com.tarena.entity;

import java.util.List;

public class Region {
	private String status;// ����API����״̬������ɹ�����"OK"�������ؽ���ֶΣ����ʧ�ܷ���"ERROR"�������ش���˵��
	private List<Cities> cities;// ֧���̻����������³����б�

	public static class Cities {
		private String city_name;// ֧���̻����������³��������������б�
		private List<Districts> districts;

		public static class Districts {
			private String district_name;
			private List<String> neighborhoods;// ֧���̻��������������������������б�

			public String getDistrict_name() {
				return district_name;
			}

			public void setDistrict_name(String district_name) {
				this.district_name = district_name;
			}

			public List<String> getNeighborhoods() {
				return neighborhoods;
			}

			public void setNeighborhoods(List<String> neighborhoods) {
				this.neighborhoods = neighborhoods;
			}

			public Districts(String district_name, List<String> neighborhoods) {
				super();
				this.district_name = district_name;
				this.neighborhoods = neighborhoods;
			}

			public Districts() {
				super();
			}

			@Override
			public String toString() {
				return "Districts [district_name=" + district_name
						+ ", neighborhoods=" + neighborhoods + "]";
			}
		}

		public String getCity_name() {
			return city_name;
		}

		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}

		public List<Districts> getDistricts() {
			return districts;
		}

		public void setDistricts(List<Districts> districts) {
			this.districts = districts;
		}

		public Cities(String city_name, List<Districts> districts) {
			super();
			this.city_name = city_name;
			this.districts = districts;
		}

		public Cities() {
			super();
		}

		@Override
		public String toString() {
			return "Cities [city_name=" + city_name + ", districts="
					+ districts + "]";
		}

	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Cities> getCities() {
		return cities;
	}

	public void setCities(List<Cities> cities) {
		this.cities = cities;
	}

	public Region(String status, List<Cities> cities) {
		super();
		this.status = status;
		this.cities = cities;
	}

	public Region() {
		super();
	}

	@Override
	public String toString() {
		return "Region [status=" + status + ", cities=" + cities + "]";
	}

}
