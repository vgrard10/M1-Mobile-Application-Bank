package com.example.mybankactivity;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
public class Taches extends AsyncTask<String, Void, Exception> {
    public Exception exception;

    protected Exception doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);

            URLConnection urlConnection = url.openConnection();
            InputStream inputstream = urlConnection.getInputStream();

        } catch (Exception e) {
            this.exception = e;
            Log.e("error", e.toString());
            return e;
        }
        return null;
    }
}
