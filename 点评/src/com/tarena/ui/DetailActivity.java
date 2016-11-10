package com.tarena.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.tarena.adapter.CommentAdapter;
import com.tarena.entity.Business.Businesses;
import com.tarena.entity.Comment;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;

public class DetailActivity extends Activity {
	private Businesses businesses;
	private ImageView imageView_back = null;// ����
	private ListView listView_detail = null;
	private CommentAdapter adapter = null;
	int[] resIds = new int[] { R.drawable.star0, R.drawable.star10,
			R.drawable.star20, R.drawable.star30, R.drawable.star35,
			R.drawable.star40, R.drawable.star45, R.drawable.star50 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		businesses = (Businesses) getIntent().getSerializableExtra("business");
		listView_detail = (ListView) findViewById(R.id.listView_detail);
		initData();
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		imageView_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.include_detail_pull1,
				listView_detail, false);
		LinearLayout ll = (LinearLayout) view
				.findViewById(R.id.linearlayout_address);
		ll.setOnClickListener(new OnClickListener() {// ��ͼ
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this,
						FindActivity.class);
				intent.putExtra("business", businesses);
				intent.putExtra("from", "detail");// ���,���̻�ҳ����ת��,��һ������,�Ǵӷ�����ת��
				startActivity(intent);
			}
		});
		initView(view);
		listView_detail.addHeaderView(view);
		adapter = new CommentAdapter(this, new ArrayList<Comment>());
		listView_detail.setAdapter(adapter);
		refresh();
	}

	private void refresh() {
		String url = businesses.getReview_list_url();
		HttpUtil.getComments(url, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				// try {
				// FileWriter fw = new FileWriter(
				// new File(
				// Environment
				// .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				// "dianping.text"));
				// fw.write(arg0);
				// fw.flush();
				// fw.close();
				// Toast.makeText(DetailActivity.this, "ok",
				// Toast.LENGTH_SHORT).show();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				Document document = Jsoup.parse(arg0);
				List<Comment> comments = new ArrayList<Comment>();
				Elements elements = document
						.select(".comment-list ul li[data-id]");
				for (Element element : elements) {
					Comment comment = new Comment();
					// ��element��ȡ����Ӧ��������Ϊcommment������
					// ȡ���ѵ��û���,ͷ��
					Elements imgElements = element.select(".pic a img");
					Element imgElement = imgElements.get(0);
					comment.setUsername(imgElement.attr("title"));
					comment.setAvatar(imgElement.attr("src"));
					// ѡȡΪ�̼ҵĴ��
					Elements spanElements = element
							.select(".user-info span[title]");
					Element spanElement = spanElements.get(0);
					String rat = spanElement.attr("class");
					// sml-rank-stars sml-str40
					String rating = rat.split(" ")[1].split("-")[1];// 40
					comment.setRating(rating);
					// ѡȡ���۵�����
					Elements contentElements = element.select(".J_brief-cont");
					Element contentElement = contentElements.get(0);
					comment.setContent(contentElement.text());
					// ѡȡ������Ѽ۸�
					Elements spans = element.select(".comm-per");
					if (spans != null && spans.size() > 0) {// �˾� ��40
						// �ṩ���˾��۸�
						comment.setAvgPrice(spans.get(0).text().split(" ")[1]);
					} else {
						// û���ṩ�˾��۸�
						comment.setAvatar("");
					}
					// ѡȡ���۵���ͼ
					// ѡȡ���۵���ͼ
					Elements imgs = element.select(".shop-photo img");
					if (imgs != null && imgs.size() > 0) {
						// ����������ͼ
						int num = imgs.size();
						if (num > 3) {
							// ��ͼ����3�ţ�����ȡ3��
							num = 3;
						}
						String[] pics = new String[num];
						for (int i = 0; i < num; i++) {
							Element img = imgs.get(i);
							pics[i] = img.attr("src");
						}
						comment.setImgs(pics);
					} else {
						// ������û����ͼ
						comment.setImgs(null);
					}
					comments.add(comment);
				}
				adapter.addAll(comments, true);
			}
		});

	}

	private void initView(View view) {
		ImageView imageView_main = (ImageView) view
				.findViewById(R.id.imageView_main);// �̵�ͼƬ
		TextView textView_name = (TextView) view
				.findViewById(R.id.textView_name);// �̵�����
		ImageView imageView_star = (ImageView) view
				.findViewById(R.id.imageView_star);// ����ͼƬ
		TextView textView_pl = (TextView) view.findViewById(R.id.textView_pl);// ������
		TextView textView_jg = (TextView) view.findViewById(R.id.textView_jg);// �۸�
		TextView textView_wz = (TextView) view.findViewById(R.id.textView_wz);// �ص�
		TextView textView_lx = (TextView) view.findViewById(R.id.textView_lx);// ����
		TextView textView_xxwz = (TextView) view
				.findViewById(R.id.textView_xxwz);// ��ϸ�ص�
		TextView textView_phone = (TextView) view
				.findViewById(R.id.textView_phone);// �绰
		String url = businesses.getS_photo_url();
		HttpUtil.displayImage(url, imageView_main);
		String name = businesses.getName().substring(0,// ������
				businesses.getName().indexOf("("));
		String branchName = businesses.getBranch_name();// �ֵ�����
		if (!TextUtils.isEmpty(branchName)) {
			name += "(" + businesses.getBranch_name() + ")";
		}
		textView_name.setText(name);// ��������+�ֵ�����
		imageView_star.setImageResource(resIds[new Random()
				.nextInt(resIds.length)]);// �����޷���ȡ,���
		textView_jg.setText("��" + (new Random().nextInt(200) + 50) + "/��");// �˾��۸��޷��õ�,���
		textView_lx.setText(businesses.getCategories().get(0)); // ��������
		List<String> regions = businesses.getRegions();
		StringBuilder sb = new StringBuilder();
		for (String string : regions) {
			sb.append(string).append("/");
		}
		textView_wz.setText(sb.deleteCharAt(sb.length() - 1).toString());// �ص�
		textView_pl.setText(new Random().nextInt(2000) + "��");
		textView_xxwz.setText(businesses.getAddress());
		textView_phone.setText(businesses.getTelephone());
	}
}
