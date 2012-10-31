package com.joedev.iou;

import android.util.DisplayMetrics;
import android.view.WindowManager;

public class Screen {
	
	private WindowManager windowManager;
	
	public Screen(WindowManager windowManager) {
		this.windowManager = windowManager;
	}
	
	public int get_width() {
		int width;
		DisplayMetrics displaymetrics = new DisplayMetrics();
		this.windowManager.getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		return width;
	}
	
}
