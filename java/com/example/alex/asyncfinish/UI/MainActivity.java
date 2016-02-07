package com.example.alex.asyncfinish.UI;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.alex.asyncfinish.R;
import com.example.alex.asyncfinish.system.Constants;

public class MainActivity extends Activity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    Button loadBtn, showBtn, fillBD;
    ListView listView;
    CursorLoader cursorLoader;
    SimpleCursorAdapter adapter;

    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBtn=(Button)findViewById(R.id.main_btn_load);
        loadBtn.setOnClickListener(this);
        showBtn=(Button)findViewById(R.id.main_btn_show);
        showBtn.setOnClickListener(this);
        listView=(ListView)findViewById(R.id.main_list);
        fillBD=(Button)findViewById(R.id.main_btn_fill);
        fillBD.setOnClickListener(this);



        getLoaderManager().initLoader(1,null,this);
    }
    void loadImageActivity(String url){
        Intent intent = new Intent(this,ImageActivity.class);
        intent.putExtra("name",url);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.main_btn_load:
                getLoaderManager().initLoader(2,null,this);
                break;
            case R.id.main_btn_show:
                loadImageActivity("http://cityfinder.esy.es/img/1.jpg");
                break;
            case R.id.main_btn_fill:
                ContentValues cv = new ContentValues();
                cv.put(Constants.ROW_NAME,"Nick");
                getContentResolver().insert(Constants.CONTENT_URI, cv);
                cv = new ContentValues();
                cv.put(Constants.ROW_NAME, "Jack");
                getContentResolver().insert(Constants.CONTENT_URI,cv);
                cv = new ContentValues();
                cv.put(Constants.ROW_NAME,"Marry");
                getContentResolver().insert(Constants.CONTENT_URI,cv);
                cv = new ContentValues();
                cv.put(Constants.ROW_NAME,"Sara");
                getContentResolver().insert(Constants.CONTENT_URI,cv);
                getLoaderManager().initLoader(1,null,this);
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        progress = ProgressDialog.show(this,getResources().getString(R.string.dialog_title),getResources().getString(R.string.dialog_message));
        cursorLoader=null;
        Log.d(Constants.LOG_TAG, id + "from main");
        switch (id){
            case 1:
                cursorLoader = new CursorLoader(this,Constants.CONTENT_URI,null,null,null,null);
                break;
            case 2:
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem="";
                        TextView textView=(TextView)view.findViewById(android.R.id.text1);
                        selectedItem=textView.getText().toString();
                        String nameArr[]=selectedItem.split("-");
                        loadImageActivity("http://cityfinder.esy.es/img/"+nameArr[1]);
                    }
                });
                cursorLoader = new CursorLoader(this,Constants.CONTENT_USER_URI,null,null,null,null);
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String from[]={Constants.ROW_NAME};
        int to[]={android.R.id.text1};
        adapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,data,from,to, Adapter.NO_SELECTION);
        listView.setAdapter(adapter);
        progress.dismiss();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
