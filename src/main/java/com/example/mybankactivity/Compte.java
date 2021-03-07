package com.example.mybankactivity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Compte extends AppCompatActivity {
        //private final String API = BuildConfig.ApiKey;
        private final String API2 ="https://60102f166c21e10017050128.mockapi.io/labbbank/accounts"; //because i didnt succeed to do it

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_compte);
            TextView textView = findViewById(R.id.textinf);

            refreshAccount();

            ImageButton refresh = findViewById(R.id.buttonRefr);
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refreshAccount();
                }
            });
        }
        private void writeToFile(String data) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }

        private String readFromFile() {
            String retour = "";
            try {
                InputStream inputStream = getApplicationContext().openFileInput("config.txt");

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader buffReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = buffReader.readLine()) != null ) {
                        stringBuilder.append(receiveString);
                    }

                    inputStream.close();
                    retour = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("login activity", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("login activity", "Can not read file: " + e.toString());
            }
            return retour;
        }

        private  void refreshAccount(){
            RequestQueue queue = Volley.newRequestQueue(this);
            try {
                AsyncTask<String, Void, Exception> e = new Taches().execute(API2);//must be API not API2

                ListView listacc = (ListView)findViewById(R.id.listAccount);
                listacc.setAdapter(null);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, API2,//must be API not API2
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    writeToFile(jsonArray.toString());

                                    ArrayList<String> items = new ArrayList<String>();
                                    for(int i=0; i < jsonArray.length() ; i++) {
                                        JSONObject json_data = jsonArray.getJSONObject(i);
                                        String account=json_data.toString();
                                        items.add(account);
                                    }
                                    ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                                    listacc.setAdapter(mArrayAdapter);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String dataFile = readFromFile();
                        try {
                            JSONArray jsonArray = new JSONArray(dataFile);

                            ArrayList<String> items = new ArrayList<String>();
                            for(int i=0; i < jsonArray.length() ; i++) {
                                JSONObject json_data = jsonArray.getJSONObject(i);
                                String account=json_data.toString();
                                items.add(account);
                            }

                            ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                            listacc.setAdapter(mArrayAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Toast toast = Toast.makeText(getApplicationContext(), "Connexion failed", Toast.LENGTH_LONG);
                        toast.show();
                    }
                });
                queue.add(stringRequest);
            }catch (Exception e){
            }
        }
}
