package com.rpimc.hari.rpimc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_COMD = "_command";
    public static final String KEY_PATH = "_path";

    public static final String DATABASE_NAME = "CMD_DB";
    public static final String TABLE_NAME = "CMD_T";
    public static final int DATABASE_VERSION = 1;

    private Helper helper;
    private Context context;
    private SQLiteDatabase database;

    public CommandDB(Context context) {
        this.context = context;
    }

    class Helper extends SQLiteOpenHelper {

        public Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
//             SQLiteDatabase.openOrCreateDatabase(DATABASE_NAME, null); for storing database at external memoy yet not required
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_COMD + " TEXT NOT NULL, " +
                    KEY_PATH + " TEXT NOT NULL);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    public CommandDB open() throws SQLiteException {
        helper = new Helper(context);
        database = helper.getWritableDatabase();
        return this;
    }

    public void close() {
        helper.close();
    }

    public long getProfilesCount() {
        long cnt = DatabaseUtils.queryNumEntries(database, TABLE_NAME);
        return cnt;
    }

    public void createEntry(String cmd, ArrayList<Line> path) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_COMD, cmd);
        String lines = toString(path);
        cv.put(KEY_PATH, lines);
        database.insert(TABLE_NAME, null, cv);
    }

    public void updateEntry(long lg, String cmd, ArrayList<Line> path) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_COMD, cmd);
        String lines = toString(path);
        contentValue.put(KEY_PATH, lines);
        database.update(TABLE_NAME, contentValue, KEY_ROWID + "=" + lg, null);
    }

    public void deleteEntry(long l) throws SQLiteException {
        database.delete(TABLE_NAME, KEY_ROWID + "=" + l, null);
    }

    public void deleteEntry(String cmd) throws SQLiteException {
        database.delete(TABLE_NAME, KEY_COMD + " = '" + cmd + "'", null);
    }

    public String getCommand(long l) {
        String[] columns = {KEY_ROWID, KEY_COMD, KEY_PATH};
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_ROWID + " = " + l, null, null, null, null);
        if (cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex(KEY_COMD));
            cursor.close();
            return result;
        }
        return null;
    }

    public ArrayList<String> getAllCommand() {
        ArrayList<String> cmds = new ArrayList<String>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                cmds.add(cursor.getString(cursor.getColumnIndex(KEY_COMD)));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return cmds;
    }

    public ArrayList<ArrayList<Line>> getAllPath() {
        ArrayList<ArrayList<Line>> paths = new ArrayList<ArrayList<Line>>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                String result = cursor.getString(cursor.getColumnIndex(KEY_PATH));
                paths.add(fromString(result));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return paths;
    }

    public ArrayList<Line> getPath(long l) {
        String[] columns = {KEY_ROWID, KEY_COMD, KEY_PATH};
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_ROWID + "=" + l, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            String result = cursor.getString(cursor.getColumnIndex(KEY_PATH));
            cursor.close();
            ArrayList<Line> arrayList = fromString(result);
            Log.i("AndroidRuntime", result);
            return arrayList;
        }
        return null;
    }

    public ArrayList<Line> getPath(String cmd) {
        String[] columns = {KEY_ROWID, KEY_COMD, KEY_PATH};
        Cursor cursor = database.query(TABLE_NAME, columns, KEY_COMD + " = '" + cmd + "'", null, null, null, null);
        if (cursor.moveToFirst()) {
            String result = cursor.getString(cursor.getColumnIndex(KEY_PATH));
            cursor.close();
            ArrayList<Line> arrayList = fromString(result);
            Log.i("AndroidRuntime", result);
            return arrayList;
        }
        return null;
    }

    private String toString(ArrayList<Line> lines) {
        String str = "";
        int i;
        for (i = 0; i < lines.size() - 1; i++)
            str += lines.get(i).toString() + "#";
        str += lines.get(i).toString();
        return str;
    }

    private ArrayList<Line> fromString(String string) {
        String[] strings = string.split("#");
        ArrayList<Line> lines = new ArrayList<Line>();
        for (int i = 0; i < strings.length; i++)
            lines.add(new Line(strings[i]));
        return lines;
    }
}