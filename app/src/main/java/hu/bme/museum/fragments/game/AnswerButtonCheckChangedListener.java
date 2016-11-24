package hu.bme.museum.fragments.game;

import android.view.View;
import android.widget.ToggleButton;

import hu.bme.museum.R;

public class AnswerButtonCheckChangedListener implements View.OnClickListener{

        private String text;
        private ToggleButton btn;

        public AnswerButtonCheckChangedListener(ToggleButton btn, String text){
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