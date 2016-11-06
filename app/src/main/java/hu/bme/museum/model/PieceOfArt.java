package hu.bme.museum.model;

import com.google.android.gms.maps.model.LatLng;

public class PieceOfArt {
    private String title;
    private String picture;
    private LatLng position;
    private String description;

    public PieceOfArt(String title, String picture, LatLng position, String Description) {
        this.title = title;
        this.picture = picture;
        this.position = position;
        this.description = description;
    }

    public LatLng getPosition(){
        return position;
    }
    public String getTitle(){
        return title;
    }

    public String getPicture() {
        return picture;
    }

    public String getDescription() {
        return description;
    }
}
