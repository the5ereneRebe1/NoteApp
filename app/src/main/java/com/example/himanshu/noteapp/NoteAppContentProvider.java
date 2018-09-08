package com.example.himanshu.noteapp;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.himanshu.noteapp.NoteAppDatabaseContract.CourseInfoEntry;
import com.example.himanshu.noteapp.NoteAppDatabaseContract.NoteInfoEntry;
import com.example.himanshu.noteapp.NoteAppProviderContract.CourseIdColumn;
import com.example.himanshu.noteapp.NoteAppProviderContract.Courses;
import com.example.himanshu.noteapp.NoteAppProviderContract.Notes;

public class NoteAppContentProvider extends ContentProvider {
    public static final String MIME_VENDOR_TYPE = "vnd." + NoteAppProviderContract.AUTHORITY + ".";

    public NoteAppContentProvider() {
    }
    private NoteAppOpenHelper mDbOpenHelper;
    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final int NOTES = 0;

    public static final int COURSES = 1;

    public static final int NOTES_EXPANDED = 2;

    public static final int NOTES_ROW = 3;

    static {
        uriMatcher.addURI(NoteAppProviderContract.AUTHORITY, Notes.PATH, NOTES);
        uriMatcher.addURI(NoteAppProviderContract.AUTHORITY, Courses.PATH, COURSES);
        uriMatcher.addURI(NoteAppProviderContract.AUTHORITY,Notes.PATH_EXPANDED, NOTES_EXPANDED);
        uriMatcher.addURI(NoteAppProviderContract.AUTHORITY,Notes.PATH+"/#", NOTES_ROW);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int row=-1;
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case NOTES_ROW:
                long rowId = ContentUris.parseId(uri);
                String rselection = NoteInfoEntry._ID +"= ?";
                String rselectionArgs[] ={Long.toString(rowId)};
                row = db.delete(NoteInfoEntry.TABLE_NAME,rselection,rselectionArgs);

        }
        return row;
    }

    @Override
    public String getType(Uri uri) {
        String mimeType=null;
        switch (uriMatcher.match(uri)){
            case COURSES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + MIME_VENDOR_TYPE + Courses.PATH;
                break;
            case NOTES:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+MIME_VENDOR_TYPE+Notes.PATH;
                break;
            case NOTES_EXPANDED:
                mimeType = ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+MIME_VENDOR_TYPE+Notes.PATH_EXPANDED;
                break;
            case NOTES_ROW:
                mimeType = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/"+MIME_VENDOR_TYPE+Notes.PATH;
        }
        return mimeType;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        long row = -1;
        Uri rowUri = null;
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case NOTES:
                row = db.insert(NoteInfoEntry.TABLE_NAME,null,values);
                rowUri=ContentUris.withAppendedId(Notes.CONTENT_URI,row);
                break;
            case COURSES:
                row = db.insert(CourseInfoEntry.TABLE_NAME,null,values);
                rowUri=ContentUris.withAppendedId(Notes.CONTENT_URI,row);

                break;
            case NOTES_EXPANDED:

                break;

        }
        return rowUri;
    }

    @Override
    public boolean onCreate() {
        mDbOpenHelper = new NoteAppOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor=null;
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case COURSES:

                cursor = db.query(CourseInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTES:

                cursor = db.query(NoteInfoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case NOTES_EXPANDED:
                cursor = notesExpandedQuery(db,projection,selection,selectionArgs,sortOrder);
                break;
            case NOTES_ROW:
                long rowId = ContentUris.parseId(uri);
                String rselection = NoteInfoEntry._ID +"= ?";
                String rselectionArgs[] ={Long.toString(rowId)};
                cursor=db.query(NoteInfoEntry.TABLE_NAME,projection,rselection,rselectionArgs,null,null,null);
        }
        return cursor;

    }

    private Cursor notesExpandedQuery(SQLiteDatabase db, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String columns[] = new String[projection.length];
        for(int i = 0;i<projection.length;i++){
            columns[i]=projection[i].equals(CourseIdColumn.COLUMN_COURSE_ID)||
                    projection[i].equals(BaseColumns._ID)?NoteInfoEntry.getQname(projection[i]):projection[i];
        }
        String tableWithJoin = NoteInfoEntry.TABLE_NAME+" JOIN "+ CourseInfoEntry.TABLE_NAME+" ON "+
                NoteInfoEntry.getQname(NoteInfoEntry.COLUMN_COURSE_ID)+" = "+CourseInfoEntry.getQname(CourseInfoEntry.COLUMN_COURSE_ID);
        return db.query(tableWithJoin,columns,selection,selectionArgs,null,null,sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int row=-1;
        SQLiteDatabase db = mDbOpenHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)){
            case NOTES_ROW:
                long rowId = ContentUris.parseId(uri);
                String rselection = NoteInfoEntry._ID +"= ?";
                String rselectionArgs[] ={Long.toString(rowId)};
                row = db.update(NoteInfoEntry.TABLE_NAME,values,rselection,rselectionArgs);

        }
        return row;
    }
}
