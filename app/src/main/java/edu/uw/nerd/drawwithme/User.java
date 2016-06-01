package edu.uw.nerd.drawwithme;

import java.util.ArrayList;

/**
 * Created by sanjaysagar on 6/1/16.
 */
public class User {
    public String email;
    public ArrayList<DrawingItem> inbox;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String email) {
        this.email = email;
        this.inbox = new ArrayList<DrawingItem>();
    }
}
