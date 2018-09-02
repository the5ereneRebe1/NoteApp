package com.example.himanshu.noteapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CourseListActivityAdapter extends RecyclerView.Adapter<CourseListActivityAdapter.ViewHolder>{

    private final List<CourseInfo> mCourse;
    private Context context;
    private LayoutInflater layoutInflater;

    public CourseListActivityAdapter(Context context, List<CourseInfo> course) {
        mCourse = course;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.list_item_course,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CourseInfo course = mCourse.get(position);
        holder.tvCourse.setText(course.getTitle());

        holder.coursePosition =position;
    }

    @Override
    public int getItemCount() {
        return mCourse.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCourse;

        private int coursePosition;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCourse = (TextView)itemView.findViewById(R.id.tv_course);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Snackbar.make(v,mCourse.get(coursePosition).getTitle(),Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }
}
