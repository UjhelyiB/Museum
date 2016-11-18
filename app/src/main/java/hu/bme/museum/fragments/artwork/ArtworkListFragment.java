package hu.bme.museum.fragments.artwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.model.Artwork;
import hu.bme.museum.model.Exhibition;

public class ArtworkListFragment extends Fragment {

    private ProgressBar mProgressBar;

    //RecyclerView
    private RecyclerView artworkRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArtworkAdapter artworkAdapter;

    private List<Artwork> artworks;

    private Exhibition exhibition;
    private String title;

    private TextView artworkListTitleTextView;
    private int linearLayoutContainerId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_artwork_list, null, false);

        artworkListTitleTextView = (TextView) rootView.findViewById(R.id.artworkListTitleTextView);
        artworkListTitleTextView.setText(title);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        artworks = FirebaseAdapter.getFirebaseAdapter().getArtworksForExhibtition(exhibition.key,
                this);

        //RecyclerViewInit
        artworkRecyclerView = (RecyclerView) rootView.findViewById(R.id.artworkRecyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        artworkRecyclerView.setLayoutManager(linearLayoutManager);

        return rootView;
    }

    public void populateArtworks() {
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        artworkAdapter = new ArtworkAdapter(artworks, this, linearLayoutContainerId);
        artworkRecyclerView.setAdapter(artworkAdapter);
        artworkRecyclerView.scrollToPosition(0);
    }

    public void setExhibition(Exhibition exhibition) {
        this.exhibition = exhibition;
        title = exhibition.name;
    }

    public void setLinearLayoutContainerId(int id) {
        this.linearLayoutContainerId = id;
    }

//    public void setArtworks(List<Artwork> artworks) {
//        this.artworks = artworks;
//    }
//
//    public void setArtworkListTitle(String title) {
//        this.title = title;
//    }

}