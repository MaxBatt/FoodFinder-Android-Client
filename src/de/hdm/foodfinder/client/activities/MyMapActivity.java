package de.hdm.foodfinder.client.activities;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;

import de.hdm.foodfinder.client.R;
import de.hdm.foodfinder.client.helpers.Restaurant;

public class MyMapActivity extends MapActivity {

	private MapView map;

	private Restaurant restaurant;
	
	private int actLatitude;
	private int actLongitude;
	private int resLatitude;
	private int resLongitude;
	
	GeoPoint actPosition;
	GeoPoint restaurantPosition;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		Bundle extras = getIntent().getExtras();
		
		Gson gson = new Gson();
		String jsonRestaurant = extras.getString("restaurant");
		restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);
		
		actLatitude = (int) (Double
				.parseDouble(extras.getString("actLatitude")) * 1e6);
		actLongitude = (int) (Double.parseDouble(extras
				.getString("actLongitude")) * 1e6);
		resLatitude = (int) (Double
				.parseDouble(extras.getString("resLatitude")) * 1e6);
		resLongitude = (int) (Double.parseDouble(extras
				.getString("resLongitude")) * 1e6);

		map = (MapView) findViewById(R.id.mapview);

		map.setClickable(true);
		map.setBuiltInZoomControls(true);
		map.setSatellite(true);
		setContentView(map);

		actPosition = new GeoPoint(actLatitude, actLongitude);
		restaurantPosition = new GeoPoint(resLatitude, resLongitude);
		// Center map to Restaurants position.
		Log.e(MyMapActivity.class.getCanonicalName(),
				"Setting center of map to: " + restaurantPosition);
		MapController controller = map.getController();
		controller.setCenter(actPosition);
		controller.setZoom(15);
		
		/*
		 * Overlays erzeugen für eigene Position und Restaurant-Position	
		 */
		//Array mit aktueller Position
		ArrayList<GeoPoint> actLocation = new ArrayList<GeoPoint>();
		actLocation.add(actPosition);
		//Array mit Restaurantposition	
		ArrayList<GeoPoint> restaurantLocation = new ArrayList<GeoPoint>();
		restaurantLocation.add(restaurantPosition);

		// Overlay für aktuelle Position erzeugen und auf Karte platzieren.
		GeoPointsOverlay actPositionOverlay = new GeoPointsOverlay(getResources()
				.getDrawable(R.drawable.pin_blue));
		actPositionOverlay.setItems(actLocation);
		map.getOverlays().add(actPositionOverlay);
		
		// Overlay für Restaurant-Position erzeugen und auf Karte platzieren.
		GeoPointsOverlay restaurantPositionOverlay = new GeoPointsOverlay(getResources()
				.getDrawable(R.drawable.pin_yellow));
		restaurantPositionOverlay.setItems(restaurantLocation);
		map.getOverlays().add(restaurantPositionOverlay);
		
		
		

		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	public class GeoPointsOverlay extends ItemizedOverlay<OverlayItem> {
		private List<GeoPoint> items;

		public GeoPointsOverlay(Drawable marker) {
			// Registrierungspunkt ist unten, mittig (wie z.B. bei Stecknadeln).
			super(boundCenterBottom(marker));
		}

		// Setzt eine Liste von Koordinaten, die markiert werden sollen.
		public void setItems(ArrayList<GeoPoint> items) {
			this.items = items;
			populate();
		}

		@Override
		// Wird intern populate() aufgerufen.
		protected OverlayItem createItem(int i) {
			String title = "Nr. " + i;
			String snippet = "Das ist Marker Nr. " + i + ".";
			return new OverlayItem(items.get(i), title, snippet);
		}

		@Override
		public int size() {
			return items.size();
		}

		@Override
		protected boolean onTap(int i) {
			if(items.get(i) == restaurantPosition){
				String text = restaurant.getName() + "\n" + restaurant.getAddress();
				Toast.makeText(getApplicationContext(), text,
						Toast.LENGTH_SHORT).show();
			}
			else{
				Toast.makeText(getApplicationContext(), getString(R.string.your_location),
						Toast.LENGTH_SHORT).show();
			}
			return true;
		}
	}
}