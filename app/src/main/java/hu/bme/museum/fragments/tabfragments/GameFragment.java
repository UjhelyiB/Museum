package hu.bme.museum.fragments.tabfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hu.bme.museum.R;
import hu.bme.museum.model.Quiz;

public class GameFragment extends TabFragment {
    private static final String QUIZ = "quiz";
    private static final String CHALLENGES = "challenges";

    private LinearLayout gameLayout;
    private LayoutInflater inflater;
    private ArrayList<Quiz> quizList = new ArrayList<Quiz>();

    private DatabaseReference databaseReference;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_game, null, false);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.gameLinearLayout);

        loadQuizFromDB();

        return rootView;
    }

    @Override
    public String getTabTitle() {
        return "Game";
    }

    public void loadQuizFromDB(){
        databaseReference = FirebaseDatabase.getInstance().getReference(CHALLENGES).child(QUIZ);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Quiz currentQuiz = snapshot.getValue(Quiz.class);
                    if(!quizList.contains(currentQuiz)){
                        quizList.add(currentQuiz);
                    }
                }
                addQuestion(quizList.get(0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Error "," in loadQuizFromDB");
                Toast.makeText(getActivity(), "Error in loadQuizFromDB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addQuestion(Quiz quiz){
        View quizView = inflater.inflate(R.layout.quiz, null);

        TextView question= (TextView) quizView.findViewById(R.id.quizQuestion);
        Button answerA = (Button) quizView.findViewById(R.id.quizAnswerButtonA);
        Button answerB = (Button) quizView.findViewById(R.id.quizAnswerButtonB);
        Button answerC = (Button) quizView.findViewById(R.id.quizAnswerButtonC);
        Button answerD = (Button) quizView.findViewById(R.id.quizAnswerButtonD);

        question.setText(quiz.question);
        answerA.setText(quiz.A);
        answerB.setText(quiz.B);
        answerC.setText(quiz.C);
        answerD.setText(quiz.D);

        gameLayout.addView(quizView, 0);
    }

}