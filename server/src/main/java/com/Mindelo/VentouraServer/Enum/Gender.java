package com.Mindelo.VentouraServer.Enum;



public enum Gender {
	MALE(0),FEMALE(1),BOTH(2);
	
    private int numVal;

    Gender(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
