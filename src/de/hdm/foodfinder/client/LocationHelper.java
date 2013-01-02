package de.hdm.foodfinder.client;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/***
 * Location Helper Class. Handles creation of the Location Manager and Location
 * Listener.
 * 
 * @author Scott Helme
 */
public class LocationHelper {

	// my location manager and listener
	public LocationManager locationManager;
	private MyLocationListener locationListener;
	private Location currentLocation;
	private Geocoder geocoder;
	// variables to store lat and long
	private String latitude;
	private String longitude;

	// flag for when we have co-ords
	private boolean gotLocation = false;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            - The context of the calling activity.
	 */
	public LocationHelper(Context context) {

		// setup the location manager
		locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// create the location listener
		locationListener = new MyLocationListener();

		// setup a callback for when the GRPS/WiFi gets a lock and we receive
		// data
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
		// setup a callback for when the GPS gets a lock and we receive data
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationListener);

		geocoder = new Geocoder(context);
	}

	/***
	 * Used to receive notifications from the Location Manager when they are
	 * sent. These methods are called when the Location Manager is registered
	 * with the Location Service and a state changes.
	 * 
	 * @author Scott Helme
	 */
	public class MyLocationListener implements LocationListener {

		// called when the location service reports a change in location
		public void onLocationChanged(Location location) {

			// LocationHelper.this.currentLocation = location;

			// now we have our location we can stop the service from sending
			// updates
			// comment out this line if you want the service to continue
			// updating the users location
			locationManager.removeUpdates(locationListener);

			// change the flag to indicate we now have a location
			if (location != null) {
				gotLocation 	= true;
				currentLocation = location;
				latitude 		= String.valueOf(location.getLatitude());
				longitude 		= String.valueOf(location.getLongitude());
			}

		}

		// called when the provider is disabled
		public void onProviderDisabled(String provider) {
		}

		// called when the provider is enabled
		public void onProviderEnabled(String provider) {
		}

		// called when the provider changes state
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	/***
	 * Stop updates from the Location Service.
	 */
	public void killLocationServices() {
		locationManager.removeUpdates(locationListener);
	}

	/***
	 * Get Latitude from GPS Helper. Should check gotLocation() first.
	 * 
	 * @return - The current Latitude.
	 */
	public String getLat() {
		return latitude;
	}

	/***
	 * Get Longitude from GPS Helper. Should check gotLocation() first.
	 * 
	 * @return - The current Longitude.
	 */
	public String getLong() {
		return longitude;
	}

	/***
	 * Check if a location has been found yet.
	 * 
	 * @return - True if a location has been acquired. False otherwise.
	 */
	public Boolean gotLocation() {
		return gotLocation;
	}

	/**
	 * Converts a location (coordinates) into an address.
	 */
	public String getStrLocation() {
		try {
			StringBuffer text = new StringBuffer();
			List<Address> adresses = geocoder.getFromLocation(
					currentLocation.getLatitude(),
					currentLocation.getLongitude(), 1);
			for (Address a : adresses) {
				for (int i = 0; i < a.getMaxAddressLineIndex(); i++) {
					text.append(a.getAddressLine(i) + ", ");
				}
				text.append("\n");
			}
			return text.toString();
		} catch (IOException e) {
			Log.e(MainActivity.class.getCanonicalName(),
					"Could not reverse geocode coordinates.", e);
			return "Problem bei Adressbestimmung\n";
		}
	}
}