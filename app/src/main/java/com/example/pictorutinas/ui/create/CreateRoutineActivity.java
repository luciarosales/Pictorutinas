package com.example.pictorutinas.ui.create;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pictorutinas.R;
import com.example.pictorutinas.data.RoutineRepository;
import com.example.pictorutinas.model.Step;
import java.util.ArrayList;
import java.util.List;

public class CreateRoutineActivity extends AppCompatActivity {
    private List<Step> selectedSteps = new ArrayList<>();
    private SelectedStepsAdapter stepsAdapter;
    private EditText etName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_routine);

        etName = findViewById(R.id.etRoutineName);
        RecyclerView rvPictos = findViewById(R.id.rvAvailablePictos);
        RecyclerView rvSelected = findViewById(R.id.rvSelectedSteps);

        // Grid de 4 columnas para pictogramas disponibles
        rvPictos.setLayoutManager(new GridLayoutManager(this, 4));
        rvPictos.setAdapter(new PictogramAdapter(getAvailablePictos(), step -> {
            selectedSteps.add(new Step(step.getTextKey(), step.getImageResName()));
            stepsAdapter.notifyDataSetChanged();
        }));

        stepsAdapter = new SelectedStepsAdapter(selectedSteps, pos -> {
            selectedSteps.remove(pos);
            stepsAdapter.notifyDataSetChanged();
        });
        rvSelected.setAdapter(stepsAdapter);

        findViewById(R.id.btnSave).setOnClickListener(v -> saveRoutine());
    }

    private void saveRoutine() {
        String name = etName.getText().toString().trim();
        if (name.isEmpty() || selectedSteps.isEmpty()) {
            new AlertDialog.Builder(this).setMessage(R.string.error_validation).setPositiveButton("OK", null).show();
            return;
        }
        new RoutineRepository(this).insertRoutine(name, selectedSteps);
        Toast.makeText(this, R.string.routine_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private List<Step> getAvailablePictos() {
        List<Step> list = new ArrayList<>();
        String[] keys = {"picto_brush_teeth", "picto_shower", "picto_eat", "picto_sleep", "picto_dress", "picto_school", "picto_wash_hands", "picto_play"};
        for (String k : keys) list.add(new Step(k, k));
        return list;
    }
}