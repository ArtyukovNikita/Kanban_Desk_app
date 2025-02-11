package com.example.kanban_desk_app;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private DataBaseHelper dbHelper;
    private RecyclerView boardsRecyclerView;
    private BoardsAdapter boardsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DataBaseHelper(this);
        boardsRecyclerView = findViewById(R.id.recyclerView_boards);
        boardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadBoards();

        FloatingActionButton fab = findViewById(R.id.fab_add_board); // Исправлено на FloatingActionButton
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddBoardDialog();
            }
        });
    }

    private void showAddBoardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить доску");

        // Поле ввода для названия доски
        final EditText inputName = new EditText(this);
        inputName.setHint("Название доски");

        // Создание компоновки для диалога
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        builder.setView(layout);

        builder.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString().trim();
                // Проверка на пустое название
                if (name.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Название доски не может быть пустым", Toast.LENGTH_SHORT).show();
                    return;
                }
                addBoard(name); // Добавляем доску в базу данных
            }
        });

        builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel(); // Закрыть диалог
            }
        });

        builder.show(); // Показать диалог
    }


    private void addBoard(String name) {
        // Вставка в базу данных
        dbHelper.addBoard(name); // ваш метод для добавления в базу
        loadBoards(); // Обновить отображение досок
    }

    private void loadBoards() {
        Cursor cursor = dbHelper.getAllBoards();
        if (cursor != null && cursor.getCount() > 0) {
            boardsAdapter = new BoardsAdapter(this, cursor);
            boardsRecyclerView.setAdapter(boardsAdapter);
        } else {
            // Обработка случая, когда нет доступных досок
            Toast.makeText(this, "Нет доступных досок", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteBoard(int boardId) {
        dbHelper.deleteBoard(boardId); // Ваш метод для удаления доски
        loadBoards(); // Обновление списка досок
    }


}
