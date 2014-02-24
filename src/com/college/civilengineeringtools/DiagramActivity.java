package com.college.civilengineeringtools;

import java.util.ArrayList;

import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiagramActivity extends FragmentActivity implements
		OnTouchListener {

	private final double feetLength = MainActivity.getLength();
	private double vNewX, mNewX, vNewY, mNewY;
	double maxShear, maxMoment;
	private DrawGraph graph;
	public static float[] vLines, mLines;

	private double actualPosition = 0;
	private double result = 0;

	private Canvas canvas = new Canvas();

	private ImageView point;

	private TextView graphType, positionTextView, resultTextView;
	private RelativeLayout rl;
	private LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.diagrams);

		getWindow().setBackgroundDrawable(GlobalProperties.resizedGraph);

		rl = (RelativeLayout) findViewById(R.id.rlDiagram);
		ll = (LinearLayout) findViewById(R.id.llDiagram);

		graphType = new TextView(this);
		if (MainActivity.graphMode == MainActivity.SHEAR)
			graphType.setText("Shear (kip)");
		else if (MainActivity.graphMode == MainActivity.MOMENT)
			graphType.setText("Moment (kip-ft)");
		rl.addView(graphType);

		point = new ImageView(this);
		point.setImageBitmap(GlobalProperties.resizedPoint);
		point.setLayoutParams(new CustomParams(
				this,
				(int) (GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth),
				(int) (0.5 * GlobalProperties.displayHeight - 0.5 * GlobalProperties.pointHeight)));
		point.setOnTouchListener(this);

		rl.addView(point);

		positionTextView = new TextView(this);
		positionTextView.setTextSize(18);

		resultTextView = new TextView(this);
		resultTextView.setTextSize(18);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		final int graphResolution = 500;

		mLines = new float[(graphResolution + 1) * 4];
		vLines = new float[(graphResolution + 1) * 4];

		double vOldX = GlobalProperties.leftMargin;
		double mOldX = GlobalProperties.leftMargin;
		double vOldY = GlobalProperties.displayHeight / 2;
		double mOldY = GlobalProperties.displayHeight / 2;

		maxShear = getMaxShear();
		ArrayList<Double> possibleMaxMomentPos = new ArrayList<Double>();

		int j = 0;
		for (double i = 0; i <= feetLength; i += (feetLength / graphResolution)) {

			double thisShear = ResultCalculator.getInstance().solveTotalShear(
					PointLoadManager.getInstance(),
					DistributedLoadManager.getInstance(), feetLength, i);

			vNewX = (GlobalProperties.pixelLength * i / feetLength) + GlobalProperties.leftMargin;

			vNewY = 0.5
					* GlobalProperties.displayHeight
					- 0.8
					* (0.5 * GlobalProperties.displayHeight * thisShear / maxShear);

			vLines[j + 0] = (float) vOldX;
			vLines[j + 1] = (float) vOldY;
			vLines[j + 2] = (float) vNewX;
			vLines[j + 3] = (float) vNewY;

			if ((vNewY >= (GlobalProperties.displayHeight / 2) && vOldY <= (GlobalProperties.displayHeight / 2))
					|| (vNewY <= (GlobalProperties.displayHeight / 2) && vOldY >= (GlobalProperties.displayHeight / 2)))
				possibleMaxMomentPos.add(i);

			vOldX = vNewX;
			vOldY = vNewY;

			j += 4;

		}

		maxMoment = getMaxMoment(possibleMaxMomentPos);
		possibleMaxMomentPos.clear();

		int k = 0;
		for (double i = 0; i < feetLength; i += (feetLength / graphResolution)) {

			double thisMoment = ResultCalculator.getInstance()
					.solveTotalMoment(PointLoadManager.getInstance(),
							DistributedLoadManager.getInstance(), feetLength, i);

			mNewX = (float) ((GlobalProperties.pixelLength * i / feetLength) + GlobalProperties.leftMargin);

			mNewY = (float) (0.5 * GlobalProperties.displayHeight - 0.8 * (0.5
					* GlobalProperties.displayHeight * thisMoment / maxMoment));

			mLines[k + 0] = (float) mOldX;
			mLines[k + 1] = (float) mOldY;
			mLines[k + 2] = (float) mNewX;
			mLines[k + 3] = (float) mNewY;

			mOldX = mNewX;
			mOldY = mNewY;

			k += 4;

			ResultCalculator.getInstance().reset();

		}

		j = k = 0;

		graph = new DrawGraph(this);

		graph.draw(canvas);

		rl.addView(graph);
		
		positionTextView.setText(String.format("x = %.2f ft", actualPosition));
		ll.addView(positionTextView);

		if (MainActivity.graphMode == MainActivity.SHEAR) {
			result = ResultCalculator.getInstance().solveTotalShear(
					PointLoadManager.getInstance(),
					DistributedLoadManager.getInstance(), feetLength, 0);
			resultTextView.setText(String.format("V = %.2f kip", result));
		} else if (MainActivity.graphMode == MainActivity.MOMENT) {
			result = ResultCalculator.getInstance().solveTotalMoment(
					PointLoadManager.getInstance(),
					DistributedLoadManager.getInstance(), feetLength, 0);
			resultTextView.setText(String.format("M = %.2f kip-ft", result));
		}
		
		ll.addView(resultTextView);

	}

	@Override
	public void onBackPressed() {
		finish();
	}

	private double getMaxShear() {
		// TODO Auto-generated method stub
		ResultCalculator.getInstance().solveTotalShear(
				PointLoadManager.getInstance(),
				DistributedLoadManager.getInstance(), feetLength, 0);
		double tmp = Math.abs(ResultCalculator.getInstance()
				.getTotalReactionA());

		ResultCalculator.getInstance().reset();

		ResultCalculator.getInstance().solveTotalShear(
				PointLoadManager.getInstance(),
				DistributedLoadManager.getInstance(), feetLength, feetLength);
		double tmp2 = Math.abs(ResultCalculator.getInstance()
				.getTotalReactionB());

		ResultCalculator.getInstance().reset();

		return (tmp > tmp2) ? tmp : tmp2;
	}

	private double getMaxMoment(ArrayList<Double> poss) {
		// TODO Auto-generated method stub

		double maxMoment = 0;

		if (MainActivity.beamType == BeamOptionsActivity.SIMPLE_BEAM) {
			double oldMax = 0;
			maxMoment = 0;

			for (int i = 0; i < poss.size(); i++) {

				double position = poss.get(i);

				ResultCalculator.getInstance().solveTotalMoment(
						PointLoadManager.getInstance(),
						DistributedLoadManager.getInstance(), feetLength, position);
				double newMax = Math.abs(ResultCalculator.getInstance()
						.getTotalMoment());

				if (newMax > oldMax) {
					maxMoment = newMax;
					oldMax = newMax;
				}

				ResultCalculator.getInstance().reset();
			}
		} else if (MainActivity.beamType == BeamOptionsActivity.CANTILEVER) {
			ResultCalculator.getInstance().solveTotalMoment(
					PointLoadManager.getInstance(),
					DistributedLoadManager.getInstance(), feetLength, 0);
			maxMoment = Math.abs(ResultCalculator.getInstance()
					.getTotalMoment());
		}

		Log.d("Max Moment", Double.toString(maxMoment));

		return maxMoment;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_MOVE) {

			RelativeLayout.LayoutParams mParams = (RelativeLayout.LayoutParams) v
					.getLayoutParams();

			double x = (double) event.getRawX();

			double y = 0;

			if (MainActivity.graphMode == MainActivity.SHEAR) {
				double shear = ResultCalculator.getInstance().solveTotalShear(
						PointLoadManager.getInstance(),
						DistributedLoadManager.getInstance(), feetLength,
						GlobalProperties.convertPixelsToFeet(x, feetLength));

				y = 0.5
						* GlobalProperties.displayHeight
						- 0.8
						* (0.5 * GlobalProperties.displayHeight * shear / maxShear)
						- 0.5 * GlobalProperties.pointHeight;
			} else if (MainActivity.graphMode == MainActivity.MOMENT) {
				double moment = ResultCalculator
						.getInstance()
						.solveTotalMoment(PointLoadManager.getInstance(),
								DistributedLoadManager.getInstance(), feetLength,
								GlobalProperties.convertPixelsToFeet(x, feetLength));

				y = 0.5
						* GlobalProperties.displayHeight
						- 0.8
						* (0.5 * GlobalProperties.displayHeight * moment / maxMoment)
						- 0.5 * GlobalProperties.pointHeight;
			}

			double actualPosition = 0;
			if (x <= GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth) {
				mParams.leftMargin = (int) (GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth);
				mParams.topMargin = (int) (0.5 * GlobalProperties.displayHeight - 0.5
						* GlobalProperties.pointHeight);
				actualPosition = GlobalProperties.convertPixelsToFeet(GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointHeight, feetLength);
			} else if (x >= GlobalProperties.pixelLength + GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth) {
				mParams.leftMargin = (int) (GlobalProperties.pixelLength + GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth);
				mParams.topMargin = (int)(0.5 * GlobalProperties.displayHeight - 0.5
						* GlobalProperties.pointHeight);
				actualPosition = GlobalProperties.convertPixelsToFeet(GlobalProperties.pixelLength + GlobalProperties.leftMargin - 0.5 * GlobalProperties.pointWidth, feetLength);
			} else {
				mParams.leftMargin = (int) x;
				mParams.topMargin = (int) y;
				actualPosition = GlobalProperties.convertPixelsToFeet(x,  feetLength);
			}

			v.setLayoutParams(mParams);

			positionTextView.setText(String.format("x = %.2f ft", actualPosition));
			if (MainActivity.graphMode == MainActivity.SHEAR) {
				result = ResultCalculator.getInstance().solveTotalShear(
						PointLoadManager.getInstance(),
						DistributedLoadManager.getInstance(), feetLength,
						actualPosition);
				resultTextView.setText(String.format("V = %.2f kip", result));
			} else if (MainActivity.graphMode == MainActivity.MOMENT) {
				result = ResultCalculator.getInstance().solveTotalMoment(
						PointLoadManager.getInstance(),
						DistributedLoadManager.getInstance(), feetLength,
						actualPosition);
				resultTextView.setText(String.format("M = %.2f kip-ft", result));
			}

		}

		return true;
	}

}
