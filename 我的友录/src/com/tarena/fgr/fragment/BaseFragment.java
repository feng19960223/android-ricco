package com.tarena.fgr.fragment;

import com.tarena.fgr.youlu.R;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ������fragment�ĸ���,����fragment��Ҫ�̳��Ը���,�����з�װ���еı�������ʼ������,
 * 
 * �ṩ�����������,��������
 * 
 * @author �����
 * 
 *         2016��9��29�� 14:49:53
 */
public class BaseFragment extends Fragment {
	protected View contentView = null;
	// �Զ������
	protected LinearLayout actionbar = null;

	/**
	 * ͨ���˷������Զ�actionbar��������,ʵ���Զ����actionbar�ĳ�ʼ����<br>
	 * <br>
	 * leftId��rightId �����ֵΪ-1���ʾ����<br>
	 * title Ϊnull��""ʱ��ʾ����
	 * 
	 * @param leftId
	 *            ���ͼƬ��Դid
	 * @param title
	 *            �м����������ı�
	 * @param rightId
	 *            �ұ�ͼƬ��Դid
	 */
	protected void initialActionbar(int leftId, String title, int rightId) {
		if (actionbar == null) {// ���û��actionbarֱ�ӷ���,����������
			return;
		}
		// ��øñ����������еĿؼ�
		ImageView imageViewLeft = (ImageView) actionbar
				.findViewById(R.id.imageView_actionbar_left);
		TextView textViewTitle = (TextView) actionbar
				.findViewById(R.id.textView_actionbar_title);
		ImageView imageViewRight = (ImageView) actionbar
				.findViewById(R.id.imageView_actionbar_right);
		// ��ͼƬ,�����ֵΪ-1���ʾ����
		if (leftId == -1) {
			imageViewLeft.setVisibility(View.INVISIBLE);
			// 2016��9��29�� 19:32:02
			// �����:�Ҷ�INVISIBLE��GONE������ʵ�ʴ������
			// ����INVISIBLEֻ�����ص���,android:layout_weight="1"��������,����ռ�õ����Ŀؼ�
			// ��GONE,��ֻ������,����android:layout_weight="1"��������,��ռ�õ����Ŀռ�
			// ����GONEӦ�ý���ʹ��,�ֿ��ܻ���ֲ���title���ҵ����
		} else {
			imageViewLeft.setVisibility(View.VISIBLE);
			imageViewLeft.setImageResource(leftId);
		}
		// �м�����,������ջ�""������
		if (TextUtils.isEmpty(title)) {
			textViewTitle.setVisibility(View.INVISIBLE);
		} else {
			textViewTitle.setVisibility(View.VISIBLE);
			textViewTitle.setText(title);
		}
		// ��ͼƬ,�����ֵΪ-1���ʾ����
		if (rightId == -1) {
			imageViewRight.setVisibility(View.INVISIBLE);
		} else {
			imageViewRight.setVisibility(View.VISIBLE);
			imageViewRight.setImageResource(rightId);
		}

	}
}
