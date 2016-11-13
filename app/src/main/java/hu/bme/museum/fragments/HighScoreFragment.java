package hu.bme.museum.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;

import hu.bme.museum.R;
import hu.bme.museum.activities.ApplicationActivity;
import hu.bme.museum.activities.LoginActivity;
import hu.bme.museum.model.User;

public class HighScoreFragment extends Fragment {
    private ArrayList<User> userList = new ArrayList<User>();

    DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_highscore, null);

        return rootView;
    }

    public void loadHighScoreFromDB(){
        databaseReference = FirebaseDatabase.getInstance().getReference(ApplicationActivity.USERS);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user;
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    user = snapshot.getValue(User.class);
                    if(!userList.contains(user)){

                        //sorting the user list manually because the ArrayList.sort() requires API level 24
                        if(userList.size() == 0) {
                            userList.add(user);
                        }else if(userList.size() == 1){
                            if(userList.get(0).score < user.score){
                                userList.add(0, user);
                            }else{
                                userList.add(1, user);
                            }
                        } else {
                            for(int i=0; i< userList.size(); i++){
                                if(i == 0){
                                    if(user.score < userList.get(i+1).score){
                                        userList.add(0, user);
                                        break;
                                    }
                                }else if(i == userList.size()-1){
                                    if(user.score > userList.get(i+1).score){
                                        userList.add(i, user);
                                        break;
                                    }
                                } else if(user.score < userList.get(i-1).score && user.score < userList.get(i+1).score){
                                    userList.add(i, user);
                                }
                            }
                        }
                    }
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
