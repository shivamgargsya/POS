package com.dhsit.pos;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AssignTable extends ListActivity {

	TextView date;
	Spinner wname,wid;
	private ArrayAdapter<Tables> adapterx;
	private List<Tables> tabs,newtabs;
	Button assign,update,clear;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assigntable);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		date = (TextView) findViewById(R.id.tvATDate);
		wname = (Spinner) findViewById(R.id.sATWaiterName);
		wid = (Spinner) findViewById(R.id.sATWaiterID);
		assign = (Button) findViewById(R.id.bATAssign);
		update = (Button) findViewById(R.id.bATUpdate);
		clear = (Button) findViewById(R.id.bATClear);
		
		
		String[] waitername = new String[]{"Ram", "Gopal", "Verma","Subhash"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waitername);
		wname.setAdapter(adapter);
	    
		String[] waiterid = new String[]{"001", "002", "003","004"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiterid);
		wid.setAdapter(adapter1);
		
		cal = Calendar.getInstance();
		pyear = cal.get(Calendar.YEAR);
	    pmonth = cal.get(Calendar.MONTH);
	    pday = cal.get(Calendar.DAY_OF_MONTH);
	    int month = pmonth+1;
		String setDate = pday + "/" + month + "/" + pyear + "";
		date.setText(setDate);
		tabs = new ArrayList<Tables>();
		for(int i=1;i<=16;i++){
			Tables table = new Tables();
			table.setId(i);
			table.setName("Table " + i);
			table.setChecked(true);
			tabs.add(table);
			
		}
		adapterx = new TableAdapter(this, tabs);
		setListAdapter(adapterx);
		
		assign.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				assignupdate();
			}
		});
		
		update.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				assignupdate();
			}
		});
		
		clear.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newtabs = new ArrayList<Tables>();
				for (int i = 0; i < adapterx.getCount(); i++) {
					Tables t = adapterx.getItem(i);
					t.setChecked(false);
					newtabs.add(t);
					
					}
				adapterx = new TableAdapter(AssignTable.this, newtabs);
				setListAdapter(adapterx);
				}
			
		});
	}
	
	private long[] toIntArray(List<Long> checked) {
		// TODO Auto-generated method stub
		long[] x = new long[checked.size()];
		for (int i = 0; i < x.length; i++) {
			x[i] = checked.get(i);
		}
		return x;
	}
	public void assignupdate(){
		List<Long> checked = new ArrayList<Long>();
		int x = adapterx.getCount();
		List<Tables> ts = new ArrayList<Tables>();
		int[] selected;
	    Tables t = new Tables();
		for (int i = 0; i < x; i++) {
			t = adapterx.getItem(i);
			if(t.isChecked())
			{
			checked.add(t.getId());
			//Toast.makeText(NextExpenditure.this, xy.getName() + " is Not Checked.", Toast.LENGTH_SHORT).show();
			//dudes.add(xy);
			}
		}
		long[] c = toIntArray(checked);
		Toast.makeText(AssignTable.this, c.length + " Tables Assigned", Toast.LENGTH_SHORT).show();
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(AssignTable.this,ActivityMain.class));
		overridePendingTransition(R.anim.open_main, R.anim.close_next);
		this.finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.withhome, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if (id == R.id.action_home) {
			AssignTable.this.finish();
			startActivity(new Intent(AssignTable.this,ActivityMain.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			
		}else if (id == android.R.id.home) {
			this.finish();
			
			overridePendingTransition(R.anim.open_main, R.anim.close_next);
		}
		return super.onOptionsItemSelected(item);
	}
}
