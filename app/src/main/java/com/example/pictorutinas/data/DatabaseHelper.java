package com.example.pictorutinas.data;
//Hola de nuevo :)
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "pictorutinas.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE rutinas (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, created_at INTEGER)");
        db.execSQL("CREATE TABLE pasos (id INTEGER PRIMARY KEY AUTOINCREMENT, rutina_id INTEGER NOT NULL, " +
                "texto_key TEXT NOT NULL, imagen_res TEXT NOT NULL, orden INTEGER NOT NULL, " +
                "FOREIGN KEY(rutina_id) REFERENCES rutinas(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS pasos");
        db.execSQL("DROP TABLE IF EXISTS rutinas");
        onCreate(db);
    }
}