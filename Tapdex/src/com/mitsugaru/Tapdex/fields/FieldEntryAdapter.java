package com.mitsugaru.Tapdex.fields;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
//http://www.softwarepassion.com/android-series-custom-listview-items-and-adapters/
public class FieldEntryAdapter extends ArrayAdapter<FieldEntry>
{
	//Class variables
	private ArrayList<FieldEntry> list;
	
	public FieldEntryAdapter(Context context, int textViewResourceId,
			ArrayList<FieldEntry> list)
	{
		super(context, textViewResourceId, list);
		this.list = list;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            FieldEntry fe = list.get(position);
            v = fe.expandView();
            /*if(fe.getType().equals(Type.TEXT))
            {
            	LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            	v = vi.inflate(R.layout.textfieldentry, null);
            	v = fe.expandView();
            			
            }*/
            /*if (v == null) {
            	//TODO fix
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //v = vi.inflate(R.layout.fieldentryrow, null);
            }*/
            
            return v;
	}
	
}
