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

import hu.bme.museum.R;
import hu.bme.museum.model.browse.Artwork;

public class ArtworkDetailsFragment extends Fragment {

    private LayoutInflater inflater;
    private Artwork artwork;

    public ImageView artworkPictureImageView;
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

        artworkPictureImageView = (ImageView) rootView.findViewById(R.id.artworkImageView);
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

        return rootView;
    }
}
