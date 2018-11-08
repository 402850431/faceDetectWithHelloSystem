package com.example.user.facedetectwithhellosystem.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.facedetectwithhellosystem.view.choose_lexicon.choose_lexicon_word.Word;

import java.util.ArrayList;

public class  MySQLite extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "my_lexicon_database";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "table1";
    private final static String _ID = "_id";
    private final static String WORD = "word";

    private String sql =
            "CREATE TABLE "+TABLE_NAME+"("+
                    _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    WORD + " STRING "+
                    ");";
    private SQLiteDatabase database;


    public MySQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sql);
        Log.e(">>>create table", "create");
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

    public void add(String tableName, String word){
        ContentValues values = new ContentValues();
        values.put(WORD, word);
        database.insert(tableName, null, values);
        Log.e(">>>added", "added");
    }

    public ArrayList<Word> getRows(String tableName){
        String query = "SELECT * FROM "+tableName;
        Cursor cursor = database.rawQuery(query,null);
        ArrayList<Word> list = new ArrayList<>();

        while(cursor.moveToNext()){
            Word mWord = new Word(
                    cursor.getInt(cursor.getColumnIndex(_ID)),
                    cursor.getString(cursor.getColumnIndex(WORD))
            );
            list.add(mWord);
        }
        cursor.close();
        return list;
    }

    public void changeTableName(String oldTableName, String newTableName) {
        database.beginTransaction();
        try{
            database.execSQL("ALTER TABLE " + oldTableName + " RENAME TO " + newTableName+";");
            database.setTransactionSuccessful();
        } finally{
            database.endTransaction();
        }
    }

    public boolean isHasAnyTable () {
        if (database == null || !database.isOpen())
        {
            return false;
        }
        String query = "SELECT count(*) FROM sqlite_master WHERE type = 'table' AND name != 'android_metadata' AND name != 'sqlite_sequence'"; //sqlite_sequence // order by name
        Cursor cursor = database.rawQuery(query, null); //SELECT COUNT(*)
//        while (cursor.moveToNext()) {
//            int count = cursor.getCount();
//        }
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean isTableExists(String tableName)
    {
        if (tableName == null || database == null || !database.isOpen())
        {
            return false;
        }
        Cursor cursor = database.rawQuery("SELECT 1 FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName}); //SELECT COUNT(*)
        if (!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public ArrayList<String> getTables(){
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' AND name!='sqlite_sequence' "; //sqlite_sequence // order by name
        Cursor cursor = database.rawQuery(query, null);
        ArrayList<String> list = new ArrayList<>();

        while(cursor.moveToNext()){
            list.add(cursor.getString(cursor.getColumnIndex("name")));
        }

        cursor.close();
        return list;
    }

    public void delete(String tableName, int id){
//        Cursor mCursor = database.rawQuery( "SELECT "+ _ID + " FROM "+ tableName +" WHERE " + WORD +"= '"+itemName+"'" , null);
//        database.delete(tableName, _ID + "=" + String.valueOf(mCursor.getLong(mCursor.getColumnIndex("_id"))), null);
//        database.delete(tableName, _ID + "=" + Integer.toString(id), null);
//        Log.e(">>>delete", String.valueOf(mCursor.getLong(mCursor.getColumnIndex("_id"))));
        database.delete(tableName, _ID + "=" + id, null);
    }

    public boolean deleteAllDataInTable(String tableName){
        return database.delete(tableName, null, null)>0;

    }

    public void deleteTable(String tableName){
        database.execSQL("DROP TABLE IF EXISTS " + tableName);

    }

    public void update(int id, String tableName, String itemText){
//        Cursor mCursor = database.rawQuery( "SELECT "+ _ID + " FROM "+ tableName +" WHERE " + WORD +"= '"+itemText+"'" , null);
        ContentValues values = new ContentValues();
        values.put(WORD, itemText);
        database.update(tableName, values, _ID + "=" + Integer.toString(id), null);
//        database.update(tableName, values, _ID + "=" + String.valueOf(mCursor.getLong(mCursor.getColumnIndex("_id"))), null);
    }


    public void createTable(String tableName) {
//        database.execSQL("DROP TABLE IF EXISTS " + tableName);
        String CREATE_NEW_TABLE ="CREATE TABLE "+tableName+"("+
                _ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                WORD + " STRING "+
                ");";
        database.execSQL(CREATE_NEW_TABLE);
    }

    public void close(){
        database.close();
    }

}