package com.example.himanshu.noteapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.himanshu.noteapp.NoteAppDatabaseContract.CourseInfoEntry;
import com.example.himanshu.noteapp.NoteAppDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteListActivityAdapter extends RecyclerView.Adapter<NoteListActivityAdapter.ViewHolder>{

    private Cursor mCursor;
    private Context context;
    private LayoutInflater layoutInflater;
    private int mCoursePos;
    private int mNoteTitlePos;
    private int mNoteTextPos;
    private int mIdpos;

    public NoteListActivityAdapter(Context context, Cursor cursor) {
        mCursor= cursor;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        populateColumnViews();
    }

    private void populateColumnViews() {
        if(mCursor==null)
            return;
        mCoursePos = mCursor.getColumnIndex(CourseInfoEntry.COLUMN_COURSE_TITLE);
        mNoteTitlePos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mIdpos = mCursor.getColumnIndex(NoteInfoEntry._ID);

    }
    public void changeCursor(Cursor cursor){
        if(mCursor!=null)
            mCursor.close();
        mCursor=cursor;
        populateColumnViews();
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String course = mCursor.getString(mCoursePos);
        String title= mCursor.getString(mNoteTitlePos);
        String text = mCursor.getString(mNoteTextPos);
        int id= mCursor.getInt(mIdpos);
        holder.tvCourse.setText(course);
        holder.tvTitle.setText(title);
        holder.tvText.setText(text);
        holder.noteid=id;
    }

    @Override
    public int getItemCount() {
        return mCursor==null?0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCourse;
        private final TextView tvTitle;
        private final TextView tvText;
        private int noteid;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView)itemView.findViewById(R.id.tv_course);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvText = (TextView)itemView.findViewById(R.id.tv_text);
            Group group = (Group) itemView.findViewById(R.id.group2);
            final ImageView start = (ImageView) itemView.findViewById(R.id.favourite);
            int refIds[] = group.getReferencedIds();
            for (final int id : refIds) {
                itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // your code here.
                        Intent intent = new Intent(context, NoteActivity.class);
//                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                        intent.putExtra(NoteActivity.NOTE_ID,noteid);
                        context.startActivity(intent);
                    }
                });
            }
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start.setImageResource(R.drawable.ic_star_black_24dp);
                }
            });

        }
    }
}
