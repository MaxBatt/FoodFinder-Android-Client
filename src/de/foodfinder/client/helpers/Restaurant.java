package de.foodfinder.client.helpers;

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
	
	
	
	public String getAddress(){
		return street + " " + streetNumber + "\n" + postcode + " " + city;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

}
