package com.example.pictorutinas.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pictorutinas.R;
import com.example.pictorutinas.data.RoutineRepository;
import com.example.pictorutinas.model.Routine;
import com.example.pictorutinas.ui.create.CreateRoutineActivity;
import com.example.pictorutinas.ui.view.ViewRoutineActivity;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RoutineRepository repo;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repo = new RoutineRepository(this);
        rv = findViewById(R.id.rvRoutines);
        rv.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.fabAdd).setOnClickListener(v ->
                startActivity(new Intent(this, CreateRoutineActivity.class)));

        loadData();
    }

    private void loadData() {
        List<Routine> routines = repo.getAllRoutines();
        RoutineAdapter adapter = new RoutineAdapter(routines,
                r -> {
                    Intent i = new Intent(this, ViewRoutineActivity.class);
                    i.putExtra("id", r.getId());
                    i.putExtra("name", r.getName());
                    startActivity(i);
                },
                r -> {
                    showDeleteDialog(r);
                    return true;
                });
        rv.setAdapter(adapter);
    }

    private void showDeleteDialog(Routine r) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    repo.deleteRoutine(r.getId());
                    loadData();
                    Snackbar.make(rv, R.string.routine_deleted, Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, null).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}