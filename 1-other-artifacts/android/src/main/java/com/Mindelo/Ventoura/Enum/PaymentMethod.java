package com.Mindelo.Ventoura.Enum;





public enum PaymentMethod {
	CASH(0), CARD(1), ALL(2);

	private int numVal;

	PaymentMethod(int numVal) {
		this.numVal = numVal;
	}

	public int getNumVal() {
		return numVal;
	}
	
	public void setNumVal(int numVal) {
		this.numVal = numVal;
	}
}
