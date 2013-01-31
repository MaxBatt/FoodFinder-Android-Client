package de.hdm.foodfinder.client;

import de.hdm.foodfinder.client.R;
import de.hdm.foodfinder.client.helpers.LocationHelper;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * MainActivity
 * 
 * Ermittelt die aktuelle Position und gibt sie an die FindFoodActivity weiter
 * 
 * @author Max Batt
 * 
 */
public class MainActivity extends Activity {

	private LocationHelper loc;
	private Button btnFindFood;
	// private Button btnAddRestaurant;
	private ProgressBar progressBar;
	private TextView msgView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnFindFood = (Button) findViewById(R.id.btnFindFood);
		// btnAddRestaurant = (Button) findViewById(R.id.btnAddRestaurant);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		msgView = (TextView) findViewById(R.id.msgView);

		// LocationHleper erstellen.
		loc = new LocationHelper(this);

		// FindFood Button deaktivieren, solang die Location nicht bekannt ist
		btnFindFood.setEnabled(false);

		// AsyncTask erstellen, um die aktuelle Position zu ermitteln
		LocationTask locTask = new LocationTask();
		locTask.execute(new Boolean[] { true });
	}

	/**
	 * LocationTask
	 * 
	 * AnsycTask für die Standortermittlung
	 * 
	 * @author Max Batt
	 * 
	 */
	class LocationTask extends AsyncTask<Boolean, Integer, Boolean> {

		// Vor dem Ausführen des Tasks LocationProvider ermitteln
		@Override
		protected void onPreExecute() {
			// Falls weder GPS noch Netzwerk verfügbar ist, Meldung geben
			if (!loc.locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER)
					&& !loc.locationManager
							.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				showDialog(getString(R.string.err_no_loc_provider));
			}
		}

		// Standort ermitteln
		@Override
		protected Boolean doInBackground(Boolean... params) {

			// Schleife läuft 30sek oder bis der Standort ermittelt ist
			long before = System.currentTimeMillis();
			while (loc.gotLocation() == false
					&& System.currentTimeMillis() < before + 30000) {

			}
			// Wenn Standort gefunden oder 30sek vorbei
			return true;

		}

		// Nach Ausführung des Tasks
		@Override
		protected void onPostExecute(Boolean result) {
			// Meldung, falls Standort nicht ermittelt werden konnte
			if (!loc.gotLocation()) {
				showDialog(getString(R.string.err_no_loc));
			} else {
				// FindFood Button aktivieren
				btnFindFood.setEnabled(true);
				// ProgressBar ausblenden
				progressBar.setVisibility(View.INVISIBLE);
				// MsgView updaten
				msgView.setText(getResources().getString(
						R.string.msgViewUpdated, loc.getStrLocation()));
			}
		}

		/**
		 * showDialog
		 * 
		 * @param msg
		 * 
		 * Gibt eine Meldung mit dem gegebenen String aus
		 * 
		 * Hat Abbrechen Button und Button, der zu den Standorteinstellungen fürht
		 */
		protected void showDialog(String msg) {
			new AlertDialog.Builder(MainActivity.this).setMessage(msg)

			// Standort-Button
					.setPositiveButton(getString(R.string.btn_settings),
					// Click Listener
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Dialog schließen
									dialog.cancel();
									// Standortzugriff aufrufen
									startActivity(new Intent(
											Settings.ACTION_LOCATION_SOURCE_SETTINGS));
								}
							})
					// Close-Button
					.setNegativeButton(getString(R.string.btn_close),
					// Click Listener
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// Dialog schließen
									dialog.cancel();
								}

								// Dialog einblenden
							}).create().show();
		}
	}

	// Bei Klick auf FindFood Button FindFoodActivity aufrufen
	// Intent-Extras: lat und long des aktuellen Standorts
	
	/**	
	 * findFood
	 * 
	 * wird bei Klick auf FindFood Button ausgeführt
	 */
	public void findFood(View view) {
		Intent myIntent = new Intent(this, FindFoodActivity.class);
		myIntent.putExtra("actLatitude", loc.getLat());
		myIntent.putExtra("actLongitude", loc.getLong());
		startActivity(myIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
}
