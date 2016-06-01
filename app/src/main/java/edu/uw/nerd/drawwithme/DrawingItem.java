package edu.uw.nerd.drawwithme;

/**
 * Created by sanjaysagar on 6/1/16.
 */
public class DrawingItem {
    public String recipient;
    public String sender;
    public String title;
    public String url;

    public DrawingItem(String recipient, String sender, String title, String url) {
        this.recipient = recipient;
        this.sender = sender;
        this.title = title;
        this.url = url;
    }
}
