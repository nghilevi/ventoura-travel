package com.Mindelo.Ventoura.Ghost.IService;

import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.JSONEntity.JSONBookingList;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerScheduleList;

public interface IBookingScheduleService {
	public long createBooking(JSONBooking booking);
	public boolean createTravellerSchedule(JSONTravellerSchedule travellerSchedule);

	/**
	 * @return true means data are updated, need to reload latest from the database
	 */
	public boolean getTravellerScheduleListFromServer(long travellerId);
	public JSONTravellerScheduleList getTravellerScheduleListFromDB(long travellerId);
	
	/**
	 * @return true means data are updated, need to reload latest from the database
	 */
	public boolean getTravellerBookingsListFromServer(long travellerId);
	public JSONBookingList getTravellerBookingsListFromDB(long travellerId);
	
	/**
	 * @return true means data are updated, need to reload latest from the database
	 */
	public boolean getGuideBookingListFromServer(long guideId);
	public JSONBookingList getGuideBookingsListFromDB(long guideId);
	
	
	public JSONBooking getBookingById(long bookingId);
	
	/**
	 * @param bookingId and the statusCode which the guide want to update the booking to 
	 */
	public boolean guideUpdateBookingStatus(long bookingId, long statusCode);
	
	/**
	 * @param bookingId and the statusCode which the traveller want to update the booking to 
	 */
	public boolean travellerUpdateBookingStatus(long bookingId, long statusCode);
	
	
	/**
	 * delete a traveller schedule
	 */
	public boolean deleteTravellerSchdeule(long travellerId, long scheduleId);

}
