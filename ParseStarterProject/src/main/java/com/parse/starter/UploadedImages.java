package com.parse.starter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class UploadedImages extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaded_images);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        final ScrollView scrollView=(ScrollView) findViewById(R.id.scrollView);
        final LinearLayout linearView=(LinearLayout) findViewById(R.id.linearView);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        Log.i("sent username is",bundle.getString("username"));
        try {

            ParseQuery<ParseObject> query=new ParseQuery<ParseObject>("Images");

            query.whereEqualTo("username",bundle.getString("username"));
            List<ParseObject> ob=query.find();

            Log.i("Object size is",Integer.toString(ob.size()));
            if(ob.size()>0)
            {
            for(ParseObject imageObject:ob)
            {
                ParseFile parseImageFile=(ParseFile)imageObject.get("userImage");
                Log.i("parse files are",parseImageFile.toString());
                parseImageFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] data, ParseException e) {
                        ImageView imageView=new ImageView(UploadedImages.this);
                        imageView.setBackgroundResource(R.mipmap.ic_launcher);
                        linearView.addView(imageView);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageView.setImageBitmap(bitmap);
                    }
                });
                //add new image view dynamically
            }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
