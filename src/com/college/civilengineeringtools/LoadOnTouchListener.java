package com.college.civilengineeringtools;

import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoadOnTouchListener implements OnTouchListener {

	private PointLoad pointLoad;
	private DistributedLoad dlLoad;
	private Context context;
	private TouchedObjectType tag;
	
	private TextView showPos;
	
	private enum TouchedObjectType {
		PL, DLS, DLE
	}
	
	private double pos, startPos, endPos;

	public LoadOnTouchListener(PointLoad pt, Context c) {
		pointLoad = pt;
		context = c;
		pos = pt.getPosition();
		tag = TouchedObjectType.PL;
	}

	public LoadOnTouchListener(DistributedLoad dl, int loc, Context c) {
		dlLoad = dl;
		context = c;

		startPos = dl.getStartingPosition();
		endPos = dl.getEndingPosition();

		if (loc == DistributedLoad.BEGIN)
			tag = TouchedObjectType.DLS;
		else if (loc == DistributedLoad.END)
			tag = TouchedObjectType.DLE;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!MainActivity.deleteTB.isChecked()) {
				Vibrator vb = (Vibrator) context
						.getSystemService(Context.VIBRATOR_SERVICE);
				
				CustomParams textParams = new CustomParams(context);

				switch (tag) {
				case PL:

					vb.vibrate(100);

					showPos = new TextView(context);
					showPos.setText(Double.toString(pos));
					textParams.setLeftMargin(pos);
					showPos.setLayoutParams(textParams);
					MainActivity.main.addView(showPos);

					break;

				case DLS:

					vb.vibrate(100);

					showPos = new TextView(context);
					showPos.setText(Double.toString(startPos));
					textParams.setLeftMargin(startPos);
					showPos.setLayoutParams(textParams);
					MainActivity.main.addView(showPos);
						
					break;
					
				case DLE:
					
					vb.vibrate(100);
					
					showPos = new TextView(context);
					showPos.setText(Double.toString(endPos));
					textParams.setLeftMargin(endPos);
					showPos.setLayoutParams(textParams);
					MainActivity.main.addView(showPos);
					
					break;
					
				}
			} else {
				
				int index;
				
				switch (tag) {
				case PL:
					
					index = pointLoad.getIndex();
					PointLoadManager.getInstance().deletePtLoad(index);
					break;
					
				case DLS:				
				case DLE:
					
					index = dlLoad.getIndex();
					DistributedLoadManager.getInstance().deleteDistLoad(index);
					break;
					
				}
			}

			break;

		case MotionEvent.ACTION_MOVE:

			if (!MainActivity.deleteTB.isChecked()) {

				RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) v
						.getLayoutParams();
				double x = (double) event.getRawX();

				if (x <= GlobalProperties.beamLeft - 0.5 * GlobalProperties.loadWidth) {
					mParams.leftMargin = (int) (GlobalProperties.beamLeft - 0.5 * GlobalProperties.loadWidth);
				} else if (x >= GlobalProperties.beamRight - 0.5 * GlobalProperties.loadWidth) {
					mParams.leftMargin = (int) (GlobalProperties.beamRight - 0.5 * GlobalProperties.loadWidth);
				} else {
					mParams.leftMargin = (int) x;
				}
				v.setLayoutParams(mParams);

				double actualPosition = (double) (Math.round(8*((x + 0.5 * GlobalProperties.loadWidth - GlobalProperties.beamLeft) * MainActivity.getLength()) 
						/ (GlobalProperties.displayWidth - 2 * GlobalProperties.beamLeft)) / 8f);

				switch (tag) {

				case PL:
					
					if (actualPosition <= 0)
						pointLoad.setPosition(0);
					else if (actualPosition >= MainActivity.getLength())
						pointLoad.setPosition(MainActivity.getLength());
					else
						pointLoad.setPosition(actualPosition);

					showPos.setText(Double.toString(pointLoad.getPosition()));
					break;
					
				case DLS:
					
					if (actualPosition <= 0)
						dlLoad.setStartingPosition(0);
					else if (actualPosition >= MainActivity.getLength())
						dlLoad.setStartingPosition(MainActivity.getLength());
					else
						dlLoad.setStartingPosition(actualPosition);

					MainActivity.distLoadLine.draw(MainActivity.distLoadCanvas);

					showPos.setText(Double.toString(dlLoad
							.getStartingPosition()));
					break;
					
				case DLE:
		
					if (actualPosition <= 0)
						dlLoad.setEndingPosition(0);
					else if (actualPosition >= MainActivity.getLength())
						dlLoad.setEndingPosition(MainActivity.getLength());
					else
						dlLoad.setEndingPosition(actualPosition);

					MainActivity.distLoadLine.draw(MainActivity.distLoadCanvas);

					showPos.setText(Double.toString(dlLoad.getEndingPosition()));
					break;
					
				default:
					break;
				}
			}
			break;

		case MotionEvent.ACTION_UP:
			MainActivity.main.removeView(showPos);
			showPos = null;
		default:
			break;
		}
		return true;

	}

}
