package edu.uw.nerd.drawwithme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class DetailActivity extends AppCompatActivity {

    public static final String IMAGE_INTENT = "IMAGE INTENT";
//    private Uri image;
    private Integer image;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // TODO: ideally should be Uri but issue with DrawingSurfaceView results in temp files...
//        Uri image = null;
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if(extras == null) {
//                image = null;
//            } else {
//                image = (Uri) extras.get(InboxFragment.DETAIL_INTENT);
//            }
//        } else {
//            image = (Uri) savedInstanceState.getSerializable(InboxFragment.DETAIL_INTENT);
//        }
//
//        ImageView img = (ImageView) findViewById(R.id.drawing_detail);
//        img.setImageURI(image);

        image = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                image = null;
            } else {
                if(extras.get(InboxFragment.DETAIL_INTENT) instanceof Integer) {
                    image = (Integer) extras.get(InboxFragment.DETAIL_INTENT);
                }
                else{
                    file = (File)extras.get(InboxFragment.DETAIL_INTENT);
                }
            }
        } else {
            if(savedInstanceState.getSerializable(InboxFragment.DETAIL_INTENT) instanceof Integer) {
                image = (Integer) savedInstanceState.getSerializable(InboxFragment.DETAIL_INTENT);
            }
            else{
                file = (File)savedInstanceState.getSerializable(InboxFragment.DETAIL_INTENT);
            }
        }

        ImageView img = (ImageView) findViewById(R.id.drawing_detail);
        if(image != null) {
            img.setImageResource(image);
        }
        else{
            if(file!=null) {
                img.setImageURI(Uri.parse(file.getAbsolutePath()));
                TextView text = (TextView)findViewById(R.id.title_text);
                text.setText(file.getName());
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit:
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra(IMAGE_INTENT, image);
                intent.putExtra("URI", file.getAbsolutePath());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
