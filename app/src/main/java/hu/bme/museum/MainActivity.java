package hu.bme.museum;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLogin();
    }


    protected void startLogin(){
        Intent intent = new Intent();

        // TODO add login support

        // Ignore login for now!
        intent.setClass(MainActivity.this, ApplicationActivity.class);
        startActivity(intent);
    }
}
