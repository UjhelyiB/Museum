package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hu.bme.museum.model.PieceOfArt;

public class ExhibitionsFragment extends TabFragment {

    private static final String ARTWORK_CHILD = "artworks";
    LinearLayout artworksLinearLayout;
    private ProgressBar mProgressBar;

    //RecyclerView
    private RecyclerView artworkRecyclerView;
    private LinearLayoutManager linearLayoutManager;

    LayoutInflater inflater;

    //Firebase
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter<PieceOfArt, MessageViewHolder> firebaseRecyclerAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mProgressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_exhibitions, null, false);

        //LinearLayout
        //artworksLinearLayout = (LinearLayout) rootView.findViewById(R.id.artworksLinearLayout);

        //RecyclerViewInit
        artworkRecyclerView = (RecyclerView) rootView.findViewById(R.id.artworkRecyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        artworkRecyclerView.setLayoutManager(linearLayoutManager);

        setUpFirebase();

        //getArtworksTheSimpleWay();

        //addArtworks();

        return rootView;
    }

    public void addArtworks() {
        for (int i = 0; i < 3; ++i) {
            View newArtwork = inflater.inflate(R.layout.artwork, null);
            TextView title = (TextView) newArtwork.findViewById(R.id.artworkTitleTextView);
            title.setText(title.getText() + " " + (i+1));
            artworksLinearLayout.addView(newArtwork);
        }
    }

    public void setUpFirebase(){
        // New child entries
        databaseReference = FirebaseDatabase.getInstance().getReference();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PieceOfArt, MessageViewHolder>(
                PieceOfArt.class,
                R.layout.artwork,
                MessageViewHolder.class,
                databaseReference.child(ARTWORK_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, PieceOfArt artwork, int position) {
                Log.d("xy", "asdfsd");
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                viewHolder.artworkTitle.setText(artwork.getName());
                viewHolder.artworkDescription.setText(artwork.getDesc());
                if (artwork.getPicture() == null) {
                    viewHolder.artworkPicture
                            .setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.artwork_placeholder));
                } else {
                    Glide.with(getContext())
                            .load(artwork.getPicture())
                            .into(viewHolder.artworkPicture);
                }
                String s = databaseReference.child("artworks").child("example_artwork_id").child("name").getKey();
                String desc = databaseReference.child("artworks").child("example_artwork_id").child("desc").getKey();

                View newArtwork = inflater.inflate(R.layout.artwork, null);
                TextView title = (TextView) newArtwork.findViewById(R.id.artworkTitleTextView);
                title.setText(s);
                TextView tvDesc = (TextView) newArtwork.findViewById(R.id.artworkDescriptionTextView);
                tvDesc.setText(desc);
                artworksLinearLayout.addView(newArtwork);

            }

        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int friendlyMessageCount = firebaseRecyclerAdapter.getItemCount();
                int lastVisiblePosition =
                        linearLayoutManager.findLastCompletelyVisibleItemPosition();
                // If the recycler view is initially being loaded or the
                // user is at the bottom of the list, scroll to the bottom
                // of the list to show the newly added message.
                if (lastVisiblePosition == -1 ||
                        (positionStart >= (friendlyMessageCount - 1) &&
                                lastVisiblePosition == (positionStart - 1))) {
                    artworkRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        artworkRecyclerView.setLayoutManager(linearLayoutManager);
        artworkRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public void getArtworksTheSimpleWay(){

        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("artworks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("xy", "asdfsd");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    PieceOfArt artwork = postSnapshot.getValue(PieceOfArt.class);

                    View newArtwork = inflater.inflate(R.layout.artwork, null);
                    TextView title = (TextView) newArtwork.findViewById(R.id.artworkTitleTextView);
                    title.setText(artwork.getName());
                    TextView tvDesc = (TextView) newArtwork.findViewById(R.id.artworkDescriptionTextView);
                    tvDesc.setText(artwork.getDesc());
                    artworksLinearLayout.addView(newArtwork);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        String s = databaseReference.child("artworks").child("example_artwork_id").child("name").getKey();
//        String desc = databaseReference.child("artworks").child("example_artwork_id").child("desc").getKey();
//
//        View newArtwork = inflater.inflate(R.layout.artwork, null);
//        TextView title = (TextView) newArtwork.findViewById(R.id.artworkTitleTextView);
//        title.setText(s);
//        TextView tvDesc = (TextView) newArtwork.findViewById(R.id.artworkDescriptionTextView);
//        tvDesc.setText(desc);
//        artworksLinearLayout.addView(newArtwork);
    }

    @Override
    public String getTabTitle() {
        return "Browse";
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public ImageView artworkPicture;
        public TextView artworkTitle;
        public TextView artworkDescription;

        public MessageViewHolder(View v) {
            super(v);
            artworkPicture = (ImageView) v.findViewById(R.id.artworkImageView);
            artworkTitle = (TextView) v.findViewById(R.id.artworkTitleTextView);
            artworkDescription = (TextView) v.findViewById(R.id.artworkDescriptionTextView);

        }
    }
}