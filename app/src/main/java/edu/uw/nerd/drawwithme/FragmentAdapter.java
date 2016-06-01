package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by DANG on 5/23/2016.
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 2;
    public static final String TAG = "FRAG_ADAPTER";
    private String tabTitles[] = new String[] { "Gallery", "Inbox"};
    public FirebaseAuth mAuth;
    public FirebaseDatabase database;

    private Context context;
    private List<String> newMsg;
    private ChildEventListener childEventListener;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        newMsg = null;

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                DrawingItem drawingItem = dataSnapshot.getValue(DrawingItem.class);

//                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
//                    DrawingItem message = messageSnapshot.getValue(DrawingItem.class);
//                    Log.v(TAG, message.sender);
//                    newMsg.add(message.url);
//                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
            }
        };
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return GalleryFragment.newInstance(position + 1);
        } else {

            //database.getReference().child("inbox").addChildEventListener(childEventListener);

            Query temp = database.getReference().child("inbox")
                    .orderByChild("recipient")
                    .equalTo(mAuth.getCurrentUser().getUid());

            temp.addChildEventListener(childEventListener);

            temp.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                        DrawingItem message = messageSnapshot.getValue(DrawingItem.class);
                        Log.v(TAG, message.sender);
                        //TODO: DATA IS RETRIEVED HERE AND NEEDS TO POPULATE INBOX

                        newMsg.add(message.url);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            return InboxFragment.newInstance(HomeActivity.dir, newMsg);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }




}