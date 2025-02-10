package com.example.kanban_desk_app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private Context context;
    private Cursor cursor;

    public TasksAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            int taskNameIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_TASK_NAME);
            int taskDescriptionIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_TASK_DESCRIPTION);
            int taskDateIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_TASK_DATE);

            if (taskNameIndex != -1 && taskDescriptionIndex != -1 && taskDateIndex != -1) {
                String taskName = cursor.getString(taskNameIndex);
                String taskDescription = cursor.getString(taskDescriptionIndex);
                String taskDate = cursor.getString(taskDateIndex);

                holder.taskNameTextView.setText(taskName);
                holder.taskDescriptionTextView.setText(taskDescription);
                holder.taskDateTextView.setText(taskDate);
            } else {
                Log.e("TasksAdapter", "Column index not found for TASK_NAME, TASK_DESCRIPTION or TASK_DATE");
                // Можно установить текст по умолчанию в TextView или обработать ошибку
                holder.taskNameTextView.setText("Неизвестная задача");
                holder.taskDescriptionTextView.setText("Нет описания");
                holder.taskDateTextView.setText("Неизвестная дата");
            }
        }
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTextView;
        TextView taskDescriptionTextView;
        TextView taskDateTextView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskNameTextView = itemView.findViewById(R.id.text_view_task_name);
            taskDescriptionTextView = itemView.findViewById(R.id.text_view_task_description);
            taskDateTextView = itemView.findViewById(R.id.text_view_task_date);
        }
    }
}
