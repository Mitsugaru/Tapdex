package com.mitsugaru.Tapdex.fields;

import java.util.HashMap;
import java.util.Map;

import com.mitsugaru.Tapdex.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class TextFieldEntry extends FieldEntry {
    private String entry = "";
    private TextView label = null;
    private EditText field = null;

    public TextFieldEntry(String title, Context context,
	    FieldEntryAdapter adapter) {
	super(Type.TEXT, title, context, adapter);
    }

    public TextFieldEntry(String title, Context context,
	    FieldEntryAdapter adapter, String entry) {
	super(Type.TEXT, title, context, adapter);
	this.entry = entry;
    }

    public String getEntry() {
	return entry;
    }

    public void setLabel(String text) {
	setName(text);
	if (label != null) {
	    label.setText(text);
	}
    }

    public void setEntry(String text) {
	this.entry = text;
	if (field != null) {
	    field.setText(text);
	}
    }

    @Override
    public View expandView() {
	final LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	View rootView = inflater.inflate(R.layout.textfieldentry, null);
	label = (TextView) rootView.findViewById(R.id.textTitle);
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
	field = (EditText) rootView.findViewById(R.id.textField);
	field.setText(entry);
	field.addTextChangedListener(new TextWatcher(){

	    @Override
	    public void afterTextChanged(Editable editable) {
		entry = editable.toString();
	    }

	    @Override
	    public void beforeTextChanged(CharSequence arg0, int arg1,
		    int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	    }

	    @Override
	    public void onTextChanged(CharSequence arg0, int arg1, int arg2,
		    int arg3) {
		// TODO Auto-generated method stub
		
	    }
	    
	});
	return rootView;
    }
    
    public Map<String, Object> getData()
    {
	Map<String, Object> data = new HashMap<String, Object>();
	data.put("type", getType().name());
	data.put("text", entry);
	return data;
    }
}
