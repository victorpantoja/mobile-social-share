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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author victor.pantoja
 *
 */
public class Util {
	
	static final String TAG = "mss";
	
	//public static final String server_name = "http://myalbumshare.com:8000/api";
	public static final String server_name = "http://192.168.0.191:9080";
	public static final String url_send_context = "/context";	
	public static final String url_login = "/login";
	public static final String url_create_acount = "/login/create";
	public static final String url_create_friendship = "/friendship/create";
	public static final String url_get_friend = "/friendship/get.json";
	public static final String url_remove_friendship = "/friendship/remove";
	public static final String url_send_invite = "/invite/send";
	public static final String url_accept_invite = "/invite/accept";
	public static final String url_get_invitations = "/invitation/get.json";
	public static final String url_send_email_envite = "/invite/email/send";
	public static final String url_accept_email_envite = "/invite/email/accept";
	public static final String url_get_user = "/user.json";
	public static final String url_api_information = server_name+"/status";
	
	public static String postData(String url, Map<Integer, String> context) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(server_name+url);
	    
	    Log.i(TAG,"Ola!! Contexto: "+context.get(1));
	    
	    try {
	        // Add your data
		    StringEntity se = new StringEntity("{\"application\":[\"twitter\"],\"context\":{\"location\":\""+context.get(new Integer(1))+"\",\"status\":\"text\"}}",HTTP.UTF_8);
	    	
		    httppost.setHeader("Content-Type","application/json;charset=UTF-8");
	    	httppost.setEntity(se);

	        // Execute HTTP Post Request
	        Log.i(TAG, "Querying URL:" + url);
	        HttpResponse response = httpclient.execute(httppost);
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
	    	 Log.e(TAG,e.getMessage());
	    } catch (IOException e) {
	        Log.e(TAG,e.getMessage());
	    }
	    return "";
	} 

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
				return json.getString("msg")+";"+json.getJSONArray("invites").length();
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
