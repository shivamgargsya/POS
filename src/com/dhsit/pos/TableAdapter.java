package com.dhsit.pos;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;


public class TableAdapter extends ArrayAdapter<Tables>{
	Context context;
	List<Tables> tabs;
	
	
	public TableAdapter(Context context, List<Tables> tabs) {
		super(context, android.R.id.content, tabs);
		this.context = context;
		this.tabs = tabs;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View view = vi.inflate(R.layout.assign_list, null);
	
        final Tables table = tabs.get(position);
        
        TextView tv = (TextView) view.findViewById(R.id.rowTextView);
        tv.setText(table.getName());
        
        final CheckBox cb = (CheckBox) view.findViewById(R.id.CheckBox01);
        cb.setChecked(table.isChecked());
        cb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean toggle = !table.isChecked();
				table.setChecked(toggle);
				cb.setChecked(toggle);
			}
		});

        
        
        return view;
	}

}
