package com.Mindelo.Ventoura.Ghost.Service;

import java.util.List;
import java.util.Locale;

import com.Mindelo.Ventoura.Ghost.IService.IGeoLocationService;

import android.content.Context;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

public class GeoLocationService implements IGeoLocationService {

	private Address address;

	public GeoLocationService(Context context) {

		LocationManager locationManager;
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		Criteria crta = new Criteria();
		crta.setAccuracy(Criteria.ACCURACY_FINE);
		crta.setAltitudeRequired(false);
		crta.setBearingRequired(false);
		crta.setCostAllowed(true);
		crta.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(crta, true);

		// fetch the location
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {

			double lattitude = location.getLatitude();
			double longitude = location.getLongitude();

			Geocoder gc = new Geocoder(context, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(lattitude,
						longitude, 1);
				if (addresses.size() == 1) {
					address = (Address) addresses.get(0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getUserCurrentCountry() {

		if (address != null) {
			return address.getCountryName();
		} else {
			return "";
		}
	}

	@Override
	public String getUserCurrentCountryCode() {
		if (address != null) {
			return address.getCountryCode();
		} else {
			return "";
		}
	}

}
