package com.Mindelo.Ventoura.Ghost.IService;

public interface IPaymentService {
	/**
	 * used a get method to retrieve a token from server, this token acts like a ID of the server's braintree account
	 */
	public String getBraintreeClientToken();

	/**
	 * post the nonce back to the server, the nonce acts list a encrypted infor of the payment details of the user
	 * @return true if payment successful
	 */
	public boolean postBraintreeNonceTokenToServer(String nonce);
}
