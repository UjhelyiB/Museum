package hu.bme.museum.model;

import com.google.android.gms.maps.model.LatLng;

public class PieceOfArt {

    private String name;
    private String picture;
    private LatLng position;
    private String desc;

    public PieceOfArt(){

    }

    public PieceOfArt(String name, String picture, LatLng position, String desc) {
        this.name = name;
        this.picture = picture;
        this.position = position;
        this.desc = desc;
    }

    public LatLng getPosition(){
        return position;
    }
    public String getName(){
        return name;
    }

    public String getPicture() {
        return picture;
    }

    public String getDesc() { return desc; }
}
