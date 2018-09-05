package com.example.himanshu.noteapp;

import android.provider.BaseColumns;

import java.sql.Statement;

public final class NoteAppDatabaseContract{
    private NoteAppDatabaseContract(){};

     public static final class CourseInfoEntry implements BaseColumns{
        public static final String TABLE_NAME = "course_info";
         public static final String COLUMN_COURSE_ID = "course_id";
         public static final String COLUMN_COURSE_TITLE = "course_title";
         public static final String INDEX_TABLE  = TABLE_NAME+"index1";
         public static final String CREATE_INDEX = "CREATE INDEX "+INDEX_TABLE+" ON "+TABLE_NAME+"("+COLUMN_COURSE_TITLE+")";
         public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+_ID+" INTEGER PRIMARY KEY,"
                +COLUMN_COURSE_ID+" TEXT UNIQUE NOT NULL,"+
                COLUMN_COURSE_TITLE+" TEXT NOT NULL)";
        public static String getQname(String column){
            return TABLE_NAME+"."+column;
        }
    }
     public static final class NoteInfoEntry implements BaseColumns{
         public static final String TABLE_NAME = "note_info";
         public static final String COLUMN_NOTE_TITLE = "note_title";
         public static final String COLUMN_NOTE_TEXT = "note_text";
         public static final String COLUMN_COURSE_ID = "course_id";
         public static final String INDEX_TABLE  = TABLE_NAME+"index1";
         public static final String CREATE_INDEX = "CREATE INDEX "+INDEX_TABLE+" ON "+TABLE_NAME+"("+COLUMN_NOTE_TITLE+")";
        public static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+"("+_ID+" INTEGER PRIMARY KEY,"
                +COLUMN_NOTE_TITLE+" TEXT NOT NULL,"+COLUMN_NOTE_TEXT+" TEXT,"+COLUMN_COURSE_ID+" TEXT NOT NULL)";
         public static String getQname(String column){
             return TABLE_NAME+"."+column;
         }
     }
}
