package de.foodfinder.client.helpers;

import java.util.ArrayList;

import de.hdm.foodfinder.client.R;
import de.hdm.foodfinder.client.R.id;
import de.hdm.foodfinder.client.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RestaurantArrayAdapter extends ArrayAdapter<Restaurant> {
  private final Context context;
  private final ArrayList<Restaurant> restaurants;

  public RestaurantArrayAdapter(Context context, ArrayList<Restaurant> restaurants) {
    super(context, R.layout.restaurant_row, restaurants);
    this.context = context;
    this.restaurants = restaurants;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
	  
    LayoutInflater inflater = (LayoutInflater) context
        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View rowView = inflater.inflate(R.layout.restaurant_row, parent, false);
    TextView restaurantName = (TextView) rowView.findViewById(R.id.restaurantName);
    TextView restaurantAddress = (TextView) rowView.findViewById(R.id.restaurantAddress);
    TextView restaurantDistance = (TextView) rowView.findViewById(R.id.restaurantDistance);
    
    restaurantName.setText(restaurants.get(position).getName());
    restaurantAddress.setText(restaurants.get(position).getAddress());
    restaurantDistance.setText(restaurants.get(position).getDistance());
    
    return rowView;
    
  }
} 