package com.example.kanban_desk_app;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.ViewHolder> {
    private Context context;
    private Cursor cursor;
    private DataBaseHelper dbHelper; // Добавляем поле для DataBaseHelper

    public BoardsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
        this.dbHelper = new DataBaseHelper(context); // Инициализация dbHelper
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (cursor.moveToPosition(position)) {
            int boardNameIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_NAME);
            int boardIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_ID);

            if (boardNameIndex != -1 && boardIdIndex != -1) { // Проверка на корректность индекса
                String boardName = cursor.getString(boardNameIndex);
                holder.boardNameTextView.setText(boardName);

                int boardId = cursor.getInt(boardIdIndex);

                // Получение задач для текущей доски
                Cursor tasksCursor = dbHelper.getTasksByBoardId(boardId);
                TasksAdapter tasksAdapter = new TasksAdapter(context, tasksCursor);
                holder.tasksRecyclerView.setAdapter(tasksAdapter);
            } else {
                // Обработка случая, когда имя столбца не найдено
                Log.e("BoardsAdapter", "Column index not found for BOARD_NAME or BOARD_ID");
            }
        }
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView boardNameTextView;
        RecyclerView tasksRecyclerView;

        public ViewHolder(View itemView) {
            super(itemView);
            boardNameTextView = itemView.findViewById(R.id.text_view_board_name);
            tasksRecyclerView = itemView.findViewById(R.id.recyclerView_tasks);
            tasksRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        }
    }
}

