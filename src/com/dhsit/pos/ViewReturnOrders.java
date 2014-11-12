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
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dhsit.pos.ViewDeleteItem.MySimpleArrayAdapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class ViewReturnOrders extends ListActivity {
	
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
	Button search,back;
	Spinner table,waiter,reason;
	TextView date;
	String waiter_name,table_no,reasons;
	DatePickerDialog.OnDateSetListener x;
	DefaultHttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewreturnorders);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		search = (Button) findViewById(R.id.bVROSearch);
		back = (Button) findViewById(R.id.bVROBack);
		table = (Spinner) findViewById(R.id.sVROTableNo);
		reason = (Spinner) findViewById(R.id.sVROReason);
		waiter = (Spinner) findViewById(R.id.sVROWaiterName);
		date = (TextView) findViewById(R.id.sVRODate);
		
        cal = Calendar.getInstance();
		pyear = cal.get(Calendar.YEAR);
        pmonth = cal.get(Calendar.MONTH);
        pday = cal.get(Calendar.DAY_OF_MONTH);
        int mo = pmonth+1;
		String Date = pday + "/" + mo + "/" + pyear + "";
		date.setText(Date);
		
		final String[] delreason = new String[]{"Loyalty Points", "Reason 2", "Reason 3"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, delreason);
		reason.setAdapter(adapter);
		reason.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				reasons=delreason[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				reasons=delreason[0];
			}
		});
		
		final String[] waiters = new String[]{"raghav", "kalia", "Waiter 3"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiters);
		waiter.setAdapter(adapter1);
		waiter.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				waiter_name=waiters[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				waiter_name=waiters[0];
			}
		});
		
		String[] tableno = new String[]{"Table 1", "Table 2", "Table 3"};
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tableno);
		table.setAdapter(adapter2);
		table.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				table_no=Integer.toString(position+1);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				table_no=Integer.toString(1);
			}
		});
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
       Button b=(Button)findViewById(R.id.bVROSearch);
       b.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			new Submit().execute();
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
		startActivity(new Intent(ViewReturnOrders.this,ReturnOrders.class));
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
			ViewReturnOrders.this.finish();
			startActivity(new Intent(ViewReturnOrders.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/return_order_search.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		
		
		params.add(new BasicNameValuePair("date", date.getText().toString()));
		Log.i("values",date.getText().toString());
		params.add(new BasicNameValuePair("waiter_name",waiter_name));
		Log.i("values",waiter_name);
		params.add(new BasicNameValuePair("reason", reasons));
		Log.i("values",reasons);
		params.add(new BasicNameValuePair("table_no", table_no));
		Log.i("values",table_no);
		
		
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
			dialog = new ProgressDialog(ViewReturnOrders.this);
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
				Toast.makeText(ViewReturnOrders.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
				try {
                   
                     
                    // Getting JSON Array node
                   JSONArray bills = result.getJSONArray("items");
                    //listdata [] list=new listdata[bills.length()];
                    List<ditem> list=new ArrayList<ditem>();
                    
                    // looping through All Contacts
                    for (int i = 0; i < bills.length(); i++) {
                        JSONObject c = bills.getJSONObject(i);
                         
                        String order_no = c.getString("order_no");
                        Log.i("atul",order_no);
                        String date = c.getString("date");
                        Log.i("atul",date);
                        String table_no = c.getString("table_no");
                        Log.i("atul",table_no);
                      
                        String waiter_name = c.getString("waiter_name");
                        Log.i("atul",waiter_name);
 
                        // Phone node is JSON Object
                        String total_amount = c.getString("total_amount");
                        Log.i("atul",total_amount);
                        String reason = c.getString("reason");
                        Log.i("atul",reason);
                        String notes = c.getString("notes");
                        ditem item=new ditem();
                        
                        item.order_no=order_no;;
                        item.date=date;
                       
                        //item.set_payment_mode(payment_mode);
                        item.table_no=table_no;
                        item.waiter_name=waiter_name;
                        item.total_amount=total_amount;
                        item.reason=reason;
                        item.note=notes;
                       list.add(item);
               
                        
                       
                      
                    }
                    MySimpleArrayAdapter adapter;
                    adapter=new  MySimpleArrayAdapter(ViewReturnOrders.this,list);
                    Log.i("atul",list.toString());
                    //Log.i("atul",values[0]);
                    
                   // if(lv!=null)
                    setListAdapter(adapter);
                    //setAdapter(adapter);
                    //Log.i("atul",values[0]);
                    
                } catch (Exception e
                		) {
                    e.printStackTrace();
                }
			}
		}
}

	
	
	//Array adapter for listview
	public class MySimpleArrayAdapter extends ArrayAdapter<ditem> {
		  private final Context context;
		  private final List<ditem> data;

		  public MySimpleArrayAdapter(Context context,List<ditem> data) {
		    super(context, R.layout.custom_bill,data);
		    this.data=data;
		    //Log.i("atul",data[0].date);
		    this.context = context;
		  
		  }
		 /* @Override
		  public int getCount()
		  {
			  return data.size();
		  }*/

		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    LayoutInflater inflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    View rowView = inflater.inflate(R.layout.returnorder,parent,false);
		    ditem x = data.get(position);
		    Log.i("atul",data.toString());
		    TextView order_no=(TextView)rowView.findViewById(R.id.rorder_no);
		    TextView date=(TextView)rowView.findViewById(R.id.rdate);
		    TextView table_no=(TextView)rowView.findViewById(R.id.rtable_no);
		    TextView reason=(TextView)rowView.findViewById(R.id.rreason);
		    TextView waiter_name=(TextView)rowView.findViewById(R.id.rwaiter_name);
		    TextView total_amount=(TextView)rowView.findViewById(R.id.rtotal_amount);
		    TextView note=(TextView)rowView.findViewById(R.id.rnote);
		   order_no.setText(x.order_no);
		    date.setText(x.date);
		    table_no.setText(x.table_no);
		    note.setText(x.note);
		    total_amount.setText(x.total_amount);
		    waiter_name.setText(x.waiter_name);
		    reason.setText(x.reason);
		    return rowView;
		  }
		} 
}
