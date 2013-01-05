package de.hdm.foodfinder.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import de.foodfinder.client.helpers.DoubleValue;
import de.foodfinder.client.helpers.Restaurant;
import de.foodfinder.client.helpers.RestaurantInfos;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantActivity extends Activity {

	private TextView tvName;
	private TextView tvAddress;
	private TextView tvRegions;
	private TextView tvCategories;
	private TextView tvDishes;
	private TextView tvDistance;
	private ImageView photoView;

	private String serverURL = "http://pfronhaus.dlinkddns.com:4567";
	private Restaurant restaurant;
	private Gson gson;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);

		Bundle extras = getIntent().getExtras();

		gson = new Gson();

		String jsonRestaurant = extras.getString("restaurant");
		restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);

		String url = serverURL + "/restaurant/" + restaurant.getId() + "/infos";

		getRestaurantInfos task = new getRestaurantInfos();
		task.execute(new String[] { url });

		tvName = (TextView) findViewById(R.id.restaurantName);
		tvAddress = (TextView) findViewById(R.id.restaurantAddress);
		tvRegions = (TextView) findViewById(R.id.restaurantRegions);
		tvCategories = (TextView) findViewById(R.id.restaurantCategories);
		tvDishes = (TextView) findViewById(R.id.restaurantDishes);
		tvDistance = (TextView) findViewById(R.id.restaurantDistance);
		photoView = (ImageView) findViewById(R.id.photoView);

		photoView.setOnClickListener(imageListener);

	}

	private class getRestaurantInfos extends
			AsyncTask<String, Void, DoubleValue> {

		ProgressDialog waitingDialog = new ProgressDialog(
				RestaurantActivity.this);

		@Override
		protected void onPreExecute() {
			waitingDialog.setTitle(getString(R.string.loading_restaurant));
			waitingDialog.show();
		}

		@Override
		protected DoubleValue doInBackground(String... urls) {

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

			RestaurantInfos restaurantInfos = gson.fromJson(response,
					RestaurantInfos.class);
			String imgURL = restaurantInfos.getPhotos();

			Bitmap bitmap = null;

			if (imgURL != "") {

				try {
					InputStream in = new java.net.URL(imgURL).openStream();
					bitmap = BitmapFactory.decodeStream(in);
				} catch (Exception e) {
					Log.d("Error", e.getMessage());
					e.printStackTrace();
				}

			}

			DoubleValue result = new DoubleValue();
			result.setRestaurantInfos(response);
			result.setBitmap(bitmap);
			return result;

		}

		@Override
		protected void onPostExecute(DoubleValue result) {
			RestaurantInfos restaurantInfos = gson.fromJson(
					result.getRestaurantInfos(), RestaurantInfos.class);
			Bitmap bitmap = result.getBitmap();
			// Toast.makeText(getApplicationContext(), dishes,
			// Toast.LENGTH_SHORT).show();

			photoView.setImageBitmap(bitmap);
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

	// ClickListener für Bild
	private OnClickListener imageListener = new OnClickListener() {

		public void onClick(View v) {
			photoView.buildDrawingCache();
			Bitmap bitmap = photoView.getDrawingCache();

			if (bitmap != null) {
				Toast.makeText(RestaurantActivity.this,
						restaurant.getLatitude(), Toast.LENGTH_SHORT).show();

			}
		}
	};

	// ClickListener für Map Button
	public void showMap(View view) {
		Intent myIntent = new Intent(this,
				MyMapActivity.class);
		myIntent.putExtra("latitude", restaurant.getLatitude());
		myIntent.putExtra("latitude", restaurant.getLongitude());
		startActivity(myIntent);
	}

}
