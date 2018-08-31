package com.example.himanshu.noteapp;

import android.provider.ContactsContract;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
    private static DataManager sDataManager;

    @BeforeClass
    public static void initDatamanager(){
        sDataManager =DataManager.getInstance();
    }
    @Before
    public void setUp(){

        sDataManager.getNotes().clear();
        sDataManager.initializeExampleNotes();
    }
    @Test
    public void createNewNote() {

        CourseInfo course = sDataManager.getCourse("android_intents");
        String noteTitle = "Working with intents";
        String noteText = "Intents are cool as we can open an activity through an intent";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo note = sDataManager.getNotes().get(noteIndex);
        note.setCourse(course);
        note.setTitle(noteTitle);
        note.setText(noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);
        assertEquals(course,compareNote.getCourse());
        assertEquals(noteTitle,compareNote.getTitle());
        assertEquals(noteText,compareNote.getText());

    }
    @Test
    public void findSimilarNotes(){

        CourseInfo course = sDataManager.getCourse("android_intents");
        String noteTitle = "Working with intents";
        String noteText1 = "Intents are cool as we can open an activity through an intent";
        String noteText2 = "Intents are cool as we can open an activity through an intent at the second time also.";

        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo note1 = sDataManager.getNotes().get(noteIndex1);
        note1.setCourse(course);
        note1.setTitle(noteTitle);
        note1.setText(noteText1);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo note2 = sDataManager.getNotes().get(noteIndex2);
        note2.setCourse(course);
        note2.setTitle(noteTitle);
        note2.setText(noteText2);

        int index1 = sDataManager.findNote(note1);
        assertEquals(noteIndex1,index1);

        int index2 = sDataManager.findNote(note2);
        assertEquals(noteIndex2,index2);
    }
    @Test
    public void removeNote(){
        CourseInfo course = sDataManager.getCourse("android_intents");
        String noteTitle = "Working with intents";
        String noteText = "Intents are cool as we can open an activity through an intent";

        int newNoteIndex = sDataManager.createNewNote();
        NoteInfo note = sDataManager.getNotes().get(newNoteIndex);
        note.setCourse(course);
        note.setTitle(noteTitle);
        note.setText(noteText);
        sDataManager.removeNote(newNoteIndex);

        int index = sDataManager.findNote(note);
        assertEquals(-1,index);

    }
    @Test
    public void checkNoteCount(){
        CourseInfo course = sDataManager.getCourse("java_lang");
        String noteTitle = "Working with intents";
        String noteText = "Intents are cool as we can open an activity through an intent";

        int noteCount = sDataManager.getNoteCount(course);
        int newNoteIndex = sDataManager.createNewNote();
        NoteInfo note = sDataManager.getNotes().get(newNoteIndex);
        note.setCourse(course);
        note.setTitle(noteTitle);
        note.setText(noteText);

        int newNoteCount = sDataManager.getNoteCount(course);
        assertEquals(noteCount+1,newNoteCount);

    }
}