package com.college.civilengineeringtools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class DrawGraph extends View {

	private Paint graphPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint circlePaintOut = new Paint();
	Context con;
	Bitmap bitmap;

	public DrawGraph(Context context) {
		super(context);
		con = context;
		graphPaint.setColor(Color.BLUE);
		circlePaint.setColor(Color.RED);
		circlePaintOut.setColor(Color.RED);
		circlePaint.setStyle(Style.FILL);
		circlePaintOut.setStyle(Style.STROKE);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.save();

		if (MainActivity.graphMode == MainActivity.SHEAR)
			canvas.drawLines(DiagramActivity.vLines, graphPaint);
		else if (MainActivity.graphMode == MainActivity.MOMENT)
			canvas.drawLines(DiagramActivity.mLines, graphPaint);

		canvas.restore();

	}

	public void stop() {
		
	}

}
