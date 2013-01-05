package de.foodfinder.client.helpers;


public class RestaurantInfos {
	private String[] dishes;
	private String[] regions;
	private String[] categories;
	private String[] photos;
	private String avgRating;
	
	
	
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
		if(photos.length > 0){
			return photos[0];
		}
		else return "";
		
	}
	
	public String getAvgRating() {
		return avgRating;
	}
	
	public static String implode(String separator, String... data) {
	    StringBuilder sb = new StringBuilder();
	    for (int i = 0; i < data.length - 1; i++) {
	    //data.length - 1 => to not add separator at the end
	        if (!data[i].matches(" *")) {//empty string are ""; " "; "  "; and so on
	            sb.append(data[i]);
	            sb.append(separator);
	        }
	    }
	    sb.append(data[data.length - 1]);
	    return sb.toString();
	}
	
}


