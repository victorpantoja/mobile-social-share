package com.victorpantoja.mss.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdateScreen extends Activity implements OnClickListener {
	static final int DIALOG_TRAFFIC_ID = 1;

	static final String TAG = "mss";
	private CheckBox mTwitterBtn, mFacebookBtn;
	private EditText reviewEdit;
	private String auth = "";
	private LocationManager locationManager;
	private LocationListener locationListener;
	
	SharedPreferences.Editor editor;
	SharedPreferences pref;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post);
        
        Bundle extras = getIntent().getExtras();
        
        auth = extras.getString("auth");
        
        reviewEdit = (EditText)findViewById(R.id.review);
        Button postButton = (Button)findViewById(R.id.postButton);
        postButton.setOnClickListener(this);
        
        CheckBox shareTraffic = (CheckBox)findViewById(R.id.shareTraffic);
        shareTraffic.setOnCheckedChangeListener(checbBoxListener);
        
        WebView apiInformation = (WebView)findViewById(R.id.apiStatusWebView);
        apiInformation.loadUrl(Util.url_api_information);
    }
    
    private OnCheckedChangeListener checbBoxListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				showDialog(DIALOG_TRAFFIC_ID);
			}
			else{
				reviewEdit.setText("");
			}
		}
    };
    
	@Override
	public void onClick(View v) {
		
		String status = reviewEdit.getText().toString();
		List<String> apps = new ArrayList<String>();
		
		if (status.equals("")) return;
		
		mTwitterBtn	= (CheckBox) findViewById(R.id.twitterCheck);
		mFacebookBtn = (CheckBox) findViewById(R.id.facebookCheck);
		
		if(mTwitterBtn.isChecked()){
			apps.add("twitter");
		}
		if(mFacebookBtn.isChecked()){
			apps.add("facebook");
		}
		
		if(apps.isEmpty()){
			Toast.makeText(getApplicationContext(), "No application selected.", Toast.LENGTH_SHORT).show();
			return;
		}
		
		this.pref = getSharedPreferences("MOBILESOCIALSHARE", MODE_PRIVATE);
		String location = pref.getString("location", "not_found");

		if(location.equals("not_found")){
			Toast.makeText(getApplicationContext(), "Could not determine your last location.", Toast.LENGTH_SHORT).show();
		}
		else{
			sendLocation(apps, location, status);
		}
	}
	

    private void sendLocation(List<String> apps, String location, String status) {
        
		String url = Util.url_send_context+"?auth="+auth;
		
        Map<String, String> context = new HashMap<String, String>();
        context.put("location", location);
        context.put("status", ""+status);
		
		String result = Util.postData(url, context, apps);
    	
		Toast.makeText(getApplicationContext(), "Result: "+result, Toast.LENGTH_SHORT).show();
	}
    
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog;
	    AlertDialog.Builder builder;
	    switch(id) {		    	
		    case DIALOG_TRAFFIC_ID:
		    	final CharSequence[] items = {"Very Light", "Light", "Normal", "Heavy", "Very Height"};

		    	builder = new AlertDialog.Builder(this);
		    	builder.setTitle("Choose Traffic Conditions");
		    	builder.setItems(items, new DialogInterface.OnClickListener() {
		    	    public void onClick(DialogInterface dialog, int item) {
		    	    	reviewEdit.setText(items[item]+" traffic");
		    	    }
		    	});
		    	dialog = builder.create();
		    	break;
		    default:
		        dialog = null;
	    }
	    return dialog;
	}
}