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

    public List<Step> getStepsByRoutineId(long routineId) {
        List<Step> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Consultamos la tabla "pasos" usando "rutina_id" como filtro
        Cursor c = db.query("pasos", null, "rutina_id = ?",
                new String[]{String.valueOf(routineId)}, null, null, "orden ASC");

        if (c != null && c.moveToFirst()) {
            do {
                // Índices según tu onCreate: 0:id, 1:rutina_id, 2:texto_key, 3:imagen_res, 4:orden
                long id = c.getLong(0);
                String texto = c.getString(2);
                String imagen = c.getString(3);

                list.add(new Step(texto, imagen));
            } while (c.moveToNext());
            c.close();
        }
        return list;
    }

    public void updateRoutine(long id, String nuevoNombre) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // ContentValues es como un contenedor para los datos que vamos a actualizar
        ContentValues values = new ContentValues();
        values.put("nombre", nuevoNombre); // "nombre" es el nombre de tu columna en la DB

        // Ejecutamos la actualización
        // El tercer parámetro "id=?" es el filtro para que solo actualice ESA rutina
        db.update("rutinas", values, "id=?", new String[]{String.valueOf(id)});
    }

    public void deleteRoutine(long id) {
        dbHelper.getWritableDatabase().delete("rutinas", "id=?", new String[]{String.valueOf(id)});
    }

    public Routine getRoutineById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Routine routine = null;

        // Consultamos la tabla de rutinas buscando por el ID
        Cursor cursor = db.query("rutinas", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Creamos el objeto Routine con los datos de la base de datos
            // Asegúrate de que los índices (0, 1) coinciden con tus columnas id y name
            routine = new Routine(cursor.getLong(0), cursor.getString(1));
            cursor.close();
        }
        return routine;
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

    public void updateRoutineWithSteps(long routineId, String nuevoNombre, List<Step> nuevosPasos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Iniciamos una transacción para que se guarde todo o nada
        db.beginTransaction();
        try {
            // 1. Actualizar el nombre de la rutina
            ContentValues values = new ContentValues();
            values.put("nombre", nuevoNombre);
            db.update("rutinas", values, "id = ?", new String[]{String.valueOf(routineId)});

            // 2. Borrar los pasos antiguos de esta rutina
            db.delete("pasos", "rutina_id = ?", new String[]{String.valueOf(routineId)});

            // 3. Insertar los nuevos pasos (los que están ahora en la lista)
            for (int i = 0; i < nuevosPasos.size(); i++) {
                Step s = nuevosPasos.get(i);
                ContentValues stepValues = new ContentValues();
                stepValues.put("rutina_id", routineId);
                stepValues.put("texto_key", s.getTextKey());
                stepValues.put("imagen_res", s.getImageResName());
                stepValues.put("orden", i); // Guardamos el orden actual
                db.insert("pasos", null, stepValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
}