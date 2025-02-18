package com.example.kanban_desk_app;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

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

            if (boardNameIndex != -1 && boardIdIndex != -1) {
                String boardName = cursor.getString(boardNameIndex);
                int boardId = cursor.getInt(boardIdIndex);
                holder.boardNameTextView.setText(boardName);

                // Установка обработчика для кнопки "три точки"
             // ImageButton myButton = holder.itemView.findViewById(R.id.my_button); // Замените на ваш ID кнопки
                holder.btn_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //showMenu(view, cursor.getInt(boardIdIndex)); // Вызов метода showMenu
                        showMenu(view, boardId);
                    }
                });

                // Получение задач для текущей доски
                Cursor tasksCursor = dbHelper.getTasksByBoardId(boardId);
                TasksAdapter tasksAdapter = new TasksAdapter(context, tasksCursor, dbHelper, boardId); // Передаем boardId
                holder.tasksRecyclerView.setAdapter(tasksAdapter);
            }
        }
    }


    // Добавляем метод showMenu
    private void showMenu(View view, int boardId) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.board_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_add_task) {
                Log.d("BoardsAdapter", "Передан boardId для добавления задания: " + boardId);
                ((MainActivity) context).showAddTaskDialog(boardId);
                return true;
            } else if (menuItem.getItemId() == R.id.action_delete_board) {
                dbHelper.deleteBoard(boardId);
                ((MainActivity) context).deleteBoard(boardId); // Вызов метода из MainActivity
                return true;
            } else if (menuItem.getItemId() == R.id.action_edit_board) {
                // Получаем текущее имя доски и запускаем диалог редактирования
                String currentName = getCurrentBoardName(boardId);
                if (currentName != null) { // Проверяем, что имя доски найдено
                    ((MainActivity) context).showEditBoardDialog(boardId, currentName);
                }
                return true;
            } else {
                return false; // В случае, если ничего не выбрано
            }
        });

        popupMenu.show(); // Показать меню
    }

    private String getCurrentBoardName(int boardId) {
        Cursor cursor = dbHelper.getAllBoards();
        String boardName = null; // Хранит имя доски

        // Определите индексы до начала цикла
        int boardIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_ID);
        int boardNameIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_NAME);

        // Проверяем, что индексы действительны
        if (boardIdIndex == -1 || boardNameIndex == -1) {
            Log.e("BoardsAdapter", "Column index not found for BOARD_ID or BOARD_NAME");
            cursor.close();
            return null; // Или выбросьте исключение, если это критично
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(boardIdIndex);

            if (id == boardId) {
                boardName = cursor.getString(boardNameIndex);
                break; // Найдено имя доски, выходим из цикла
            }
        }

        cursor.close(); // Закрываем курсор
        return boardName; // Возвращаем имя (или null, если не найдено)
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView boardNameTextView;
        RecyclerView tasksRecyclerView;
        ImageButton btn_menu;

        public ViewHolder(View itemView) {
            super(itemView);
            boardNameTextView = itemView.findViewById(R.id.text_view_board_name);
            tasksRecyclerView = itemView.findViewById(R.id.recyclerView_tasks);
            tasksRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
            btn_menu = itemView.findViewById(R.id.btn_menu); // Убедитесь, что идентификатор правильный
        }
    }
    public void updateTasks(Cursor tasksCursor) {
        this.cursor = tasksCursor;
        notifyDataSetChanged();
    }

    public boolean isActiveBoardAtPosition(int position, int boardId) {
        if (cursor.moveToPosition(position)) {
            int currentBoardIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_BOARD_ID);
            if (currentBoardIdIndex != -1) {
                int currentBoardId = cursor.getInt(currentBoardIdIndex);
                return currentBoardId == boardId;
            }
        }
        return false;
    }
}

