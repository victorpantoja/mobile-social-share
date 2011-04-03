package com.victorpantoja.mss.screen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.victorpantoja.mss.R;

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
import android.widget.EditText;
import android.widget.Toast;

public class StatusUpdateScreen extends Activity implements OnClickListener {
	static final String TAG = "mss";
	EditText textToSend;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        textToSend = (EditText)findViewById(R.id.editText1);
        
        Button button = (Button)findViewById(R.id.button1);
        button.setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		
		LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		      sendLocation(location);
		    }

		    private void sendLocation(Location location) {
		    	
		    	String location_str = location.getLatitude()+","+location.getLongitude();

		        Log.i(TAG, "textToSend: "+textToSend);
		    	//TODO - tratar os parametros
				String url = "http://192.168.0.154:9080/context?location="+location_str+"&text="+textToSend.getText().toString();
				String result = queryRESTurl(url);
		    	
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
	
	private String queryRESTurl(String url) {  
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setStaleCheckingEnabled(params, true);
		
		HttpClient httpclient = new DefaultHttpClient(params);

		HttpGet httpget = new HttpGet(url);  
		HttpResponse response;

		try {
			Log.i(TAG, "Querying URL:" + url);
			response = httpclient.execute(httpget);  
			// Log.i(TAG, "Status:[" + response.getStatusLine().toString() + "]");  
			HttpEntity entity = response.getEntity();  

			if (entity != null) {  

				InputStream instream = entity.getContent();  
				String result = convertStreamToString(instream);  
				// Log.i(TAG, "Result of converstion: [" + result + "]");  

				instream.close();  
				return result;  
			}  
		} catch (ClientProtocolException e) {  
			Log.e(TAG, "There was a protocol based error", e);  
		} catch (IOException e) {  
			Log.e(TAG, "There was an IO Stream related error", e);  
		}
		return null;  
	}
	
	private String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}