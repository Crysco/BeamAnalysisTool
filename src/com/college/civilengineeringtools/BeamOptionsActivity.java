package com.college.civilengineeringtools;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class BeamOptionsActivity extends Activity implements OnItemSelectedListener, OnClickListener {

	private Button startButton;
	private Spinner optionsSpinner;
	private ArrayAdapter<CharSequence> optionsAdapter;
	
	public static final int SIMPLE_BEAM = 0;
	public static final int CANTILEVER = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.beam_options);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		startButton = (Button) findViewById(R.id.bStart);
		startButton.setOnClickListener(this);
		
		optionsSpinner = (Spinner) findViewById(R.id.svOptions);
		optionsAdapter =  ArrayAdapter.createFromResource(this,
		        R.array.beam_options_array, android.R.layout.simple_spinner_item);
		optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		optionsSpinner.setAdapter(optionsAdapter);

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View v, int pos,
			long id) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(optionsSpinner.getSelectedItemPosition()) {
		case SIMPLE_BEAM:
			MainActivity.setBeamType(SIMPLE_BEAM);
			Intent i = new Intent(BeamOptionsActivity.this, MainActivity.class);
			BeamOptionsActivity.this.startActivity(i);
			break;
		case CANTILEVER:
			MainActivity.setBeamType(CANTILEVER);
			i = new Intent(BeamOptionsActivity.this, MainActivity.class);
			BeamOptionsActivity.this.startActivity(i);
			break;
		}
		
	}
	
	

}
