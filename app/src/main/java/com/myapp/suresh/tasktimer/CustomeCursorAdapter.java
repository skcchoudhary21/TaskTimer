package com.myapp.suresh.tasktimer;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomeCursorAdapter extends CursorAdapter {

    public CustomeCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // when the view will be created for first time,
        // we need to tell the adapters, how each item will look
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View retView = inflater.inflate(R.layout.task_list, parent, false);
        return retView;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // here we are setting our data
        // that means, take the data from the cursor and put it in views

        //TextView tvEmpid = (TextView) view.findViewById(R.id.etEmpId);
        //tvEmpid.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));
        TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
        tvDate.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1))));


        TextView tvTask = (TextView) view.findViewById(R.id.tvTask);
        tvTask.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2))));

        TextView tvProject = (TextView) view.findViewById(R.id.tvProject);
        tvTask.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(3))));

        TextView tvHours = (TextView) view.findViewById(R.id.tvTimeTaken);
        tvHours.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4))));

        TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
        tvTime.setText(cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5))));
    }
}