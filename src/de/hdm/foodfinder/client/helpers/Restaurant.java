package de.hdm.foodfinder.client.helpers;

import com.google.gson.Gson;

/**
 * Restaurant
 * 
 * Clientseitige ReprŠesentation eines Restaurants Dient als POJO fŸr die
 * Deserialisierung aus JSON String
 * 
 * @author Max Batt
 * 
 */
public class Restaurant {
	private String id;
	private String ownerID;
	private String name;
	private String street;
	private String streetNumber;
	private String city;
	private String postcode;
	private String country;
	private String latitude;
	private String longitude;
	private String distance;

	// String Arrays fŸr die zusŠtzlichen RestaurantInfos
	private String[] dishes;
	private String[] regions;
	private String[] categories;
	private String[] photos;
	private String avgRating;

	// Getter
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDistance() {
		return distance;
	}

	public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public String getOwnerID() {
		return ownerID;
	}

	public String getAddress() {
		return street + " " + streetNumber + "\n" + postcode + " " + city;
	}

	public String getCountry() {
		return country;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getDishes() {
		return " - " + implode("\n - ", dishes);
	}

	public String getRegions() {
		return implode(", ", regions);
	}

	public String getCategories() {
		return implode(", ", categories);
	}

	public String getPhotos() {
		if (photos.length > 0) {
			return photos[0];
		} else
			return "";

	}

	public String getAvgRating() {
		return avgRating;
	}

	// Implodiert String Array
	public static String implode(String separator, String... data) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < data.length - 1; i++) {
			// data.length - 1 => to not add separator at the end
			if (!data[i].matches(" *")) {// empty string are ""; " "; "  "; and
											// so on
				sb.append(data[i]);
				sb.append(separator);
			}
		}
		sb.append(data[data.length - 1]);
		return sb.toString();
	}

	public String getJson() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		return json;
	}
}
