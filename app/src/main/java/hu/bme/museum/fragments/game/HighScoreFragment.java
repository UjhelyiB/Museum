package hu.bme.museum.fragments.game;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Comparator;

import hu.bme.museum.R;
import hu.bme.museum.activities.ApplicationActivity;
import hu.bme.museum.activities.LoginActivity;
import hu.bme.museum.model.User;

public class HighScoreFragment extends Fragment {
    private ArrayList<User> userList = new ArrayList<User>();

    DatabaseReference databaseReference;

    LayoutInflater inflater;
    View rootView;
    LinearLayout highscoreLayout;
    RelativeLayout ownScoreLinearLayout;
    TextView ownName;
    TextView ownScore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_highscore, null);

        initUI();

        databaseReference = FirebaseDatabase.getInstance().getReference(ApplicationActivity.USERS);
        loadHighScoreFromDB();

        return rootView;
    }

    private void initUI() {
        highscoreLayout = (LinearLayout) rootView.findViewById(R.id.highScoreLinearLayout);
        ownScoreLinearLayout = (RelativeLayout) rootView.findViewById(R.id.ownScoreLinearLayout);
        ownName = (TextView) ownScoreLinearLayout.findViewById(R.id.highScoreOwnName);
        ownScore = (TextView) ownScoreLinearLayout.findViewById(R.id.highScoreOwnScore);
    }

    public void loadHighScoreFromDB(){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                highscoreLayout.removeAllViews();
                userList.clear();

                User user;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);
                    if(!userList.contains(user)){
                        userList.add(user);
                    }
                }

                //sorting the user list manually because the ArrayList.sort() requires API level 24
                for(int i=0; i< userList.size()-1; i++){
                    for(int j=1; j< userList.size(); j++){
                        User u1 = userList.get(j-1);
                        User u2 = userList.get(j);

                        int compare = compareUsers(u1, u2);
                        if (compare == -1) {
                            userList.remove(j-1);
                            userList.remove(j-1);
                            userList.add(j-1, u2);
                            userList.add(j, u1);
                        }
                    }

                }

                //at most top 10 users are shown
                int listLength;
                if(userList.size() >= 10){
                    listLength = 10;
                }else{
                    listLength = userList.size();
                }
                for(int i=0; i< listLength; i++){
                    View row = inflater.inflate(R.layout.highscore_row, null);

                    TextView tvName = (TextView) row.findViewById(R.id.highscoreRowName);
                    TextView tvScore = (TextView) row.findViewById(R.id.highscoreRowScore);

                    tvName.setText(userList.get(i).name);
                    tvScore.setText(String.valueOf(userList.get(i).score));

                    highscoreLayout.addView(row);
                }

                refreshOwnScoreInHighScore();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public int compareUsers(User u1, User u2){
        if(u1.score > u2.score){
            return 1;
        }else if(u1.score < u2.score){
            return -1;
        }else{
            return 0;
        }
    }

    public void refreshOwnScoreInHighScore(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.child(user.getUid()).getValue(User.class);
                ownName.setText(currentUser.name);
                ownScore.setText(String.valueOf(currentUser.score));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
