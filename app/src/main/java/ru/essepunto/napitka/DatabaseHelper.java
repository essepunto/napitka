package ru.essepunto.napitka;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "mydatabase.db";
    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_ID = "id";
    private  static final String COLUMN_CODE = "code";
    private static final String COLUMN_NAME = "name";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, "+ COLUMN_CODE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String name, String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_CODE, code);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("Range")
    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM "+ TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            names.add(cursor.getString(cursor.getColumnIndex("name")));
            cursor.moveToNext();
        }
        cursor.close();
        return names;
    }

    @SuppressLint("Range")
    public ArrayList<String> getNamesAndData() {
        ArrayList<String> namesAndData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT code, name FROM " + TABLE_NAME, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String code = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            namesAndData.add(code + ": " + name);
            cursor.moveToNext();
        }
        cursor.close();
        return namesAndData;
    }


    public void removeAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseHelper.TABLE_NAME);
        db.execSQL("VACUUM");
    }

        public String getRecordCount() {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return String.valueOf(count);

        }



}
