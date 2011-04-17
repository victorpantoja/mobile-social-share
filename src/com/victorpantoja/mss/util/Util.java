/**
 * 
 */
package com.victorpantoja.mss.util;

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

import android.util.Log;

/**
 * @author victor.pantoja
 *
 */
public class Util {
	
	static final String TAG = "mss";
	
	public static String queryRESTurl(String url) {  
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
			Log.i(TAG, "Status:[" + response.getStatusLine().toString() + "]");  
			HttpEntity entity = response.getEntity();  

			if (entity != null) {  

				InputStream instream = entity.getContent();  
				String result = convertStreamToString(instream);  
				Log.i(TAG, "Result of converstion: [" + result + "]");  

				instream.close();  
				return result;  
			}  
		} catch (ClientProtocolException e) {  
			Log.e(TAG, "There was a protocol based error", e);  
		} catch (IOException e) {  
			Log.e(TAG, "There was an IO Stream related error", e);  
		}
		return "";  
	}
	
	public static String convertStreamToString(InputStream is) {

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
