package com.embroidermodder.embroideryviewer;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private int SELECT_FILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (intent != null && intent.getAction() == "android.intent.action.VIEW") {
            try {
                Pattern p = ReadFromUri(intent.getData());
                DrawView drawView = new DrawView(this, p);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainContentArea);
                relativeLayout.addView(drawView);

            } catch (FileNotFoundException ex) {

            }
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                    intent.setDataAndType(uri, "*/*");
                    startActivityForResult(Intent.createChooser(intent, "Open folder"), SELECT_FILE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_open_file) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if(userChoosenTask.equals("Open folder"))
//                        cameraIntent();
//                } else {
//                    //code for deny
//                }
//                break;
//        }
//    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFileResult(data);
            }
        }
    }

    private void onSelectFileResult(Intent data) {

            try {
                Uri uri = data.getData();
                Pattern p = ReadFromUri(uri);
                DrawView drawView = new DrawView(this, p);
                RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainContentArea);
                relativeLayout.addView(drawView);

        } catch (FileNotFoundException ex) {

        }
    }

    private Pattern ReadFromUri(Uri uri) throws FileNotFoundException {
        Cursor returnCursor =
                getContentResolver().query(uri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        String filename = returnCursor.getString(nameIndex);
        IFormatReader formatReader = Pattern.getReaderByFilename(filename);
        if (formatReader != null) {
            InputStream is = getContentResolver().openInputStream(uri);
            DataInputStream in = new DataInputStream(is);
            Pattern p = formatReader.Read(in);
            return p;
        }
        return null;
    }
}
