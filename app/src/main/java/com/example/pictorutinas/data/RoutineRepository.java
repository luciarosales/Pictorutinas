package com.example.pictorutinas.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.pictorutinas.model.Routine;
import com.example.pictorutinas.model.Step;
import java.util.ArrayList;
import java.util.List;

public class RoutineRepository {
    private DatabaseHelper dbHelper;

    public RoutineRepository(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insertRoutine(String name, List<Step> steps) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues v = new ContentValues();
            v.put("nombre", name);
            v.put("created_at", System.currentTimeMillis());
            long routineId = db.insert("rutinas", null, v);

            for (int i = 0; i < steps.size(); i++) {
                Step s = steps.get(i);
                ContentValues vs = new ContentValues();
                vs.put("rutina_id", routineId);
                vs.put("texto_key", s.getTextKey());
                vs.put("imagen_res", s.getImageResName());
                vs.put("orden", i);
                db.insert("pasos", null, vs);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Routine> getAllRoutines() {
        List<Routine> list = new ArrayList<>();
        Cursor c = dbHelper.getReadableDatabase().query("rutinas", null, null, null, null, null, "created_at DESC");
        while (c.moveToNext()) list.add(new Routine(c.getLong(0), c.getString(1)));
        c.close();
        return list;
    }

    public List<Step> getStepsForRoutine(long id) {
        List<Step> list = new ArrayList<>();
        Cursor c = dbHelper.getReadableDatabase().query("pasos", null, "rutina_id=?", new String[]{String.valueOf(id)}, null, null, "orden ASC");
        while (c.moveToNext()) list.add(new Step(c.getString(2), c.getString(3)));
        c.close();
        return list;
    }

    public void deleteRoutine(long id) {
        dbHelper.getWritableDatabase().delete("rutinas", "id=?", new String[]{String.valueOf(id)});
    }

    public List<Routine> searchRoutinesByName(String query) {
        List<Routine> list = new ArrayList<>();
        String selection = "nombre LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor c = dbHelper.getReadableDatabase().query(
                "rutinas",
                null,
                selection,
                selectionArgs,
                null,
                null,
                "created_at DESC"
        );

        while (c.moveToNext()) {
            list.add(new Routine(c.getLong(0), c.getString(1)));
        }
        c.close();
        return list;
    }
}