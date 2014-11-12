package com.dhsit.pos;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ViewReservation extends ListActivity{
	
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
    TextView date;
    EditText etname,etphone;
	String name,phone;
	Button search;
	DatePickerDialog.OnDateSetListener x;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation_view);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		search = (Button) findViewById(R.id.bSearch);
		date = (TextView) findViewById(R.id.sDate);
		etname = (EditText) findViewById(R.id.etViewCusName);
		etphone = (EditText) findViewById(R.id.etViewCusMobile);
		
        cal = Calendar.getInstance();
		
        pyear = cal.get(Calendar.YEAR);
        pmonth = cal.get(Calendar.MONTH);
        pday = cal.get(Calendar.DAY_OF_MONTH);
        int mo = pmonth+1;
		String Date = pday + "/" + mo + "/" + pyear + "";
		date.setText(Date);
		date.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                   x = new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						// TODO Auto-generated method stub
						pyear = year;
						pmonth = monthOfYear;
						pday = dayOfMonth;
						int month = pmonth+1;
						String setDate = pday + "/" + month + "/" + pyear + "";
						date.setText(setDate);
						
					}
				};
				showDialog(0);
			}
		});
		
		search.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = etname.getText().toString();
				phone = etphone.getText().toString();
				if(name.contentEquals("")||phone.contentEquals("")){
					
				}
			}
		});
		
		
	}
	protected android.app.Dialog onCreateDialog(int id) {
    	switch (id) {
        case 0:
            return new DatePickerDialog(this, 
                        x,
                        pyear, pmonth, pday);
        
        }
		return null;}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(ViewReservation.this,Reservation.class));
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
			ViewReservation.this.finish();
			startActivity(new Intent(ViewReservation.this,ActivityMain.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			
		}else if (id == android.R.id.home) {
			this.finish();
			
			overridePendingTransition(R.anim.open_main, R.anim.close_next);
		}
		return super.onOptionsItemSelected(item);
	}
}
