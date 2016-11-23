package com.fgr.miaoxin.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;

public class LocationActivity extends BaseActivity {

	String from;// "mylocation"����λ showlocation������ʾλ��
	MapView mMapView = null;

	BaiduMap baiduMap;// ��ͼ����

	ProgressDialog pd;// ��ͼ�����и��û���ʾ

	// ��λ�ͻ���
	public LocationClient mLocationClient = null;
	// ��λ������
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_location);

	}

	@Override
	public void init() {
		super.init();
		from = getIntent().getStringExtra("from");
		// ��ȡ��ͼ�ؼ�����
		mMapView = (MapView) findViewById(R.id.bmapView);
		// ��ȡ��ͼ����
		baiduMap = mMapView.getMap();
		// ����һ�±�����(16 ������200�� ~ 20 ������Ϊ10��)
		baiduMap.setMaxAndMinZoomLevel(20, 16);

		if ("mylocation".equals(from)) {
			// ��ʾ��ǰ�û���λ��
			setHeaderTitle("�ҵ�λ��");
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});

			setHeaderImage(Position.END, R.drawable.ic_map_snap, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							pd = ProgressDialog.show(LocationActivity.this, "",
									"��ͼ��...");
							// ��ͼ��ͼ
							baiduMap.snapshot(new SnapshotReadyCallback() {

								@Override
								public void onSnapshotReady(Bitmap bitmap) {
									try {
										// ��ͼ�ϴ���Bmob�ļ�������
										File file = new File(
												Environment
														.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
												System.currentTimeMillis()
														+ ".jpg");
										OutputStream stream = new FileOutputStream(
												file);
										bitmap.compress(CompressFormat.JPEG,
												30, stream);
										final String path = file
												.getAbsolutePath();
										final BmobFile bf = new BmobFile(file);
										bf.uploadblock(LocationActivity.this,
												new UploadFileListener() {

													@Override
													public void onSuccess() {
														final String url = bf
																.getFileUrl(LocationActivity.this);
														// ���ݾ�γ�Ȳ��ҽֵ����ƣ��������λ�ò�ѯ��
														GeoCoder geoCoder = GeoCoder
																.newInstance();
														geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

															@Override
															public void onGetReverseGeoCodeResult(
																	ReverseGeoCodeResult arg0) {
																pd.dismiss();
																String address = "";
																if (arg0 == null
																		|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {
																	address = "δ֪��ַ";
																} else {
																	address = arg0
																			.getAddress();
																}

																// �����н���ش���ChatActivity
																Intent data = new Intent();
																data.putExtra(
																		"lat",
																		MyApp.lastPoint
																				.getLatitude());
																data.putExtra(
																		"lng",
																		MyApp.lastPoint
																				.getLongitude());
																data.putExtra(
																		"address",
																		address);
																data.putExtra(
																		"url",
																		url);
																data.putExtra(
																		"path",
																		path);
																setResult(
																		RESULT_OK,
																		data);
																finish();

															}

															@Override
															public void onGetGeoCodeResult(
																	GeoCodeResult arg0) {
															}
														});

														ReverseGeoCodeOption option = new ReverseGeoCodeOption();
														option.location(new LatLng(
																MyApp.lastPoint
																		.getLatitude(),
																MyApp.lastPoint
																		.getLongitude()));
														geoCoder.reverseGeoCode(option);

													}

													@Override
													public void onFailure(
															int arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub

													}
												});

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
						}
					});

			getMyLocation();
		} else {

			String address = getIntent().getStringExtra("address");
			setHeaderTitle(address);
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});

			showLocation();
		}
	}

	private void getMyLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // ����LocationClient��
		mLocationClient.registerLocationListener(myListener); // ע���������

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000 * 60 * 5;// ����ҵ�����󣨶�λ�������Ϊ5���ӣ�
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
		mLocationClient.start();

	}

	private void showLocation() {
		// �����ChatActivity��λ��������Ϣ��
		// �ڵ�ͼ����ʾ��Ӧ��λ�õ�
		double lat = getIntent().getDoubleExtra("lat", -1);
		double lng = getIntent().getDoubleExtra("lng", -1);

		MarkerOptions option = new MarkerOptions();
		option.position(new LatLng(lat, lng));
		option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		baiduMap.addOverlay(option);

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(lat,
				lng));
		baiduMap.animateMapStatus(msu);
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

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			int code = location.getLocType();
			double lat = -1;
			double lng = -1;

			if (code == 61 || code == 66 || code == 161) {
				// ��λ�ɹ���
				lat = location.getLatitude();
				lng = location.getLongitude();
			} else {
				// ��λʧ����
				// ���ֶ�ָ��һ��ֵ(�ҵĲ���)
				lat = MyApp.lastPoint.getLatitude();
				lng = MyApp.lastPoint.getLongitude();
			}
			LatLng mylocation = new LatLng(lat, lng);
			MarkerOptions option = new MarkerOptions();
			option.position(mylocation);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_marker));
			baiduMap.addOverlay(option);

			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mylocation);
			baiduMap.animateMapStatus(msu);

			TextView view = new TextView(LocationActivity.this);

			view.setText("������");
			view.setTextColor(Color.WHITE);
			view.setBackgroundColor(Color.RED);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			view.setPadding(5, 5, 5, 5);
			InfoWindow infowindow = new InfoWindow(view, mylocation, -50);
			baiduMap.showInfoWindow(infowindow);

			// �Ƿ���Ҫ�ظ���λ��
			// �ҵ��߼�����ʾ�ɹ��󣬾�ֹͣ��λ
			if (mLocationClient.isStarted()) {
				mLocationClient.unRegisterLocationListener(myListener);
				mLocationClient.stop();
			}

			// ��ʱmyLocation������һ�εĶ�λ���
			// Ӧ����MyApp.lastPoint���бȽϣ�
			// �����һ�£�Ӧ�ö�MyApp.lastPoint���и���
			if (mylocation.latitude != MyApp.lastPoint.getLatitude()
					|| mylocation.longitude != MyApp.lastPoint.getLongitude()) {
				// ����MyApp.lastPoint
				MyApp.lastPoint = new BmobGeoPoint(mylocation.longitude,
						mylocation.latitude);
				// ���µ�ǰ��¼�û���_user���е�λ��
				updateUserLocation(null);
			}

		}

	}

}
