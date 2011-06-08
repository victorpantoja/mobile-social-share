package com.victorpantoja.mss.screen;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdateScreen extends Activity implements OnClickListener {
	static final int DIALOG_TRAFFIC_ID = 1;

	static final String TAG = "mss";
	private CheckBox mTwitterBtn;
	private EditText reviewEdit;
	private String auth = "";
	private LocationManager locationManager;
	private LocationListener locationListener;

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
		
		String review = reviewEdit.getText().toString();
		
		if (review.equals("")) return;
		
		mTwitterBtn	= (CheckBox) findViewById(R.id.twitterCheck);
		
		locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      sendLocation(location);
		    }

			public void onStatusChanged(String provider, int status, Bundle extras) {}

		    public void onProviderEnabled(String provider) {}

		    public void onProviderDisabled(String provider) {
				Toast.makeText(getApplicationContext(), "Could not find location.", Toast.LENGTH_SHORT).show();
		    }
		  };
		  
		// Register the listener with the Location Manager to receive location updates
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
	}
	

    private void sendLocation(Location location) {
    	
    	String location_str = location.getLatitude()+","+location.getLongitude();

        Log.i(TAG, "textToSend: "+reviewEdit);
    	//TODO - tratar os parametros
        
		String url = Util.url_send_context+"?location="+location_str+"&text="+reviewEdit.getText().toString()+"&auth="+auth;
		String result = Util.queryRESTurl(url);
    	
		Toast.makeText(getApplicationContext(), "Result: "+result, Toast.LENGTH_SHORT).show();
		locationManager.removeUpdates(locationListener);
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