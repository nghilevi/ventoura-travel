package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.XMPPConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.HttpFieldConstant;
import com.Mindelo.VentouraServer.Constant.PushConstant;
import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Entity.TravellerLastUpdatedLog;
import com.Mindelo.VentouraServer.Entity.TravellerSchedule;
import com.Mindelo.VentouraServer.Enum.BookingStatus;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IService.IBookingService;
import com.Mindelo.VentouraServer.IService.IGuideService;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ITravellerScheduleService;
import com.Mindelo.VentouraServer.JSONEntity.JSONBooking;
import com.Mindelo.VentouraServer.JSONEntity.JSONKeyValueMessage;
import com.Mindelo.VentouraServer.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.VentouraServer.JSONEntity.JSONTravellerTripList;
import com.Mindelo.VentouraServer.Main.IMManager;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/traveller")
public class TravellerTripController {

	@Autowired
	private IBookingService bookingService;
	@Autowired
	private ITravellerScheduleService travellerScheduleService;
	@Autowired
	private IGuideService guideService;
	@Autowired
	private IIMService imService;
	@Autowired
	private ILastUpdatedService luService;

	private XMPPConnection connection;

	/**************************/
	/* Traveller Bookings Mappings */
	/**************************/

	@RequestMapping(value = "/createTravellerSchedule", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> uploadTravellerSchedulePost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		TravellerSchedule travellerSchedule;
		try {
			travellerSchedule = loadTravellerScheduleFormField(request);
			travellerScheduleService.saveTravellerSchdeule(travellerSchedule);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
			luService.setTravellerToursLastUpdated(time_modified.getTime(),
					travellerSchedule.getTravellerId());

			JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
			msg.setKey("scheduleId");
			msg.setValue("" + travellerSchedule.getId());

			return msg;
		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}

	}

	@RequestMapping(value = "/createBooking", method = RequestMethod.POST)
	public @ResponseBody JSONKeyValueMessage<String, String> createBookingPost(
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		Booking booking;
		try {
			booking = loadBookingFormField(request);
			bookingService.saveNewBooking(booking);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
			luService.setGuideBookingsLastUpdated(time_modified.getTime(),
					booking.getGuideId());
			luService.setTravellerBookingsLastUpdated(time_modified.getTime(),
					booking.getTravellerId());

			JSONKeyValueMessage<String, String> msg = new JSONKeyValueMessage<String, String>();
			msg.setKey("bookingId");
			msg.setValue("" + booking.getId());

			/*
			 * notice the guide that a new booking is created
			 */
			connection = IMManager.getXMPPConnection();
			imService.sendMessage(connection, booking.getGuideId(),
					UserRole.GUIDE, PushConstant.PUSH_TRAVELLER_CREATE_BOOKING
							+ booking.getId());

			return msg;
		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}

	}

	@RequestMapping(value = "/updateTravellerSchedule", method = RequestMethod.POST)
	public void updateTravellerSchedulePost(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		TravellerSchedule travellerSchedule;
		try {
			travellerSchedule = loadTravellerScheduleFormField(request);
			travellerSchedule
					.setId(Long.valueOf(request
							.getParameter(HttpFieldConstant.SERVER_TRAVELLER_SCHEDULE_ID)));

			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			travellerScheduleService.updateTravellerSchedule(travellerSchedule);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
			luService.setTravellerToursLastUpdated(time_modified.getTime(),
					travellerSchedule.getTravellerId());

		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/updateBooking", method = RequestMethod.POST)
	public void updateBookingPost(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Booking booking;
		try {
			booking = loadBookingFormField(request);
			booking.setId(Long.valueOf(request
					.getParameter(HttpFieldConstant.SERVER_BOOKING_ID)));

			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			bookingService.updateBooking(booking);

			/*
			 * set update log
			 */
			Date time_modified = new Date();
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
			luService.setGuideBookingsLastUpdated(time_modified.getTime(),
					booking.getGuideId());
			luService.setTravellerBookingsLastUpdated(time_modified.getTime(),
					booking.getTravellerId());

		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(value = "/deleteTravellerSingleSchedule/{scheduleId}", method = RequestMethod.GET)
	public void deleteTravellerSingleSchedule(HttpServletResponse response,
			@PathVariable("scheduleId") final long scheduleId) {
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		TravellerSchedule travellerSchedule = travellerScheduleService
				.getTravellerScheduleById(scheduleId);
		travellerScheduleService.deleteScheduleByScheduleId(scheduleId);

		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
		luService.setTravellerToursLastUpdated(time_modified.getTime(),
				travellerSchedule.getTravellerId());
	}

	@RequestMapping(value = "/deleteTravellerAllSchedule/{travellerId}", method = RequestMethod.GET)
	public void deleteTravellerAllSchedules(HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		travellerScheduleService.deleteScheduleByTravellerId(travellerId);

		/*
		 * set update log
		 */
		Date time_modified = new Date();
		response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(time_modified));	
		luService.setTravellerToursLastUpdated(time_modified.getTime(), travellerId);
	}

	@RequestMapping(value = "/getTravellerTrip/{travellerId}", method = RequestMethod.GET)
	public @ResponseBody JSONTravellerTripList getTravellerSchedules(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("travellerId") final long travellerId) {

		JSONTravellerTripList travellerScheduleList = new JSONTravellerTripList();

		/*
		 * no changes in the server
		 */
		TravellerLastUpdatedLog travellerLastUpdatedLog = luService
				.getTravellerLastUpdatedLogByTravellerId(travellerId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {

				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (travellerLastUpdatedLog != null) {
					if (lastUpdatedTime == travellerLastUpdatedLog
							.getBookingsLastUpdated()
							&& lastUpdatedTime == travellerLastUpdatedLog
									.getToursLastUpdated()) {
						/*
						 * TODO maybe we need to seperate the APIs for the tours
						 * and the bookings of the traveller
						 */
						response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					}
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if(travellerLastUpdatedLog != null){
			Date date_modified = new Date();
			if( travellerLastUpdatedLog
					.getBookingsLastUpdated() >= travellerLastUpdatedLog
							.getToursLastUpdated()){
				date_modified.setTime(travellerLastUpdatedLog.getBookingsLastUpdated());	
			}else{
				date_modified.setTime(travellerLastUpdatedLog.getToursLastUpdated());	
			}
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(date_modified));	
		}
		
		/*
		 * return the databody 
		 */
		try {
			// load JSONBookings
			List<Booking> bookings = bookingService
					.getAllBookingsByTravellerId(travellerId);
			List<JSONBooking> jsonBookings = new ArrayList<JSONBooking>();
			for (Booking booking : bookings) {
				JSONBooking jsonBooking = new JSONBooking(booking);
				jsonBookings.add(jsonBooking);
			}

			// load JSONTravellerSchedules
			List<TravellerSchedule> schedules = travellerScheduleService
					.getTravellerSchedulesByTravellerId(travellerId);
			List<JSONTravellerSchedule> jsonSchedules = new ArrayList<JSONTravellerSchedule>();

			for (TravellerSchedule schedule : schedules) {
				JSONTravellerSchedule jsonSchedule = new JSONTravellerSchedule(
						schedule);
				jsonSchedules.add(jsonSchedule);
			}

			travellerScheduleList.setTravellerBookingList(jsonBookings);
			travellerScheduleList.setTravellerScheduleList(jsonSchedules);

			return travellerScheduleList;
		} catch (Exception e) {
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}

	}

	private Booking loadBookingFormField(HttpServletRequest request)
			throws Exception {
		Booking booking = new Booking();
		booking.setBookedDate(new Date());
		booking.setBookingStatus(BookingStatus.NEEDACCEPT);
		booking.setGuideId(Long.valueOf(request
				.getParameter(HttpFieldConstant.BOOKING_GUIDE_ID)));
		booking.setTravellerId(Long.valueOf(request
				.getParameter(HttpFieldConstant.BOOKING_TRAVELLER_ID)));
		booking.setTourPrice(Float.valueOf(request
				.getParameter(HttpFieldConstant.BOOKING_TOUR_PRICE)));
		booking.setTourDate(DateTimeUtil.fromStringToDate(request
				.getParameter(HttpFieldConstant.BOOKING_TOUR_DATE)));
		booking.setTravellerFirstname(request
				.getParameter(HttpFieldConstant.BOOKING_TRAVELLER_FIRST_NAME));
		booking.setGuideFirstname(request
				.getParameter(HttpFieldConstant.BOOKING_GUIDE_FIRST_NAME));
		return booking;
	}

	private TravellerSchedule loadTravellerScheduleFormField(
			HttpServletRequest request) throws Exception {

		TravellerSchedule schedule = new TravellerSchedule();

		schedule.setCity(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TRAVELLER_SCHEDULE_CITY)));
		schedule.setCountry(Integer.valueOf(request
				.getParameter(HttpFieldConstant.TRAVELLER_SCHEDULE_COUNTRY)));

		schedule.setStartTime(DateTimeUtil.fromToStringTODATE_DDMMYYYY(request
				.getParameter(HttpFieldConstant.TRAVELLER_SCHEDULE_START_DATE)));
		schedule.setEndTime(DateTimeUtil.fromToStringTODATE_DDMMYYYY(request
				.getParameter(HttpFieldConstant.TRAVELLER_SCHEDULE_END_DATE)));

		schedule.setTravellerId(Long.valueOf(request
				.getParameter(HttpFieldConstant.TVT_TRAVELLER_ID)));

		return schedule;
	}
}
