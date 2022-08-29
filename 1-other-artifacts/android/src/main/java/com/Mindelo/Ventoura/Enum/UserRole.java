package com.Mindelo.Ventoura.Enum;

public enum UserRole {
	GUIDE(0), TRAVELLER(1), BOTH(2);

	private int numVal;

	UserRole(int numVal) {
		this.numVal = numVal;
	}

	public int getNumVal() {
		return numVal;
	}
	public void setNumVal(int numVal) {
		this.numVal = numVal;
	}
}
