package com.college.civilengineeringtools;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends FragmentActivity implements OnClickListener,
		LoadOptionsDialogFragment.NoticeDialogListener,
		InputDialogFragment.NoticeDialogListener {
	
	public static enum LoadType{
		POINT_LOAD, DISTRIBUTED_LOAD
	}
	
	private static LoadType loadMode;

	public static int beamType;

	private Button addLoad, showShear, showMom;
	public static ToggleButton deleteTB;
	private static EditText lgth;

	private static double length = 0;

	private DialogFragment addOptions;

	public static ResultCalculator resCalc;

	public static RelativeLayout main, rlpl, rldl, dlTop;
	
	public static int graphMode;

	public final static int SHEAR = 0;
	public final static int MOMENT = 1;

	public static Canvas distLoadCanvas, beamCanvas;

	public static DrawDistLoad distLoadLine;

	private View beam;

	public static DialogFragment addPtLoad;

	public static LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		GlobalProperties GLOBAL = new GlobalProperties(this);

		if (savedInstanceState != null) {
			ArrayList<PointLoad> pointLoads = new ArrayList<PointLoad>();
			ArrayList<DistributedLoad> distributedLoads = new ArrayList<DistributedLoad>();

			pointLoads = savedInstanceState
					.getParcelableArrayList("Point Loads");
			PointLoadManager.getInstance().setPtLoads(pointLoads);
			distributedLoads = savedInstanceState
					.getParcelableArrayList("Distributed Loads");
			DistributedLoadManager.getInstance().setDistLoads(distributedLoads);

			pointLoads = null;
			distributedLoads = null;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		addLoad = (Button) findViewById(R.id.bAddLoad);
		addLoad.setOnClickListener(this);
		
		showShear = (Button) findViewById(R.id.bShowShear);
		showShear.setOnClickListener(this);
		
		showMom = (Button) findViewById(R.id.bShowMoment);
		showMom.setOnClickListener(this);

		deleteTB = (ToggleButton) findViewById(R.id.tbDelete);
		deleteTB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
			}

		});

		lgth = (EditText) findViewById(R.id.etLength);

		rlpl = (RelativeLayout) findViewById(R.id.rlSimpleBeamPLImages);
		rldl = (RelativeLayout) findViewById(R.id.rlSimpleBeamDLImages);
		main = (RelativeLayout) findViewById(R.id.rlSimpleBeam);
		dlTop = (RelativeLayout) findViewById(R.id.rlDistLoadTop);

		beam = new DrawBeam(this);
		beamCanvas = new Canvas();

		distLoadLine = new DrawDistLoad(this);
		distLoadCanvas = new Canvas();

		dlTop.addView(distLoadLine, dlTop.getChildCount());

		main.addView(beam);
		beam.draw(beamCanvas);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		deleteTB.setChecked(false);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(savedInstanceState);

		savedInstanceState.putParcelableArrayList("Point Loads",
				PointLoadManager.getInstance().getPtLoads());
		savedInstanceState.putParcelableArrayList("Distributed Loads",
				DistributedLoadManager.getInstance().getDistLoads());

	}
	
	@Override
	public void onBackPressed() {
	    new AlertDialog.Builder(this)
        .setTitle("Exit?")
        .setMessage("This will clear all loads.")
        .setPositiveButton("I want to stay.", null)
        .setNegativeButton("...kill...me", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface arg0, int arg1) {
                MainActivity.super.onBackPressed();
        		PointLoadManager.getInstance().getPtLoads().clear();
        		DistributedLoadManager.getInstance().getDistLoads().clear();
        		rlpl.removeAllViews();
        		rldl.removeAllViews();
        		dlTop.removeAllViews();
        		finish();
            }

        }).create().show();
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.bAddLoad:
			if (lgth.getText().toString().matches("")) {
				Toast.makeText(this, "YOU SHALL NOT PASS...without a length.", Toast.LENGTH_SHORT)
						.show();
			} else {
				addOptions = new LoadOptionsDialogFragment();
				addOptions.show(getSupportFragmentManager(), "Options");
			}
			break;
		case R.id.bShowShear:
			if (lgth.getText().toString().matches("")) {
				Toast.makeText(this, "YOU SHALL NOT PASS...without a length.", Toast.LENGTH_SHORT)
						.show();
			} else if (PointLoadManager.getInstance().getSize() == 0 && DistributedLoadManager.getInstance().getSize() == 0) {
				Toast.makeText(this, "I pity the foo without loads.", Toast.LENGTH_SHORT)
				.show();
			} else {
				graphMode = SHEAR;
				Intent i = new Intent(MainActivity.this, DiagramActivity.class);
				MainActivity.this.startActivity(i);
			}
			break;
		case R.id.bShowMoment:
			if (lgth.getText().toString().matches("")) {
				Toast.makeText(this, "YOU SHALL NOT PASS...without a length.", Toast.LENGTH_SHORT)
						.show();
			} else if (PointLoadManager.getInstance().getSize() == 0 && DistributedLoadManager.getInstance().getSize() == 0) {
				Toast.makeText(this, "I pity the foo without loads.", Toast.LENGTH_SHORT)
				.show();
			} else {
				graphMode = MOMENT;
				Intent i = new Intent(MainActivity.this, DiagramActivity.class);
				MainActivity.this.startActivity(i);
			}
			break;
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog, int resource) {
		// TODO Auto-generated method stub
		switch (resource) {
		case R.id.rbPtLoad:
			loadMode = LoadType.POINT_LOAD;
			addPtLoad = new InputDialogFragment(this);
			addPtLoad.show(getSupportFragmentManager(), "PtLoadDialog");
			break;
		case R.id.rbDistLoad:
			loadMode = LoadType.DISTRIBUTED_LOAD;
			DialogFragment addDistLoad = new InputDialogFragment(this);
			addDistLoad.show(getSupportFragmentManager(), "DistLoadDialog");
			break;
		}
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog,
			String sMag, String eMag, String sPos, String ePos) {
		// TODO Auto-generated method stub

		switch (loadMode) {
		case POINT_LOAD:
			PointLoad pl = new PointLoad(sMag, sPos, this);

			CustomParams ptParams = new CustomParams(this, loadMode);

			ptParams.setLeftMargin(Double.parseDouble(sPos));
			
			ImageView image = pl.getImage();

			PointLoadManager.getInstance().addPtLoad(pl, image, ptParams);

			break;
		case DISTRIBUTED_LOAD:
			DistributedLoad dl = new DistributedLoad(sMag, eMag, sPos, ePos,
					this);
			
			CustomParams[] dlParams = new CustomParams[2];
			dlParams[0] = new CustomParams(this, loadMode);
			dlParams[1] = new CustomParams(this, loadMode);

			dlParams[0].setLeftMargin(Double.parseDouble(sPos));
			dlParams[1].setLeftMargin(Double.parseDouble(ePos));
			
			ImageView[] images = new ImageView[2];
			images[0] = dl.getImages()[0];
			images[1] = dl.getImages()[1];
			
			DistributedLoadManager.getInstance().addDistLoad(dl, images, dlParams);

			break;
		}
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	public static double getLength() {
		length = Double.parseDouble(lgth.getText().toString());
		return length;
	}
	
	public static LoadType getLoadType() {
		return loadMode;
	}

	public static void setBeamType(int type) {
		beamType = type;
	}

}
