package com.example.user.facedetectwithhellosystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class  MySQLite extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "note_database";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "data_table";
    private final static String _ID = "_id";
    private final static String DATE = "date";
    private final static String TIME = "time";
    private final static String VALUE = "value";

    private String sql =
            "CREATE TABLE "+TABLE_NAME+"("+
                    _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    DATE + " DATE, "+
                    TIME + " TIME, "+
                    VALUE + " FLOAT "+
                    ");";
    private SQLiteDatabase database;


    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        onCreate(db);
        final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME ;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public Cursor select(){
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        return cursor;
    }
    public void insert(String date, String time, float value){
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(VALUE, value);
        database.insert(TABLE_NAME, null, values);
    }

    public void delete(int id){
        database.delete(TABLE_NAME, _ID + "=" + Integer.toString(id), null);
    }

    public void deleteAll(){
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
//        database.execSQL("delete from "+ TABLE_NAME);

    }

    public void update(int id, String itemText){
        ContentValues values = new ContentValues();
//        values.put(FEILD_TEXT, itemText);
        database.update(TABLE_NAME, values, _ID + "=" + Integer.toString(id), null);
    }

    public void createUserTable(String tableName) {

//        database.execSQL("DROP TABLE IF EXISTS " + Table_Name);
//        String CREATE_NEW_TABLE = "CREATE TABLE " + tableName + "(" + CID + " TEXT PRIMARY KEY, " + DName + " TEXT);";
//        database.execSQL(CREATE_NEW_TABLE);
//        database.close();
    }

    public void close(){
        database.close();
    }

}