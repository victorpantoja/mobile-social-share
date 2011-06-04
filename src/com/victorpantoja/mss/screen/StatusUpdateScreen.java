package com.victorpantoja.mss.screen;

import com.victorpantoja.mss.R;
import com.victorpantoja.mss.util.Util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdateScreen extends Activity implements OnClickListener {
	static final String TAG = "mss";
	private CheckBox mTwitterBtn;
	private EditText reviewEdit;
	private String auth = "";

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
    }

	@Override
	public void onClick(View v) {
		
		String review = reviewEdit.getText().toString();
		
		if (review.equals("")) return;
		
		mTwitterBtn	= (CheckBox) findViewById(R.id.twitterCheck);
		
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      sendLocation(location);
		    }

		    private void sendLocation(Location location) {
		    	
		    	String location_str = location.getLatitude()+","+location.getLongitude();

		        Log.i(TAG, "textToSend: "+reviewEdit);
		    	//TODO - tratar os parametros
		        
				String url = Util.url_send_context+"?location="+location_str+"&text="+reviewEdit.getText().toString()+"&auth="+auth;
				String result = Util.queryRESTurl(url);
		    	
				Toast.makeText(getApplicationContext(), "Result: "+result, Toast.LENGTH_SHORT).show();
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
}