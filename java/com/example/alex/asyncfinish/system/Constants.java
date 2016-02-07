package com.example.alex.asyncfinish.system;

import android.net.Uri;

/**
 * Created by Alex on 12.11.2015.
 */
public class Constants {
    public static final String ROW_NAME = "name";
    public static final String ID = "_id";
    public static final String LOG_TAG="myLog";
    public static final String USERS_PASS="users";

    public static final String PROVIDER_NAME="com.providerandloader"; //AUTHORITY
    public static final String URI="content://"+PROVIDER_NAME+"/cte"; //cte=название таблицы
    public static final Uri CONTENT_URI=Uri.parse(URI);
    public static final String URI_USERS="content://"+PROVIDER_NAME+"/"+USERS_PASS;
    public static final Uri CONTENT_USER_URI=Uri.parse(URI_USERS);


}
