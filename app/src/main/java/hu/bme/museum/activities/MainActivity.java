package hu.bme.museum.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import hu.bme.museum.R;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "MainActivity";

    private static FirebaseAuth firebaseAuth;
    private static FirebaseUser firebaseUser;

    //private static GoogleApiClient googleApiClient;

    private static String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLogin();
    }

    protected void checkLogin(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser == null) {
            // Not signed in, launch the LogIn activity
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);

            finish();
            return;
        } else {
            userName = firebaseUser.getDisplayName();

            startActivity(new Intent(MainActivity.this, ApplicationActivity.class));

            finish();
            return;

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

//    public static FirebaseAuth getFirebaseAuth() {
//        return firebaseAuth;
//    }
//
//    public static FirebaseUser getFirebaseUser() {
//        return firebaseUser;
//    }
//    public static GoogleApiClient getGoogleApiClient() {
//        return googleApiClient;
//    }

}
