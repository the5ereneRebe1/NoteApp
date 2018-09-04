package com.example.himanshu.noteapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        mDbopenHelper = new NoteAppOpenHelper(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NoteActivity.class));
            }
        });
        PreferenceManager.setDefaultValues(this,R.xml.pref_general,false);
        initializeDisplayContent();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        mDbopenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteListActivityAdapter.notifyDataSetChanged();
        setNavigationHeader();
    }

    private void setNavigationHeader() {
        NavigationView nav = (NavigationView)findViewById(R.id.nav_view);
        View header = nav.getHeaderView(0);
        TextView userName = (TextView)header.findViewById(R.id.tv_user_name);
        TextView userEmail = (TextView)header.findViewById(R.id.tv_user_email);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String user = pref.getString("user_display_name"," ");
        String email = pref.getString("user_email"," ");
        userName.setText(user);
        userEmail.setText(email);
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
                intent.putExtra(NoteActivity.NOTE_POSITION, position);
                startActivity(intent);
            }
        });*/
        DataManager.loadFromDatabase(mDbopenHelper);
        recyclerView = (RecyclerView)findViewById(R.id.note_list);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this,2);
        displayNotes();
    }

    private void displayNotes() {
        recyclerView.setLayoutManager(linearLayoutManager);
        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        noteListActivityAdapter = new NoteListActivityAdapter(this,notes);
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
}
