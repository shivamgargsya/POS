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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dhsit.pos.Bill.MySimpleArrayAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ActivityMain extends Activity implements View.OnClickListener{
	
	private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    Button reservatn,bills,returnOrd,assigntab,cusbill,todaybill,delist,addmenu;
    private String[] tabs;
    
    DefaultHttpClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main1);
		reservatn = (Button) findViewById(R.id.bReservations);
		bills = (Button) findViewById(R.id.bBills);
		returnOrd = (Button) findViewById(R.id.bReturnOrders);
		assigntab = (Button) findViewById(R.id.bAssignTable);
		cusbill = (Button) findViewById(R.id.bCustomerBill);
		todaybill = (Button) findViewById(R.id.bTodaysBills);
		delist = (Button) findViewById(R.id.bDeletedItemsList);
		addmenu = (Button) findViewById(R.id.bAddMenuItem);
		
		reservatn.setOnClickListener(this);
		bills.setOnClickListener(this);
		returnOrd.setOnClickListener(this);
		assigntab.setOnClickListener(this);
		cusbill.setOnClickListener(this);
		todaybill.setOnClickListener(this);
		delist.setOnClickListener(this);
		addmenu.setOnClickListener(this);
		
		tabs = new String[] {"Table Structure","Reservations","Add Menu Items","Return Orders","Assign Tables View","Reports","Add Waiter"};
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	    mDrawerList = (ListView) findViewById(R.id.left_drawer);
	    
	    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, tabs));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(R.string.app_name);
            	//getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(R.string.app_name);
            	//getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
	}
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        if(tabs[position].contentEquals("Reservations")){
        	startActivity(new Intent(ActivityMain.this,CustomerBill.class));
        }else if(tabs[position].contentEquals("Return Orders")){
        	startActivity(new Intent(ActivityMain.this,ReturnOrders.class));
        }else if(tabs[position].contentEquals("Return Orders")){
        	startActivity(new Intent(ActivityMain.this,ReturnOrders.class));
        }else if(tabs[position].contentEquals("Assign Tables View")){
        	startActivity(new Intent(ActivityMain.this,AssignTable.class));
        }else if(tabs[position].contentEquals("Reports")){
        	startActivity(new Intent(ActivityMain.this,Reports.class));
        }else if(tabs[position].contentEquals("Add Menu Items")){
        	startActivity(new Intent(ActivityMain.this,AddMenuItem.class));
        }else if(tabs[position].contentEquals("Table Structure")){
        	startActivity(new Intent(ActivityMain.this,TableStructure.class));
        }else if(tabs[position].contentEquals("Table Structure")){
        	startActivity(new Intent(ActivityMain.this,Reports.class));
        }
        else if(tabs[position].contentEquals("Add Waiter")){
        	startActivity(new Intent(ActivityMain.this,add_waiter.class));
        }

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        //setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // The action bar home/up action should open or close the drawer.
         // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        
       
            return super.onOptionsItemSelected(item);
        }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bReservations:
			startActivity(new Intent(ActivityMain.this,Reservation.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bBills:
			startActivity(new Intent(ActivityMain.this,Bill.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bReturnOrders:
			startActivity(new Intent(ActivityMain.this,ViewReturnOrders.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bAssignTable:
			startActivity(new Intent(ActivityMain.this,AssignTable.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bCustomerBill:
			startActivity(new Intent(ActivityMain.this,CustomerBill.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bTodaysBills:
			startActivity(new Intent(ActivityMain.this,Bill.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bDeletedItemsList:
			startActivity(new Intent(ActivityMain.this,ViewDeleteItem.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		case R.id.bAddMenuItem:
			startActivity(new Intent(ActivityMain.this,AddMenuItem.class));
			overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
			break;
		}
	}
	public JSONObject service() throws ClientProtocolException,IOException,JSONException{
		JSONObject json=null;
		String jsonstr="";
		InputStream is=null;
		try{
		client = new DefaultHttpClient();
		HttpPost p = new HttpPost("http://10.0.2.2:80/tpos/get_waiters.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		
		
		
		
		
		
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
			dialog = new ProgressDialog(ActivityMain.this);
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
				Toast.makeText(ActivityMain.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
				try {
                   
                     
                    // Getting JSON Array node
                    JSONArray waiters = result.getJSONArray("waiters");
                    //listdata [] list=new listdata[bills.length()];
                   
                    final String [] values=new String[waiters.length()];
                    // looping through All Contacts
                    for (int i = 0; i < waiters.length(); i++) {
                        JSONObject c = waiters.getJSONObject(i);
                         
                       
                        String waiter_name = c.getString("waiter_name");
                        Log.i("atul",waiter_name);
 
                        // Phone node is JSON Object
                       
                        
                        
                        
                     
                        values[i]=waiter_name;
                        
                       
                      
                    }
                   
                    
                } catch (Exception e
                		) {
                    e.printStackTrace();
                }
			}
		}
}
}
