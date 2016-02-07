package com.example.alex.asyncfinish.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.asyncfinish.R;
import com.example.alex.asyncfinish.system.AsyncLoadImage;

import java.util.concurrent.ExecutionException;

public class ImageActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressDialog progress;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent=getIntent();
        title=(TextView)findViewById(R.id.image_activity_text);
        String name=intent.getStringExtra("name");
        title.setText(name);
        progress = ProgressDialog.show(this,getResources().getString(R.string.dialog_title),getResources().getString(R.string.dialog_message));
        imageView=(ImageView)findViewById(R.id.image_activity_image);



        AsyncLoadImage asyncLoadImage=new AsyncLoadImage(this);
        asyncLoadImage.execute(name);
        try {
            imageView.setImageBitmap(asyncLoadImage.get());
        } catch (InterruptedException e) {
            Log.d("myLog","InterruptedException");
            e.printStackTrace();
        } catch (ExecutionException e) {
            Log.d("myLog","ExecutionException");
            e.printStackTrace();
        }
        progress.dismiss();

    }
}
