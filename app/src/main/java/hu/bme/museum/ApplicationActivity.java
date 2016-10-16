package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ApplicationActivity extends AppCompatActivity {

    public static final String LOGIN_SUCCESSFUL = "Login successful!";
    public static final String PLEASE_LOG_IN_TO_PLAY_THE_GAME = "Please log in to play the game!";

    Button btGame;
    Button btExhibitions;
    Button btMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        if( getIntent().getBooleanExtra(LoginActivity.KEY_TOP, false)){
            Toast.makeText(ApplicationActivity.this, LOGIN_SUCCESSFUL, Toast.LENGTH_SHORT).show();
        }

        initUI();
    }

    private void initUI() {
        btGame = (Button) findViewById(R.id.btGame);
        btExhibitions = (Button) findViewById(R.id.btExhibitions);
        btMap = (Button) findViewById(R.id.btMap);

        btGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!getIntent().getBooleanExtra(LoginActivity.KEY_TOP, false)){
                    Toast.makeText(ApplicationActivity.this, PLEASE_LOG_IN_TO_PLAY_THE_GAME, Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });

    }
}
