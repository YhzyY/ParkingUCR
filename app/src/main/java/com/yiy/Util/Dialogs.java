package com.yiy.Util;

import android.app.ProgressDialog;
import android.content.Context;

public class Dialogs {
	private String s="searching ...";
	public static ProgressDialog dialog;
	public Dialogs(Context context){
		dialog = new ProgressDialog(context);
		dialog.setTitle("Wait");
		dialog.setMessage(s);
	}

}
