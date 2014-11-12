package com.dhsit.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
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
import android.app.ProgressDialog;
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

public class DeleteItems extends Activity implements View.OnClickListener,OnItemSelectedListener{

	
	TextView orderNum, tableNum, date, cusName, cusMobile, waiterName, waiterId, totalamount,itemname,quantity,totalprice;
	Button total,save,viewdel;
	Spinner sDelReason;
	EditText etDiscount, etNotes;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
    String name,price,iquantity,order_no,waiter_name,reason;
    HttpClient client;
    String[] delreason;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delete_items);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		orderNum = (TextView) findViewById(R.id.tvDelOrderNo);
		tableNum = (TextView) findViewById(R.id.tvDelTableNo);
		date = ((TextView) findViewById(R.id.tvDelDate));
		cusName = (TextView) findViewById(R.id.tvDelCustID);
		cusMobile = (TextView) findViewById(R.id.tvDelCusMobile);
		waiterName  = (TextView) findViewById(R.id.tvDelWaiterName);
		waiterId = (TextView) findViewById(R.id.tvDelWaiterID);
		totalamount = (TextView) findViewById(R.id.tvDelTotalAmount);
		itemname = (TextView) findViewById(R.id.tvItemName);
		quantity = (TextView) findViewById(R.id.tvQuantity);
		totalprice = (TextView) findViewById(R.id.tvTotalPrice);
		sDelReason = (Spinner) findViewById(R.id.sDeleteReason);
		etNotes=(EditText)findViewById(R.id.etDeleteNotes);
		total = (Button) findViewById(R.id.bDelTotalAmount);
		save = (Button) findViewById(R.id.bDelSave);
		viewdel = (Button) findViewById(R.id.bViewDeleteItems);
		
		delreason= new String[]{"Wrong Order", "Cancel Order", "Reason 3"};
		reason=delreason[0];
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, delreason);
		sDelReason.setAdapter(adapter);
		
		cal = Calendar.getInstance();
		pyear = cal.get(Calendar.YEAR);
	    pmonth = cal.get(Calendar.MONTH);
	    pday = cal.get(Calendar.DAY_OF_MONTH);
	    int month = pmonth+1;
		String setDate = pday + "/" + month + "/" + pyear + "";
		date.setText(setDate);
		Intent i=getIntent();
		name=i.getStringExtra("name");
		TextView tvname=(TextView)findViewById(R.id.tvItemNamev);
		tvname.setText((String)name);
	    price=i.getStringExtra("price");
		TextView tvname2=(TextView)findViewById(R.id.tvTotalPricev);
		tvname2.setText((String)price);
	    iquantity=i.getStringExtra("quantity");
		TextView tvname3=(TextView)findViewById(R.id.tvQuantityv);
		tvname3.setText((String)iquantity);
		tableNum.setText((String)i.getStringExtra("table_no"));
		cusName.setText(i.getStringExtra("cname"));
		cusMobile.setText((String)i.getStringExtra("cmobile"));
		waiterName.setText((String)i.getStringExtra("wname"));
		waiterId.setText((String)i.getStringExtra("wid"));
		orderNum.setText((String)i.getStringExtra("order_no"));
		Log.i("save","button called");
		Button b=(Button)findViewById(R.id.bDelSave);
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new Submit().execute();
			}
		});
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.i("save","button called");
		switch(v.getId()){
		case R.id.bDelSave:
			Log.i("save","button called");
			//new Submit().execute();
			break;
		case R.id.bDelTotalAmount:
			
			break;
		case R.id.bViewDeleteItems:
			startActivity(new Intent(DeleteItems.this,ViewDeleteItem.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
			
		
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(DeleteItems.this,CustomerBill.class));
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
			DeleteItems.this.finish();
			startActivity(new Intent(DeleteItems.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/delete_item.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_no", orderNum.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("table_no", tableNum.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("date",date.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("customer_name",cusName.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("phone_no", cusMobile.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("waiter_name", waiterName.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("waiter_id",waiterId.getText().toString()));
		Log.i("save","button called");
		//waiterId.getText().toString())
		
		params.add(new BasicNameValuePair("reason",reason));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("notes", etNotes.getText().toString()));
		Log.i("save","button called");
		params.add(new BasicNameValuePair("total_amount", totalamount.getText().toString()));
		Log.i("save","button called");
		//Chnage them
		params.add(new BasicNameValuePair("quantity",iquantity));
		params.add(new BasicNameValuePair("price",price));
		params.add(new BasicNameValuePair("name",name));
		
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
			dialog = new ProgressDialog(DeleteItems.this);
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
				Toast.makeText(DeleteItems.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
			}
		}
}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		reason=delreason[position];
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		reason=delreason[0];
		
	}
}
