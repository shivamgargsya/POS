package com.dhsit.pos;

import android.app.Activity;
import android.app.ProgressDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReturnOrders extends Activity implements View.OnClickListener{
	
	Button save,vieworder;
	TextView date, table,order;
	EditText note;
	Spinner reason;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
    DefaultHttpClient client;
    String waiter,total_amount;
	String reasons;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.return_orders);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		save = (Button) findViewById(R.id.bSaveOrder);
		vieworder = (Button) findViewById(R.id.bViewReturnOrders);
		date = (TextView) findViewById(R.id.tvRODate);
		table = (TextView) findViewById(R.id.tvROTableNo);
		order = (TextView) findViewById(R.id.tvROOrderNo);
		note = (EditText) findViewById(R.id.etRONote);
		reason = (Spinner) findViewById(R.id.reason_spinner);
		
		save.setOnClickListener(this);
		vieworder.setOnClickListener(this);
		
		cal = Calendar.getInstance();
		pyear = cal.get(Calendar.YEAR);
	    pmonth = cal.get(Calendar.MONTH);
	    pday = cal.get(Calendar.DAY_OF_MONTH);
	    int month = pmonth+1;
		String setDate = pday + "/" + month + "/" + pyear + "";
		date.setText(setDate);
		
		Spinner sp1 = (Spinner) findViewById(R.id.reason_spinner);
		final String[] items = new String[]{"Loyalty Points", "Reason 2", "Reason 3", "Reason 4"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		sp1.setAdapter(adapter);
		sp1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				reasons=items[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				reasons=items[0];
			}
		});
		Intent i=getIntent();
		order.setText(i.getStringExtra("order_no"));
		table.setText(i.getStringExtra("table_no"));
		waiter=i.getStringExtra("waiter_name");
		total_amount=i.getStringExtra("total_amount");
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bViewReturnOrders:
			startActivity(new Intent(ReturnOrders.this,ViewReturnOrders.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bSaveOrder:
			new Submit().execute();
			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(ReturnOrders.this,ActivityMain.class));
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
			ReturnOrders.this.finish();
			startActivity(new Intent(ReturnOrders.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/return_order.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_no", order.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("table_no", table.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("date",date.getText().toString()));
		Log.i("save","button called");
		
		params.add(new BasicNameValuePair("waiter_name", waiter));
		Log.i("save","button called");
		
		
		params.add(new BasicNameValuePair("reason",reasons));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("notes", note.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("total_amount", total_amount));
		Log.i("save","button called");
		//Chnage them
		
		
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
        } catch (Exception e) {
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
			dialog = new ProgressDialog(ReturnOrders.this);
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
				Toast.makeText(ReturnOrders.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
			}
		}
}

	
}
