package com.example.spredicts;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ArrayList<Card> matchListScheduled = new ArrayList<Card>();//the list of cards scheduled matches
    public ArrayList<Card> matchListFinished = new ArrayList<Card>();//the list of cards finished matches
    private ArrayList<Card> match = new ArrayList<Card>();
    private RecyclerView recyclerView;//a view for the cards

    //creating the database for the predicts
    public DBHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase = null;
    private ArrayList<Predict> Prelist;

    private EditText etHome;
    private EditText etAway;
    private Button update;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;
    private int score=0;


    BottomNavigationView bottomNavigationView;
    Cache cache;// Instantiate the cache
    Network network;// Set up the network to use HttpURLConnection as the HTTP client.
    private RequestQueue requestQueue = new RequestQueue(cache, network);//create the json request
    Context thiscontext;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        thiscontext = container.getContext();
        etHome = view.findViewById(R.id.ethomenave);
        etAway = view.findViewById(R.id.etawayyy);
        update = view.findViewById(R.id.updatebt);

        sharedPref = getContext().getSharedPreferences(String.valueOf(R.string.sharedFile), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //these things are required for the API request
        requestQueue = Volley.newRequestQueue(getContext());
        this.cache = new DiskBasedCache(getCacheDir(), 1024 * 1024); // 1MB cap
        network = new BasicNetwork(new HurlStack());
        RequestQueue requestQueue = new RequestQueue(cache, network);
        requestQueue.start();
        fetchMatches();

        //a view that show the card matches
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //database for the user's predicts
        dbHelper = new DBHelper(getContext());
        sqLiteDatabase = dbHelper.getWritableDatabase();
        dbHelper.onCreate(sqLiteDatabase);
        Prelist = new ArrayList<Predict>();
        if (true || dbHelper.doesDatabaseExist(getContext(), "predicts.db") == false) {//if the database is not exist the system creating it with default predicts
            String url = "http://api.football-data.org/v2/competitions/PD/matches";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method
                    .GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONArray match = response.getJSONArray("matches");//the json file
                        for (int i = 0; i < match.length(); i++) {
                            JSONObject jsonObject = match.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            Predict p1 = new Predict(id, 0, 0);//create a prediction for a match
                            dbHelper.insert(p1);
                            Prelist.add(p1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            }) {
                public Map getHeaders() throws AuthFailureError {
                    HashMap headers = new HashMap();
                    headers.put("Content-Type", "application/json");
                    headers.put("X-Auth-Token", "c901f588758f4d438e2ded697081cb8c");
                    return headers;
                }
            };
            requestQueue.add(request);
            Log.println(Log.ASSERT, "Response", request.toString());
        }
        //---------------------------------------------------------------------------------------------------------
        //a top menu that distinguish between finished or scheduled matches
        Spinner homeSpinner = view.findViewById(R.id.Spinner);
        ArrayAdapter<String> homeAdapter = new ArrayAdapter<String>(thiscontext,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.names));
        homeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        homeSpinner.setAdapter(homeAdapter);
        homeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1://finished
                        match.clear();
                        match.addAll(matchListFinished);
                        MatchAdapterFinished adapter2 = new MatchAdapterFinished(getContext(), match);
                        recyclerView.setAdapter(adapter2);
                        break;

                    case 0://scheduled
                        match.clear();
                        match.addAll(matchListScheduled);
                        MatchAdapterScheduled adapter3 = new MatchAdapterScheduled(getContext(), match);
                        recyclerView.setAdapter(adapter3);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //-----------------------------------------------------------------------------------------------------------
        //here we send the score that the user accumulate
        for (int i = 0;i<matchListFinished.size();i++){
           Predict p2 = dbHelper.getById(matchListFinished.get(i).getId());
           if (p2.getHomePredict()==matchListFinished.get(i).getHomeScore() && p2.getAwayPredict()==matchListFinished.get(i).getAwayScore()){
               score=score+3;
           }
        }
        editor.putInt(String.valueOf(R.string.score),score);
        editor.apply();
        score = 0;
        return view;//the end of the on createView
    }

    private File getCacheDir() {
        return this.getContext().getCacheDir();
    }

    //the function for the request to the json file and for the pumping out the media
    public void fetchMatches() {
        String url = "http://api.football-data.org/v2/competitions/PD/matches";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method
                .GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray match = response.getJSONArray("matches");//the json file
                    for (int i = 0; i < match.length(); i++) {
                        JSONObject jsonObject = match.getJSONObject(i);
                        int id = jsonObject.getInt("id");
                        String status = jsonObject.getString("status");
                        JSONObject Score = jsonObject.getJSONObject("score");
                        JSONObject FullTime = Score.getJSONObject("fullTime");
                        int homeScore = FullTime.getInt("homeTeam");
                        int awayScore = FullTime.getInt("awayTeam");
                        JSONObject homeTeam = jsonObject.getJSONObject("homeTeam");
                        JSONObject awayTeam = jsonObject.getJSONObject("awayTeam");
                        String homeName = homeTeam.getString("name");
                        String awayName = awayTeam.getString("name");
//                        if (status.equals("SCHEDULED")) {
                            Card card = new Card(homeScore + " - " + awayScore, homeName, awayName, id, homeScore, awayScore);
                            matchListScheduled.add(card);
//                        } else if (status.equals("FINISHED")) {
                            Card card2 = new Card(homeScore + " - " + awayScore, homeName, awayName, id, homeScore, awayScore);
                            matchListFinished.add(card2);
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-Auth-Token", "c901f588758f4d438e2ded697081cb8c");
                return headers;
            }
        };
        requestQueue.add(request);
        Log.println(Log.ASSERT, "Response", request.toString());
    }

}