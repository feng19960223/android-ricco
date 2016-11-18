package com.fgr.miaoxin.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.RobotAdapter;
import com.fgr.miaoxin.bean.MyRobot;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.LogUtil;

public class RobotActivity extends BaseActivity {
	@Bind(R.id.lv_robot_listview)
	ListView listview;
	RobotAdapter adapter;
	List<MyRobot> robots;

	@Bind(R.id.et_robot_content)
	EditText etContent;

	private String[] hello = { "�����Ѿ������ʱ��!��", "����B����ԶҪ������!", "����B��,֪���������ϵ�һ��",
			"����B�θ�Ӧ����,����Ҫ��", "��������,�ҽ������,������ҵ����", "��ǿ�����Ѿ�����,��ʱ���Գ���",
			"�þò���,��������!��", "��ʲô���԰��������?", "������������Ц��...��ȫ����,ҪŮ���Ѹ�ʲô?" };
	RequestQueue queque;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queque = Volley.newRequestQueue(this);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_robot);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("������");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initListView() {
		robots = new ArrayList<MyRobot>();
		robots.add(new MyRobot(hello[new Random().nextInt(hello.length)],
				MyRobot.RECEIVER, getTime()));// �ʺ���
		adapter = new RobotAdapter(this, robots);
		listview.setAdapter(adapter);
	}

	@OnClick(R.id.btn_robot_send)
	public void send(View v) {
		String content = etContent.getText().toString();
		adapter.addItem(new MyRobot(content, MyRobot.SEND, getTime()));
		if (robots.size() > 30) {// ����30����¼,���������¼,дTXT�ļ�......
			for (int i = 0; i < robots.size(); i++) {
				robots.remove(i);
			}
		}
		loaddata(content);
	}

	private double currentTime = 0, oldTime = 0;

	@SuppressLint("SimpleDateFormat")
	private String getTime() {// ��������,5����ʾһ��ʱ��
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if (currentTime - oldTime >= 1000 * 60) {
			oldTime = currentTime;
			return str;
		} else {
			return "";
		}
	}

	public void loaddata(String info) {
		String url = "http://www.tuling123.com/openapi/api?key=fdb4cb1a9303446dba1be5dfdf95e9f7&info="
				+ info;
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				LogUtil.i("TAG:response", response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					adapter.addItem(new MyRobot(jsonObject.getString("text"),
							1, getTime()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				toastAndLog("����B����æ,���Ժ�����", error.getMessage());
			}
		});
		queque.add(request);
	}

}
