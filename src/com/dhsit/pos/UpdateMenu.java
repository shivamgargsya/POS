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
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateMenu extends Activity implements  View.OnClickListener, OnItemSelectedListener{
	
	
	private static int RESULT_LOAD_IMAGE_BIG = 1;
	private static int RESULT_LOAD_IMAGE_SMALL = 2;
	private int pyear;
    private int pmonth;
    private int pday;
    Calendar cal;
	TextView time,date;
	Spinner cat,subcat;
	EditText ename,eid,edesc,ecarbo,eproteins,efats,esugars,evitamins,eminerals,eenergy,emisc;
	String name,id,desc,carbo,proteins,fats,sugars,vitamins,minerals,energy,misc,nutri,c,sc;
	Button bbigimage,bsmallimage,save;
	ImageView ibigimage,ismallimage;
	HttpClient client;
	String[] category, subcategory;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addmenu);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		time = (TextView) findViewById(R.id.tvMenuTime);
		date = (TextView) findViewById(R.id.tvMenuDate);
		cat = (Spinner) findViewById(R.id.sMenuCategory);
		subcat = (Spinner) findViewById(R.id.sMenySubCategory);
		ename = (EditText) findViewById(R.id.etMenuName);
		eid = (EditText) findViewById(R.id.etMenuId);
		edesc = (EditText) findViewById(R.id.etMenuDesc);
		ecarbo = (EditText) findViewById(R.id.etMenuCarboHydrates);
		eproteins = (EditText) findViewById(R.id.etMenuProteins);
		efats = (EditText) findViewById(R.id.etMenuFats);
		esugars = (EditText) findViewById(R.id.etMenuSugars);
		evitamins = (EditText) findViewById(R.id.etMenuVitamins);
		eminerals = (EditText) findViewById(R.id.etMenuMinerals);
		eenergy = (EditText) findViewById(R.id.etMenuEnergy);
		emisc = (EditText) findViewById(R.id.etMenuMisc);
		bbigimage = (Button) findViewById(R.id.bMenuBigImage);
		bsmallimage = (Button) findViewById(R.id.bMenuSmallImage);
		ibigimage = (ImageView) findViewById(R.id.ivMenuBigImage);
		ismallimage = (ImageView) findViewById(R.id.ivMenuSmallImage);
		save = (Button) findViewById(R.id.bMenuSave);
		
		
				
		
		bbigimage.setOnClickListener(this);
		bsmallimage.setOnClickListener(this);
		save.setOnClickListener(this);
		
		refreshdate();
		category = new String[]{"Starter", "Soups", "Main Course","Beverages","Desert"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, category);
		cat.setAdapter(adapter);
		
		cat.setOnItemSelectedListener(this);
		
		subcategory = new String[]{"SubCat1", "SubCat2", "SubCat3","SubCat4","SubCat5"};
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subcategory);
		subcat.setAdapter(adapter1);
		subcat.setOnItemSelectedListener(this);
		
	}
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Spinner spinner = (Spinner) parent;
	     if(spinner.getId() == R.id.sMenuCategory)
	     {
	       c = category[position];                    
	     }
	     else if(spinner.getId() == R.id.sMenySubCategory)
	     {
	       sc = subcategory[position];
	     }
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.bMenuSave:
			
			name = ename.getText().toString();
			id = eid.getText().toString();
			desc = edesc.getText().toString();
			carbo = ecarbo.getText().toString();
			proteins = eproteins.getText().toString();
			fats = efats.getText().toString();
			sugars = esugars.getText().toString();
			vitamins = evitamins.getText().toString();
			minerals = eminerals.getText().toString();
			energy = eenergy.getText().toString();
			misc = emisc.getText().toString();
			
			nutri = "Carbohydrates = " + carbo +  "\n" + 
					"Proteins = " + proteins + "\n" + 
					"Fats = " + fats + "\n" + 
					"Sugars = " + sugars + "\n" + 
					"Vitamins = " + vitamins + "\n" + 
					"Minerals = " + minerals + "\n" + 
					"Energy = " + energy + "\n" + 
					"\n" + misc;
			
			break;
		case R.id.bMenuBigImage:
			Intent i = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_LOAD_IMAGE_BIG);
			break;
		case R.id.bMenuSmallImage:
			Intent j = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(j, RESULT_LOAD_IMAGE_SMALL);
			break;
		}
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         
        if (requestCode == RESULT_LOAD_IMAGE_BIG && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //picturePath contains path of the selected picture
            ibigimage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        }else if (requestCode == RESULT_LOAD_IMAGE_SMALL && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
 
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
 
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //picturePath contains path of the selected picture
            ismallimage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
         
        } 
     
     
    }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		startActivity(new Intent(UpdateMenu.this,ActivityMain.class));
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
			UpdateMenu.this.finish();
			startActivity(new Intent(UpdateMenu.this,ActivityMain.class));
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
		HttpPost p = new HttpPost("http://192.168.127.1/pos/menu_item_insert.php");
		Log.i("JSONinTranSum", "passed number,operatrId,tt,amount : ");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("category", c));
		params.add(new BasicNameValuePair("sub_category", sc));
		params.add(new BasicNameValuePair("cost", id));
		params.add(new BasicNameValuePair("image", ""));
		params.add(new BasicNameValuePair("nutrition", nutri));
		params.add(new BasicNameValuePair("info", desc));
		params.add(new BasicNameValuePair("likes", "0"));
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
                sb.append(line + "n");
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
			dialog = new ProgressDialog(UpdateMenu.this);
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
				Toast.makeText(UpdateMenu.this, "Error in Connection!", Toast.LENGTH_SHORT).show();
			}else{
				Log.i("atul",result.toString());
			}
		}
}

	

}
