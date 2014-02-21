package com.college.civilengineeringtools;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawDistLoad extends View {

	private Paint paint = new Paint();
	Context con;
	private ArrayList<DistributedLoad> loads;
	private double startPositions, endPositions;

	public DrawDistLoad(Context context) {
		super(context);
		con = context;
		paint.setColor(Color.rgb(0, 128, 0));
		loads = new ArrayList<DistributedLoad>();
		startPositions = 0;
		endPositions = 0;
	}

	public void update() {

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		loads = DistributedLoadManager.getInstance().getDistLoads();

		for (int i = 0; i < loads.size(); i++) {
			startPositions = loads.get(i).getStartingPosition();
			endPositions = loads.get(i).getEndingPosition();

			double startX = GlobalProperties.convertFeetToPixels(
					startPositions, MainActivity.getLength());
			double endX = GlobalProperties.convertFeetToPixels(
					endPositions, MainActivity.getLength());

			canvas.drawLine(
					(float) startX,
					(float) (GlobalProperties.beamTop - GlobalProperties.distLoadHeight),
					(float) endX,
					(float) (GlobalProperties.beamTop - GlobalProperties.distLoadHeight),
					paint);

			for (double j = startX; j <= endX; j += 0.5 * GlobalProperties.loadWidth) {
				canvas.drawLine(
						(float) j,
						(float) (GlobalProperties.beamTop - GlobalProperties.distLoadHeight),
						(float) j, (float) GlobalProperties.beamTop, paint);
			}
		}
		
		//this.invalidate();

	}

	public void stop() {

	}

}