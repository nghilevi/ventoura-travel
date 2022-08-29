package com.Mindelo.VentouraServer.Enum;

public enum BookingStatus {
	NEEDACCEPT(0), REFUSED(1), PAID(2), NOTPAID(3), PENDING(4);
	
    private int status;

    BookingStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

}
