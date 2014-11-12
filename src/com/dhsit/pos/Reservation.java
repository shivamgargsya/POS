package com.dhsit.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Reservation extends Activity implements View.OnClickListener, OnItemSelectedListener{
	
	TextView date,time,resno;
	Button refresh,save,update,clear,viewres;
	EditText etname,etphone;
	String name,phone,tablex;
	String [] items;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
	Spinner stableno;
	HttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		date = (TextView) findViewById(R.id.tvDate);
		time = (TextView) findViewById(R.id.tvTime);
		resno = (TextView) findViewById(R.id.tvReserveNo);
		refresh = (Button) findViewById(R.id.bRefresh);
		save = (Button) findViewById(R.id.bSave);
		update = (Button) findViewById(R.id.bUpdate);
		clear = (Button) findViewById(R.id.bClear);
		viewres = (Button) findViewById(R.id.bViewReservationList);
		etname = (EditText) findViewById(R.id.etCusName);
		etphone = (EditText) findViewById(R.id.etCusMobile);
		stableno = (Spinner) findViewById(R.id.sTableNo);
		stableno.setOnItemSelectedListener(this);
		
		refresh.setOnClickListener(this);
		save.setOnClickListener(this);
		update.setOnClickListener(this);
		clear.setOnClickListener(this);
		viewres.setOnClickListener(this);
		
		refreshdate();
       				
		items = new String[]{"Table 1", "Table 2", "Table 3","This is Dynamic"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		stableno.setAdapter(adapter);
		stableno.setOnItemSelectedListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		
		case R.id.bRefresh:
			
			break;
		case R.id.bViewReservationList:
			startActivity(new Intent(Reservation.this,ViewReservation.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bSave:
			name = etname.getText().toString();
			phone = etphone.getText().toString();
			if(name.contentEquals("") || phone.contentEquals("")){
				Toast.makeText(Reservation.this, "Please enter Customer name and number", Toast.LENGTH_SHORT).show();
			}else{
				//Add to DB
				Toast.makeText(Reservation.this, "Reservation Added", Toast.LENGTH_SHORT).show();
				new Submit().execute();
			}
			break;
		case R.id.bClear:
			refreshdate();
			etname.setText("");
			etphone.setText("");
			break;
		case R.id.bUpdate:
			
			break;
		}
	}
	
	public void refreshdate(){
		 cal = Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");	
		 String setTime = sdf.format(cal.getTime());
	     pyear = cal.get(Calendar.YEAR);
	     pmonth = cal.get(Calendar.MONTH);
	     pday = cal.get(Calendar.DAY_OF_MONTH);
	     int month = pmonth+1;
		 String setDate = pday + "/" + month + "/" + pyear + "";
		 date.setText(setDate);
		 time.setText(setTime);
	}
	

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(Reservation.this,ActivityMain.class));
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
			Reservation.this.finish();
			startActivity(new Intent(Reservation.this,ActivityMain.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			
		}else if (id == android.R.id.home) {
			this.finish();
			
			overridePendingTransition(R.anim.open_main, R.anim.close_next);
		}
		return super.onOptionsItemSelected(item);
	}
	public JSONObject service() throws ClientProtocolException,IOException,JSONException{
		JSONObject json=null;
		String jsonstr="";
		InputStream is=null;
		try{
		client = new DefaultHttpClient();
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/reservation_insert.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("date", date.getText().toString()));
		params.add(new BasicNameValuePair("time", time.getText().toString()));
		params.add(new BasicNameValuePair("customer_name", name));
		params.add(new BasicNameValuePair("phone_no", phone));
		params.add(new BasicNameValuePair("table_no", tablex));
		
		p.setEntity(new UrlEncodedFormEntity(params));
		
		HttpResponse r = client.execute(p);
		HttpEntity entity = r.getEntity();
		is = entity.getContent();
		Log.i("json","resopnse = \n*" + is.toString() + "*");
		}catch (UnsupportedEncodingException e) {
			Log.i("UnsoppertedEncodingException","Error : " + e.toString());
            e.printStackTrace();
        } catch (ClientProtocolException e) {
        	Log.i("ClientProtocolException","Error : " + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
        	Log.i("IOException","Error : " + e.toString());
            e.printStackTrace();
        }
		try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsonstr = sb.toString();
            Log.i("JSON", jsonstr);
        } catch (Exception e) {
            Log.i("Buffer Error", "Error converting result " + e.toString());
        }
		try {
            //JSONArray array = new JSONArray(jsonstr);
			//json = array.getJSONObject(0);
			json = new JSONObject(jsonstr);
        } catch (JSONException e) {
            Log.i("JSON Parser", "Error parsing data " + e.toString());
        } //To here
		return json;
		
	}

	public class Submit extends AsyncTask<String, Integer, JSONObject>{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = new ProgressDialog(Reservation.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Please Wait...");
			dialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				
				JSONObject x = service();
				Log.i("json","ok upto return");
				
				return x;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				Log.i("Client Protocol Exception", "Error : " + e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.i("IO Exception", "Error : " + e.toString());
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.i("JSON Exception", "Error : " + e.toString());
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			dialog.dismiss();
			if(result==null){
				Log.i("JSONinMain", "Null returned in onPostExecute ");
				Toast.makeText(Reservation.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
			}
		}
}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		tablex = items[position];
		Log.i("atul",tablex);
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}

}
