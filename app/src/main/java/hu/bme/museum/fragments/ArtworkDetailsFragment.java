package hu.bme.museum.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.R;
import hu.bme.museum.model.Artwork;

public class ArtworkDetailsFragment extends Fragment {

    private LayoutInflater inflater;
    private Artwork artwork;

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_artwork_details, null, false);

        return rootView;
    }
}
