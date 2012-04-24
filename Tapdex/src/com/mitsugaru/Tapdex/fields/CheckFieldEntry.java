package com.mitsugaru.Tapdex.fields;

import java.util.HashMap;
import java.util.Map;

import com.mitsugaru.Tapdex.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class CheckFieldEntry extends FieldEntry {
    private TextView label = null;
    private boolean checked = false;
    private CheckBox checkbox = null;

    public CheckFieldEntry(String title, Context context,
	    FieldEntryAdapter adapter) {
	super(Type.CHECK, title, context, adapter);
    }

    public CheckFieldEntry(String title, Context context,
	    FieldEntryAdapter adapter, boolean check) {
	super(Type.CHECK, title, context, adapter);
	this.checked = check;
    }

    public boolean isChecked() {
	return checked;
    }

    public void setChecked(boolean c) {
	this.checked = c;
	if (checkbox != null) {
	    checkbox.setChecked(c);
	}
    }

    public void setLabel(String text) {
	setName(text);
	if (label != null) {
	    label.setText(text);
	}
    }

    @Override
    public View expandView() {
	final LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View rootView = inflater.inflate(R.layout.checkfieldentry, null);
	label = (TextView) rootView.findViewById(R.id.checkTitle);
	label.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);

		alert.setTitle("Set Field Title");
		alert.setMessage("Previous: " + name);

		final EditText input = new EditText(context);
		alert.setView(input);

		alert.setPositiveButton("Ok",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int whichButton) {
				setLabel(input.getText().toString());
				getAdapter().notifyDataSetChanged();
			    }
			});

		alert.setNegativeButton("Cancel",
			new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog,
				    int whichButton) {
				return;
			    }
			});

		alert.show();
	    }
	});
	label.setText(name);
	checkbox = (CheckBox) rootView.findViewById(R.id.checkBox);
	checkbox.setChecked(checked);
	checkbox.setOnClickListener(new OnClickListener(){

	    @Override
	    public void onClick(View arg0) {
		if(!checkbox.isChecked())
		{
		    checked = false;
		}
		else
		{
		    checked = true;
		}
	    }
	    
	});
	return rootView;
    }

    public Map<String, Object> getData()
    {
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("type", getType().name());
	data.put("checked", checked);
	return data;
    }
}
