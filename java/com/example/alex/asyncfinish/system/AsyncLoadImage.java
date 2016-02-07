package com.example.alex.asyncfinish.system;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.example.alex.asyncfinish.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Вiталя on 23.11.2015.
 */
public class AsyncLoadImage extends AsyncTask<String, Integer, Bitmap> {

    Context context;

    public AsyncLoadImage(Context cntx) {
        this.context = cntx;
    }

    protected void onPreExecute(){
        Log.d(Constants.LOG_TAG, "On Pre Exec");
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = "";
        if( params.length > 0 ){
            url = params[0];
        }

        InputStream input = null;

        try {
            URL urlConn = new URL(url);
            input = urlConn.openStream();
        }
        catch (MalformedURLException e) {
            Log.d(Constants.LOG_TAG, "Oops, Something wrong with URL...");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d(Constants.LOG_TAG,"Oops, Something wrong with input stream...");
            e.printStackTrace();
        }

        return BitmapFactory.decodeStream(input);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {

        super.onProgressUpdate(values[0]);
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);
    }
}


