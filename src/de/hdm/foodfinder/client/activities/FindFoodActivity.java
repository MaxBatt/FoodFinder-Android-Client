package de.hdm.foodfinder.client.activities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import de.hdm.foodfinder.client.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FindFoodActivity extends Activity {

	private String serverUrl = "http://pfronhaus.dlinkddns.com:4567/restaurants?";

	private String latitude;
	private String longitude;

	private EditText etDishes;
	private CheckBox cb1;
	private CheckBox cb2;
	private CheckBox cb3;
	private CheckBox cb4;
	private CheckBox cb5;
	private CheckBox cb6;
	private CheckBox cb7;
	private SeekBar distanceSeeker;
	private TextView seekText;
	private Spinner regionSpinner;

	private final List<CheckBox> allCheckBoxes = new ArrayList<CheckBox>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_food);

		Bundle extras = getIntent().getExtras();

		latitude = extras.getString("actLatitude");
		longitude = extras.getString("actLongitude");

		etDishes = (EditText) findViewById(R.id.etDishes);
		cb1 = (CheckBox) findViewById(R.id.cb1);
		cb2 = (CheckBox) findViewById(R.id.cb2);
		cb3 = (CheckBox) findViewById(R.id.cb3);
		cb4 = (CheckBox) findViewById(R.id.cb4);
		cb5 = (CheckBox) findViewById(R.id.cb5);
		cb6 = (CheckBox) findViewById(R.id.cb6);
		cb7 = (CheckBox) findViewById(R.id.cb7);
		distanceSeeker = (SeekBar) findViewById(R.id.distanceSeeker);
		distanceSeeker.setProgress(5);
		seekText = (TextView) findViewById(R.id.seekText);
		regionSpinner = (Spinner) findViewById(R.id.regionSpinner);

		// Spinner aus Array bef�llen
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.regions_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSpinner.setAdapter(adapter);

		// IDs an Checkboxen vergeben und Checkboxen in Liste packen
		cb1.setId(1);
		cb2.setId(2);
		cb3.setId(3);
		cb4.setId(4);
		cb5.setId(5);
		cb6.setId(6);
		cb7.setId(7);

		allCheckBoxes.add(cb1);
		allCheckBoxes.add(cb2);
		allCheckBoxes.add(cb3);
		allCheckBoxes.add(cb4);
		allCheckBoxes.add(cb5);
		allCheckBoxes.add(cb6);
		allCheckBoxes.add(cb7);

		// OnClickListener f�r Dishes EditText
		etDishes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(FindFoodActivity.this,
						getString(R.string.dishes_toast), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 100);
				toast.show();
			}
		});

		// OnChange Listener f�r Umkreis Seekbar
		distanceSeeker
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub

						seekText.setText(String.valueOf(progress) + "km");
					}
				});

	}

	public void searchRestaurants(View view) {
		// Params, die an die URL geh�ngt werden
		String params = "";
		// JSON Helper
		Gson gson = new Gson();

		// Position an Params anh�ngen
		params += "latitude=" + latitude + "&longitude=" + longitude;

		// Gerichte auslesen
		String strDishes = etDishes.getText().toString();
		String[] dishes = new String[0];
		if (strDishes.length() > 0) {
			// In Array packen
			dishes = strDishes.split(",");
		}

		// In JSON umwandeln und an params anh�ngen
		params += "&dishes="
				+ gson.toJson(dishes).replace("\"", "'").replace(" ", "_");

		// Nationalit�t auslesen
		params += "&region="
				+ regionSpinner.getSelectedItem().toString().replace(" ", "_");

		// Kategorien-Liste leeren
		ArrayList<Integer> categories = new ArrayList<Integer>();
		// Kategorien auslesen und Liste packen
		for (CheckBox cb : allCheckBoxes) {
			if (cb.isChecked()) {
				categories.add(cb.getId());
			}
		}
		// Liste in JSON umwandeln und an Params h�ngen
		params += "&categories=" + gson.toJson(categories);

		// SeekBar Progress auslesen und an params anh�gen
		params += "&distance=" + String.valueOf(distanceSeeker.getProgress());

		/*
		 * Toast toast = Toast.makeText(FindFoodActivity.this, params,
		 * Toast.LENGTH_SHORT); toast.setGravity(Gravity.TOP, 0, 100);
		 * toast.show();
		 */
		SearchTask task = new SearchTask();
		task.execute(new String[] { serverUrl + params });
	}

	private class SearchTask extends AsyncTask<String, Void, String> {

		ProgressDialog waitingDialog = new ProgressDialog(FindFoodActivity.this);

		@Override
		protected void onPreExecute() {
			waitingDialog.setTitle(getString(R.string.searching_restaurants));
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

			waitingDialog.dismiss();
			Intent myIntent = new Intent(FindFoodActivity.this,
					RestaurantListActivity.class);
			myIntent.putExtra("json", result);
			myIntent.putExtra("actLatitude", latitude);
			myIntent.putExtra("actLongitude", longitude);
			startActivity(myIntent);

		}

		@Override
		protected void onCancelled() {
			Toast toast = Toast.makeText(FindFoodActivity.this,
					getString(R.string.err_no_connection), Toast.LENGTH_SHORT);
			toast.show();
		}
	}

}