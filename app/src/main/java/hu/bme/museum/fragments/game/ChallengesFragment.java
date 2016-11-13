package hu.bme.museum.fragments.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import hu.bme.museum.R;
import hu.bme.museum.fragments.game.HighScoreFragment;
import hu.bme.museum.model.Quiz;

public class ChallengesFragment extends Fragment {
    private static final String QUIZ = "quiz";
    private static final String CHALLENGES = "challenges";

    LayoutInflater inflater;
    LinearLayout gameLayout;
    Button highScoreButton;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    ArrayList<Quiz> quizList = new ArrayList<Quiz>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        View rootView = inflater.inflate(R.layout.fragment_challenges, null, false);
        gameLayout = (LinearLayout) rootView.findViewById(R.id.gameLinearLayout);
        highScoreButton = (Button) rootView.findViewById(R.id.gameHighScoreButton);

        setListeners();

        loadQuizFromDB();

        return rootView;
    }

    public void setListeners(){
        highScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment highScoreFragment = new HighScoreFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.gameFragmentLinearLayout, highScoreFragment).addToBackStack(null).commit();

            }
        });
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

    public void addQuestion(final Quiz quiz){
        View quizView = inflater.inflate(R.layout.quiz, null);

        TextView question= (TextView) quizView.findViewById(R.id.quizQuestion);
        final ToggleButton answerA = (ToggleButton) quizView.findViewById(R.id.quizAnswerButtonA);
        ToggleButton answerB = (ToggleButton) quizView.findViewById(R.id.quizAnswerButtonB);
        ToggleButton answerC = (ToggleButton) quizView.findViewById(R.id.quizAnswerButtonC);
        ToggleButton answerD = (ToggleButton) quizView.findViewById(R.id.quizAnswerButtonD);

        question.setText(quiz.question);
        answerA.setText(quiz.A);
        answerB.setText(quiz.B);
        answerC.setText(quiz.C);
        answerD.setText(quiz.D);

        answerA.setOnClickListener(new AnswerButtonCheckChangedListener(answerA, quiz.A));
        answerB.setOnClickListener(new AnswerButtonCheckChangedListener(answerB, quiz.B));
        answerC.setOnClickListener(new AnswerButtonCheckChangedListener(answerC, quiz.C));
        answerD.setOnClickListener(new AnswerButtonCheckChangedListener(answerD, quiz.D));

        gameLayout.addView(quizView, 0);
    }

    public class AnswerButtonCheckChangedListener implements View.OnClickListener{

        String text;
        ToggleButton btn;

        AnswerButtonCheckChangedListener(ToggleButton btn, String text){
            this.text = text;
            this.btn = btn;
        }

        @Override
        public void onClick(View view) {
            if(btn.isChecked()){
                btn.setText(text);
                btn.setBackgroundResource(R.drawable.button_checked);

            }else{
                btn.setText(text);
                btn.setBackgroundResource(R.drawable.button_original);
            }
        }
    }
}
