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
import butterknife.Bind;
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

	String from;// "mylocation"������Ҫ��λ "showaddress"������Ҫ��ʾһ��ָ����ַ
	@Bind(R.id.bmapView)
	MapView mapView;
	BaiduMap baiduMap;

	LocationClient client;
	BDLocationListener listener;

	ProgressDialog pd;

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
		baiduMap = mapView.getMap();
		initBaiduMap();
		if ("mylocation".equals(from)) {
			// ��λ
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
										File file = new File(
												Environment
														.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
												System.currentTimeMillis()
														+ ".jpg");
										OutputStream stream = new FileOutputStream(
												file);
										bitmap.compress(CompressFormat.JPEG,
												30, stream);
										final String localfilePath = file
												.getAbsolutePath();
										// ����ͼ��ͼ�ϴ���������
										final BmobFile bf = new BmobFile(file);
										bf.uploadblock(LocationActivity.this,
												new UploadFileListener() {

													@Override
													public void onSuccess() {
														final String url = bf
																.getFileUrl(LocationActivity.this);
														// ���ݶ�λ�õ��ľ�γ�ȣ����нֵ����ƵĲ�ѯ
														GeoCoder geoCoder = GeoCoder
																.newInstance();
														geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

															@Override
															public void onGetReverseGeoCodeResult(
																	ReverseGeoCodeResult arg0) {
																pd.dismiss();
																// ���ݸ����ľ�γ���ҵ��˶�Ӧ�Ľֵ�����
																String address;
																if (arg0 == null
																		|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {

																	address = "λ�õ�·";

																} else {
																	address = arg0
																			.getAddress();

																}

																Intent data = new Intent();
																data.putExtra(
																		"address",
																		address);
																data.putExtra(
																		"localFilePath",
																		localfilePath);
																data.putExtra(
																		"url",
																		url);
																setResult(
																		RESULT_OK,
																		data);
																finish();

															}

															@Override
															public void onGetGeoCodeResult(
																	GeoCodeResult arg0) {
																// TODO
																// Auto-generated
																// method stub

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
														pd.dismiss();
														toastAndLog(
																"��ͼʧ�ܣ��Ժ�����",
																arg0, arg1);

													}
												});

									} catch (Exception e) {
										if (pd != null)
											pd.dismiss();
										e.printStackTrace();
									}
								}
							});
						}
					});

			getMyLocation();

		} else {
			// ��ʾһ��λ��
			String address = getIntent().getStringExtra("address");
			setHeaderTitle(address);
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();

						}
					});

			showAddress();
		}
	}

	private void showAddress() {
		double lat = getIntent().getDoubleExtra("lat", 0.0);
		double lng = getIntent().getDoubleExtra("lng", 0.0);

		LatLng location = new LatLng(lat, lng);

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(location);

		baiduMap.animateMapStatus(msu);

		MarkerOptions option = new MarkerOptions();
		option.position(location);
		option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		baiduMap.addOverlay(option);

	}

	private void getMyLocation() {
		client = new LocationClient(this);
		listener = new MyLocationListener();
		client.registerLocationListener(listener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// ��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setCoorType("bd09ll");// ��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		int span = 1000 * 60 * 5;
		option.setScanSpan(span);// ��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		option.setIsNeedAddress(true);// ��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setOpenGps(true);// ��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setLocationNotify(true);// ��ѡ��Ĭ��false�������Ƿ�GPS��Чʱ����1S/1��Ƶ�����GPS���
		option.setIsNeedLocationDescribe(true);// ��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationPoiList(true);// ��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIgnoreKillProcess(false);// ��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��
		option.SetIgnoreCacheException(false);// ��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.setEnableSimulateGps(false);// ��ѡ��Ĭ��false�������Ƿ���Ҫ����GPS��������Ĭ����Ҫ
		client.setLocOption(option);
		client.start();

	}

	private void initBaiduMap() {
		baiduMap.setMaxAndMinZoomLevel(20, 15);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onDestroy();
		if (client != null) {
			client.stop();
			client = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mapView.onPause();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();

			double lat = -1;
			double lng = -1;

			if (code == 61 || code == 66 || code == 161) {

				lat = location.getLatitude();
				lng = location.getLongitude();

			} else {

				lat = MyApp.lastPoint.getLatitude();
				lng = MyApp.lastPoint.getLongitude();
			}

			LatLng mylocation = new LatLng(lat, lng);

			// �ƶ���Ļ���ĵ�
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mylocation);
			baiduMap.animateMapStatus(msu);
			// ��һ����־
			MarkerOptions option = new MarkerOptions();
			option.position(mylocation);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_marker));
			baiduMap.addOverlay(option);

			// ��һ����Ϣ��

			TextView textview = new TextView(LocationActivity.this);
			textview.setText("������");
			textview.setBackgroundColor(Color.RED);
			textview.setTextColor(Color.WHITE);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics());
			textview.setPadding(padding, padding, padding, padding);
			InfoWindow infowindow = new InfoWindow(textview, mylocation, -50);
			baiduMap.showInfoWindow(infowindow);

			if (client.isStarted()) {
				client.stop();
				client.registerLocationListener(listener);
			}

			if (mylocation.latitude != MyApp.lastPoint.getLatitude()
					|| mylocation.longitude != MyApp.lastPoint.getLongitude()) {
				MyApp.lastPoint = new BmobGeoPoint(lng, lat);
				updateUserLocation(null);
			}

		}

	}

}
