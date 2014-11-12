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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dhsit.pos.Bill.MySimpleArrayAdapter;
import com.dhsit.pos.Reservation.Submit;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerBill extends Activity implements View.OnClickListener,OnItemSelectedListener{
	
	TextView orderNum, tableNum, date, cusName, cusMobile, waiterName, waiterId, totalamount;
	Button print,btotalAmnt,bReturnOrder;
	Spinner sDiscReason, sPaymentMode;
	EditText etDiscount, etNotes;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
    HttpClient client;
    String einame,eprice,equantity,eono,etable,stable;
    
   
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.customer_bill);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		orderNum = (TextView) findViewById(R.id.tvBillOrderNo);
		tableNum = (TextView) findViewById(R.id.tvBillTableNo);
		date = ((TextView) findViewById(R.id.tvBillDate));
		cusName = (TextView) findViewById(R.id.tvBillCustID);
		cusMobile = (TextView) findViewById(R.id.tvBillCusMobile);
		waiterName  = (TextView) findViewById(R.id.tvBillWaiterName);
		waiterId = (TextView) findViewById(R.id.tvBillWaiterID);
		totalamount = (TextView) findViewById(R.id.tvTotalAmount);
		
		print = (Button) findViewById(R.id.bPrintBill);
		btotalAmnt = (Button) findViewById(R.id.bTotalAmount);
		bReturnOrder = (Button) findViewById(R.id.bReturnOrder);
		sDiscReason = (Spinner) findViewById(R.id.sDiscountReason);
		sPaymentMode = (Spinner) findViewById(R.id.sPaymentMode);
		etDiscount = (EditText) findViewById(R.id.etDiscount);
		etNotes = (EditText) findViewById(R.id.etBillNotes);
		
		print.setOnClickListener(this);
		btotalAmnt.setOnClickListener(this);
		bReturnOrder.setOnClickListener(this);
		sDiscReason.setOnItemSelectedListener(this);
		sPaymentMode.setOnItemSelectedListener(this);
		
		String[] paymode = new String[]{"Cash", "Card", "Check"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymode);
		sPaymentMode.setAdapter(adapter);
	    
		String[] disreason = new String[]{"Loyalty Points", "Reason 2", "Reason 3"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymode);
		sDiscReason.setAdapter(adapter1);
		
		
		cal = Calendar.getInstance();
		pyear = cal.get(Calendar.YEAR);
	    pmonth = cal.get(Calendar.MONTH);
	    pday = cal.get(Calendar.DAY_OF_MONTH);
	    int month = pmonth+1;
		String setDate = pday + "/" + month + "/" + pyear + "";
		date.setText(setDate);
		
		
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.table_dialog);
		dialog.setTitle("Select Table");

		// set the custom dialog components - text, image and button
		
		Spinner table = (Spinner) dialog.findViewById(R.id.tables);
		final String[] tables = new String[]{"1", "2", "3"};
		ArrayAdapter<String> dadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tables);
		table.setAdapter(dadapter);
		table.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				stable=tables[arg2];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				stable=tables[0];
			}
		});
		Button dialogButton = (Button) dialog.findViewById(R.id.bok);
		
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new getdata().execute();
			}
		});

		dialog.show();
		 
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bPrintBill:
			Toast.makeText(CustomerBill.this, "Bill Added", Toast.LENGTH_SHORT).show();
			new Submit().execute();
			break;
		case R.id.bReturnOrder:
			Intent i=new Intent(CustomerBill.this,ReturnOrders.class);
			i.putExtra("order_no",orderNum.getText().toString());
			i.putExtra("table_no",tableNum.getText().toString());
			i.putExtra("waiter_name",waiterName.getText().toString());
			i.putExtra("total_amount",totalamount.getText().toString());
			startActivity(i);
			  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bTotalAmount:
			
			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(CustomerBill.this,ActivityMain.class));
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
			CustomerBill.this.finish();
			startActivity(new Intent(CustomerBill.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/bill.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("order_no", orderNum.getText().toString()));
		params.add(new BasicNameValuePair("table_no", tableNum.getText().toString()));
		params.add(new BasicNameValuePair("date",date.getText().toString()));
		params.add(new BasicNameValuePair("customer_name",cusName.getText().toString()));
		
		params.add(new BasicNameValuePair("phone_no", cusMobile.getText().toString()));
		params.add(new BasicNameValuePair("waiter_name", waiterName.getText().toString()));
		params.add(new BasicNameValuePair("waiter_id","Waiter 1"));
		//waiterId.getText().toString())
		params.add(new BasicNameValuePair("discount", etDiscount.getText().toString()));
		params.add(new BasicNameValuePair("reason", etNotes.getText().toString()));
		params.add(new BasicNameValuePair("notes", etNotes.getText().toString()));
		params.add(new BasicNameValuePair("total_amount", totalamount.getText().toString()));
		//Chnage them
		
		params.add(new BasicNameValuePair("payment_mode", etNotes.getText().toString()));
		params.add(new BasicNameValuePair("item_list", etNotes.getText().toString()));
		
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
			dialog = new ProgressDialog(CustomerBill.this);
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
				Toast.makeText(CustomerBill.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
			}
		}
}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	//********************  GET THE ACTIVITY DATA***********************************************************
	
	
	
	
	public JSONObject servicedata() throws ClientProtocolException,IOException,JSONException{
		JSONObject json=null;
		String jsonstr="";
		InputStream is=null;
		try{
		client = new DefaultHttpClient();
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/customer_bill_fill.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		params.add(new BasicNameValuePair("table_no",stable));
		
		
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

	public class getdata extends AsyncTask<String, Integer, JSONObject>{

		ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			dialog = new ProgressDialog(CustomerBill.this);
			dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			dialog.setMessage("Please Wait...");
			dialog.show();
		}
		
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				
				JSONObject x = servicedata();
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
				Toast.makeText(CustomerBill.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
				try{
					String success = result.getString("success");
					
					Log.i("shivam",success);
						String customer_name = result.getString("customer_name");
						Log.i("atul",customer_name);
						String customer_mobile = result.getString("customer_mobile");
						Log.i("atul",customer_mobile);
						String waiter_id = result.getString("waiter_id");
						Log.i("atul",waiter_id);
						String waiter_name = result.getString("waiter_name");
						Log.i("atul", waiter_name);
						String order_no=result.getString("order_no");
						Log.i("atul",order_no);
						String table_no=result.getString("table_no");
						Log.i("atul",table_no);
						TextView cname=(TextView)findViewById(R.id.tvBillCustID);
						TextView cmobile=(TextView)findViewById(R.id.tvBillCusMobile);
						TextView wname=(TextView)findViewById(R.id.tvBillWaiterName);
						TextView wid=(TextView)findViewById(R.id.tvBillWaiterID);
						TextView order=(TextView)findViewById(R.id.tvBillOrderNo);
						TextView table=(TextView)findViewById(R.id.tvBillTableNo);
						cname.setText(customer_name);
						Log.i("atul",(String)cname.getText());
						cmobile.setText(customer_mobile);
						Log.i("atul",(String)cmobile.getText());
						wname.setText(waiter_name);
						Log.i("atul",(String)wname.getText());
						wid.setText(waiter_id);
						Log.i("atul",(String)wid.getText());
						order.setText(order_no);
						Log.i("atul",(String)order.getText());
						table.setText(table_no);
						Log.i("atul",(String)table.getText());
						JSONArray jas=result.getJSONArray("item_list");
						
						 List<bill_list> list=new ArrayList<bill_list>();
		                    
		                    // looping through All Contacts
		                    for (int i = 0; i < jas.length(); i++) {
		                        JSONObject c = jas.getJSONObject(i);
		                         
		                        String iorder_no = c.getString("order_no");
		                        Log.i("atul",order_no);
		                        String name = c.getString("name");
		                        Log.i("atul",name);
		                        String quantity = c.getString("quantity");
		                        Log.i("atul",quantity);
		                        String price = c.getString("price");
		                        Log.i("atul",price);
		                        String itable_no = c.getString("table_no");
		                        Log.i("atul",table_no);
		 
		                        // Phone node is JSON Object
		                       
		                        bill_list item=new bill_list();
		                        item.name=name;
		                        item.order_no=iorder_no;
		                        item.price=price;
		                        item.quantity=quantity;
		                        item.table_no=itable_no;
		                        
		                        
		                       list.add(item);
		                       
		                        
		                       
		                      
		                    }
		                    
		                    itemAdapter adapter2=new itemAdapter(CustomerBill.this,list);
		                    ListView lv=(ListView)findViewById(R.id.ilist);
		                    lv.setAdapter(adapter2);
		                    
		                    Log.i("atul",list.toString());
		                    //Log.i("atul",values[0]);
					}
				
				catch (Exception e)
				{
					Log.i("atul",e.getMessage());
				}
			}
			
		}
}
	
	//Array adapter for listview
		public class itemAdapter extends ArrayAdapter<bill_list> implements OnItemClickListener {
			  private final Context context;
			  private final List<bill_list> data;
			  public TextView name;
			  public TextView price;
			  public EditText quantity;
			  public TextView table;
			  public itemAdapter(Context context,List<bill_list> data) {
			    super(context, R.layout.item_list,data);
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
			    View rowView = inflater.inflate(R.layout.item_list,parent,false);
			    final bill_list x = data.get(position);
			    Log.i("atul",data.toString());
			     name=(TextView)rowView.findViewById(R.id.ilname);
			    price=(TextView)rowView.findViewById(R.id.ilprice);
			    quantity=(EditText)rowView.findViewById(R.id.ilquantity);
			    table=(TextView)rowView.findViewById(R.id.iltable_no);
			    ImageButton delete=(ImageButton)rowView.findViewById(R.id.delete);
			    Button b=(Button)rowView.findViewById(R.id.edit);
			    b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						einame=name.getText().toString();
						eprice=price.getText().toString();
						equantity=quantity.getText().toString();
						etable=table.getText().toString();//unit price
						eono=orderNum.getText().toString();
						int q=Integer.parseInt(equantity);
						int u=Integer.parseInt(etable);
						int p=q*u;
						x.quantity=equantity;
						
						eprice=Integer.toString(p);
						price.setText(Integer.toString(p));
						x.price=eprice;
						new edit().execute();
					}
				});
			    delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent i=new Intent(CustomerBill.this,DeleteItems.class);
						i.putExtra("name",x.name);
						 Log.i("onclickdelete",(String)x.name);
						i.putExtra("price",x.price);
						 Log.i("onclickdelete",(String)x.price);
						i.putExtra("quantity",x.quantity);
						 Log.i("onclickdelete",(String)x.quantity);
						i.putExtra("order_no",x.order_no);
						 Log.i("onclickdelete",(String)x.order_no);
						i.putExtra("table_no",x.table_no);
						 Log.i("onclickdelete",(String)x.table_no);
						
						i.putExtra("cname",(String)cusName.getText());
						  Log.i("onclickdelete",(String)cusName.getText());
						
						i.putExtra("cmobile",(String)cusMobile.getText());
						 Log.i("onclickdelete",(String)cusMobile.getText());
						
						i.putExtra("wname",(String)waiterName.getText());
						  Log.i("onclickdelete",(String)waiterName.getText());
						  i.putExtra("wid",(String)waiterId.getText());
						  Log.i("onclickdelete",(String)waiterId.getText());
						  startActivity(i);
						  overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
						 
						
						
						
					}
				});
			    
			   
			    
			   name.setText(x.name);
			    price.setText(x.price);
			    table.setText(x.table_no);
			    quantity.setText(x.quantity);
			    
			    return rowView;
			  }

			

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				
			}
			} 
		
		//service for the edit button
		public JSONObject editdata() throws ClientProtocolException,IOException,JSONException{
			JSONObject json=null;
			String jsonstr="";
			InputStream is=null;
			try{
			client = new DefaultHttpClient();
			HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/update_final_order.php");
			Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			
			params.add(new BasicNameValuePair("order_no",eono));
			Log.i("editdata",eono);
			params.add(new BasicNameValuePair("unit_price",etable));
			Log.i("editdata",etable);
			params.add(new BasicNameValuePair("name",einame));
			Log.i("editdata",einame);
			params.add(new BasicNameValuePair("price",eprice));
			Log.i("editdata",eprice);
			params.add(new BasicNameValuePair("quantity",equantity));
			Log.i("editdata",equantity);
			
			
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

		public class edit extends AsyncTask<String, Integer, JSONObject>{

			ProgressDialog dialog;
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				dialog = new ProgressDialog(CustomerBill.this);
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
					Toast.makeText(CustomerBill.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
				}else{
					Log.i("atul",result.toString());
					
				}
				
			}
	}
	
	
	
}
