package de.hdm.foodfinder.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import de.foodfinder.client.helpers.Restaurant;
import de.foodfinder.client.helpers.RestaurantInfos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
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
	
	private String serverURL = "http://pfronhaus.dlinkddns.com:4567";
	private Restaurant restaurant;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);

		Bundle extras = getIntent().getExtras();
		Gson gson = new Gson();
		
		String jsonRestaurant = extras.getString("restaurant");
		restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);
		
		String url = serverURL + "/restaurant/"
		+ restaurant.getId() + "/infos";
		
		getRestaurantInfos task = new getRestaurantInfos();
		task.execute(new String[] { url});
		
		
		tvName = (TextView) findViewById(R.id.restaurantName);
		tvAddress = (TextView) findViewById(R.id.restaurantAddress);
		tvRegions = (TextView) findViewById(R.id.restaurantRegions);
		tvCategories = (TextView) findViewById(R.id.restaurantCategories);
		tvDishes = (TextView) findViewById(R.id.restaurantDishes);
		tvDistance = (TextView) findViewById(R.id.restaurantDistance);
	}
	
	private class getRestaurantInfos extends AsyncTask<String, Void, String> {
		
		ProgressDialog waitingDialog = new ProgressDialog(RestaurantActivity.this);
		
		@Override
		protected void onPreExecute(){
			waitingDialog.setTitle(getString(R.string.loading_restaurant));
			waitingDialog.show();
		}
		
		@Override
		protected String doInBackground(String... urls) {
			
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				try {
					HttpGet httpGet = new HttpGet(url);
					HttpResponse execute = client.execute(httpGet);
					InputStream content = execute.getEntity().getContent();

					BufferedReader buffer = new BufferedReader(
							new InputStreamReader(content));
					String s = "";
					while ((s = buffer.readLine()) != null) {
						response += s;
					}

				} catch (Exception e) {
					e.printStackTrace();
					this.cancel(true);
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Gson gson = new Gson();
			RestaurantInfos restaurantInfos = gson.fromJson(result, RestaurantInfos.class);
			
			//Toast.makeText(getApplicationContext(), dishes, Toast.LENGTH_SHORT).show(); 
			
			tvName.setText(restaurant.getName());
			tvAddress.setText(restaurant.getAddress());
			tvRegions.setText(restaurantInfos.getRegions());
			tvCategories.setText(restaurantInfos.getCategories());
			tvDishes.setText(restaurantInfos.getDishes());
			tvDistance.setText(restaurant.getDistance());
			
			waitingDialog.dismiss();
		}

		@Override
		protected void onCancelled() {
			waitingDialog.dismiss();
			
			Toast toast = Toast.makeText(RestaurantActivity.this,
					getString(R.string.err_no_connection), Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	
	

}
