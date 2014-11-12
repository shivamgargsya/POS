package com.dhsit.pos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.dhsit.pos.CustomerBill.edit;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class add_waiter extends Activity {
	EditText name,address,pnumber,password;
	String wname,wphone,waddress,wpassword;
	DefaultHttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waiter_add);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		name=(EditText)findViewById(R.id.et_waitername);
		address=(EditText)findViewById(R.id.et_waiter_address);
		pnumber=(EditText)findViewById(R.id.et_waiter_pno);
		password=(EditText)findViewById(R.id.et_waiter_pass);
		Button b=(Button)findViewById(R.id.bsave_waiter);
		
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				wname=name.getText().toString();
				waddress=address.getText().toString();
				wphone=pnumber.getText().toString();
				wpassword=password.getText().toString();
				new Submit().execute();
				
			}
		});
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(add_waiter.this,ActivityMain.class));
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
			add_waiter.this.finish();
			startActivity(new Intent(add_waiter.this,ActivityMain.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			
		}else if (id == android.R.id.home) {
			this.finish();
			
			overridePendingTransition(R.anim.open_main, R.anim.close_next);
		}
		return super.onOptionsItemSelected(item);
	}
	public JSONObject editdata() throws ClientProtocolException,IOException,JSONException{
		JSONObject json=null;
		String jsonstr="";
		InputStream is=null;
		try{
		client = new DefaultHttpClient();
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/add_waiter.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("waiter_name",wname));
		Log.i("editdata",wname);
		params.add(new BasicNameValuePair("phone_no",wphone));
		params.add(new BasicNameValuePair("login_id",wphone));
		Log.i("editdata",wphone);
		params.add(new BasicNameValuePair("address",waddress));
		params.add(new BasicNameValuePair("password",wpassword));
		Log.i("editdata",waddress);
		
		
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
			dialog = new ProgressDialog(add_waiter.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Please Wait...");
			dialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				
				JSONObject x = editdata();
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
				Toast.makeText(add_waiter.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
				try {
					String succ=result.getString("success");
					if(succ.contentEquals("2"))
					{
						TextView tv=(TextView)findViewById(R.id.warning);
						tv.setText("The Mobile Number already exist");
					}
					else if(succ.contentEquals("1"))
					{
						startActivity(new Intent(add_waiter.this,ActivityMain.class));
						overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			
		}
}

}
