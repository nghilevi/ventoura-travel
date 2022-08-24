package com.Mindelo.Ventoura.Model;

import java.io.Serializable;
import java.util.List;

import com.Mindelo.Ventoura.Enum.PaymentMethod;
import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

@Data
public class PaymentMethodSlidingFragmentAdapterItem implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int bgColorResId;
	
	private int navigationIVBgResId;

	private String topicTitle;

	private List<String> descriptionContents;

	private String paymentMethodButtonTitle;
	
	private PaymentMethod paymentMethod;
	
	private int paymentMethodButtonSelectorResid;

}
