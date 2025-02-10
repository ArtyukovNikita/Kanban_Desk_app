package com.example.kanban_desk_app;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BoardsAdapter extends RecyclerView.Adapter<BoardsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;

    public BoardsAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
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
            int columnIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_NAME);
            if (columnIndex != -1) { // Убедитесь, что индекс допустимый
                String boardName = cursor.getString(columnIndex);
                holder.boardNameTextView.setText(boardName);
            } else {
                // Обработка ошибки: индекс не найден
                holder.boardNameTextView.setText("Неизвестное название");
            }

            holder.itemView.setOnClickListener(v -> {
                // Код для отображения задания по доске
            });
        }
    }


    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView boardNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            boardNameTextView = itemView.findViewById(R.id.text_view_board_name);
        }
    }
}
