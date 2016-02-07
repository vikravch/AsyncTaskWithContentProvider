package com.example.alex.asyncfinish.system;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class MyContentProvider extends ContentProvider {
    private SQLiteDatabase db;
    static final String DB_NAME="mydb";
    static final String TABLE_NAME="names";
    static final int DB_VERSION=1;
    static final String CREATE_DB="CREATE TABlE "+TABLE_NAME+"("
    +Constants.ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +Constants.ROW_NAME+" TEXT NOT NULL);";

    static final int URI_CODE=1;
    static final int URI_USERS=2;

    static final UriMatcher uriMatcher;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "cte" ,URI_CODE);
        uriMatcher.addURI(Constants.PROVIDER_NAME,"users",URI_USERS);
    }
    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowId;
            rowId=db.delete(TABLE_NAME,"_id = "+selection,null);
        Log.d("contextMenu",rowId+" was deleted");
        return rowId;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId = db.insert(TABLE_NAME,null,values);

        if(rowId>0){
            Uri _uri= ContentUris.withAppendedId(Constants.CONTENT_URI,rowId);
            getContext().getContentResolver().notifyChange(_uri,null);
            Log.d("dbLog",_uri.toString());
            return _uri;
        }
            return null;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DbHelper dbHelper=new DbHelper(context);
        db = dbHelper.getWritableDatabase();
        if(db!=null)return true;
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(TABLE_NAME);
        Cursor cursor=null;
        Log.d(Constants.LOG_TAG,uriMatcher.match(uri)+"");
        switch (uriMatcher.match(uri)){
            case URI_CODE:
                cursor = qb.query(db,projection,selection,selectionArgs,null,null,sortOrder);
                cursor.setNotificationUri(getContext().getContentResolver(),uri);
                break;
            case URI_USERS:
                Log.d(Constants.LOG_TAG,"asdsada");
                cursor = getFromSiteUsers("http://cityfinder.esy.es/getuser.php");
                break;
        }
        return cursor;
    }

    private Cursor getFromSiteUsers(String url) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("name", "Vitaly");
        data.put("password", "mypass");
        AsyncHttpPost asyncHttpPost = new AsyncHttpPost(data);
        String res= "";
        try {
            res = asyncHttpPost.execute("http://cityfinder.esy.es/getuser.php").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Log.d("AsyncTask", res);

        String arrRes[] = res.split(";");
        MatrixCursor cursor = new MatrixCursor(new String[]{"_id","name"});
        cursor.moveToFirst();
        for(int i=1;i<=arrRes.length;i++){
            cursor.addRow(new Object[]{i,arrRes[i-1]});
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int res;
        res = db.update(TABLE_NAME,values,"_id = "+selection,null);
        return res;
    }

    static private class DbHelper extends SQLiteOpenHelper {

        public DbHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}
