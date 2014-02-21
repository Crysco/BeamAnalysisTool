package com.college.civilengineeringtools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.WindowManager;

public class GlobalProperties {

	private static WindowManager wm;
	private static Display display;

	public static double displayWidth = 0;
	public static double displayHeight = 0;

	public static double resolutionScaleX = 0;
	public static double resolutionScaleY = 0;

	public static double ptLoadHeight = 0;
	public static double loadWidth = 0;
	public static double distLoadHeight;

	public static double beamTop = 0;
	public static double beamBottom = 0;
	public static double beamLeft = 0;
	public static double beamRight = 0;

	public static Bitmap resizedPt = null;
	public static Bitmap resizedDist = null;
	public static Drawable resizedGraph = null;
	public static Bitmap resizedPoint = null;
	
	public static double pointWidth = 0;
	public static double pointHeight = 0;
	
	public static double leftMargin = 0;
	public static double pixelLength = 0;

	public GlobalProperties(Context context) {

		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();

		displayWidth = display.getWidth();
		displayHeight = display.getHeight();

		resolutionScaleX = displayWidth / 1600;
		resolutionScaleY = displayHeight / 960;

		Bitmap bitmapPt = BitmapFactory.decodeResource(context.getResources(), R.drawable.point_load);
		Bitmap bitmapDist = BitmapFactory.decodeResource(context.getResources(), R.drawable.dist_load);
		int width = 80;
		int ptHeight = 160;
		int distHeight = 120;

		resizedPt = Bitmap.createScaledBitmap(bitmapPt, (int) (resolutionScaleX * width), (int) (GlobalProperties.resolutionScaleY * ptHeight), false);
		resizedDist = Bitmap.createScaledBitmap(bitmapDist, (int) (resolutionScaleX * width), (int) (GlobalProperties.resolutionScaleY * distHeight), false);

		ptLoadHeight = (double) resizedPt.getHeight();
		loadWidth = (double) resizedPt.getWidth();
		distLoadHeight = (double) resizedDist.getHeight();

		Bitmap pointOrg = BitmapFactory.decodeResource(context.getResources(), R.drawable.point);
		int pointSize = 80;

		resizedPoint = Bitmap.createScaledBitmap(pointOrg, (int) (resolutionScaleX * pointSize), (int) (GlobalProperties.resolutionScaleY * pointSize), false);
		
		pointHeight = (double) resizedPoint.getHeight();
		pointWidth = (double) resizedPoint.getWidth();

		Bitmap graph = BitmapFactory.decodeResource(context.getResources(), R.drawable.graph);
		int backWidth = 1600;
		int backHeight = 960;

		resizedGraph = new BitmapDrawable(context.getResources(), Bitmap.createScaledBitmap(graph, (int) (resolutionScaleX * backWidth), 
				(int) (resolutionScaleY * backHeight), false));

		beamTop = (double) (0.45 * displayHeight);
		beamBottom = (double) (0.55 * displayHeight);
		beamLeft = (double) (0.025 * displayWidth);
		beamRight = (double) (0.975 * displayWidth);
		
		leftMargin = (double) (0.05 * displayWidth);
		pixelLength = (double) (0.75 * displayWidth - leftMargin);

	}

	public static double convertFeetToPixels(double x, double length) {

		return x * ((displayWidth - 2 * beamLeft) / length) + beamLeft;
	}

	public static double convertPixelsToFeet(double x, double length) {

		return (double) (Math.round(8*((x + 0.5 * GlobalProperties.pointWidth - GlobalProperties.leftMargin) * length) 
				/ pixelLength) / 8f);
	}

}
