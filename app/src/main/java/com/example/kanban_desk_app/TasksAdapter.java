package com.example.kanban_desk_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.TaskViewHolder> {
    private Context context;
    private Cursor cursor;
    private DataBaseHelper dbHelper;

    public TasksAdapter(Context context, Cursor cursor, DataBaseHelper dbHelper) {
        this.context = context;
        this.cursor = cursor;
        this.dbHelper = dbHelper; // Инициализация
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
            int taskIdIndex = cursor.getColumnIndex(DataBaseHelper.COLUMN_TASK_ID); // Не забудьте об идентификаторе задания


            if (taskNameIndex != -1 && taskDescriptionIndex != -1 && taskDateIndex != -1) {
                String taskName = cursor.getString(taskNameIndex);
                String taskDescription = cursor.getString(taskDescriptionIndex);
                String taskDate = cursor.getString(taskDateIndex);
                final int taskId = cursor.getInt(taskIdIndex); // Сохраняем идентификатор задания


                holder.taskNameTextView.setText(taskName);
                holder.taskDescriptionTextView.setText(taskDescription);
                holder.taskDateTextView.setText(taskDate);

                // Обработка длительного нажатия
                holder.itemView.setOnLongClickListener(v -> {
                    showEditTaskDialog(taskId, taskName, taskDescription, taskDate);
                    return true; // Возвращаем true, если обработано
                });
            } else {
                Log.e("TasksAdapter", "Column index not found for TASK_NAME, TASK_DESCRIPTION or TASK_DATE");
                // Установка текста по умолчанию в TextView
                holder.taskNameTextView.setText("Неизвестная задача");
                holder.taskDescriptionTextView.setText("Нет описания");
                holder.taskDateTextView.setText("Неизвестная дата");
            }
        }
    }

    private void showEditTaskDialog(int taskId, String currentName, String currentDescription, String currentDate) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Редактировать задание");

        // Создаем компоновку для диалогового окна
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);

        // Поле для названия задания
        final EditText inputName = new EditText(context);
        inputName.setHint("Название задания");
        inputName.setText(currentName);
        layout.addView(inputName); // Добавляем в компоновку

        // Поле для описания задания
        final EditText inputDescription = new EditText(context);
        inputDescription.setHint("Описание задания");
        inputDescription.setText(currentDescription);
        layout.addView(inputDescription); // Добавляем в компоновку

        // Поле для даты задания
        final EditText inputDate = new EditText(context);
        inputDate.setHint("Дата задания");
        inputDate.setText(currentDate);
        layout.addView(inputDate); // Добавляем в компоновку

        // Устанавливаем компоновку в диалог
        builder.setView(layout);

        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString().trim();
                String description = inputDescription.getText().toString().trim();
                String date = inputDate.getText().toString().trim();

                // Проверка на пустое название
                if (!name.isEmpty()) {
                    dbHelper.updateTaskName(taskId, name);
                    dbHelper.updateTaskDescription(taskId, description);
                    dbHelper.updateTaskDate(taskId, date);


                    // Обновите данные адаптера, чтобы новые значения отобразились
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Название задания не может быть пустым", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public Cursor getCursor() {
        return cursor; // Возвращаем текущий курсор
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
