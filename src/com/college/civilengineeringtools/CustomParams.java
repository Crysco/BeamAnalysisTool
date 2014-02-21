package com.college.civilengineeringtools;

import android.content.Context;
import android.widget.RelativeLayout;

public class CustomParams extends RelativeLayout.LayoutParams {
	
	public CustomParams(Context context) {
		super(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		this.topMargin = (int) (GlobalProperties.beamTop
				- GlobalProperties.ptLoadHeight);

	}
	
	public CustomParams(Context context, int x, int y) {
		super(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		this.topMargin = (int) y;
		this.leftMargin = (int) x;

	}

	public CustomParams(Context context, int loadType) {
		super(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		if(loadType == MainActivity.POINT_LOAD) 
			this.topMargin = (int) (GlobalProperties.beamTop- GlobalProperties.ptLoadHeight);
		else if(loadType == MainActivity.DIST_LOAD)
			this.topMargin = (int) (GlobalProperties.beamTop- GlobalProperties.distLoadHeight);

	}

	public void setLeftMargin(double pos) {
		this.leftMargin = (int) (GlobalProperties.convertFeetToPixels(pos, MainActivity.getLength()) - 0.5 * GlobalProperties.loadWidth);
	}
}
