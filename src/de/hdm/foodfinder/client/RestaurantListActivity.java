package de.hdm.foodfinder.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RestaurantListActivity extends ListActivity implements
		OnItemClickListener {

	ArrayList<Restaurant> restaurants;
	RestaurantArrayAdapter adapter;
	Gson gson;

	String dishes = "";
	String regions = "";
	String categories = "";
	String jsonRestaurant;

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
			// System.out.println(restaurant.getAddress());
		}

		adapter = new RestaurantArrayAdapter(this, restaurants);
		setListAdapter(adapter);

		ListView shot = getListView();
		shot.setOnItemClickListener(this);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		jsonRestaurant = gson.toJson(restaurants.get(position));

		String url = "http://pfronhaus.dlinkddns.com:4567/restaurant/"
				+ restaurants.get(position).getId() + "/dishes";

		String url2 = "http://pfronhaus.dlinkddns.com:4567/restaurant/"
				+ restaurants.get(position).getId() + "/regions";

		String url3 = "http://pfronhaus.dlinkddns.com:4567/restaurant/"
				+ restaurants.get(position).getId() + "/categories";

		getRestaurantTask task = new getRestaurantTask();
		task.execute(new String[] { url, url2, url3 });
	}

	private class getRestaurantTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			dishes = getHttpResponse(urls[0]);
			regions = getHttpResponse(urls[1]);
			categories = getHttpResponse(urls[2]);
			return "";
		}

		@Override
		protected void onPostExecute(String result) {
	/*
			Toast toast = Toast.makeText(RestaurantListActivity.this, dishes,
					Toast.LENGTH_LONG);
			toast.show();
			
			Toast toast1 = Toast.makeText(RestaurantListActivity.this, regions,
					Toast.LENGTH_LONG);
			toast1.show();
			
			Toast toast2 = Toast.makeText(RestaurantListActivity.this, categories,
					Toast.LENGTH_LONG);
			toast2.show();
			
			Toast toast3 = Toast.makeText(RestaurantListActivity.this, jsonRestaurant,
					Toast.LENGTH_LONG);
			toast3.show();
*/
			
			 Intent myIntent = new Intent(RestaurantListActivity.this, RestaurantActivity.class);
			 myIntent.putExtra("restaurant", jsonRestaurant);
			 myIntent.putExtra("dishes", dishes);
			 myIntent.putExtra("regions", regions);
			 myIntent.putExtra("categories", categories);
			 
			 startActivity(myIntent);
			 
			 
		}

		@Override
		protected void onCancelled() {
			Toast toast = Toast.makeText(RestaurantListActivity.this,
					getString(R.string.err_no_connection), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public String getHttpResponse(String url) {
		String response = "";

		DefaultHttpClient client = new DefaultHttpClient();
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpResponse execute = client.execute(httpGet);
			InputStream content = execute.getEntity().getContent();

			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					content));
			String s = "";
			while ((s = buffer.readLine()) != null) {
				response += s;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

}
