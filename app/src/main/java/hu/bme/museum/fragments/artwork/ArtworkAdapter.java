package hu.bme.museum.fragments.artwork;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import hu.bme.museum.R;
import hu.bme.museum.model.browse.Artwork;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ViewHolder> {

    private List<Artwork> artworks;

    private Fragment fragment;
    private int linearLayoutContainerId;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView artworkCardView;

        public ImageView artworkPicture;
        public TextView artworkTitle;
        public TextView artworkAuthorTextView;
        public TextView artworkDate;

        public ViewHolder(View v) {
            super(v);

            artworkCardView = (CardView) v.findViewById(R.id.artworkCardView);

            artworkPicture = (ImageView) v.findViewById(R.id.artworkImageView);
            artworkTitle = (TextView) v.findViewById(R.id.artworkTitleTextView);
            artworkAuthorTextView = (TextView) v.findViewById(R.id.artworkArtistNameTextView);
            artworkDate = (TextView) v.findViewById(R.id.artworkDateTextView);
        }
    }

    public ArtworkAdapter(List<Artwork> artworks, Fragment fragment, int linearLayoutContainerId) {
        this.artworks = artworks;
        this.fragment = fragment;
        this.linearLayoutContainerId = linearLayoutContainerId;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtworkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artwork_cardview, parent, false);

        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.artworkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArtworkDetailsFragment artworkDetailsFragment =
                        new ArtworkDetailsFragment();
                artworkDetailsFragment.setArtwork(artworks.get(position));

                fragment.getFragmentManager().beginTransaction()
                        .replace(linearLayoutContainerId, artworkDetailsFragment)
                        .addToBackStack(null).commit();
            }
        });

        holder.artworkTitle.setText(artworks.get(position).name);
        holder.artworkAuthorTextView.setText(artworks.get(position).author);
        holder.artworkDate.setText(artworks.get(position).date);

        if (artworks.get(position).imageLink == null) {
            holder.artworkPicture
                    .setImageDrawable(
                            ContextCompat.getDrawable(fragment.getContext(),
                                    R.mipmap.artwork_placeholder));
        } else {
            Glide.with(fragment.getContext())
                    .load(artworks.get(position).imageLink)
                    .into(holder.artworkPicture);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return artworks.size();
    }
}