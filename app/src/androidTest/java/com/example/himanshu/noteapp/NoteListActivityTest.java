package com.example.himanshu.noteapp;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static org.hamcrest.Matchers.*;

import static org.junit.Assert.*;
@RunWith(AndroidJUnit4.class)
public class NoteListActivityTest {

    static DataManager sDataManager;
    @BeforeClass
    public static void initDataManager(){
        sDataManager = DataManager.getInstance();
    }
    @Before
    public void setUp(){

        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }
    @Rule
    public ActivityTestRule<MainActivity> mNoteListActivity = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void createNote(){
        CourseInfo course = sDataManager.getCourse("android_intents");
        String noteTitle = "Working with intents";
        String noteText = "Intents are cool as we can open an activity through an intent";



        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner_courses)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());
        onView(withId(R.id.spinner_courses))
                .check(matches(withSpinnerText(course.getTitle())));
        onView(withId(R.id.text_note_title)).perform(typeText(noteTitle), ViewActions.closeSoftKeyboard())
                .check(matches(withText(containsString(noteTitle))));
        onView(withId(R.id.text_note_text)).perform(typeText(noteText),ViewActions.closeSoftKeyboard());

        //Check an Assertion
        onView(withId(R.id.text_note_text)).check(matches(withText(containsString(noteText))));
        Espresso.pressBack();

        int index = sDataManager.getNotes().size()-1;
        NoteInfo note = sDataManager.getNotes().get(index);
        assertEquals(course,note.getCourse());
        assertEquals(noteTitle,note.getTitle());
        assertEquals(noteText,note.getText());

    }
    @Test
    public void checkNextNotesButton() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));
        onView(withId(R.id.note_list)).perform(RecyclerViewActions.
                <NoteListActivityAdapter.ViewHolder>actionOnItemAtPosition(0,click()));
        List<NoteInfo> notes = DataManager.getInstance().getNotes();

        for(int i=0; i<=notes.size()-1;i++){
            NoteInfo singleNote = notes.get(i);
            onView(withId(R.id.spinner_courses)).check(matches(withSpinnerText(singleNote.getCourse().getTitle())));
            onView(withId(R.id.text_note_title)).check(matches(withText(singleNote.getTitle())));
            onView(withId(R.id.text_note_text)).check(matches(withText(containsString(singleNote.getText()))));

            if(i<notes.size()-1)
                onView(allOf(withId(R.id.action_next),isEnabled())).perform(click());


        }
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())));
        Espresso.pressBack();
    }
}