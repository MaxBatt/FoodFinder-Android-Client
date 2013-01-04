package de.hdm.foodfinder.client;

import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantActivity extends Activity{
	
	private TextView tvName;
	private TextView tvAddress;
	private TextView tvRegions;
	private TextView tvCategories;
	private TextView tvDishes;
	private TextView tvDistance;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);

		Bundle extras = getIntent().getExtras();
		Gson gson = new Gson();
		
		String jsonRestaurant = extras.getString("restaurant");
		Restaurant restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);

		//Strings für Gerichte, Kategorien und Regionen aus JSON Arrays implodieren
		String dishes= implode("\n", gson.fromJson(extras.getString("dishes"), String[].class)); 
		String categories= implode(", ", gson.fromJson(extras.getString("categories"), String[].class));
		String regions= implode(", ", gson.fromJson(extras.getString("regions"), String[].class));
		
		
		tvName = (TextView) findViewById(R.id.restaurantName);
		tvAddress = (TextView) findViewById(R.id.restaurantAddress);
		tvRegions = (TextView) findViewById(R.id.restaurantRegions);
		tvCategories = (TextView) findViewById(R.id.restaurantCategories);
		tvDishes = (TextView) findViewById(R.id.restaurantDishes);
		tvDistance = (TextView) findViewById(R.id.restaurantDistance);
		
		tvName.setText(restaurant.getName());
		tvAddress.setText(restaurant.getAddress());
		tvRegions.setText(regions);
		tvCategories.setText(categories);
		tvDishes.setText(dishes);
		tvDistance.setText(restaurant.getDistance());
	
		
		/*
		Toast toast = Toast.makeText(this,
				extras.getString("restaurant"), Toast.LENGTH_SHORT);
		toast.show();
		
		Toast toast1 = Toast.makeText(this,
				extras.getString("dishes"), Toast.LENGTH_SHORT);
		toast1.show();
		
		*/
		
		

	}
	
	
	public static String implode(String separator, String... data) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < data.length - 1; i++) {
	    //data.length - 1 => to not add separator at the end
	        if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
	            sb.append(data[i]);
	            sb.append(separator);
	        }
	    }
	    sb.append(data[data.length - 1]);
	    return sb.toString();
	}

}
