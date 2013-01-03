package de.hdm.foodfinder.client;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RestaurantListActivity extends ListActivity {

	ArrayList<Restaurant> restaurants;
	RestaurantArrayAdapter adapter;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Bundle extras = getIntent().getExtras();
		String json = extras.getString("json");

		Gson gson = new Gson();
		JsonParser parser = new JsonParser();

		JsonArray Jarray;

		Jarray = parser.parse(json).getAsJsonArray();
		restaurants = new ArrayList<Restaurant>();

		for (JsonElement obj : Jarray) {
			Restaurant restaurant = gson.fromJson(obj, Restaurant.class);
			restaurants.add(restaurant);
			//System.out.println(restaurant.getAddress());
		}

		adapter = new RestaurantArrayAdapter(this, restaurants);
		setListAdapter(adapter);

	}

}
