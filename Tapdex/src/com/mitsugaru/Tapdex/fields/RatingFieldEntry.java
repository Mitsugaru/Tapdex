package com.mitsugaru.Tapdex.fields;

import com.mitsugaru.Tapdex.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class RatingFieldEntry extends FieldEntry
{
	private TextView label = null;
	private RatingBar rating = null;
	private float value = 0f;
	
	public RatingFieldEntry(String title, Context context, FieldEntryAdapter adapter)
	{
		super(Type.RATING, title, context, adapter);
	}
	
	public RatingFieldEntry(String title, Context context, FieldEntryAdapter adapter, float value)
	{
		super(Type.TEXT, title, context, adapter);
		this.value = value;
	}
	
	public void setLabel(String text)
	{
		setName(text);
		if(label != null)
		{
			label.setText(text);
		}
	}
	
	public void setRating(float value)
	{
		this.value = value;
		if(rating != null)
		{
			rating.setRating(value);
		}
	}
	
	@Override
	public View expandView()
	{
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rootView = inflater.inflate(R.layout.ratingfieldentry, null);
		label = (TextView) rootView.findViewById(R.id.ratingTitle);
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
		rating = (RatingBar) rootView.findViewById(R.id.ratingBar);
		rating.setRating(value);
		return rootView;
	}
	
}
