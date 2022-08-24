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
import org.apache.http.util.EntityUtils;

import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Ghost.IService.IPaymentService;

public class PaymentService implements IPaymentService {

	@Override
	public String getBraintreeClientToken() {
		try {
			HttpGet get = new HttpGet(Http_API_URL.URL_SERVER_PAYMENT_GET_CLIENT_TOKEN); // TODO
																				// cache
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				String responseString = EntityUtils.toString(
						response.getEntity(), "UTF-8");
				return responseString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public boolean postBraintreeNonceTokenToServer(String nonce) {
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs.add(new BasicNameValuePair("payment_method_nonce", nonce));
			
			HttpPost post = new HttpPost(Http_API_URL.URL_SERVER_PAYMENT_POST_NONCE_PURCHASE);
			
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);
			if(response.getStatusLine().getStatusCode() == 200){
				return true;
			}else{
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
