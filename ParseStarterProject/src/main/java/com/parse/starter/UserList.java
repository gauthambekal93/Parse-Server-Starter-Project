package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserList extends AppCompatActivity {
     ArrayAdapter arrayAdapter;
     ListView listView;
     ArrayList<String> user_names=new ArrayList<String>();
     String username;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.logout:
                Log.i("Log","Out");
                ParseUser.getCurrentUser().logOut();
                if(ParseUser.getCurrentUser()==null)
                {
                    Toast.makeText(this, "You have been logged out!", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                }
                return true;


                case R.id.upload:
                Log.i("Upload","Photo");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        //     requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    }

                return true;

                default:
                return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,@NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(requestCode==1)
        {
            if(grantResults.length>0 &&grantResults[0]==PackageManager.PERMISSION_GRANTED);
            {
                getPhoto();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode==RESULT_OK &&data!=null)
        {

            try {
                Uri selectedImage=data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                ParseObject imageObject=new ParseObject("Images");
                ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                byte[]byteArray=outputStream.toByteArray();
                ParseFile parseImagefile = new ParseFile("testImage1",byteArray);

                imageObject.put("userImage",parseImagefile);
                imageObject.put("username",username);
                imageObject.save();

                Log.i("Image","Uploaded to parse server!!!");
                outputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public  void getPhoto()
    {
        Intent intent_photo=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent_photo,1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        listView=(ListView) findViewById(R.id.user_list);
        Log.i("2nd ","Activity");

         Intent intent=getIntent();
         Log.i("Opion value!!!!!",Integer.toString(intent.getIntExtra("commonTag",0)));
        // Log.i("username is!!!!!!!",intent.getStringExtra("LoggedIn"));
   //     Log.i("Data string",intent.getDataString());
         if(1==(intent.getIntExtra("commonTag", 0)))
         {
             Log.i("Option","SignUp");
             Bundle bundle=intent.getExtras();
             username=bundle.getString("SignUp");
             Log.i("The username SignUp",username);
         }
         if(2==(intent.getIntExtra("commonTag", 0)))
         {
             Log.i("Option","Login");
             Bundle bundle=intent.getExtras();
             username=bundle.getString("Login");
             Log.i("The username Login",username);
         }

         if(3==(intent.getIntExtra("commonTag", 0)))
         {
             Log.i("Option","LoggedIn");
             Bundle bundle=intent.getExtras();
             username=bundle.getString("LoggedIn");
             Log.i("The username LoggedIn",username);
         }


ParseQuery<ParseObject> query=ParseQuery.getQuery("_User");

    query.findInBackground(new FindCallback<ParseObject>() {
    @Override
    public void done(List<ParseObject> objects, ParseException e) {
        if(e==null)
        {
            if(objects.size()>0)
            {
                for (ParseObject object:objects)
                {
        //            Log.i("Users",object.getString("username"));
                    if(!(username.equals(object.getString("username"))))
                    {
                        user_names.add(object.getString("username"));
                    }
                    }
                Collections.sort(user_names);
                for(String temp: user_names){
                    Log.i("Sorted list",temp);
                }
            }
        }
    }
});

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,user_names);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Log.i("List View","Clicked");
            Log.i("item clicked",adapterView.getItemAtPosition(i).toString());
            Intent intent =new Intent(getApplicationContext(),  UploadedImages.class);
            intent.putExtra("username",adapterView.getItemAtPosition(i).toString());
            startActivity(intent);
            }
        });
    }
}
