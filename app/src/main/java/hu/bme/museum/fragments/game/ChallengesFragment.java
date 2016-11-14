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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

import hu.bme.museum.R;
import hu.bme.museum.activities.ApplicationActivity;
import hu.bme.museum.fragments.game.HighScoreFragment;
import hu.bme.museum.model.Quiz;
import hu.bme.museum.model.User;

public class ChallengesFragment extends Fragment {
    private static final String QUIZ = "quiz";
    private static final String CHALLENGES = "challenges";

    private boolean challengeAlreadyGenerated = false;

    LayoutInflater inflater;
    LinearLayout gameLayout;
    Button highScoreButton;
    Button newGameButton;

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
        newGameButton = (Button) rootView.findViewById(R.id.gameNewChallengeButton);

        setListeners();

        if(!challengeAlreadyGenerated){
            loadQuizFromDB();
            challengeAlreadyGenerated = true;
        }

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
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChallengesFragment challengesFragment = new ChallengesFragment();
                getFragmentManager().beginTransaction()
                        .replace(R.id.gameFragmentLinearLayout, challengesFragment).addToBackStack(null).commit();
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

                Random random = new Random();
                addQuestion(quizList.get(random.nextInt(quizList.size())));
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
        GameButton answerA = (GameButton) quizView.findViewById(R.id.quizAnswerButtonA);
        GameButton answerB = (GameButton) quizView.findViewById(R.id.quizAnswerButtonB);
        GameButton answerC = (GameButton) quizView.findViewById(R.id.quizAnswerButtonC);
        GameButton answerD = (GameButton) quizView.findViewById(R.id.quizAnswerButtonD);
        Button sendButton = (Button) quizView.findViewById(R.id.sendAnswerButton);

        question.setText(quiz.question);
        answerA.setText(quiz.A);
        answerB.setText(quiz.B);
        answerC.setText(quiz.C);
        answerD.setText(quiz.D);

        if(quiz.correctAnswer.equals("A")){
            answerA.setCorrect(true);
        }else if(quiz.correctAnswer.equals("B")){
            answerB.setCorrect(true);
        }else if(quiz.correctAnswer.equals("C")){
            answerC.setCorrect(true);
        }else{
            answerD.setCorrect(true);
        }

        final ArrayList<GameButton> gameButtons = new ArrayList<GameButton>();
        gameButtons.add(answerA);
        gameButtons.add(answerB);
        gameButtons.add(answerC);
        gameButtons.add(answerD);


        answerA.setOnClickListener(new AnswerButtonCheckChangedListener(answerA, quiz.A));
        answerB.setOnClickListener(new AnswerButtonCheckChangedListener(answerB, quiz.B));
        answerC.setOnClickListener(new AnswerButtonCheckChangedListener(answerC, quiz.C));
        answerD.setOnClickListener(new AnswerButtonCheckChangedListener(answerD, quiz.D));

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsCorrect = true;

                for(int i=0; i< gameButtons.size(); i++){
                    gameButtons.get(i).answerSent();
                    gameButtons.get(i).setEnabled(false);
                    if(!gameButtons.get(i).isCorrect && gameButtons.get(i).isChecked()){
                        answerIsCorrect = false;
                    }else if(gameButtons.get(i).isCorrect && !gameButtons.get(i).isChecked()){
                        answerIsCorrect = false;
                    }
                }

                if(answerIsCorrect){
                    Toast.makeText(getActivity(), R.string.correct_answer, Toast.LENGTH_SHORT).show();
                    givePointToUser(FirebaseDatabase.getInstance().getReference(ApplicationActivity.USERS), FirebaseAuth.getInstance().getCurrentUser());
                }else{
                    Toast.makeText(getActivity(), R.string.wrong_answer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        gameLayout.addView(quizView, 0);
    }

    public void givePointToUser(final DatabaseReference databaseReference, final FirebaseUser user){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User updatedUser = dataSnapshot.child(user.getUid()).getValue(User.class);
                updatedUser.score += 1;
                databaseReference.child(user.getUid()).setValue(updatedUser);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
