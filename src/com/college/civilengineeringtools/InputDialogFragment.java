package com.college.civilengineeringtools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

public class InputDialogFragment extends DialogFragment {

	int type, n;
	private Context context;
	EditText inputName, inputType, inputSMag, inputEMag, inputSPos, inputEPos;

	public InputDialogFragment(Context context, int tag) {
		type = tag;
		this.context = context;
	}

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface NoticeDialogListener {
		public void onDialogPositiveClick(DialogFragment dialog, int type,
				String start_pos, String end_post, String start_mag,
				String end_pos);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	NoticeDialogListener mListener;

	// Override the Fragment.onAttach() method to instantiate the
	// NoticeDialogListener
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// Verify that the host activity implements the callback interface
		try {
			// Instantiate the NoticeDialogListener so we can send events to the
			// host
			mListener = (NoticeDialogListener) activity;
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString()
					+ " must implement NoticeDialogListener");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		int layout[] = { R.layout.dialog_input_point_load,
				R.layout.dialog_input_dist_load };
		if (type == MainActivity.POINT_LOAD) {
			n = 0;
		} else if (type == MainActivity.DIST_LOAD) {
			n = 1;
		}

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(layout[n], null))
				// Add action buttons
				.setPositiveButton("Continue",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {

								inputSMag = (EditText) ((Dialog) dialog)
										.findViewById(R.id.etStartMag);
								inputEMag = (EditText) ((Dialog) dialog)
										.findViewById(R.id.etEndMag);
								inputSPos = (EditText) ((Dialog) dialog)
										.findViewById(R.id.etStartPos);
								inputEPos = (EditText) ((Dialog) dialog)
										.findViewById(R.id.etEndPos);

								if (n == 0) {
									if (inputSMag.getText().toString()
											.equals("")

											|| inputSPos.getText().toString()
													.equals("")) {
										Toast.makeText(context,
												"Missing some values.",
												Toast.LENGTH_SHORT).show();
									} else if(Double.parseDouble(inputSPos.getText().toString()) > MainActivity.getLength() || Double.parseDouble(inputSPos.getText().toString()) < 0) {
										Toast.makeText(context,
												"Can't put a load on this air!",
												Toast.LENGTH_LONG).show();
									} else {
										mListener.onDialogPositiveClick(
												InputDialogFragment.this, type,
												inputSMag.getText().toString(),
												null, inputSPos.getText()
														.toString(), null);
									}
								} else if (n == 1) {
									if (inputSMag.getText().toString()
											.equals("")
											|| inputEMag.getText().toString()
													.equals("")
											|| inputSPos.getText().toString()
													.equals("")
											|| inputEPos.getText().toString()
													.equals("")) {
										Toast.makeText(context,
												"Missing some values.",
												Toast.LENGTH_SHORT).show();
									} else if (Double.parseDouble(inputSPos.getText().toString()) > Double.parseDouble(inputEPos.getText().toString())) {
										Toast.makeText(context,
												"Ending position cannot be less than the starting position.",
												Toast.LENGTH_LONG).show();
									} else if(Double.parseDouble(inputSPos.getText().toString()) > MainActivity.getLength() || Double.parseDouble(inputEPos.getText().toString()) > MainActivity.getLength()
											|| Double.parseDouble(inputSPos.getText().toString()) < 0 || Double.parseDouble(inputSPos.getText().toString()) > 0) {
										Toast.makeText(context,
												"Can't put a load on thin air!",
												Toast.LENGTH_LONG).show();
									} else {
										mListener.onDialogPositiveClick(
												InputDialogFragment.this, type,
												inputSMag.getText().toString(),
												inputEMag.getText().toString(),
												inputSPos.getText().toString(),
												inputEPos.getText().toString());
									}
								}

							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								InputDialogFragment.this.getDialog().cancel();
							}
						});

		return builder.create();
	}

}