package de.hdm.foodfinder.client;

import java.io.InputStream;
import com.google.gson.Gson;
import de.foodfinder.client.helpers.Restaurant;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
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

	private Restaurant restaurant;
	private Gson gson;
	String actLatitude;
	String actLongitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.restaurant);

		Bundle extras = getIntent().getExtras();
		actLatitude = extras.getString("actLatitude");
		actLongitude = extras.getString("actLongitude");

		gson = new Gson();

		String jsonRestaurant = extras.getString("restaurant");
		restaurant = gson.fromJson(jsonRestaurant, Restaurant.class);

		tvName = (TextView) findViewById(R.id.restaurantName);
		tvAddress = (TextView) findViewById(R.id.restaurantAddress);
		tvRegions = (TextView) findViewById(R.id.restaurantRegions);
		tvCategories = (TextView) findViewById(R.id.restaurantCategories);
		tvDishes = (TextView) findViewById(R.id.restaurantDishes);
		tvDistance = (TextView) findViewById(R.id.restaurantDistance);
		photoView = (ImageView) findViewById(R.id.photoView);
		photoView.setOnClickListener(imageListener);

		tvName.setText(restaurant.getName());
		tvAddress.setText(restaurant.getAddress());
		tvRegions.setText(restaurant.getRegions());
		tvCategories.setText(restaurant.getCategories());
		tvDishes.setText(restaurant.getDishes());
		tvDistance.setText(restaurant.getDistance());

		if (restaurant.getPhotos().length() > 0) {
			getPhoto task = new getPhoto();
			task.execute(restaurant.getPhotos());
			System.out.println("nich leer");
		} else {
			photoView.setImageBitmap(null);
		}

	}

	private class getPhoto extends AsyncTask<String, Void, Bitmap> {

		ProgressDialog waitingDialog = new ProgressDialog(
				RestaurantActivity.this);

		@Override
		protected void onPreExecute() {
			waitingDialog.setTitle(getString(R.string.loading_restaurant));
			waitingDialog.show();
		}

		@Override
		protected Bitmap doInBackground(String... urls) {

			String imgURL = urls[0];

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
			return bitmap;

		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			photoView.setImageBitmap(bitmap);
			waitingDialog.dismiss();
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
		Intent myIntent = new Intent(this, MyMapActivity.class);
		myIntent.putExtra("actLatitude", actLatitude);
		myIntent.putExtra("actLongitude", actLongitude);
		myIntent.putExtra("resLatitude", restaurant.getLatitude());
		myIntent.putExtra("resLongitude", restaurant.getLongitude());
		startActivity(myIntent);
	}

}
