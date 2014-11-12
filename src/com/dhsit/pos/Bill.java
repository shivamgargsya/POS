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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dhsit.pos.Reservation.Submit;

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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class Bill extends ListActivity implements OnItemSelectedListener {
	
	
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;TextView date;
	Button search;
	Spinner waiter;
	HttpClient client;
	String waiter_no;
	String[] waiters;
	JSONArray bills = null;
	DatePickerDialog.OnDateSetListener x;
	Context content=this;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bill);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		date = (TextView) findViewById(R.id.sVBDate);
		waiter = (Spinner) findViewById(R.id.sVBWaiterName);
		search = (Button) findViewById(R.id.bVBSearch);
		
		waiters= new String[]{"Waiter 1", "Waiter 2", "Waiter 3"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, waiters);
		waiter.setAdapter(adapter1);
		
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
				Toast.makeText(Bill.this, "Retriving bill", Toast.LENGTH_SHORT).show();
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
		startActivity(new Intent(Bill.this,ActivityMain.class));
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
			Bill.this.finish();
			startActivity(new Intent(Bill.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/bill_search.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		
		
		params.add(new BasicNameValuePair("date", date.getText().toString()));
		params.add(new BasicNameValuePair("waiter_id", waiter_no));
		
		
		
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
			dialog = new ProgressDialog(Bill.this);
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
				Toast.makeText(Bill.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
				try {
                   
                     
                    // Getting JSON Array node
                    bills = result.getJSONArray("bills");
                    //listdata [] list=new listdata[bills.length()];
                    List<listdata> list=new ArrayList<listdata>();
                    String [] values=new String[bills.length()];
                    // looping through All Contacts
                    for (int i = 0; i < bills.length(); i++) {
                        JSONObject c = bills.getJSONObject(i);
                         
                        String order_no = c.getString("id");
                        Log.i("atul",order_no);
                        String date = c.getString("date");
                        Log.i("atul",date);
                        String table_no = c.getString("table_no");
                        Log.i("atul",table_no);
                        String customer_name = c.getString("customer_name");
                        Log.i("atul",customer_name);
                        String waiter_name = c.getString("waiter_name");
                        Log.i("atul",waiter_name);
 
                        // Phone node is JSON Object
                        String total_amount = c.getString("total_amount");
                        Log.i("atul",total_amount);
                        String payment_mode = c.getString("payment_mode");
                        Log.i("atul",payment_mode);
                        listdata item=new listdata();
                        
                        item.set_order_no(order_no);
                        item.set_date(date);
                        item.set_customer_name(customer_name);
                        item.set_payment_mode(payment_mode);
                        item.set_table_no(table_no);
                        item.set_waiter_name(waiter_name);
                        item.set_total_amount(total_amount);
                        
                       list.add(item);
                        values[i]=order_no;
                        
                       
                      
                    }
                    MySimpleArrayAdapter adapter;
                    adapter=new  MySimpleArrayAdapter(Bill.this,list);
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

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		waiter_no=waiters[position];
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		waiter_no=waiters[0];
	}
	
	//Array adapter for listview
	public class MySimpleArrayAdapter extends ArrayAdapter<listdata> {
		  private final Context context;
		  private final List<listdata> data;

		  public MySimpleArrayAdapter(Context context,List<listdata> data) {
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
		    View rowView = inflater.inflate(R.layout.custom_bill,parent,false);
		    listdata x = data.get(position);
		    Log.i("atul",data.toString());
		    TextView order_no=(TextView)rowView.findViewById(R.id.c_order_no);
		    TextView date=(TextView)rowView.findViewById(R.id.c_date);
		    TextView table_no=(TextView)rowView.findViewById(R.id.c_table_no);
		    TextView customer_name=(TextView)rowView.findViewById(R.id.c_customer_name);
		    TextView waiter_name=(TextView)rowView.findViewById(R.id.c_waiter_name);
		    TextView total_amount=(TextView)rowView.findViewById(R.id.c_total_amount);
		    TextView payment_mode=(TextView)rowView.findViewById(R.id.c_payment_mode);
		   order_no.setText(x.get_order_no());
		    date.setText(x.get_date());
		    table_no.setText(x.get_table_no());
		    payment_mode.setText(x.get_payment_mode());
		    total_amount.setText(x.get_total_amount());
		    waiter_name.setText(x.get_waiter_name());
		    customer_name.setText(x.get_customer_name());
		    return rowView;
		  }
		} 
	
}
