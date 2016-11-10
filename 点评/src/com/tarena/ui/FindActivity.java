package com.tarena.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tarena.app.MyApp;
import com.tarena.entity.Business.Businesses;
import com.tarena.groupon.R;

//�����õ�λ�����ѹ��ܣ���Ҫimport����

public class FindActivity extends Activity {
	private Businesses businesses;
	private String from;// detail,main//��detail��ת����main��ת��
	private MapView mMapView = null;
	private BaiduMap baiduMap = null;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private Button button1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		button1 = (Button) findViewById(R.id.button1);
		businesses = (Businesses) getIntent().getSerializableExtra("business");
		from = getIntent().getStringExtra("from");
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.bmapView);
		initBaiduMap();
		if ("detail".equals(from)) {
			// ����������ҳ����ת������
			// ����Ʒ��ַ��ʾ�ڵ�ͼ��
			showAddress();
			button1.setVisibility(View.INVISIBLE);
		} else {// main
			// ��λ
			showLocation();
			button1.setVisibility(View.VISIBLE);
		}
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				Bundle bundle = arg0.getExtraInfo();
				if (bundle != null) {
					String name = bundle.getString("name");
					String address = bundle.getString("address");
					double d = DistanceUtil.getDistance(MyApp.lastpoint,
							arg0.getPosition());
					d = Math.round(d * 10000) / 10000;
					// �ڵ�ͼ�������Ϣ��
					TextView view = new TextView(FindActivity.this);
					view.setText(name + "\n" + address + "\n" + d + "��");
					view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					view.setTextColor(Color.WHITE);
					int padding = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
									.getDisplayMetrics());
					view.setPadding(padding, padding, padding, padding);
					view.setBackgroundColor(Color.BLACK);
					InfoWindow infowindow = new InfoWindow(view, arg0
							.getPosition(), -80);
					baiduMap.showInfoWindow(infowindow);
				}
				return false;
			}
		});
	}

	public void snap(View view) {// ����
		baiduMap.snapshot(new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap arg0) {
				// �����λ����stream����
				try {
					OutputStream stream = new FileOutputStream(new File(
							Environment.DIRECTORY_PICTURES, System
									.currentTimeMillis() + ".png"));
					arg0.compress(CompressFormat.PNG, 100, stream);// ѹ��������
					Toast.makeText(FindActivity.this, "��ͼ���",
							Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// ����
	public void shou(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("��ѡ��");
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		final String[] items = new String[] { "��ʳ", "����", "��ӰԺ", "����", "�ư�" };
		builder.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String keyword = items[which];
				showAround(keyword);
			}
		});
		builder.create().show();
	}

	private void showAround(final String keyword) {
		// point of interest//��Ȥ��poi
		PoiSearch poiSearch = PoiSearch.newInstance();
		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult arg0) {
						if (arg0 == null
								|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {
							Toast.makeText(FindActivity.this, "����û��" + keyword,
									Toast.LENGTH_SHORT).show();
							return;
						}
						List<PoiInfo> pois = arg0.getAllPoi();
						baiduMap.clear();
						for (PoiInfo poiInfo : pois) {
							// ��ÿһ��poi��λ�ö���Ϊһ��������
							MarkerOptions option = new MarkerOptions();
							option.position(poiInfo.location);
							option.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.home_scen_icon_locate));
							Marker marker = (Marker) baiduMap
									.addOverlay(option);
							Bundle bundle = new Bundle();
							bundle.putString("name", poiInfo.name);
							bundle.putString("address", poiInfo.address);
							marker.setExtraInfo(bundle);
						}
						// ����Լ���λ��,clear������Լ���λ��
						MarkerOptions option = new MarkerOptions();
						option.position(MyApp.lastpoint);
						option.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.shop_footerbar_locate_checked));
						baiduMap.addOverlay(option);

					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {

					}
				});
		PoiNearbySearchOption option = new PoiNearbySearchOption();
		// 1.���������ĵ�
		option.location(MyApp.lastpoint);
		// 2.�����İ뾶(��λ:��)
		option.radius(1000);
		// 3.����������
		option.keyword(keyword);
		poiSearch.searchNearby(option);
	}

	private void showLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������
		initLocation();
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000 * 60 * 10;// �������,���˶�Ӧ��,��λʱ,��������ô����ʱ��
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ
		mLocationClient.setLocOption(option);
	}

	private void initBaiduMap() {
		baiduMap = mMapView.getMap();
		// ����������
		baiduMap.setMaxAndMinZoomLevel(20, 16);// �������ż���
	}

	private void showAddress() {
		// ���ݵ�ַ�õ���γ��(����λ�ñ���)
		GeoCoder geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
				// ���ݾ�γ�Ȳ�ѯ��ַ(�������λ�ñ����ѯ)
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// ���ݵ�ַ��ѯ��γ��
				if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(FindActivity.this, "��������æ,���Ժ�����",// ��ʵ��û��ַ,����һ��,��˵��û��
							Toast.LENGTH_SHORT).show();
					return;
				}
				LatLng latLng = arg0.getLocation();// ��γ��,�����û�����,Ӧ�ð����λ������Ϊ���ĵ�

				// ���ݾ�γ�Ƚ���λ�ó����ڰٶȵ�ͼ��
				// OverlayOptions option;
				MarkerOptions option = new MarkerOptions();// 3.0�汾
				// ��������ӵ�λ��
				option.position(latLng);
				// ������ʹ�õ�ͼ��
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.home_scen_icon_locate));
				baiduMap.addOverlay(option);// ��Ӹ�����

				// ����Ļ���ĵ�ӱ����찲��,�ƶ����̻���λ��
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
						.newLatLng(latLng);
				baiduMap.animateMapStatus(mapStatusUpdate);

				// �ڵ�ͼ�������Ϣ��
				TextView view = new TextView(FindActivity.this);
				view.setText(businesses.getAddress());
				view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				view.setTextColor(Color.WHITE);
				int padding = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
								.getDisplayMetrics());
				view.setPadding(padding, padding, padding, padding);
				view.setBackgroundColor(Color.BLACK);
				InfoWindow infowindow = new InfoWindow(view, latLng, -80);
				baiduMap.showInfoWindow(infowindow);
			}
		});
		GeoCodeOption option = new GeoCodeOption();
		option.address(businesses.getAddress());
		option.city(businesses.getCity());// ��ֹ����,���Ƴ���
		geoCoder.geocode(option);// �����������ѯ
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			double lat = -1;
			double lng = -1;
			String address = "";
			if (code == 61 || code == 66 || code == 161) {
				// 61 �� GPS��λ�����GPS��λ�ɹ���
				// 66 �� ���߶�λ�����ͨ��requestOfflineLocaiton����ʱ��Ӧ�ķ��ؽ����
				// 161�� ���綨λ��������綨λ�ɹ���
				// ��λ�ɹ�
				lat = location.getLatitude();
				lng = location.getLongitude();
				address = location.getAddrStr();
			} else {
				// ��λ���ɹ�
				// �ֶ�ָ����γ��,�����찲�Ź���
				lat = 40;
				lng = 116;
				address = "�����찲��";
			}
			LatLng latLng = new LatLng(lat, lng);

			MyApp.lastpoint = new LatLng(lat, lng);

			MarkerOptions option = new MarkerOptions();
			option.position(latLng);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.shop_footerbar_locate_checked));
			baiduMap.addOverlay(option);
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLng(latLng);
			baiduMap.animateMapStatus(mapStatusUpdate);
			TextView view = new TextView(FindActivity.this);

			view.setText(address);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			view.setTextColor(Color.WHITE);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
							.getDisplayMetrics());
			view.setPadding(padding, padding, padding, padding);
			view.setBackgroundColor(Color.BLACK);
			InfoWindow infowindow = new InfoWindow(view, latLng, -80);
			baiduMap.showInfoWindow(infowindow);

			// ֹͣ��������λ����
			if (mLocationClient.isStarted()) {
				mLocationClient.unRegisterLocationListener(myListener);// ���
				mLocationClient.stop();// ֹͣ
			}
		}
	}
}
