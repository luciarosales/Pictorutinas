package com.example.pictorutinas.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictorutinas.R;
import com.example.pictorutinas.data.RoutineRepository;
import com.example.pictorutinas.model.Routine;
import com.example.pictorutinas.ui.create.CreateRoutineActivity;
import com.example.pictorutinas.ui.create.EditRoutineActivity; // Tu clase de edición
import com.example.pictorutinas.ui.view.ViewRoutineActivity; // Tu clase de visualización
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RoutineRepository repo;
    private RecyclerView rv;
    private EditText etSearch;
    private View llEmptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        repo = new RoutineRepository(this);
        rv = findViewById(R.id.rvRoutines);
        etSearch = findViewById(R.id.etSearchQuery);
        llEmptyState = findViewById(R.id.llEmptyState);

        rv.setLayoutManager(new LinearLayoutManager(this));


        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRoutines(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });


        findViewById(R.id.fabAdd).setOnClickListener(v ->
                startActivity(new Intent(this, CreateRoutineActivity.class)));

        loadData();
    }

    private void filterRoutines(String query) {
        List<Routine> filteredList = repo.searchRoutinesByName(query);
        updateRecyclerView(filteredList);
    }

    private void loadData() {
        try {
            String query = etSearch.getText().toString();
            List<Routine> routines = query.isEmpty() ? repo.getAllRoutines() : repo.searchRoutinesByName(query);
            updateRecyclerView(routines);
        } catch (Exception e) {
            Toast.makeText(this, R.string.error_db, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateRecyclerView(List<Routine> list) {

        if (list.isEmpty()) {
            llEmptyState.setVisibility(View.VISIBLE);
            rv.setVisibility(View.GONE);
        } else {
            llEmptyState.setVisibility(View.GONE);
            rv.setVisibility(View.VISIBLE);
        }


        RoutineAdapter adapter = new RoutineAdapter(
                list,
                r -> {
                    Intent i = new Intent(this, ViewRoutineActivity.class);
                    i.putExtra("id", r.getId());
                    i.putExtra("name", r.getName());
                    startActivity(i);
                },
                r -> {
                    showDeleteDialog(r);
                    return true;
                },
                (Routine r) -> {
                    Intent i = new Intent(this, EditRoutineActivity.class);
                    i.putExtra("routine_id", r.getId());
                    startActivity(i);
                }
        );
        rv.setAdapter(adapter);
    }

    private void showDeleteDialog(Routine r) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.delete_confirm)
                .setMessage(R.string.delete_confirm_msg)
                .setPositiveButton(R.string.delete, (d, w) -> {
                    try {
                        repo.deleteRoutine(r.getId());
                        loadData(); // Refrescar lista
                        Snackbar.make(rv, R.string.routine_deleted, Snackbar.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(this, R.string.error_delete, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}