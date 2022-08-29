package com.Mindelo.VentouraServer.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jivesoftware.smack.XMPPConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.Mindelo.VentouraServer.Constant.ConfigurationConstant;
import com.Mindelo.VentouraServer.Constant.PushConstant;
import com.Mindelo.VentouraServer.Entity.Booking;
import com.Mindelo.VentouraServer.Entity.GuideLastUpdatedLog;
import com.Mindelo.VentouraServer.Enum.BookingStatus;
import com.Mindelo.VentouraServer.Enum.UserRole;
import com.Mindelo.VentouraServer.IService.IBookingService;
import com.Mindelo.VentouraServer.IService.IIMService;
import com.Mindelo.VentouraServer.IService.ILastUpdatedService;
import com.Mindelo.VentouraServer.IService.ITravellerService;
import com.Mindelo.VentouraServer.JSONEntity.JSONBooking;
import com.Mindelo.VentouraServer.JSONEntity.JSONGuideBookingList;
import com.Mindelo.VentouraServer.Main.IMManager;
import com.Mindelo.VentouraServer.Util.DateTimeUtil;

@Controller
@RequestMapping(value = "/guide")
public class GuideBookingController {

	@Autowired
	private IBookingService bookingService;
	@Autowired
	private ITravellerService travellerService;
	@Autowired
	private IIMService imService;
	@Autowired
	private ILastUpdatedService luService;

	private XMPPConnection connection;

	/**************************/
	/* Guide Bookings Mappings */
	/**************************/

	@RequestMapping(value = "/getAllBookings/{guideId}", method = RequestMethod.GET)
	public @ResponseBody JSONGuideBookingList getAllBookingsForGuide(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("guideId") final long guideId) {

		JSONGuideBookingList bookingList = new JSONGuideBookingList();

		/*
		 * no changes in the server
		 */
		GuideLastUpdatedLog guideLastUpdatedLog = luService
				.getGuideLastUpdatedLogByGuideId(guideId);
		try {
			String modifiedSince = request
					.getHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE);
			if (modifiedSince != null) {
				
				long lastUpdatedTime = DateTimeUtil.fromStringToDate_GMT(
						modifiedSince).getTime();
				if (guideLastUpdatedLog != null
						&& lastUpdatedTime == guideLastUpdatedLog
								.getBookingsLastUpdated()) {
					response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
					return null;
				}
			}
		} catch (Exception e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			return null;
		}


		/*
		 * some changes happened in the server
		 */
		response.setStatus(HttpServletResponse.SC_ACCEPTED);
		if(guideLastUpdatedLog != null){
			Date date_modified = new Date();
			date_modified.setTime(guideLastUpdatedLog.getBookingsLastUpdated());
			response.setHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED, DateTimeUtil.fromDateToString_GMT(date_modified));	
		}
		
		try {
			// load JSONBookings
			List<Booking> bookings = bookingService
					.getAllBookingsByGuideId(guideId);
			List<JSONBooking> jsonBookings = new ArrayList<JSONBooking>();
			for (Booking booking : bookings) {
				if (booking.getBookingStatus() == BookingStatus.REFUSED) {
					continue;
				}
				JSONBooking jsonBooking = new JSONBooking(booking);
				jsonBookings.add(jsonBooking);
			}
			bookingList.setBookings(jsonBookings);

			return bookingList;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}

	@RequestMapping(value = "/updateBookingStatus/{bookingId}/{decision}", method = RequestMethod.GET)
	public void responseBookings(HttpServletResponse response,
			@PathVariable("bookingId") final long bookingId,
			@PathVariable("decision") final int decision) {

		try {
			response.setStatus(HttpServletResponse.SC_ACCEPTED);
			Booking booking = bookingService.getBookingById(bookingId);

			if (decision == 0) {
				bookingService.updateBookingStatus(bookingId,
						BookingStatus.REFUSED);

				/*
				 * notice the traveller his booking is refused
				 */
				connection = IMManager.getXMPPConnection();
				imService.sendMessage(
						connection,
						booking.getTravellerId(),
						UserRole.TRAVELLER,
						PushConstant.PUSH_GUIDE_REFUSE_BOOKING
								+ booking.getId());

			} else {
				bookingService.updateBookingStatus(bookingId,
						BookingStatus.NOTPAID);

				/*
				 * notice the traveller his booking is accepted
				 */
				connection = IMManager.getXMPPConnection();
				imService.sendMessage(
						connection,
						booking.getTravellerId(),
						UserRole.TRAVELLER,
						PushConstant.PUSH_GUIDE_ACCEPT_BOOKING
								+ booking.getId());
			}

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
			e.printStackTrace();
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
