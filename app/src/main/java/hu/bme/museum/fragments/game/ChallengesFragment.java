package hu.bme.museum.fragments.game;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.model.Quiz;

public class ChallengesFragment extends Fragment {
    private static final String QUIZ = "quiz";
    private static final String CHALLENGES = "challenges";

    private boolean challengeAlreadyGenerated = false;

    private LayoutInflater inflater;
    private LinearLayout gameLayout;
    private Button highScoreButton;
    private Button newGameButton;

    private List<Quiz> quizList = new ArrayList<Quiz>();
    private List<String> alreadyAnsweredQuizKeysList = new ArrayList<>();

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

        alreadyAnsweredQuizKeysList = FirebaseAdapter.getInstance().getAlreadyAnsweredQuizKeysList(this);

        if(!challengeAlreadyGenerated){
            quizList = FirebaseAdapter.getInstance().getQuiz(this);
            challengeAlreadyGenerated = true;
        }

        return rootView;
    }

    public List<String> getAlreadyAnsweredQuizKeysList() {
        return alreadyAnsweredQuizKeysList;
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

    public void populateQuizes(){
        Random random = new Random();
        addQuestion(quizList.get(random.nextInt(quizList.size())));
    }

    public void addQuestion(final Quiz quiz){
        gameLayout.removeAllViews();

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

                    FirebaseAdapter.getInstance().givePointToCurrentUser();
                }else{
                    Toast.makeText(getActivity(), R.string.wrong_answer, Toast.LENGTH_SHORT).show();
                }
            }
        });

        gameLayout.addView(quizView, 0);
    }
}
