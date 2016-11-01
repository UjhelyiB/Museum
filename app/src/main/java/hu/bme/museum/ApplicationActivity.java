package hu.bme.museum;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Map;

public class ApplicationActivity extends AppCompatActivity {

    public static final String LOGIN_SUCCESSFUL = "Login successful!";

    Button btGame;
    Button btExhibitions;
    Button btMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        Toast.makeText(ApplicationActivity.this, LOGIN_SUCCESSFUL, Toast.LENGTH_SHORT).show();

        initUI();
    }

    private void initUI() {
        btGame = (Button) findViewById(R.id.btGame);
        btExhibitions = (Button) findViewById(R.id.btExhibitions);
        btMap = (Button) findViewById(R.id.btMap);

        btGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startGame();
            }
        });

        btExhibitions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startExhibitions();
            }
        });

        btMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMap();
            }
        });

    }

    public void startGame(){
        Intent intent = new Intent();
        intent.setClass(ApplicationActivity.this, GameActivity.class);
        startActivity(intent);
    }
    public void startExhibitions(){
        Intent intent = new Intent();
        intent.setClass(ApplicationActivity.this, ExhibitionsActivity.class);
        startActivity(intent);
    }

    public void startMap(){
        Intent intent = new Intent();
        intent.setClass(ApplicationActivity.this, MapActivity.class);
        startActivity(intent);
    }
}
