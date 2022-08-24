package com.Mindelo.VentouraServer.Enum;


public enum UserRole {
	GUIDE(0), TRAVELLER(1);

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
