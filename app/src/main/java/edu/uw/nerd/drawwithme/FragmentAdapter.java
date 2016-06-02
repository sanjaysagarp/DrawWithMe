package edu.uw.nerd.drawwithme;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    private FragmentManager fragManager;

    private Context context;
    private List<String> newMsg;
    private ChildEventListener childEventListener;

    public FragmentAdapter(FragmentManager fm, Context context) {
        super(fm);

        fragManager = fm;

        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        newMsg = new ArrayList<String>();

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
            return InboxFragment.newInstance(HomeActivity.dir, newMsg);
        }
    }

    // updates the content of the inbox with new inputs when changed
    public void update(List<String> updatedMsgs) {
        List<Fragment> ls = fragManager.getFragments();
        if (ls != null) {
            InboxFragment inbox = (InboxFragment) ls.get(0);

            if (inbox != null) {
                newMsg.clear();
                for (int i = 0; i < updatedMsgs.size(); i++) {
                    newMsg.add(updatedMsgs.get(i));
                }
                inbox.update(newMsg);
            }
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }




}