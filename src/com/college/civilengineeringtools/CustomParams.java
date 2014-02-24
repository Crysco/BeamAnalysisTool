package com.college.civilengineeringtools;

import com.college.civilengineeringtools.MainActivity.LoadType;

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

	public CustomParams(Context context, LoadType type) {
		super(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		
		if(type == LoadType.POINT_LOAD) 
			this.topMargin = (int) (GlobalProperties.beamTop- GlobalProperties.ptLoadHeight);
		else if(type == LoadType.DISTRIBUTED_LOAD)
			this.topMargin = (int) (GlobalProperties.beamTop- GlobalProperties.distLoadHeight);

	}

	public void setLeftMargin(double pos) {
		this.leftMargin = (int) (GlobalProperties.convertFeetToPixels(pos, MainActivity.getLength()) - 0.5 * GlobalProperties.loadWidth);
	}
}
