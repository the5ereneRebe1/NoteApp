package com.example.himanshu.noteapp;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.example.himanshu.noteapp.NoteAppDatabaseContract.CourseInfoEntry;
import com.example.himanshu.noteapp.NoteAppDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final String NOTE_ID = "com.jwhh.jim.notekeeper.NOTE_ID";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.jwhh.jim.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.jwhh.jim.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.jwhh.jim.notekeeper.ORIGINAL_NOTE_TEXT";
    public static final int ID_NOT_SET = -1;
    public static final int NOTES_LOADER = 0;
    public static final int COURSE_LOADER = 1;
    private NoteInfo mNote = new NoteInfo(DataManager.getInstance().getCourses().get(0), "", "");
    private boolean mIsNewNote;
    private Spinner mSpinnerCourses;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private int mNoteId;
    private boolean mIsCancelling;
    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;
    private NoteAppOpenHelper mOpenDbHelper;
    private Cursor cursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;
    private SimpleCursorAdapter adapterCourses;
    private boolean courseLoaderQueryFinished;
    private boolean notesLoaderQueryFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mOpenDbHelper = new NoteAppOpenHelper(this);
        mSpinnerCourses = (Spinner) findViewById(R.id.spinner_courses);


        adapterCourses = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,null,
                new String[] {CourseInfoEntry.COLUMN_COURSE_TITLE},new int[]{android.R.id.text1},0);
        adapterCourses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapterCourses);

        //loadCourseData();
        getLoaderManager().initLoader(COURSE_LOADER,null,this);
        readDisplayStateValues();
        if(savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteTitle = (EditText) findViewById(R.id.text_note_title);
        mTextNoteText = (EditText) findViewById(R.id.text_note_text);

        if(!mIsNewNote)
            getLoaderManager().initLoader(NOTES_LOADER,null,this);
    }

    private void loadCourseData() {
        SQLiteDatabase db = mOpenDbHelper.getReadableDatabase();
        String courseColumns[] = {
                CourseInfoEntry.COLUMN_COURSE_TITLE,CourseInfoEntry.COLUMN_COURSE_ID,CourseInfoEntry._ID};
        Cursor courseCursor = db.query(CourseInfoEntry.TABLE_NAME,courseColumns,null,null,null,null,CourseInfoEntry.COLUMN_COURSE_TITLE);
        adapterCourses.changeCursor(courseCursor);
    }

    private void loadNoteData() {
        SQLiteDatabase db = mOpenDbHelper.getReadableDatabase();
        String selection = NoteInfoEntry._ID +"= ?";
        String selectionArgs[] ={Integer.toString(mNoteId)};
        String[] noteColumns = {NoteInfoEntry.COLUMN_COURSE_ID,NoteInfoEntry.COLUMN_NOTE_TITLE,NoteInfoEntry.COLUMN_NOTE_TEXT};
        cursor = db.query(NoteInfoEntry.TABLE_NAME,noteColumns,selection,selectionArgs,null,null,null);
        mCourseIdPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        cursor.moveToNext();
        displayNote();
    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }

    private void saveOriginalNoteValues() {
        if(mIsNewNote)
            return;
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
        mOriginalNoteTitle = mNote.getTitle();
        mOriginalNoteText = mNote.getText();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling) {
            if(mIsNewNote) {
                DataManager.getInstance().removeNote(mNoteId);
            } else {
                storePreviousNoteValues();
            }
        } else {
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void displayNote() {

        String courseId = cursor.getString(mCourseIdPos);

        int courseIndex = getCourseIndexOf(courseId);
        mSpinnerCourses.setSelection(courseIndex);
        mTextNoteTitle.setText(cursor.getString(mNoteTitlePos));
        mTextNoteText.setText(cursor.getString(mNoteTextPos));
    }

    private int getCourseIndexOf(String courseId) {
        Cursor courseCursor = adapterCourses.getCursor();
        int courseindex=0;
        int columnIndex = courseCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_ID);
        boolean move = courseCursor.moveToFirst();
        while(move){
            if(courseId.equals(courseCursor.getString(columnIndex))){
                break;
            }
            courseindex++;
            move = courseCursor.moveToNext();
        }
        return courseindex;
    }

    private void readDisplayStateValues() {
        Intent intent = getIntent();
        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mIsNewNote = mNoteId == ID_NOT_SET;
        if(mIsNewNote) {
            createNewNote();
        }

        Log.i("TAG", "mNoteId: " + mNoteId);
//        mNote = DataManager.getInstance().getNotes().get(mNoteId);

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteId = dm.createNewNote();
        mNote = dm.getNotes().get(mNoteId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_mail) {
            sendEmail();
            return true;
        } else if (id == R.id.action_cancel) {
            mIsCancelling = true;
            finish();
        }
        else if(id == R.id.action_next){
            nextItem();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNoteId <lastIndex);

        return super.onPrepareOptionsMenu(menu);
    }

    private void nextItem() {
        saveNote();

        ++mNoteId;
        mNote=DataManager.getInstance().getNotes().get(mNoteId);
        saveOriginalNoteValues();
        displayNote();
        invalidateOptionsMenu();

    }



    private void sendEmail() {
        CourseInfo course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String text = "Checkout what I learned in the Pluralsight course \"" +
                course.getTitle() +"\"\n" + mTextNoteText.getText().toString();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id == NOTES_LOADER){
            loader = createLoaderNotes();
        }
        else if(id==COURSE_LOADER){
            loader = createLoaderCourse();
        }
        return loader;
    }

    private CursorLoader createLoaderCourse() {
        courseLoaderQueryFinished = false;
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase db = mOpenDbHelper.getReadableDatabase();
                String courseColumns[] = {
                        CourseInfoEntry.COLUMN_COURSE_TITLE,CourseInfoEntry.COLUMN_COURSE_ID,CourseInfoEntry._ID};
                return db.query(CourseInfoEntry.TABLE_NAME,courseColumns,null,null,null,null,CourseInfoEntry.COLUMN_COURSE_TITLE);

            }
        };
    }

    private CursorLoader createLoaderNotes() {
        notesLoaderQueryFinished = false;
        return new CursorLoader(this){
            @Override
            public Cursor loadInBackground() {
                SQLiteDatabase db = mOpenDbHelper.getReadableDatabase();
                String selection = NoteInfoEntry._ID +"= ?";
                String selectionArgs[] ={Integer.toString(mNoteId)};
                String[] noteColumns = {NoteInfoEntry.COLUMN_COURSE_ID,NoteInfoEntry.COLUMN_NOTE_TITLE,NoteInfoEntry.COLUMN_NOTE_TEXT};
                return db.query(NoteInfoEntry.TABLE_NAME,noteColumns,selection,selectionArgs,null,null,null);

            }
        };

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId()==NOTES_LOADER){
            loadFinishedNotes(data);
        }
        if(loader.getId()==COURSE_LOADER){
            adapterCourses.changeCursor(data);
            courseLoaderQueryFinished = true;
            displayNotesWhenQueryFinished();
        }
    }

    private void loadFinishedNotes(Cursor data) {
        cursor=data;
        mCourseIdPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = cursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        cursor.moveToNext();
        notesLoaderQueryFinished = true;
        displayNotesWhenQueryFinished();
    }

    private void displayNotesWhenQueryFinished() {
        if(notesLoaderQueryFinished && courseLoaderQueryFinished){
            displayNote();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId()==NOTES_LOADER){
            if(cursor!=null){
                cursor.close();
            }
        }
        else if(loader.getId()==COURSE_LOADER){
            adapterCourses.changeCursor(null);
        }
    }
}












