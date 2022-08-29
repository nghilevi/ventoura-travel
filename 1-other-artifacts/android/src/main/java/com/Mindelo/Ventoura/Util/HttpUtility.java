package com.Mindelo.Ventoura.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;

import com.Mindelo.Ventoura.Entity.ImageProfile;
import com.Mindelo.Ventoura.JSONEntity.KeyValueMessage;
import com.google.gson.Gson;

public class HttpUtility {
	public static String fromHttpJsonResponseToString(HttpResponse response){
		
        try {
        	InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append((line + "\n"));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } 
	}
	
	public static BufferedReader fromHttpJsonResponseToReader(HttpResponse response){
		
		  try {
	        	InputStream is = response.getEntity().getContent();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	            return reader;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        } 
	}
	
	public static long parseResponseMessageLongValue(HttpResponse response){
		
		if (response.getStatusLine().getStatusCode() == 202 || response.getStatusLine().getStatusCode() == 200) {
			Gson gson = new Gson();

			String content = HttpUtility
					.fromHttpJsonResponseToString(response);

			@SuppressWarnings("unchecked")
			KeyValueMessage<String, String> msg = gson.fromJson(content,
					KeyValueMessage.class);
			if (msg.getValue() != "-1") {
				return Long.valueOf(msg.getValue());
			} else {
				return -1;
			}
		}else{
			// server error code
			return -1;
		}
	}
	
public static int parseResponseMessageIntValue(HttpResponse response){
		
		if (response.getStatusLine().getStatusCode() == 202 || response.getStatusLine().getStatusCode() == 200) {
			Gson gson = new Gson();

			String content = HttpUtility
					.fromHttpJsonResponseToString(response);

			@SuppressWarnings("unchecked")
			KeyValueMessage<String, String> msg = gson.fromJson(content,
					KeyValueMessage.class);
			if (msg.getValue() != "-1") {
				return Integer.valueOf(msg.getValue());
			} else {
				return -1;
			}
		}else{
			// server error code
			return -1;
		}
	}
	
	public static byte[] downloadImageFromUrl(String url) throws IOException{
		URL imageUrl = new URL(url);
		return IOUtils.toByteArray(imageUrl.openConnection().getInputStream());
	}
}
