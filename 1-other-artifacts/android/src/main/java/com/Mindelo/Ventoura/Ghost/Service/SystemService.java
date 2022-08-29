package com.Mindelo.Ventoura.Ghost.Service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Ghost.IService.ISystemService;
import com.Mindelo.Ventoura.Util.HttpUtility;

public class SystemService implements ISystemService{
	
	
	public long traverllerLoginProbe(String facebookAccount) {
		try {
			HttpGet get = new HttpGet(Http_API_URL.URL_SERVER_TRAVELLER_LOGIN_PROBE + "/" + facebookAccount);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public long guideLoginProbe(String facebookAccount) {
		try {
			HttpGet get = new HttpGet(Http_API_URL.URL_SERVER_GUIDE_LOGIN_PROBE + "/" + facebookAccount);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * because Android doesn't have to send any device infor to the server. we can use the old GET APIs
	 */
	/*public long traverllerLoginProbe(String facebookAccount) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.LOGIN_USER_DEVICE_OSTYPE, "" + 1));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.LOGIN_USER_DEVICE_TOKEN, ""));
		
		try {
			HttpPost post = new HttpPost(Http_API_URL.URL_SERVER_TRAVELLER_LOGIN_PROBE + "/" + facebookAccount);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public long guideLoginProbe(String facebookAccount) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.LOGIN_USER_DEVICE_OSTYPE, "" + 1));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.LOGIN_USER_DEVICE_TOKEN, ""));
		
		try {
			HttpPost post = new HttpPost(Http_API_URL.URL_SERVER_GUIDE_LOGIN_PROBE + "/" + facebookAccount);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}*/

}
