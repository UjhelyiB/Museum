package hu.bme.museum.model.game;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import hu.bme.museum.R;
import hu.bme.museum.db.FirebaseAdapter;
import hu.bme.museum.fragments.map.MapFragment;

public class ShortAnswer extends Challenge {
    public String correctAnswer;
    public String question;

    @Override
    public void addQuestion(LinearLayout layout, LayoutInflater inflater, final Activity activity) {
        layout.removeAllViews();

        View shortAnswerView = inflater.inflate(R.layout.challenge_short_answer, null);

        TextView question = (TextView) shortAnswerView.findViewById(R.id.shortAnswerQuestion);
        final EditText editText = (EditText) shortAnswerView.findViewById(R.id.shortAnswerEditText);
        Button sendButton = (Button) shortAnswerView.findViewById(R.id.sendShortAnswerButton);

        question.setText(this.question);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean answerIsCorrect = true;

                if(editText.getText().toString().toLowerCase().equals(correctAnswer.toLowerCase())){
                    answerIsCorrect = true;
                }else{
                    answerIsCorrect = false;
                }

                sendAnswer(answerIsCorrect, activity);
            }
        });

        layout.addView(shortAnswerView, 0);
    }

    @Override
    public void sendAnswer(boolean answerIsCorrect, Activity activity) {
        FirebaseAdapter.getInstance().addQuizToUserCompletedQuizes(key);

        if(answerIsCorrect){
            Toast.makeText(activity, R.string.correct_answer, Toast.LENGTH_SHORT).show();

            FirebaseAdapter.getInstance().givePointToCurrentUser();
        }else{
            Toast.makeText(activity, R.string.wrong_answer, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setMapFragment(MapFragment mapFragment) {}
}
