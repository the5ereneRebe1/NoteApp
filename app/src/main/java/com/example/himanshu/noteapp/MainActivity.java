package com.example.himanshu.noteapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.himanshu.noteapp.NoteAppDatabaseContract.CourseInfoEntry;
import com.example.himanshu.noteapp.NoteAppDatabaseContract.NoteInfoEntry;
import com.example.himanshu.noteapp.NoteAppProviderContract.Notes;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    public static final int NOTES_LOADER = 0;
    private NoteListActivityAdapter noteListActivityAdapter;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private NoteAppOpenHelper mDbopenHelper;
    private GridLayoutManager gridLayoutManager;
    private CourseListActivityAdapter courseListActivityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setStrictModePolicy();

        mDbopenHelper = new NoteAppOpenHelper(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        //PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);
        initializeDisplayContent();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setStrictModePolicy() {
        if(BuildConfig.DEBUG){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build();
            StrictMode.setThreadPolicy(policy);

        }
    }

    @Override
    protected void onDestroy() {
        mDbopenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //loadNotes();
        getLoaderManager().restartLoader(NOTES_LOADER,null,this);
        setNavigationHeader();
        openNavigationDrawer();

    }

    private void openNavigationDrawer() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(Gravity.START);
            }
        },1000);
    }
public void swapList(List<Object> list){
        Object temp=list.get(0);
        list.add(0,list.get(list.size()-1));
        list.add(list.size()-1,temp);
}
  /*  private void loadNotes() {
        SQLiteDatabase db = mDbopenHelper.getReadableDatabase();
        String[] noteColumns = {NoteInfoEntry.COLUMN_NOTE_TITLE, NoteInfoEntry.COLUMN_NOTE_TEXT, NoteInfoEntry.COLUMN_COURSE_ID, NoteInfoEntry._ID};
        Cursor noteQuery = db.query(NoteInfoEntry.TABLE_NAME, noteColumns, null, null, null, null, NoteInfoEntry.COLUMN_COURSE_ID+","+ NoteInfoEntry.COLUMN_NOTE_TITLE);
        noteListActivityAdapter.changeCursor(noteQuery);
    }*/

    private void setNavigationHeader() {
        AsyncTask<Context,Void,SharedPreferences> task = new AsyncTask<Context, Void, SharedPreferences>() {
            @Override
            protected SharedPreferences doInBackground(Context... contexts) {
                Context context = contexts[0];
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
                return pref;
            }

            @Override
            protected void onPostExecute(SharedPreferences pref) {
                NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
                View header = nav.getHeaderView(0);
                TextView userName = (TextView)header.findViewById(R.id.tv_user_name);
                TextView userEmail = (TextView)header.findViewById(R.id.tv_user_email);
                String user = pref.getString("user_display_name"," ");
                String email = pref.getString("user_email"," ");
                userName.setText(user);
                userEmail.setText(email);
            }
        };

    }

    private void initializeDisplayContent() {
       /* final ListView listNotes = (ListView) findViewById(R.id.list_notes);

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        mAdapterNotes = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);

        listNotes.setAdapter(mAdapterNotes);

        listNotes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NoteListActivity.this, NoteActivity.class);
//                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                intent.putExtra(NoteActivity.NOTE_ID, position);
                startActivity(intent);
            }
        });*/
        //DataManager.loadFromDatabase(mDbopenHelper);
        recyclerView = (RecyclerView)findViewById(R.id.note_list);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this,2);
        displayNotes();
    }

    private void displayNotes() {
        recyclerView.setLayoutManager(linearLayoutManager);

        noteListActivityAdapter = new NoteListActivityAdapter(this,null);
        recyclerView.setAdapter(noteListActivityAdapter);


        SetNavigationMenuItemChecked(R.id.nav_notes);
    }
    private void displayCourses(){
        recyclerView.setLayoutManager(gridLayoutManager);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        courseListActivityAdapter = new CourseListActivityAdapter(this,courses);
        recyclerView.setAdapter(courseListActivityAdapter);

        SetNavigationMenuItemChecked(R.id.nav_courses);
    }
    private void SetNavigationMenuItemChecked(int id) {
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        menu.findItem(id).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        else if(id==R.id.action_backup){
            Intent intent = new Intent(this,BackupIntentService.class);
            intent.putExtra(BackupIntentService.EXTRA_COURSE_ID,NoteBackup.ALL_COURSES);
            startService(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notes) {
            displayNotes();
        } else if (id == R.id.nav_courses) {
            displayCourses();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if(id==NOTES_LOADER){
            String[] noteColumns = {Notes.COLUMN_NOTE_TITLE,
                            Notes.COLUMN_NOTE_TEXT,
                            NoteInfoEntry.getQname(NoteInfoEntry._ID),
                            Notes.COLUMN_COURSE_TITLE};
            String orderBy = Notes.COLUMN_COURSE_TITLE + "," + Notes.COLUMN_NOTE_TITLE;
            loader =new CursorLoader(this, Notes.CONTENT_URI_EXPANDED,noteColumns,null,null,orderBy);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(loader.getId()==NOTES_LOADER){
            noteListActivityAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if(loader.getId()==NOTES_LOADER){
            noteListActivityAdapter.changeCursor(null);
        }
    }
}
