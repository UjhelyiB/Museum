package hu.bme.museum.model.game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.game.AnswerButtonCheckChangedListener;
import hu.bme.museum.fragments.game.GameButton;
import hu.bme.museum.fragments.map.MapFragment;

public class Quiz extends Challenge {
    public String question;
    public String A;
    public String B;
    public String C;
    public String D;
    public String correctAnswer;


    @Override
    public void addQuestion(LinearLayout layout, LayoutInflater inflater, final Activity activity){
        layout.removeAllViews();

        View quizView = inflater.inflate(R.layout.challenge_quiz, null);

        TextView question= (TextView) quizView.findViewById(R.id.quizQuestion);
        GameButton answerA = (GameButton) quizView.findViewById(R.id.quizAnswerButtonA);
        GameButton answerB = (GameButton) quizView.findViewById(R.id.quizAnswerButtonB);
        GameButton answerC = (GameButton) quizView.findViewById(R.id.quizAnswerButtonC);
        GameButton answerD = (GameButton) quizView.findViewById(R.id.quizAnswerButtonD);
        Button sendButton = (Button) quizView.findViewById(R.id.sendQuizAnswerButton);

        question.setText(this.question);
        answerA.setText(this.A);
        answerB.setText(this.B);
        answerC.setText(this.C);
        answerD.setText(this.D);

        if(this.correctAnswer.equals("A")){
            answerA.setCorrect(true);
        }else if(this.correctAnswer.equals("B")){
            answerB.setCorrect(true);
        }else if(this.correctAnswer.equals("C")){
            answerC.setCorrect(true);
        }else{
            answerD.setCorrect(true);
        }

        final ArrayList<GameButton> gameButtons = new ArrayList<GameButton>();
        gameButtons.add(answerA);
        gameButtons.add(answerB);
        gameButtons.add(answerC);
        gameButtons.add(answerD);


        answerA.setOnClickListener(new AnswerButtonCheckChangedListener(answerA, this.A));
        answerB.setOnClickListener(new AnswerButtonCheckChangedListener(answerB, this.B));
        answerC.setOnClickListener(new AnswerButtonCheckChangedListener(answerC, this.C));
        answerD.setOnClickListener(new AnswerButtonCheckChangedListener(answerD, this.D));

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
                sendAnswer(answerIsCorrect, activity);
            }
        });

        layout.addView(quizView, 0);
    }

    @Override
    public void sendAnswer(boolean answerIsCorrect, Activity activity) {
        FirebaseAdapter.getInstance().addQuizToUserCompletedQuizes(key);
        if(answerIsCorrect){
            Toast.makeText(activity, R.string.correct_answer, Toast.LENGTH_SHORT).show();

            FirebaseAdapter.getInstance().givePointToCurrentUser(key);
        }else{
            Toast.makeText(activity, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setMapFragment(MapFragment mapFragment) {}
}
