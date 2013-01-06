package de.hdm.foodfinder.client.helpers;

import java.util.ArrayList;

import de.hdm.foodfinder.client.R;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RestaurantArrayAdapter extends ArrayAdapter<Restaurant> {
	private final Context context;
	private final ArrayList<Restaurant> restaurants;

	private TextView name;
	private TextView address;
	private TextView distance;
	private TextView regions;

	public RestaurantArrayAdapter(Context context,
			ArrayList<Restaurant> restaurants) {
		super(context, R.layout.restaurant_row, restaurants);
		this.context = context;
		this.restaurants = restaurants;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.restaurant_row, parent, false);
		name = (TextView) rowView.findViewById(R.id.restaurantName);
		address = (TextView) rowView.findViewById(R.id.restaurantAddress);
		distance = (TextView) rowView.findViewById(R.id.restaurantDistance);
		regions = (TextView) rowView.findViewById(R.id.restaurantRegions);

		name.setText(restaurants.get(position).getName());
		address.setText(restaurants.get(position).getAddress());
		distance.setText(restaurants.get(position).getDistance());
		regions.setText(restaurants.get(position).getRegions());

		return rowView;

	}

}