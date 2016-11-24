package hu.bme.museum.db;

import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import hu.bme.museum.activities.ApplicationActivity;
import hu.bme.museum.fragments.artwork.ArtworkListFragment;
import hu.bme.museum.fragments.browse.ExhibitionsFragment;
import hu.bme.museum.fragments.game.ChallengesFragment;
import hu.bme.museum.fragments.game.HighScoreFragment;
import hu.bme.museum.fragments.map.marker_clustering.MuseumClusterManager;
import hu.bme.museum.model.browse.Artwork;
import hu.bme.museum.model.game.Challenge;
import hu.bme.museum.model.browse.Exhibition;
import hu.bme.museum.model.game.Quiz;
import hu.bme.museum.model.User;
import hu.bme.museum.model.game.ShortAnswer;
import hu.bme.museum.model.game.VisitLocation;

public class FirebaseAdapter {

    private static final String LOG_TAG = "FirebaseAdapter";

    private static FirebaseAdapter instance = null;

    private DatabaseReference databaseReference;

    private static final String EXHIBTION_CHILD = "exhibitions";
    private static final String ARTWORK_CHILD = "artworks";
    private static final String CHALLENGES_CHILD = "challenges";
    private static final String QUIZ_CHILD = "quiz";
    private static final String SHORT_ANSWER_CHILD = "shortAnswer";
    private static final String VISIT_LOCATION_CHILD = "visitLocation";
    private static final String USERS_CHILD = "users";
    private static final String SCORE_CHILD = "score";
    private static final String LAST_ACTIVE = "lastActive";

    private boolean alreadyGreetedTheUser = false;

    private FirebaseAdapter() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public static FirebaseAdapter getInstance() {
        if (instance == null) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            instance = new FirebaseAdapter();

        }
        return instance;
    }

    public List<Exhibition> getExhibitions(final ExhibitionsFragment exhibitionsFragment) {

        final List<Exhibition> exhibitions = new ArrayList<>();

        databaseReference.child(EXHIBTION_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        exhibitions.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Exhibition exhibition = snapshot.getValue(Exhibition.class);

                            Log.w(LOG_TAG, "Found exhibition " + exhibition.name);

                            exhibition.key = snapshot.getKey();
                            if (!exhibitions.contains(exhibition)){
                                exhibitions.add(exhibition);
                            }
                        }

                        exhibitionsFragment.populateExhibitions();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Error ","in getExhibitions");
                    }
                });

        return exhibitions;
    }

    public List<Artwork> getArtworksForExhibition(String exhibitionKey,
                                                   final ArtworkListFragment artworkListFragment) {

        final List<Artwork> artworks = new ArrayList<>();

        databaseReference.child(EXHIBTION_CHILD).child(exhibitionKey).child(ARTWORK_CHILD)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        artworks.clear();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Artwork artwork = snapshot.getValue(Artwork.class);
                            artwork.position = new LatLng(artwork.lat, artwork.lng);

                            Log.w(LOG_TAG, "Found artwork " + artwork.name);

                            if (!artworks.contains(artwork)){
                                artworks.add(artwork);
                            }
                        }

                        artworkListFragment.populateArtworks();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Error ","in getArtworksForExhibtition");
                    }
                });

        return artworks;
    }

    public List<Artwork> getArtworksForSearchQuery(final String query,
                                                   final ArtworkListFragment artworkListFragment) {
        final List<Artwork> matchedArtworks = new ArrayList<>();

        databaseReference.child(EXHIBTION_CHILD).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        matchedArtworks.clear();

                        for (DataSnapshot exhibition : dataSnapshot.getChildren()) {
                            for (DataSnapshot snapshot : exhibition.child(ARTWORK_CHILD).getChildren()) {
                                Artwork artwork = snapshot.getValue(Artwork.class);

                                if (artwork.containsQuery(query)) {
                                    matchedArtworks.add(artwork);
                                }
                            }
                        }

                        artworkListFragment.populateArtworks();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("Error ","in getArtworksForSearchQuery");
                    }
                });

        return matchedArtworks;
    }

    public List<Challenge> getChallenge(final ChallengesFragment challengesFragment){
        final List<Challenge> quizes = new ArrayList<>();
        final List<Challenge> allQuizes = new ArrayList<>();

        getAlreadyAnsweredQuizKeysList(null);

        databaseReference.child(CHALLENGES_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quizes.clear();

                for(final DataSnapshot quizSnapshot : dataSnapshot.child(QUIZ_CHILD).getChildren()) {
                    final Quiz quiz = quizSnapshot.getValue(Quiz.class);
                    quiz.key = quizSnapshot.getKey();

                    allQuizes.add(quiz);
                    if(!challengesFragment.getAlreadyAnsweredQuizKeysList().contains(quizSnapshot.getKey())){
                        quizes.add(quiz);
                    }
                }

                for(final DataSnapshot shortAnswerSnapshot : dataSnapshot.child(SHORT_ANSWER_CHILD).getChildren()) {
                    final ShortAnswer shortAnswer = shortAnswerSnapshot.getValue(ShortAnswer.class);
                    shortAnswer.key = shortAnswerSnapshot.getKey();

                    allQuizes.add(shortAnswer);
                    if(!challengesFragment.getAlreadyAnsweredQuizKeysList().contains(shortAnswerSnapshot.getKey())){
                        quizes.add(shortAnswer);
                    }
                }

                for(final DataSnapshot visitLocationSnapshot : dataSnapshot.child(VISIT_LOCATION_CHILD).getChildren()) {
                    final VisitLocation visitLocation = visitLocationSnapshot.getValue(VisitLocation.class);
                    visitLocation.key = visitLocationSnapshot.getKey();
                    visitLocation.goalLocation = new Location("");
                    visitLocation.goalLocation.setLatitude(visitLocation.lat);
                    visitLocation.goalLocation.setLongitude(visitLocation.lng);

                    allQuizes.add(visitLocation);
                    if(!challengesFragment.getAlreadyAnsweredQuizKeysList().contains(visitLocationSnapshot.getKey())){
                        quizes.add(visitLocation);
                    }
                }

                if(quizes.isEmpty()){
                    quizes.addAll(allQuizes);

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    databaseReference.child(USERS_CHILD).child(currentUser.getUid()).child(CHALLENGES_CHILD).removeValue();
                }


                challengesFragment.populateQuizes();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error ","in getChallenge");
            }
        });

        return quizes;
    }

    public List<User> getUsers(final HighScoreFragment highscoreFragment){
        final List<User> users = new ArrayList<>();

        databaseReference.child(USERS_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();

                for(DataSnapshot quizSnapshot : dataSnapshot.getChildren()){
                    User user = quizSnapshot.getValue(User.class);
                    users.add(user);
                }

                highscoreFragment.populateHighscores();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error ","in getUsers");
            }
        });

        return users;
    }

    public void refreshCurrentUserData(final HighScoreFragment highScoreFragment){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(USERS_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.child(user.getUid()).getValue(User.class);

                highScoreFragment.refreshOwnScoreInHighScore(currentUser);

                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void givePointToCurrentUser(){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User updatedUser = dataSnapshot.child(currentUser.getUid()).getValue(User.class);
                updatedUser.score += 1;
                databaseReference.child(USERS_CHILD).child(currentUser.getUid()).child(SCORE_CHILD).setValue(updatedUser.score);

                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void addQuizToUserCompletedQuizes(final String key){
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(USERS_CHILD).child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                databaseReference.child(USERS_CHILD).child(currentUser.getUid()).child(CHALLENGES_CHILD).child(key).setValue(1);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public List<Artwork> getAllArtworksForClusterManager(final MuseumClusterManager museumClusterManager){
        final ArrayList<Artwork> artworks = new ArrayList<>();

        databaseReference.child(EXHIBTION_CHILD).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot exhibitionsSnapshot : dataSnapshot.getChildren()){
                    for(DataSnapshot concreteArtworks : exhibitionsSnapshot.child(ARTWORK_CHILD).getChildren()){
                        Artwork newArtwork = concreteArtworks.getValue(Artwork.class);
                        newArtwork.position = new LatLng(newArtwork.lat, newArtwork.lng);

                        artworks.add(newArtwork);
                    }
                }

                museumClusterManager.populateMuseumClusterManager();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return artworks;
    }

    public List<String> getAlreadyAnsweredQuizKeysList(final ChallengesFragment challengesFragment) {
        final ArrayList<String> alreadyAnsweredQuizes = new ArrayList<>();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference.child(USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(currentUser.getUid()).hasChild(CHALLENGES_CHILD)){
                    for(DataSnapshot completedChallengesSnapshot : dataSnapshot.child(currentUser.getUid()).child(CHALLENGES_CHILD).getChildren()){
                        alreadyAnsweredQuizes.add(completedChallengesSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return alreadyAnsweredQuizes;
    }

    public void addUserToDBIfItIsANewUser(final ApplicationActivity applicationActivity){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.getToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            databaseReference.child(USERS_CHILD).addListenerForSingleValueEvent(new ValueEventListener() {

                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User currentUser = new User();
                                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
//                                        //for some reason, at some time, Firebase could not convert properly its data into the class
//                                        //the error was: com.google.firebase.database.DatabaseException: Failed to convert a value of type java.lang.String to int
//                                        //so I've put in this code to fix it but somehow it works now, so I just commented it out
//
//                                        currentUser.email = (String) snapshot.child("email").getValue();
//                                        currentUser.name = (String) snapshot.child("name").getValue();
//                                        currentUser.imageLink = (String) snapshot.child("imageLink").getValue();
//                                        currentUser.lastActive = (long) snapshot.child("lastActive").getValue();
//                                        currentUser.score = Integer.parseInt(String.valueOf(snapshot.child("score").getValue()));

                                        currentUser = snapshot.getValue(User.class);
                                        if(currentUser.email.equals(user.getEmail())){
                                            databaseReference.child(USERS_CHILD).child(user.getUid())
                                                    .child(LAST_ACTIVE).setValue(System.currentTimeMillis()/1000);

                                            return;
                                        }
                                    }
                                    currentUser.email = user.getEmail();
                                    currentUser.name = user.getDisplayName();
                                    currentUser.score = 0;
                                    currentUser.lastActive = System.currentTimeMillis()/1000;
                                    databaseReference.child(USERS_CHILD).child(user.getUid()).setValue(currentUser);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        } else {
                            Log.d("Error "," in LoginCompleteListener");
                            Toast.makeText(applicationActivity.getBaseContext(), "Error in LoginCompleteListener", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
