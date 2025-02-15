package com.example.kanban_desk_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kanban.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица досок
    public static final String TABLE_BOARDS = "boards";
    public static final String COLUMN_BOARD_ID = "board_id";
    public static final String COLUMN_BOARD_NAME = "board_name";

    // Таблица заданий
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_TASK_BOARD_ID = "board_id";
    public static final String COLUMN_TASK_ID = "task_id";
    public static final String COLUMN_TASK_NAME = "task_name";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";
    public static final String COLUMN_TASK_DATE = "task_date";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы досок
        String createBoardsTable = "CREATE TABLE " + TABLE_BOARDS + " (" +
                COLUMN_BOARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOARD_NAME + " TEXT NOT NULL)";
        db.execSQL(createBoardsTable);

        // Создание таблицы заданий
        String createTasksTable = "CREATE TABLE " + TABLE_TASKS + " (" +
                COLUMN_TASK_BOARD_ID + " INTEGER, " +
                COLUMN_TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TASK_NAME + " TEXT NOT NULL, " +
                COLUMN_TASK_DESCRIPTION + " TEXT, " +
                COLUMN_TASK_DATE + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TASK_BOARD_ID + ") REFERENCES " + TABLE_BOARDS + "(" + COLUMN_BOARD_ID + "))";
        db.execSQL(createTasksTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOARDS);
        onCreate(db);
    }

    // Методы для работы с таблицей досок
    public void addBoard(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_BOARD_NAME, name); // только имя доски
        db.insert(TABLE_BOARDS, null, contentValues);
        db.close();
    }

    public void deleteBoard(int boardId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BOARDS, COLUMN_BOARD_ID + "=?", new String[]{String.valueOf(boardId)});
        db.close();
    }

    public void updateBoardName(int boardId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOARD_NAME, newName);
        db.update(TABLE_BOARDS, values, COLUMN_BOARD_ID + "=?", new String[]{String.valueOf(boardId)});
        db.close();
    }


    // Методы для работы с таблицей заданий
    public void addTask(int boardId, String taskName, String taskDescription, String taskDate) {
        //Log.d("AddTask", "Добавление задания к доске с ID: " + boardId);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_BOARD_ID, boardId);
        values.put(COLUMN_TASK_NAME, taskName);
        values.put(COLUMN_TASK_DESCRIPTION, taskDescription);
        values.put(COLUMN_TASK_DATE, taskDate);
        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void updateTaskName(int taskId, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_NAME, newName);
        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void updateTaskDescription(int taskId, String newDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DESCRIPTION, newDescription);
        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    public void updateTaskDate(int taskId, String newDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TASK_DATE, newDate);
        db.update(TABLE_TASKS, values, COLUMN_TASK_ID + "=?", new String[]{String.valueOf(taskId)});
        db.close();
    }

    // Получение всех досок
    public Cursor getAllBoards() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_BOARDS, null);
    }

    // Получение всех заданий по id доски
    public Cursor getTasksByBoardId(int boardId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_TASKS + " WHERE " + COLUMN_TASK_BOARD_ID + "=?", new String[]{String.valueOf(boardId)});
    }

    public void addBoard(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("board_name", name); // Название столбца для имени доски
        contentValues.put("board_description", description); // Название столбца для описания доски
        db.insert(TABLE_BOARDS, null, contentValues);
        db.close();
    }

}
