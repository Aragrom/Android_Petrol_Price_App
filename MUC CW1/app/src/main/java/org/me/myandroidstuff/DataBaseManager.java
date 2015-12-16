package org.me.myandroidstuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * Created by Graham on 16/12/2015.
 */
public class DataBaseManager extends SQLiteOpenHelper {

    private static final int DB_VER = 1;
    private static final String DB_PATH = "/data/data/org.me.myandroidstuff/";
    private static final String DB_NAME = "newDataBase.s3db";

    public static final String COL_QUERY_ID = "queryObjectID";
    public static final String TBL_QUERYOBJECT = "queryobject";

    public static final String COL_DIESEL_HIGH = "DieselHigh";
    public static final String COL_DIESEL_AVG = "DieselAvg";
    public static final String COL_DIESEL_LOW = "DieselLow";

    private final Context appContext;

    public DataBaseManager(Context context, String name,
                                SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.appContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_STARSIGNSINFO_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TBL_QUERYOBJECT
                        + "(" + COL_QUERY_ID + " INTEGER PRIMARY KEY,"
                        + COL_DIESEL_HIGH + " TEXT,"
                        + COL_DIESEL_AVG + " TEXT,"
                        + COL_DIESEL_LOW + "TEXT"
                        + ")";
        db.execSQL(CREATE_STARSIGNSINFO_TABLE);
        Log.e("DataBaseManager", "onCreate()");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + TBL_QUERYOBJECT);
            onCreate(db);
            Log.e("DataBaseManager", "onUpgrade()");
        }
    }

    // ================================================================================
    // Creates a empty database on the system and rewrites it with your own database.
    // ================================================================================
    public void dbCreate() throws IOException {

        boolean dbExist = dbCheck();

        Log.e("DataBaseManager", "Trying 'Database create'");

        if(!dbExist){
            //By calling this method an empty database will be created into the default system path
            //of your application so we can overwrite that database with our database.
            this.getReadableDatabase();

            /*try {

                copyDBFromAssets();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }*/
        }

    }

    // ============================================================================================
    // Check if the database already exist to avoid re-copying the file each time you open the application.
    // @return true if it exists, false if it doesn't
    // ============================================================================================
    private boolean dbCheck(){

        Log.e("DataBaseManager", "Doing database check");

        SQLiteDatabase db = null;

        try{
            String dbPath = DB_PATH + DB_NAME;
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
            db.setLocale(Locale.getDefault());
            db.setVersion(1);

        }catch(SQLiteException e){

            Log.e("SQLHelper", "Database not Found!");

        }

        if(db != null){

            db.close();

        }

        return db != null ? true : false;
    }

    // ============================================================================================
    // Copies your database from your local assets-folder to the just created empty database in the
    // system folder, from where it can be accessed and handled.
    // This is done by transfering bytestream.
    // ============================================================================================
    private void copyDBFromAssets() throws IOException{

        Log.e("DataBaseManager", "Copying Database from Assets");

        InputStream dbInput = null;
        OutputStream dbOutput = null;
        String dbFileName = DB_PATH + DB_NAME;

        try {

            dbInput = appContext.getAssets().open(DB_NAME);
            dbOutput = new FileOutputStream(dbFileName);
            //transfer bytes from the dbInput to the dbOutput
            byte[] buffer = new byte[1024];
            int length;
            while ((length = dbInput.read(buffer)) > 0) {
                dbOutput.write(buffer, 0, length);
            }

            //Close the streams
            dbOutput.flush();
            dbOutput.close();
            dbInput.close();
        } catch (IOException e)
        {
            throw new Error("Problems copying DB!");
        }
    }


    public void addObject(QueryObject queryObject) {

        ContentValues values = new ContentValues();
        values.put(COL_QUERY_ID, String.valueOf(queryObject.objectID));
        values.put(COL_DIESEL_HIGH, String.valueOf(queryObject.aFloatDiesel[2]));
        values.put(COL_DIESEL_AVG, String.valueOf(queryObject.aFloatDiesel[1]));
        values.put(COL_DIESEL_LOW, String.valueOf(queryObject.aFloatDiesel[0]));

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(TBL_QUERYOBJECT, null, values);
        db.close();
    }

    public QueryObject findObject(String string) {
        String query = "Select * FROM " + TBL_QUERYOBJECT + " WHERE " + COL_QUERY_ID + " =  \"" + string + "\"";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        QueryObject newQuery = new QueryObject();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            newQuery.aFloatDiesel[2] = (Float.parseFloat(cursor.getString(0)));
            newQuery.aFloatDiesel[1] = (Float.parseFloat(cursor.getString(0)));
            newQuery.aFloatDiesel[0] = (Float.parseFloat(cursor.getString(0)));
            cursor.close();
        } else {
            newQuery = null;
        }
        db.close();
        return newQuery;
    }

    public boolean removeObject(String str) {

        boolean result = false;

        String query = "Select * FROM " + TBL_QUERYOBJECT + " WHERE " + COL_QUERY_ID + " =  \"" + str + "\"";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        QueryObject newQuery = new QueryObject();

        if (cursor.moveToFirst()) {
            db.delete(TBL_QUERYOBJECT, COL_QUERY_ID + " = ?",
                    new String[] { newQuery.objectID });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}

