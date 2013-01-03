package de.hdm.foodfinder.client;

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

	
	

	// Getter
	public String getName() {
		return name;
	}
	
	public String getAddress(){
		return street + " " + streetNumber + "\n" + postcode + " " + city;
	}

	public String getDistance() {
		return distance;
	}

}
