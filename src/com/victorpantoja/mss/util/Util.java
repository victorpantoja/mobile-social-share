/**
 * 
 */
package com.victorpantoja.mss.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

/**
 * @author victor.pantoja
 *
 */
public class Util {
	
	static final String TAG = "mss";
	
	private static final String server_name = "http://192.168.0.154:9080";
	public static final String url_send_context = "/context";	
	public static final String url_login = "/login";
	public static final String url_create_acount = "/login/create";
	public static final String url_create_friendship = "/friendship/create";
	public static final String url_get_friend = "/friendship/get.json";
	public static final String url_remove_friendship = "/friendship/remove";
	public static final String url_send_invite = "/invite/send";
	public static final String url_accept_invite = "/invite/accept";
	public static final String url_get_invites = "/invite/get.json";
	public static final String url_send_email_envites = "/invite/email/send";
	public static final String url_accept_email_envite = "/invite/email/accept";
	public static final String url_get_user = "/user.json";

	public static String queryRESTurl(String url) {  
		HttpParams params = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(params, 10 * 1000);
		HttpConnectionParams.setSoTimeout(params, 10 * 1000);
		HttpConnectionParams.setTcpNoDelay(params, true);
		HttpConnectionParams.setStaleCheckingEnabled(params, true);
		
		HttpClient httpclient = new DefaultHttpClient(params);

		HttpGet httpget = new HttpGet(server_name+url);  
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
	
	public static String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();
	        
	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        return hexString.toString();
	        
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
	
	
	public static String tryAuthenticate (String login, String pass)
	{
		String url = url_login+"?username="+login+"&password="+pass;
		
		String result = queryRESTurl(url);
		
		if(result.equals(""))
		{			
			return "";
		}
		
		try{
			JSONObject json = new JSONObject(result);
									
			if (json.getString("status").equals("ok"))
			{	
				return json.getString("msg");
			}
			else{
				return "";
			}
		}  
		catch (JSONException e) {  
			Log.e("JSON", "There was an error parsing the JSON", e);  
		}

		return "";
	}
}
