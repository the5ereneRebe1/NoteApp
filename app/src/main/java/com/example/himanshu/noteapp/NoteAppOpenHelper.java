package com.example.himanshu.noteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteAppOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NoteApp.db";
    public static final int DATABSE_VERSION = 2;
    public NoteAppOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABSE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(NoteAppDatabaseContract.CourseInfoEntry.CREATE_TABLE);
        db.execSQL(NoteAppDatabaseContract.NoteInfoEntry.CREATE_TABLE);
        db.execSQL(NoteAppDatabaseContract.CourseInfoEntry.CREATE_INDEX);
        db.execSQL(NoteAppDatabaseContract.NoteInfoEntry.CREATE_INDEX);
        DatabaseDataWorker dataWorker = new DatabaseDataWorker(db);
        dataWorker.insertCourses();
        dataWorker.insertSampleNotes();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion<2){
            db.execSQL(NoteAppDatabaseContract.CourseInfoEntry.CREATE_INDEX);
            db.execSQL(NoteAppDatabaseContract.NoteInfoEntry.CREATE_INDEX);
        }
    }
}
