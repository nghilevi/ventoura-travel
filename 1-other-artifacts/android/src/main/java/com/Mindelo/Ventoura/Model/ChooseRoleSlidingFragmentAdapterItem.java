package com.Mindelo.Ventoura.Model;

import java.io.Serializable;
import java.util.List;

import com.Mindelo.Ventoura.Enum.UserRole;

import lombok.Data;

@Data
public class ChooseRoleSlidingFragmentAdapterItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8827017392309744629L;

	private int bgColorResId;
	
	private int navigationIVBgResId;

	private String topicTitle;

	private List<String> descriptionContents;

	private String loginButtonTitle;
	
	private UserRole userRole;
	
	private int loginButtonSelectorResid;

}
