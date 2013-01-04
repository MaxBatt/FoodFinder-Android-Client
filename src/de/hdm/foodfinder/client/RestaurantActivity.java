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
		String json = extras.getString("json");
		
		Restaurant restaurant = gson.fromJson(json, Restaurant.class);
		
		Toast toast = Toast.makeText(this,
				restaurant.getAddress(), Toast.LENGTH_SHORT);
		toast.show();

	}

}
