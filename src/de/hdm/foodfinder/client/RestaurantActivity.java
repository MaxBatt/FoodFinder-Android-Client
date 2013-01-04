package de.hdm.foodfinder.client;

import com.google.gson.Gson;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class RestaurantActivity extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);

		Bundle extras = getIntent().getExtras();
		
		Gson gson = new Gson();
		String jsonRestaurant = extras.getString("restaurant");
		
		Restaurant restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);
		
		Toast toast = Toast.makeText(this,
				extras.getString("restaurant"), Toast.LENGTH_SHORT);
		toast.show();
		
		Toast toast1 = Toast.makeText(this,
				extras.getString("dishes"), Toast.LENGTH_SHORT);
		toast1.show();

	}

}
