package com.Mindelo.Ventoura.Ghost.Service;

import java.io.BufferedReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.Mindelo.Ventoura.Constant.ConfigurationConstant;
import com.Mindelo.Ventoura.Constant.DBConstant;
import com.Mindelo.Ventoura.Constant.HttpFieldConstant;
import com.Mindelo.Ventoura.Constant.Http_API_URL;
import com.Mindelo.Ventoura.Entity.GuideUpdatedLog;
import com.Mindelo.Ventoura.Entity.TravellerUpdatedLog;
import com.Mindelo.Ventoura.Ghost.DBHelper.VentouraDBHelper;
import com.Mindelo.Ventoura.Ghost.DBManager.BookingDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.GuideUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerScheduleDBManager;
import com.Mindelo.Ventoura.Ghost.DBManager.TravellerUpdatedLogDBManager;
import com.Mindelo.Ventoura.Ghost.IService.IBookingScheduleService;
import com.Mindelo.Ventoura.JSONEntity.JSONBooking;
import com.Mindelo.Ventoura.JSONEntity.JSONBookingList;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerSchedule;
import com.Mindelo.Ventoura.JSONEntity.JSONTravellerScheduleList;
import com.Mindelo.Ventoura.Util.DateTimeUtil;
import com.Mindelo.Ventoura.Util.HttpUtility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BookingScheduleService implements IBookingScheduleService {

	private SQLiteDatabase db;
	private VentouraDBHelper ventouraDbHelper;
	private TravellerScheduleDBManager scheduleManager;
	private BookingDBManager bookingDBManager;
	private TravellerUpdatedLogDBManager tulDbManager;
	private GuideUpdatedLogDBManager gulDbManager;

	public BookingScheduleService(Context context) {
		ventouraDbHelper = new VentouraDBHelper(context);
		db = ventouraDbHelper.getWritableDatabase();

		bookingDBManager = new BookingDBManager(this.db);
		scheduleManager = new TravellerScheduleDBManager(this.db);
		tulDbManager = new TravellerUpdatedLogDBManager(this.db);
		gulDbManager = new GuideUpdatedLogDBManager(this.db);
	}

	@Override
	public JSONTravellerScheduleList getTravellerScheduleListFromDB(
			long travellerId) {

		JSONTravellerScheduleList jsonTravellerScheduleList = new JSONTravellerScheduleList();
		/*
		 * no data changed, use the local cached data
		 */
		List<JSONTravellerSchedule> schedules = scheduleManager
				.findMany(" where "
						+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_TRAVELLER_ID
						+ "=" + travellerId);

		jsonTravellerScheduleList.setTravellerScheduleList(schedules);
		return jsonTravellerScheduleList;

	}

	@Override
	public JSONBooking getBookingById(long bookingId) {
		return bookingDBManager.findOne(" where "
				+ DBConstant.TABLE_BOOKINGS_FIELD_ID + "=" + bookingId);
	}

	@Override
	public JSONBookingList getTravellerBookingsListFromDB(long travellerId) {

		JSONBookingList bookingsList = new JSONBookingList();

		List<JSONBooking> bookings = bookingDBManager.findMany(" where "
				+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_ID + "="
				+ travellerId);

		bookingsList.setBookings(bookings);

		return bookingsList;
	}

	@Override
	public JSONBookingList getGuideBookingsListFromDB(long guideId) {

		JSONBookingList bookingsList = new JSONBookingList();

		List<JSONBooking> bookings = bookingDBManager.findMany(" where "
				+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_ID + "=" + guideId);

		bookingsList.setBookings(bookings);

		return bookingsList;
	}

	public boolean getTravellerScheduleListFromServer(long travellerId) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVELLER_SCHEDULE + "/"
							+ travellerId);

			HttpClient client = new DefaultHttpClient();
			// set the header
			setTravellerScheduleHttpModifiedHeader(get, travellerId);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONTravellerScheduleList schedules = gson.fromJson(reader,
						JSONTravellerScheduleList.class);
				/*
				 * cache task
				 */
				scheduleManager
						.deleteMany(" where "
								+ DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_TRAVELLER_ID
								+ "=" + travellerId); // delete all the matches
				scheduleManager.saveAll(schedules.getTravellerScheduleList());

				updateTravellerScheduleLastModifiedHeader(response, travellerId);

				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean getTravellerBookingsListFromServer(long travellerId) {
		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_TRAVELLER_BOOKINGS + "/"
							+ travellerId);

			HttpClient client = new DefaultHttpClient();
			setTravellerBookingHttpModifiedHeader(get, travellerId);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONBookingList bookings = gson.fromJson(reader,
						JSONBookingList.class);

				/*
				 * cache task
				 */
				bookingDBManager.deleteMany(" where "
						+ DBConstant.TABLE_BOOKINGS_FIELD_TRAVELLER_ID + "="
						+ travellerId);
				bookingDBManager.saveAll(bookings.getBookings());

				updateTravellerBookingsLastModifiedHeader(response, travellerId);

				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public long createBooking(JSONBooking booking) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs = loadBookingField(booking);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_LOAD_TRAVELLER_BOOKIN_CREATE_BOOKING);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			return HttpUtility.parseResponseMessageLongValue(response);

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

	}

	public boolean createTravellerSchedule(JSONTravellerSchedule schedule) {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		try {

			nameValuePairs = loadTravellerScheduleField(schedule);

			HttpPost post = new HttpPost(
					Http_API_URL.URL_SERVER_LOAD_TRAVELLER_CREATE_SCHEDULE);
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(post);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				// save it to local DB
				schedule.setId(HttpUtility
						.parseResponseMessageLongValue(response));
				scheduleManager.save(schedule);

				return true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	private List<NameValuePair> loadTravellerScheduleField(
			JSONTravellerSchedule schedule) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_TRAVELLER_ID, ""
						+ schedule.getTravellerId()));

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_START_DATE, schedule
						.getStartTime()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_START_DATE, schedule
						.getStartTime()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_END_DATE, schedule
						.getEndTime()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_CITY, ""
						+ schedule.getCity()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.TRAVELLER_SCHEDULE_COUNTRY, ""
						+ schedule.getCountry()));

		return nameValuePairs;
	}

	private List<NameValuePair> loadBookingField(JSONBooking booking) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_GUIDE_ID, "" + booking.getGuideId()));

		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_TRAVELLER_ID, ""
						+ booking.getTravellerId()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_GUIDE_FIRST_NAME, booking
						.getGuideFirstname()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_TRAVELLER_FIRST_NAME, booking
						.getTravellerFirstname()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_TOUR_PRICE, ""
						+ booking.getTourPrice()));
		nameValuePairs.add(new BasicNameValuePair(
				HttpFieldConstant.BOOKING_TOUR_DATE, booking.getTourDate()));

		return nameValuePairs;
	}

	public boolean getGuideBookingListFromServer(long guideId) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss")
				.create();

		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GET_GUIDE_BOOKINGS + "/" + guideId);

			HttpClient client = new DefaultHttpClient();
			setGuideBookingHttpModifiedHeader(get, guideId);
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {

				BufferedReader reader = HttpUtility
						.fromHttpJsonResponseToReader(response);
				JSONBookingList guideBookings = gson.fromJson(reader,
						JSONBookingList.class);

				/*
				 * cache task
				 */
				bookingDBManager.deleteMany(" where "
						+ DBConstant.TABLE_BOOKINGS_FIELD_GUIDE_ID + "="
						+ guideId);
				bookingDBManager.saveAll(guideBookings.getBookings());

				updateGuideBookingsLastModifiedHeader(response, guideId);

				return true;

			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean deleteTravellerSchdeule(long travellerId, long scheduleId) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_DELETE_TRAVELLER_SCHEDULE + "/"
							+ scheduleId);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				
				scheduleManager.deleteMany(" where " + DBConstant.TABLE_TRAVELLER_SCHEDULE_FIELD_ID + "=" + scheduleId);
				
				updateTravellerScheduleLastModifiedHeader(response, travellerId);
				
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean guideUpdateBookingStatus(long bookingId, long statusCode) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_GUIDE_UPDATE_BOOKING_STATUS + "/"
							+ bookingId + "/" + statusCode);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean travellerUpdateBookingStatus(long bookingId, long statusCode) {
		try {
			HttpGet get = new HttpGet(
					Http_API_URL.URL_SERVER_TRAVELLER_UPDATE_BOOKING_STATUS
							+ "/" + bookingId + "/" + statusCode);

			HttpClient client = new DefaultHttpClient();
			HttpResponse response = client.execute(get);

			if (response.getStatusLine().getStatusCode() == 202
					|| response.getStatusLine().getStatusCode() == 200) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/****************************************************/
	/**** Cache functions ****/
	/****************************************************/

	private void setTravellerScheduleHttpModifiedHeader(
			HttpRequestBase request, long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getToursLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void setTravellerBookingHttpModifiedHeader(HttpRequestBase request,
			long travellerId) {
		/*
		 * set modified header
		 */
		TravellerUpdatedLog log = tulDbManager.findOne(" where "
				+ DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TRAVELLER_ID
				+ "=" + travellerId);
		if (log != null) {
			Date date = new Date(log.getBookingsLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void setGuideBookingHttpModifiedHeader(HttpRequestBase request,
			long travellerId) {
		/*
		 * set modified header
		 */
		GuideUpdatedLog log = gulDbManager.findOne(" where "
				+ DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_GUIDE_ID + "="
				+ travellerId);
		if (log != null) {
			Date date = new Date(log.getBookingsLastUpdated());
			String time_GMT = DateTimeUtil.fromDateToString_GMT(date);
			request.setHeader(ConfigurationConstant.HTTP_HEADER_MODIFIED_SINCE,
					time_GMT);
		}
	}

	private void updateTravellerScheduleLastModifiedHeader(
			HttpResponse response, long travellerId) {

		try {
			Header header = response
					.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
			tulDbManager.updateUpdatedLog(
					DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_TOURS_UPDATED,
					DateTimeUtil.fromStringToDate_GMT(header.getValue())
							.getTime(), travellerId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateTravellerBookingsLastModifiedHeader(
			HttpResponse response, long travellerId) {

		try {
			Header header = response
					.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
			tulDbManager
					.updateUpdatedLog(
							DBConstant.TABLE_TRAVELLER_UPDATED_LOG_FIELD_BOOKINGS_UPDATED,
							DateTimeUtil
									.fromStringToDate_GMT(header.getValue())
									.getTime(), travellerId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void updateGuideBookingsLastModifiedHeader(HttpResponse response,
			long guideId) {

		try {
			Header header = response
					.getFirstHeader(ConfigurationConstant.HTTP_HEADER_LASR_MODIFIED);
			gulDbManager.updateUpdatedLog(
					DBConstant.TABLE_GUIDE_UPDATED_LOG_FIELD_BOOKINGS_UPDATED,
					DateTimeUtil.fromStringToDate_GMT(header.getValue())
							.getTime(), guideId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
