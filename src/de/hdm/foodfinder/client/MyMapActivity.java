package de.hdm.foodfinder.client;

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

public class MyMapActivity extends MapActivity {

	String APIKEY = "0N-56Pydas2rL05rrn84t0Mguor0G0i86V2SACA";
	MapView map;

	int actLatitude;
	int actLongitude;
	int resLatitude;
	int resLongitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_activity);

		Bundle extras = getIntent().getExtras();
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

		// Center map to new position.
		GeoPoint restaurantPosition = new GeoPoint(resLatitude, resLongitude);
		Log.e(MyMapActivity.class.getCanonicalName(),
				"Setting center of map to: " + restaurantPosition);
		MapController controller = map.getController();
		controller.setCenter(restaurantPosition);
		controller.setZoom(15);

		GeoPoint actPosition = new GeoPoint(actLatitude, actLongitude);

		ArrayList<GeoPoint> locations = new ArrayList<GeoPoint>();
		locations.add(restaurantPosition);
		locations.add(actPosition);

		// Overlay-Objekte erzeugen und Karte hinzufügen.
		GeoPointsOverlay gpOverlay = new GeoPointsOverlay(getResources()
				.getDrawable(R.drawable.ic_launcher));
		gpOverlay.setItems(locations);
		map.getOverlays().add(gpOverlay);
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
			Toast.makeText(getApplicationContext(), "Nr. " + i + " getippt.",
					Toast.LENGTH_SHORT).show();
			return true;
		}
	}
}