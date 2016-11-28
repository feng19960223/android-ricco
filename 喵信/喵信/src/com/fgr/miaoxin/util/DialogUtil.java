package com.fgr.miaoxin.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.widget.Button;

public class DialogUtil {
	public static void showDialog(Context context, String title,
			String message, boolean cancelButton, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("ȷ��", listener);
		if (cancelButton) {
			builder.setNegativeButton("ȡ��", null);
		} else {
			builder.setCancelable(false);
		}

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// �ı䰴ť��ɫ
		// һ��Ҫд��show��������
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);// ȷ��
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// ��������һ��btn�п��ܻ��и߶�ƫ��,����ֱ��������2��
		// ��ʹ��AlertDialog.THEME_DEVICE_DEFAULT_LIGHT��仰�ͻᷢ��ƫ��
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);// ȡ��
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}
}
