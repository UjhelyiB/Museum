package hu.bme.museum.fragments.artwork;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flaviofaria.kenburnsview.KenBurnsView;

import hu.bme.museum.R;
import hu.bme.museum.fragments.TabChildFragment;
import hu.bme.museum.model.browse.Artwork;

public class ArtworkDetailsFragment extends TabChildFragment {

    private boolean imagePaused = false;

    private LayoutInflater inflater;
    private Artwork artwork;

    public KenBurnsView artworkPictureImageView;
    public TextView artworkTitleTextView;
    public TextView artworkAuthorTextView;
    public TextView artworkDateTextView;
    public TextView artworkDescriptionTextView;

    public void setArtwork(Artwork artwork) {
        this.artwork = artwork;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_artwork_details, null, false);

        artworkPictureImageView = (KenBurnsView) rootView.findViewById(R.id.artworkImageView);
        artworkTitleTextView = (TextView) rootView.findViewById(R.id.artworkTitleTextView);
        artworkAuthorTextView = (TextView) rootView.findViewById(R.id.artworkArtistNameTextView);
        artworkDateTextView = (TextView) rootView.findViewById(R.id.artworkDateTextView);
        artworkDescriptionTextView = (TextView) rootView.findViewById(R.id.artworkDescriptionTextView);

        artworkTitleTextView.setText(artwork.name);
        artworkAuthorTextView.setText(artwork.author);
        artworkDateTextView.setText(artwork.date);
        artworkDescriptionTextView.setText(artwork.description);

        if (artwork.imageLink == null) {
            artworkPictureImageView
                .setImageDrawable(
                        ContextCompat.getDrawable(getContext(),
                                R.mipmap.artwork_placeholder));
        } else {
            Glide.with(getContext())
                .load(artwork.imageLink)
                .into(artworkPictureImageView);
        }

        artworkPictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imagePaused) {
                    artworkPictureImageView.resume();
                } else {
                    artworkPictureImageView.pause();
                }
                imagePaused = !imagePaused;
            }
        });

        return rootView;
    }
}
