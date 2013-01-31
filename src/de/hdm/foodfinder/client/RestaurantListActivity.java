package de.hdm.foodfinder.client;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import de.hdm.foodfinder.client.helpers.Restaurant;
import de.hdm.foodfinder.client.helpers.RestaurantArrayAdapter;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;

import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * RestaurtantListActivity
 * 
 * * Wandelt Json-Array in Liste von Restaurant-Objekten um und befüllt mit
 * dieser den Listenadapter
 * 
 * @author Max Batt
 */
public class RestaurantListActivity extends ListActivity implements
		OnItemClickListener {

	ArrayList<Restaurant> restaurants;
	RestaurantArrayAdapter adapter;
	Gson gson;
	String actLatitude;
	String actLongitude;

	String jsonRestaurant;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		// Extras
		Bundle extras = getIntent().getExtras();
		String json = extras.getString("json");
		actLatitude = extras.getString("actLatitude");
		actLongitude = extras.getString("actLongitude");

		// Json in ArrayList<Restaurants> umwandeln
		gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray Jarray;
		Jarray = parser.parse(json).getAsJsonArray();
		restaurants = new ArrayList<Restaurant>();

		for (JsonElement obj : Jarray) {
			Restaurant restaurant = gson.fromJson(obj, Restaurant.class);
			restaurants.add(restaurant);
			// System.out.println(restaurant.getAddress());
		}

		// Wenn Restaurants gefunden
		if (restaurants.size() > 0) {

			// Adapter befüllen
			adapter = new RestaurantArrayAdapter(this, restaurants);
			setListAdapter(adapter);

			// ListView befüllen
			ListView listView = getListView();
			// OnItemClickListener setzen
			listView.setOnItemClickListener(this);
		} else {
			showDialog(getString(R.string.err_no_entries));
		}

	}

	// OnItemClickListener für ListView
	// Gibt Restaurant als Json string und die aktuellen Koordinaten an
	// RestaurantView weiter
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		jsonRestaurant = gson.toJson(restaurants.get(position));

		Intent myIntent = new Intent(RestaurantListActivity.this,
				RestaurantActivity.class);
		myIntent.putExtra("restaurant", jsonRestaurant);
		myIntent.putExtra("actLatitude", actLatitude);
		myIntent.putExtra("actLongitude", actLongitude);
		startActivity(myIntent);
	}
	
	
	/**	
	 * showDialog
	 * 
	 * @param msg
	 * 
	 * zeigt Fehlermeldung an, falls keine Restaurants mit den entsprechenden Kriterein gefunden wurden
	 */
	protected void showDialog(String msg) {
		new AlertDialog.Builder(RestaurantListActivity.this).setMessage(msg)

		// Standort-Button
				.setPositiveButton(getString(R.string.back_to_search),
				// Click Listener
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Dialog schließen
								dialog.cancel();
								// Standortzugriff aufrufen
								RestaurantListActivity.super.onBackPressed();
							}
						}
				// Dialog einblenden
				).create().show();
	}

}
