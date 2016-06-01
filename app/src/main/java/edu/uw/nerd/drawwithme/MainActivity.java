package edu.uw.nerd.drawwithme;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import com.android.colorpicker.ColorPickerDialog;
import com.android.colorpicker.ColorPickerSwatch;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements DrawingSurfaceView.getDrawing,
        NumberPicker.OnValueChangeListener {
    private static final String TAG = "Main";

    private DrawingSurfaceView view;
    private float x, y;
    private GestureDetector detector;
    private ArrayList<Line> drawing;
    private Line tempLine;
    private int colorLine;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private FirebaseAuth.AuthStateListener mAuthListener;
    public static File dir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
            }
        };

//        dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File root = new File(dir, "Draw With Me");
//        if(!root.exists()){
//            root.mkdirs();
//        }
//        dir = root;

        String backgroundImage = null;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                checkDir(dir);
            } else {
                dir = (File) extras.get(HomeActivity.FILE_INTENT);
                backgroundImage = (String)extras.get("URI");
            }
        } else {
            dir = (File) savedInstanceState.getSerializable(HomeActivity.FILE_INTENT);
        }

        dir = checkDir(dir);
        ///storage/sdcard/Android/data/edu.uw.nerd.drawwithme/files/Pictures/Draw With Me/Draw With Me
        view = (DrawingSurfaceView) findViewById(R.id.drawingView);
        if(backgroundImage!=null) {
            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(new File(backgroundImage)));
                view.setBmp(b);
                ((EditText)findViewById(R.id.title)).setText((new File(backgroundImage)).getName());
//                Drawable drawable = new BitmapDrawable(getResources(), b);
//                view.setBackground(drawable);
            }
            catch(FileNotFoundException fe){
                Log.v(TAG, "File not found");
            }
        }


        detector = new GestureDetector(this, new MyGestureListener());
        tempLine = new Line();
        drawing = new ArrayList<Line>();

        x = view.pointer.getX();
        y = view.pointer.getY();

        ObjectAnimator xAnim = ObjectAnimator.ofFloat(view.pointer, "x", x);
        xAnim.setDuration(2000);
        ObjectAnimator yAnim = ObjectAnimator.ofFloat(view.pointer, "y", y);
        yAnim.setDuration(3000);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(yAnim, xAnim);
        set.start();

        View changeColor = findViewById(R.id.pen);
        View changeBackground = findViewById(R.id.background);
        changeColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showColourPicker(v);
            }
        });
        changeBackground.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.canvas_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveCanvas();
                return true;
            case R.id.send_btn:
                //FirebaseDatabase database = FirebaseDatabase.getInstance();
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.select_recipient, null);
        dialogBuilder.setView(dialogView);

        final EditText title = (EditText) dialogView.findViewById(R.id.title_msg);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit_recipient);

        dialogBuilder.setTitle("Send your image!");
        dialogBuilder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                //TODO: NEED TO CHECK IF EMAIL EXISTS IN DB?
                //TODO: NEED TO UPLOAD TO IMGUR AND SET URL
                user = FirebaseAuth.getInstance().getCurrentUser();

                String URL = "http://imgur.com/I5yPFUD";
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                String recipient = edt.getText().toString();
                DrawingItem item = new DrawingItem(edt.getText().toString(), user.getEmail(), title.getText().toString(), URL);

                //need user id
                String key = database.getReference().child(recipient).child("inbox").push().getKey(); //pushes to [recipient_email]/inbox/[unique_id]/[DrawingItem]
                Map<String, Object> postValues = item.toMap();

                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/"+recipient+"/inbox/"+ key, postValues);

                database.getReference().updateChildren(childUpdates);

                Toast.makeText(MainActivity.this, "Sent to " + recipient, Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    private Uri drawingUri;

    //helper method for saving the current drawing
    private void saveCanvas() {
        if (isExternalStorageWriteable()) {
            try {
                dir = checkDir(dir);
                Log.v(TAG, "Saving.. hopefully.." + dir.getAbsolutePath());
                String title = ((EditText)findViewById(R.id.title)).getText().toString();
                File file;
                if(title == null) {
                    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    file = new File(dir, "drawing_" + timestamp);
                }
                else{
                    file = new File(dir, title);
                }

                file.createNewFile();

                drawingUri = Uri.fromFile(file);

                try {
                    Log.v(TAG, "writing using outputstream");
                    OutputStream out = new BufferedOutputStream(new FileOutputStream(file));
                    Bitmap bmp = view.getBmp();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (IOException e) {
                    Log.w(TAG, e);
                }
            } catch (IOException io) {
                Log.d(TAG, Log.getStackTraceString(io));
            }


            //set image to the picture
            //imageView.setImageURI(pictureUri);
        }
        Toast.makeText(this, "Drawing saved!", Toast.LENGTH_SHORT ).show();
        Intent i = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(i);
    }

    public boolean isExternalStorageWriteable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        x = event.getX();
        y = event.getY() - getSupportActionBar().getHeight() - ((EditText)findViewById(R.id.title)).getHeight();

        int action = MotionEventCompat.getActionMasked(event);


        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.v(TAG, "Touch!");
                // draw current point
                view.pointer.isDown = true;
                view.pointer.setXY(x, y);
                tempLine.addPoint(view.pointer.cx, view.pointer.cy);
                tempLine.setColor(view.defaultPaint.getColor());
                tempLine.setStrokeWidth(view.width);
                drawing.add(tempLine);
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                Log.v(TAG, "Another finger!");
                return true;
            case MotionEvent.ACTION_POINTER_UP:
                Log.v(TAG, "A finger left");
                return true;
            case MotionEvent.ACTION_UP:
                Log.v(TAG, "Last finger left");
                view.pointer.isDown = false;
                //drawing.add(tempLine);
                //refresh tempLine
                tempLine = new Line();
                return true;
            case (MotionEvent.ACTION_MOVE): //move
                // draw current point
                view.pointer.isDown = true;
                view.pointer.setXY(x, y);
                tempLine.addPoint(view.pointer.cx, view.pointer.cy);
                tempLine.setColor(view.defaultPaint.getColor());
                tempLine.setStrokeWidth(view.width);
                drawing.add(tempLine);
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public ArrayList<Line> getDrawing() {
        return drawing;
    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        //fill
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
            //return super.onDown(e);
        }

    }

    public void showColourPicker(View v) {
        final ColorPickerDialog colorPickerDialog = new ColorPickerDialog();
        colorPickerDialog.initialize(R.string.color_picker_default_title,
                new int[]{
                        getResources().getColor(R.color.colorPrimary),
                        getResources().getColor(R.color.colorPrimaryDark),
                        getResources().getColor(R.color.colorAccent),
                        getResources().getColor(R.color.colorBlack),
                        getResources().getColor(R.color.colorCyan),
                        getResources().getColor(R.color.colorGold),
                        getResources().getColor(R.color.colorOrange),
                        getResources().getColor(R.color.colorGreen)
                }, colorLine, 3, 2);
        colorPickerDialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int colour) {
                view.defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                view.defaultPaint.setStyle(Paint.Style.STROKE);
                view.defaultPaint.setColor(colour);
            }
        });

        FragmentManager fm = this.getFragmentManager();
        colorPickerDialog.show(fm, "colorpicker");
    }

    public void show() {

        final Dialog d = new Dialog(MainActivity.this);
        d.setTitle("NumberPicker");
        d.setContentView(R.layout.dialog);
        Button b1 = (Button) d.findViewById(R.id.button1);
        Button b2 = (Button) d.findViewById(R.id.button2);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(50);
        np.setMinValue(1);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v(TAG, String.valueOf(np.getValue()));
                view.width = np.getValue();
                view.defaultPaint.setStrokeWidth(view.width);
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();


    }

    private File checkDir(File file){
            file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Draw With Me");
            if (!file.exists()) {
                file.mkdirs();
            }
        return file;
    }
}
