package de.hdm.foodfinder.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;



import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
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
	private Button btnSearch;

	private ArrayList<Integer> categories = new ArrayList<Integer>();
	private ArrayList<String> dishes = new ArrayList<String>();
	private final List<CheckBox> allCheckBoxes = new ArrayList<CheckBox>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.find_food);

		Bundle extras = getIntent().getExtras();

		latitude = extras.getString("latitude");
		longitude = extras.getString("longitude");

		etDishes = (EditText) findViewById(R.id.etDishes);
		cb1 = (CheckBox) findViewById(R.id.cb1);
		cb2 = (CheckBox) findViewById(R.id.cb2);
		cb3 = (CheckBox) findViewById(R.id.cb3);
		cb4 = (CheckBox) findViewById(R.id.cb4);
		cb5 = (CheckBox) findViewById(R.id.cb5);
		cb6 = (CheckBox) findViewById(R.id.cb6);
		cb7 = (CheckBox) findViewById(R.id.cb7);
		distanceSeeker = (SeekBar) findViewById(R.id.distanceSeeker);
		regionSpinner = (Spinner) findViewById(R.id.regionSpinner);
		btnSearch = (Button) findViewById(R.id.btnSearch);

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

		// OnClickListener für Dishes EditText
		etDishes.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast toast = Toast.makeText(FindFoodActivity.this,
						getString(R.string.dishes_toast), Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 0, 100);
				toast.show();
			}
		});

	}
	
	public void searchRestaurants(View view) {
		
		
		
		SearchTask task = new SearchTask();
		task.execute(new String[] { serverUrl });
	}
	
	
	private class SearchTask extends AsyncTask<String, Void, String> {
		@Override
		protected String doInBackground(String... urls) {
			String response = "";
			for (String url : urls) {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				try {
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
				}
			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast toast = Toast.makeText(FindFoodActivity.this,
					result, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.TOP, 0, 100);
			toast.show();
		}
	}
	
	
}
