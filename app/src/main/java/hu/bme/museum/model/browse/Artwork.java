package hu.bme.museum.model.browse;

import com.google.android.gms.maps.model.LatLng;

public class Artwork {

    public String name;
    public String author;
    public String description;
    public String imageLink;
    public double lat;
    public double lng;
    public LatLng position;
    public String date;

    public Artwork() {

    }

    public Artwork(String name, String imageLink, LatLng position, String description, String author) {
        this.name = name;
        this.imageLink = imageLink;
        this.position = position;
        this.description = description;
        this.author = author;
    }

    public boolean containsQuery(String query) {
        for (String word : query.toLowerCase().split(" ")) {
            if (author != null && name != null && description != null) {
                if (author.toLowerCase().contains(word) ||
                        name.toLowerCase().contains(word) ||
                        description.toLowerCase().contains(word)) {
                    return true;
                }
            }
        }
        return false;
    }
}
