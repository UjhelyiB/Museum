package hu.bme.museum;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class ApplicationActivity extends AppCompatActivity {

    public static final String LOGIN_SUCCESSFUL = "Login successful!";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);

        initUI();
    }

    private void initUI() {
        if( getIntent().getBooleanExtra(LoginActivity.KEY_TOP, false)){
            Toast.makeText(ApplicationActivity.this, LOGIN_SUCCESSFUL, Toast.LENGTH_SHORT).show();
        }
    }
}
