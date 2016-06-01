package edu.uw.nerd.drawwithme;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sanjaysagar on 6/1/16.
 */
public class DrawingItem {
    public String recipient;
    public String sender;
    public String title;
    public String url;

    public DrawingItem() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }
    public DrawingItem(String recipient, String sender, String title, String url) {
        this.recipient = recipient;
        this.sender = sender;
        this.title = title;
        this.url = url;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("recipient", recipient);
        result.put("sender", sender);
        result.put("title", title);
        result.put("url", url);

        return result;
    }
}
