package com.example.himanshu.noteapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class NoteListActivityAdapter extends RecyclerView.Adapter<NoteListActivityAdapter.ViewHolder>{

    private final List<NoteInfo> mNotes;
    private Context context;
    private LayoutInflater layoutInflater;

    public NoteListActivityAdapter(Context context, List<NoteInfo> notes) {
        mNotes = notes;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NoteInfo note = mNotes.get(position);
        holder.tvCourse.setText(note.getCourse().getTitle());
        holder.tvTitle.setText(note.getTitle());
        holder.tvText.setText(note.getText());
        holder.notePosition=position;
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCourse;
        private final TextView tvTitle;
        private final TextView tvText;
        private int notePosition;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView)itemView.findViewById(R.id.tv_course);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvText = (TextView)itemView.findViewById(R.id.tv_text);
            LinearLayout noteArea = (LinearLayout)itemView.findViewById(R.id.note_area);
            final ImageButton start = (ImageButton)itemView.findViewById(R.id.favourite);
            noteArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NoteActivity.class);
//                NoteInfo note = (NoteInfo) listNotes.getItemAtPosition(position);
                    intent.putExtra(NoteActivity.NOTE_POSITION, notePosition);
                    context.startActivity(intent);
                }
            });
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    start.setImageResource(R.drawable.ic_star_black_24dp);
                }
            });

        }
    }
}
