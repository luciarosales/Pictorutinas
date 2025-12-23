package com.example.pictorutinas.ui.view;

import android.os.Bundle;
import android.os.Handler;      // Necesario para el retraso
import android.os.Looper;       // Necesario para el retraso
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
            new AlertDialog.Builder(this)
                    .setMessage(R.string.no_steps)
                    .setPositiveButton("OK", (d, w) -> finish())
                    .show();
            return;
        }

        img = findViewById(R.id.imgViewPicto);
        tvDesc = findViewById(R.id.tvViewDesc);
        tvCount = findViewById(R.id.tvCounter);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);

        updateUI();

        // Configuración del botón Siguiente / Finalizar
        btnNext.setOnClickListener(v -> {
            if (index < steps.size() - 1) {
                // Si quedan pasos, avanzamos normalmente
                index++;
                updateUI();
            } else {
                // Si es el último paso, mostramos notificación y volvemos al inicio
                Snackbar.make(img, R.string.completed, Snackbar.LENGTH_LONG).show();

                // Deshabilitamos botones para evitar múltiples clics durante la espera
                btnNext.setEnabled(false);
                btnPrev.setEnabled(false);

                // Esperamos 2 segundos para que el usuario lea el mensaje y cerramos
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    finish(); // Cierra esta actividad y vuelve a MainActivity
                }, 2000);
            }
        });

        // Configuración del botón Anterior
        btnPrev.setOnClickListener(v -> {
            if (index > 0) {
                index--;
                updateUI();
            }
        });
    }

    private void updateUI() {
        Step s = steps.get(index);

        // Carga dinámica de la imagen y el texto
        int imageResId = getResources().getIdentifier(s.getImageResName(), "drawable", getPackageName());
        int textResId = getResources().getIdentifier(s.getTextKey(), "string", getPackageName());

        img.setImageResource(imageResId);
        tvDesc.setText(getString(textResId));

        // Actualiza el contador (ej: "Paso 1 de 5")
        tvCount.setText(getString(R.string.step_x_of_n, index + 1, steps.size()));

        // Solo activamos el botón atrás si no estamos en el primer paso
        btnPrev.setEnabled(index > 0);
    }
}