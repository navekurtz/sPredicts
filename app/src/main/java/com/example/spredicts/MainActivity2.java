package com.example.spredicts;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity2 extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;//the bottom menu
    HomeFragment homeFragment = new HomeFragment();//creating the home screen
    ScoreFragment scoreFragment = new ScoreFragment();//creating the score screen
    Intent serviceIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
        getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        //play music
        serviceIntent = new Intent(this,PlayService.class);
        PlayAudio();
    }

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = new NavigationBarView.OnItemSelectedListener() {
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, homeFragment).commit();
                    return true;

                case R.id.score:
                    getSupportFragmentManager().beginTransaction().replace(R.id.flFragment, scoreFragment).commit();
                    return true;
            }
            return false;
        }
    };

    private void PlayAudio() {
//        //strLink = "https://www.youtube.com/watch?v=P3cffdsEXXw.mp3";
//        serviceIntent.putExtra("link",R.raw.music);
            Intent myService = new Intent(MainActivity2.this, PlayService.class);
            startService(myService);

    }
}