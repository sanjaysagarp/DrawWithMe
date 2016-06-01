package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignupActivity extends AppCompatActivity {

    private final String TAG = "Sign up";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signup = (Button) findViewById(R.id.signup1);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, "Play button clicked");
                Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(i);
                Toast.makeText(SignupActivity.this, "Sign up succesful!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
