package com.mitsugaru.Tapdex.fields;

import com.mitsugaru.Tapdex.R;
import com.mitsugaru.Tapdex.TapdexActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class TextFieldEntry extends FieldEntry
{
	private String		entry	= "";
	private TextView label = null;
	private EditText	field	= null;
	private View rootView = null;
	
	public TextFieldEntry(String title, Context context)
	{
		super(Type.TEXT, title, context);
	}
	
	public TextFieldEntry(String title, Context context, String entry)
	{
		super(Type.TEXT, title, context);
		this.entry = entry;
	}
	
	public String getEntry()
	{
		return entry;
	}
	
	public void setLabel(String text)
	{
		setName(text);
		if(label != null)
		{
			label.setText(text);
		}
	}
	
	public void setEntry(String text)
	{
		this.entry = text;
		if (field != null)
		{
			field.setText(text);
		}
	}
	
	@Override
	public View expandView()
	{
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		rootView = inflater.inflate(R.layout.textfieldentry, null);
		label = (TextView) rootView.findViewById(R.id.textTitle);
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
								rootView.postInvalidate();
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
		field = (EditText) rootView.findViewById(R.id.textField);
		field.setText(entry);
		// TODO listeners?
		return rootView;
	}
}
