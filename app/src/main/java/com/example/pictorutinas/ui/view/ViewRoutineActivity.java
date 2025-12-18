package com.example.pictorutinas.ui.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.pictorutinas.R;
import com.example.pictorutinas.data.RoutineRepository;
import com.example.pictorutinas.model.Step;
import com.google.android.material.snackbar.Snackbar;
import java.util.List;

public class ViewRoutineActivity extends AppCompatActivity {
    private List<Step> steps;
    private int index = 0;
    private ImageView img;
    private TextView tvDesc, tvCount;
    private Button btnPrev, btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_routine);

        long id = getIntent().getLongExtra("id", -1);
        steps = new RoutineRepository(this).getStepsForRoutine(id);

        if (steps.isEmpty()) {
            new AlertDialog.Builder(this).setMessage(R.string.no_steps).setPositiveButton("OK", (d, w) -> finish()).show();
            return;
        }

        img = findViewById(R.id.imgViewPicto);
        tvDesc = findViewById(R.id.tvViewDesc);
        tvCount = findViewById(R.id.tvCounter);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        updateUI();

        btnNext.setOnClickListener(v -> {
            if (index < steps.size() - 1) { index++; updateUI(); }
            else { Snackbar.make(img, R.string.completed, Snackbar.LENGTH_LONG).show(); }
        });
        btnPrev.setOnClickListener(v -> { if (index > 0) { index--; updateUI(); } });
    }

    private void updateUI() {
        Step s = steps.get(index);
        img.setImageResource(getResources().getIdentifier(s.getImageResName(), "drawable", getPackageName()));
        tvDesc.setText(getString(getResources().getIdentifier(s.getTextKey(), "string", getPackageName())));
        tvCount.setText(getString(R.string.step_x_of_n, index + 1, steps.size()));
        btnPrev.setEnabled(index > 0);
    }
}