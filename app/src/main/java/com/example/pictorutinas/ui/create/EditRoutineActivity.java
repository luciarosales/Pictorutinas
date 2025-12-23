package com.example.pictorutinas.ui.create;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pictorutinas.R;
import com.example.pictorutinas.data.RoutineRepository;
import com.example.pictorutinas.model.Routine;
import com.example.pictorutinas.model.Step;

import java.util.ArrayList;
import java.util.List;

public class EditRoutineActivity extends AppCompatActivity {
    private long routineId;
    private RoutineRepository repo;
    private EditText etName;

    // Listas y Adaptadores (Igual que en la creación)
    private List<Step> selectedSteps = new ArrayList<>();
    private SelectedStepsAdapter stepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Muestra la flecha
            getSupportActionBar().setTitle(getString(R.string.edit_routine));
        }

        repo = new RoutineRepository(this);
        etName = findViewById(R.id.etRoutineName);

        // 1. Configurar RECYCLERVIEW DE OPCIONES (Arriba - Cuadrícula)
        RecyclerView rvPictos = findViewById(R.id.rvAvailablePictos);
        rvPictos.setLayoutManager(new GridLayoutManager(this, 4));
        rvPictos.setAdapter(new PictogramAdapter(getAvailablePictos(), step -> {
            // Al pulsar arriba, se añade a la lista de abajo
            selectedSteps.add(new Step(step.getTextKey(), step.getImageResName()));
            stepsAdapter.notifyDataSetChanged();
        }));

        // 2. Configurar RECYCLERVIEW SELECCIONADOS (Abajo - Horizontal)
        RecyclerView rvSelected = findViewById(R.id.rvSelectedSteps);
        rvSelected.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        stepsAdapter = new SelectedStepsAdapter(selectedSteps, pos -> {
            // Al pulsar en el botón de eliminar de abajo, se quita
            selectedSteps.remove(pos);
            stepsAdapter.notifyDataSetChanged();
        });
        rvSelected.setAdapter(stepsAdapter);

        // 3. Cargar los datos actuales de la rutina
        routineId = getIntent().getLongExtra("routine_id", -1);
        if (routineId != -1) {
            loadInitialData();
        } else {
            finish();
        }

        // 4. Botón de Actualizar
        findViewById(R.id.btnUpdate).setOnClickListener(v -> updateRoutine());
    }

    private void loadInitialData() {
        Routine r = repo.getRoutineById(routineId);
        if (r != null) {
            etName.setText(r.getName());

            // Cargamos los pasos que ya existen en la base de datos
            List<Step> dbSteps = repo.getStepsByRoutineId(routineId);
            if (dbSteps != null) {
                selectedSteps.clear();
                selectedSteps.addAll(dbSteps);
                stepsAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRoutine() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty() || selectedSteps.isEmpty()) {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.error_validation)
                    .setPositiveButton("OK", null)
                    .show();
            return;
        }

        // Guardamos los cambios
        repo.updateRoutineWithSteps(routineId, name, selectedSteps);

        Toast.makeText(this, R.string.routine_updated, Toast.LENGTH_SHORT).show();
        finish();
    }

    private List<Step> getAvailablePictos() {
        List<Step> list = new ArrayList<>();
        String[] keys = {"picto_brush_teeth", "picto_shower", "picto_eat", "picto_sleep", "picto_dress", "picto_school", "picto_wash_hands", "picto_play"};
        for (String k : keys) list.add(new Step(k, k));
        return list;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra la actividad actual y vuelve a la principal
        return true;
    }
}