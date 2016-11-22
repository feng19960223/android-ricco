package com.fgr.miaoxin.ui;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearFriendActivity extends BaseActivity {

	@Bind(R.id.mv_nearfriend_mapview)
	MapView mMapView;

	BaiduMap baiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_near_friend);
	}

	@Override
	public void init() {
		super.init();
		setHeaderTitle("��������", Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});

		initBaiduMap();
	}

	private void initBaiduMap() {
		baiduMap = mMapView.getMap();

		baiduMap.setMaxAndMinZoomLevel(20, 15);
		// ���Marker�����¼�������
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// �ӵ����Marker��ȡ����Ϣ
				Bundle bundle = marker.getExtraInfo();

				String username = bundle.getString("username");
				String avatar = bundle.getString("avatar");
				String time = bundle.getString("time");
				double lat = bundle.getDouble("lat");
				double lng = bundle.getDouble("lng");

				// �����Ϣ������ͼ����
				View infowindow = getLayoutInflater()
						.inflate(R.layout.infowindow_near_friend_layout,
								mMapView, false);

				TextView tvUsername = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_username);
				TextView tvTime = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_time);
				TextView tvDistance = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_distance);
				ImageView ivAvatar = (ImageView) infowindow
						.findViewById(R.id.iv_infowindow_nearfriend_avatar);
				Button btnAdd = (Button) infowindow
						.findViewById(R.id.btn_infowindow_nearfriend_add);

				tvUsername.setText(username);

				tvTime.setText(time.split(" ")[0]);

				if (TextUtils.isEmpty(avatar)) {
					ivAvatar.setImageResource(R.drawable.ic_launcher);
				} else {
					ImageLoader.getInstance().displayImage(avatar, ivAvatar);
				}

				tvDistance.setText((int) (MyApp.lastPoint
						.distanceInKilometersTo(new BmobGeoPoint(lng, lat)) * 1000)
						+ "��");
				// �����ťӦ����Ŀ���û�����һ����Ӻ�������
				btnAdd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO

						// ͨ���û���

						// BmobChatUser user = null;
						// BmobChatManager.getInstance(NearFriendActivity.this)
						// .sendTagMessage("add", user.getObjectId(),
						// new PushListener() {
						//
						// @Override
						// public void onSuccess() {
						// }
						//
						// @Override
						// public void onFailure(int arg0,
						// String arg1) {
						// LogUtil.d("TAG", "������Ӻ�������ʧ����,"
						// + arg0 + "," + arg1);
						// }
						// });

					}
				});
				// ������Ϣ��
				InfoWindow window = new InfoWindow(infowindow, new LatLng(lat,
						lng), -30);
				// �ڵ�ͼ����ʾ��Ϣ��
				baiduMap.showInfoWindow(window);

				return true;
			}
		});

		// Ϊ�ٶȵ�ͼ���һ�������¼�������
		// �������ͼʱ��������Ϣ��
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				baiduMap.hideInfoWindow();
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ��activityִ��onDestroyʱִ��mMapView.onDestroy()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// ��activityִ��onResumeʱִ��mMapView. onResume ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onResume();
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// ��activityִ��onPauseʱִ��mMapView. onPause ()��ʵ�ֵ�ͼ�������ڹ���
		mMapView.onPause();
	}

	private void refresh() {
		// ������������
		bmobUserManager.queryKiloMetersListByPage(false, 0, // ҳ��
				"location",// ��ʾλ�õ����Ե�����
				MyApp.lastPoint.getLongitude(),// �������ĵ�ľ���(��ǰ��¼�û�λ�õľ���)
				MyApp.lastPoint.getLatitude(),// �������ĵ��γ��(��ǰ��¼�û�λ�õ�γ��)
				false,// �Ƿ�������Щ�Ѿ��ǵ�ǰ��¼�û����ѵ���
				5.0, // �����뾶
				null, // �Ƿ�����ʱ����Ҫ�������������
				null, // ���ָ����������������ô��������������ֵ
				new FindListener<MyUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						toastAndLog("��ѯ����ʱ���ִ������Ժ�����", "��ѯ����ʱ���ִ���," + arg0
								+ ":" + arg1);

					}

					@Override
					public void onSuccess(List<MyUser> arg0) {
						if (arg0 != null && arg0.size() > 0) {

							showUserOnMap(arg0);

						} else {

							toast("����һ������Ҳû��/(��o��)/~~");
						}

					}
				});
	}

	/**
	 * �����������԰ٶȵ�ͼMarker����ʽ��ӵ���ͼ��
	 * 
	 * @param users
	 *            �������ĸ�������
	 */
	protected void showUserOnMap(List<MyUser> users) {
		// �ƶ���ͼ���ĵ㵽��ǰ��¼�û�λ��(MyApp.lastPoint)
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(
				MyApp.lastPoint.getLatitude(), MyApp.lastPoint.getLongitude()));
		baiduMap.animateMapStatus(msu);
		// ������������
		for (MyUser mu : users) {
			// ����Marker�Ĳ���
			final MarkerOptions option = new MarkerOptions();
			option.position(new LatLng(mu.getLocation().getLatitude(), mu
					.getLocation().getLongitude()));

			// �����û��Ա�����Markerʹ��ʲô����ͼƬ
			if (mu.getGender()) {
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.boy));
			} else {
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.girl));
			}
			// ��Marker��ӵ���ͼ��
			Marker marker = (Marker) baiduMap.addOverlay(option);
			// ���û��ĸ�����Ϣ��Ϣ��ΪMarker��ExtraInfo��ӵ�Marker��
			// ��Щ��Ϣ��Marker�������ʱ��ŵ�InfoWindow�г���
			Bundle bundle = new Bundle();

			bundle.putString("username", mu.getUsername());
			bundle.putString("avatar", mu.getAvatar());
			bundle.putString("time", mu.getUpdatedAt());
			bundle.putDouble("lat", mu.getLocation().getLatitude());
			bundle.putDouble("lng", mu.getLocation().getLongitude());
			bundle.putString("objectId", mu.getObjectId());

			marker.setExtraInfo(bundle);

		}
	}
}
