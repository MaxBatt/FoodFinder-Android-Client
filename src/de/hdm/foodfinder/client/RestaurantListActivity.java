package de.hdm.foodfinder.client;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RestaurantListActivity extends ListActivity implements OnItemClickListener{

	ArrayList<Restaurant> restaurants;
	RestaurantArrayAdapter adapter;
	Gson gson;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Bundle extras = getIntent().getExtras();
		String json = extras.getString("json");

		gson = new Gson();
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
		
		ListView shot = getListView();
        shot.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		/*
		Toast toast = Toast.makeText(this,
				"yiieha", Toast.LENGTH_SHORT);
		toast.show();
		*/
		
		Intent myIntent = new Intent(this,
				RestaurantActivity.class);
		myIntent.putExtra("json", gson.toJson(restaurants.get(position)));
		startActivity(myIntent);
	}

}
