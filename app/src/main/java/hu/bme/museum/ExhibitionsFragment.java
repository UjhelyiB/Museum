package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DatabaseReference;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

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


        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_exhibitions, null, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);

        //RecyclerViewInit
        artworkRecyclerView = (RecyclerView) rootView.findViewById(R.id.artworkRecyclerView);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setStackFromEnd(true);
        artworkRecyclerView.setLayoutManager(linearLayoutManager);

        setUpFirebase();

        return rootView;
    }

    public void setUpFirebase(){
        // New child entries
        databaseReference = FirebaseDatabase.getInstance().getReference();


        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PieceOfArt, MessageViewHolder>(
                PieceOfArt.class,
                R.layout.artwork_cardview,
                MessageViewHolder.class,
                databaseReference.child(ARTWORK_CHILD)) {

            @Override
            protected void populateViewHolder(MessageViewHolder viewHolder, PieceOfArt artwork, int position) {
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                viewHolder.artworkCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "View Artwork Details", Toast.LENGTH_SHORT).show();
                    }
                });

                viewHolder.artworkTitle.setText(artwork.name);
                viewHolder.artworkAuthorTextView.setText(artwork.author);
                viewHolder.artworkDate.setText(artwork.date);

                if (artwork.imageLink == null) {
                    viewHolder.artworkPicture
                            .setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.artwork_placeholder));
                } else {
                    Glide.with(getContext())
                            .load(artwork.imageLink)
                            .into(viewHolder.artworkPicture);
                }
            }

        };

        firebaseRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                artworkRecyclerView.scrollToPosition(0);
            }
        });

        artworkRecyclerView.setLayoutManager(linearLayoutManager);
        artworkRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public String getTabTitle() {
        return "Browse";
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public CardView artworkCardView;

        public ImageView artworkPicture;
        public TextView artworkTitle;
        public TextView artworkAuthorTextView;
        public TextView artworkDate;

        public MessageViewHolder(View v) {
            super(v);
            artworkCardView = (CardView) v.findViewById(R.id.artworkCardView);

            artworkPicture = (ImageView) v.findViewById(R.id.artworkImageView);
            artworkTitle = (TextView) v.findViewById(R.id.artworkTitleTextView);
            artworkAuthorTextView = (TextView) v.findViewById(R.id.artworkArtistNameTextView);
            artworkDate = (TextView) v.findViewById(R.id.artworkDateTextView);

        }
    }
}