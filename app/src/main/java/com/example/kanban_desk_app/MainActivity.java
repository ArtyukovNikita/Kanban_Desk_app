package com.example.kanban_desk_app;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

        FloatingActionButton fab = findViewById(R.id.fab_add_board);
        fab.setOnClickListener(view -> {
            // Код для добавления новой доски
        });
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

}
