package com.example.pictorutinas.ui.create;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    private List<Step> selectedSteps = new ArrayList<>();
    private SelectedStepsAdapter stepsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_routine);

        // --- SOLUCIÓN PARA EL CORTE SUPERIOR ---
        // Buscamos la vista raíz por el ID "main" que definimos en el XML
        View mainView = findViewById(R.id.main);
        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Aplicamos padding superior e inferior dinámico
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // ---------------------------------------

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.edit_routine));
        }

        repo = new RoutineRepository(this);
        etName = findViewById(R.id.etRoutineName);

        // 1. Configurar RECYCLERVIEW DE OPCIONES
        RecyclerView rvPictos = findViewById(R.id.rvAvailablePictos);
        rvPictos.setLayoutManager(new GridLayoutManager(this, 4));
        rvPictos.setAdapter(new PictogramAdapter(getAvailablePictos(), step -> {
            selectedSteps.add(new Step(step.getTextKey(), step.getImageResName()));
            stepsAdapter.notifyDataSetChanged();
        }));

        // 2. Configurar RECYCLERVIEW SELECCIONADOS
        RecyclerView rvSelected = findViewById(R.id.rvSelectedSteps);
        rvSelected.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        stepsAdapter = new SelectedStepsAdapter(selectedSteps, pos -> {
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

        // 4. Botón de Modificar
        findViewById(R.id.btnUpdate).setOnClickListener(v -> updateRoutine());
    }

    private void loadInitialData() {
        Routine r = repo.getRoutineById(routineId);
        if (r != null) {
            etName.setText(r.getName());
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
        finish();
        return true;
    }
}