package hu.bme.museum.fragments.artwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.model.Exhibition;

public class ExhibitionArtworkListFragment extends ArtworkListFragment {

    private Exhibition exhibition;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setArtworks(FirebaseAdapter.getInstance().getArtworksForExhibtition(exhibition.key, this));

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;

        setArtworkListTitle(exhibition.name);
    }
}