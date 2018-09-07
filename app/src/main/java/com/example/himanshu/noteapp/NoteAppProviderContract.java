package com.example.himanshu.noteapp;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NoteAppProviderContract {
    private NoteAppProviderContract(){}
    public static final String AUTHORITY = "com.example.himanshu.noteapp.provider";
    public static final Uri AUTHORITY_URI= Uri.parse("content://"+AUTHORITY);

    protected interface CourseIdColumn{
        public static final String COLUMN_COURSE_ID = "course_id";
    }
    protected interface CourseColumns{
        public static final String COLUMN_COURSE_TITLE = "course_title";
    }
    protected interface NoteColumns{
        public static final String COLUMN_NOTE_TITLE = "note_title";
        public static final String COLUMN_NOTE_TEXT = "note_text";

    }

    public static final class Courses implements BaseColumns,CourseIdColumn,CourseColumns{
        public static final String PATH = "courses";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH);
    }
    public static final class Notes implements BaseColumns,NoteColumns,CourseIdColumn,CourseColumns{
        public static final String PATH = "notes";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI,PATH);
        public static final String PATH_EXPANDED = "notes_expanded";
        public static final Uri CONTENT_URI_EXPANDED = Uri.withAppendedPath(AUTHORITY_URI,PATH_EXPANDED);
    }
}
