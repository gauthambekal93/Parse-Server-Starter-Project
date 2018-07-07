/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity implements  View.OnKeyListener {

  TextView swap;
  EditText username;
  EditText password;
  Button submit;
  RelativeLayout layout;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    swap=(TextView)findViewById(R.id.swapText);
    username=(EditText)findViewById(R.id.username);
    password=(EditText)findViewById(R.id.password);
    submit=(Button) findViewById(R.id.submit);
    layout=(RelativeLayout)findViewById(R.id.background);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    Log.i("test","123!!!!!");

    if (ParseUser.getCurrentUser()!=null) {
      Log.i("Session","not expired!!!");
      Log.i("current user in MAIN",ParseUser.getCurrentUser().getUsername());

      Intent intent=new Intent(getApplicationContext(),UserList.class);
      intent.putExtra("commonTag",3);
      intent.putExtra("LoggedIn",ParseUser.getCurrentUser().getUsername());
      startActivity(intent);
      }
      else {
      // show the signup or login screen
      Log.i("Session","expired!!!");
    }
  }


  @Override
  public boolean onKey(View view, int i, KeyEvent keyEvent) {
    if(i==KeyEvent.KEYCODE_ENTER)
    {
      Log.i("Enter","Pressed");
      submit(view);
    }
    Log.i("key code is",Integer.toString(keyEvent.getKeyCode()));
    return false;
  }


  public void submit(View view)
  {
    if(username.getText().toString().isEmpty())
    {
      Toast.makeText(this.getBaseContext(),"Please enter user name",
              Toast.LENGTH_SHORT).show();
    }
    if(password.getText().toString().isEmpty())
    {
      Toast.makeText(this.getBaseContext(),"Please enter password",
              Toast.LENGTH_SHORT).show();
    }
    ParseUser user=new ParseUser();
    user.setUsername(username.getText().toString());
    user.setPassword(password.getText().toString());

    Log.i("Button text",submit.getText().toString());

    if(submit.getText().toString().equals("SIGN UP"))
    {
      Log.i("SIGN UP","Clicked");
      user.signUpInBackground(new SignUpCallback() {
        @Override
        public void done(ParseException e) {
          if(e==null)
          {
            Log.i("Sign up","successful");

            Intent intent=new Intent(getApplicationContext(),UserList.class);
            intent.putExtra("commonTag",1);
            intent.putExtra("SignUp",username.getText().toString());
            startActivity(intent);
          }
          else {
            Toast.makeText(MainActivity.this,"Sign up failed",
                    Toast.LENGTH_SHORT).show();
            Log.i("Sign up","failed");
          }
        }
      });
    }
    else
    {
      Log.i("LOGIN","CLICKED");
      ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
        @Override
        public void done(ParseUser user, ParseException e) {
          if(user!=null)
          {
            Log.i("Login","Successful");

            Intent intent=new Intent(getApplicationContext(),UserList.class);
            intent.putExtra("commonTag",2);
            intent.putExtra("Login",username.getText().toString());
            startActivity(intent);
          }
          else {
            Toast.makeText(MainActivity.this,"Login failed",
                    Toast.LENGTH_SHORT).show();
            Log.i("Login","Failed"+e.toString());
          }
        }
      });
    }
  }
  public  void  swap(View view)
  {
    if(swap.getText().toString().equals("or Login")) {
      submit.setText("LOGIN");
      swap.setText("or Sign Up");
    }
    else
    {        submit.setText("SIGN UP");
      swap.setText("or Login");
    }
  }
  //close the keyboard if background is clicked
  public void backgroundClicked(View view)
  {
    InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
  }
}