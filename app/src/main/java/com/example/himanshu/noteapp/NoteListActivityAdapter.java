package com.example.himanshu.noteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCourse;
        private final TextView tvTitle;
        private final TextView tvText;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView)itemView.findViewById(R.id.tv_course);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvText = (TextView)itemView.findViewById(R.id.tv_text);
        }
    }
}