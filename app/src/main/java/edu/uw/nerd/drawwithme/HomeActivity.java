package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by DANG on 5/23/2016.
 */
public class HomeActivity extends AppCompatActivity {
    public static final String FILE_INTENT = "DIRECTORY";
    public static File dir;

    public FirebaseAuth mAuth;

    public static List<String> allMsg;

    public FirebaseAuth.AuthStateListener mAuthListener;
    public FirebaseDatabase database;
    private String TAG = "HomeActivity";

    private FragmentAdapter fragmentAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        allMsg = new ArrayList<String>();

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), HomeActivity.this);

        viewPager.setAdapter(fragmentAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        // getexternalstoragepublicdirectory
        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File root = new File(dir, "Inbox");
        if(!root.exists()){
            root.mkdirs();
        }
        dir = root;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };


        Query temp = database.getReference().child("inbox")
                .orderByChild("recipient")
                .equalTo(mAuth.getCurrentUser().getUid());

       // temp.addChildEventListener(childEventListener);

        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    DrawingItem message = messageSnapshot.getValue(DrawingItem.class);
                    Log.v(TAG, message.sender);

                    //TODO: DATA IS RETRIEVED HERE AND NEEDS TO POPULATE INBOX
//                    allMsg.add(message.url);
                    allMsg.add("https://api.imgur.com/3/image/I5yPFUD");

                    Log.v(TAG, message.url);


                }
                // have some sort of call here right after the data updates
                fragmentAdapter.get
                fragmentAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void initiateDraw(View v) {
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        intent.putExtra(FILE_INTENT, dir);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_logout:
                mAuth.signOut();
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
