package com.mitsugaru.Tapdex.fields;

import java.util.ArrayList;

import com.mitsugaru.Tapdex.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SpinnerFieldEntry extends FieldEntry
{
	private int						position		= 0;
	private TextView				label			= null;
	private Spinner					spinner			= null;
	private ArrayList<String>		list			= null;
	private ArrayAdapter<String>	spinnerAdapter	= null;
	
	public SpinnerFieldEntry(String title, Context context,
			FieldEntryAdapter adapter, ArrayList<String> list)
	{
		super(Type.SPINNER, title, context, adapter);
		this.list = list;
	}
	
	public SpinnerFieldEntry(String title, Context context,
			FieldEntryAdapter adapter, ArrayList<String> list, int pos)
	{
		super(Type.SPINNER, title, context, adapter);
		this.list = list;
		this.position = pos;
	}
	
	public int getPosition()
	{
		return position;
	}
	
	public void setLabel(String text)
	{
		setName(text);
		if (label != null)
		{
			label.setText(text);
		}
	}
	
	public void setPosition(int pos)
	{
		this.position = pos;
		if (spinner != null)
		{
			spinner.setSelection(pos);
		}
	}
	
	@Override
	public View expandView()
	{
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.spinnerfieldentry, null);
		label = (TextView) rootView.findViewById(R.id.spinnerLabel);
		label.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				
				alert.setTitle("Set Field Title");
				alert.setMessage("Previous: " + name);
				
				final EditText input = new EditText(context);
				alert.setView(input);
				
				alert.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								setLabel(input.getText().toString());
								getAdapter().notifyDataSetChanged();
							}
						});
				
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								return;
							}
						});
				
				alert.show();
			}
		});
		label.setText(name);
		spinner = (Spinner) rootView.findViewById(R.id.spinnerField);
		spinnerAdapter = new ArrayAdapter<String>(context,
				android.R.layout.simple_spinner_dropdown_item, list);
		spinner.setAdapter(spinnerAdapter);
		spinner.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v)
			{
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
				
				alert.setTitle("Modify Spinner Groups");
				
				alert.setPositiveButton("Add",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										context);
								
								alert.setTitle("Add group");
								
								final EditText input = new EditText(context);
								alert.setView(input);
								
								alert.setPositiveButton("Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{
												list.add(input.getText().toString());
												spinnerAdapter
														.notifyDataSetChanged();
												getAdapter()
														.notifyDataSetChanged();
											}
										});
								
								alert.setNegativeButton("Cancel",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int whichButton)
											{
												return;
											}
										});
								
								alert.show();
							}
						});
				alert.setNeutralButton("Remove",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								AlertDialog.Builder alert = new AlertDialog.Builder(
										context);
								
								alert.setTitle("Remove group");
								final CharSequence[] items = list
										.toArray(new CharSequence[0]);
								alert.setItems(items,
										new DialogInterface.OnClickListener() {
											
											@Override
											public void onClick(
													DialogInterface dialog,
													int which)
											{
												list.remove(which);
												spinnerAdapter
														.notifyDataSetChanged();
												getAdapter()
														.notifyDataSetChanged();
											}
										});
								
								alert.show();
							}
						});
				alert.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton)
							{
								return;
							}
						});
				alert.show();
				return true;
			}
		});
		spinner.setSelection(position);
		return rootView;
	}
	
}
