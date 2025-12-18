package com.example.pictorutinas.ui.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pictorutinas.R;
import com.example.pictorutinas.model.Step;
import java.util.List;

public class SelectedStepsAdapter extends RecyclerView.Adapter<SelectedStepsAdapter.ViewHolder> {
    private List<Step> selected;
    private OnRemoveClickListener listener;

    public interface OnRemoveClickListener { void onRemove(int position); }

    public SelectedStepsAdapter(List<Step> selected, OnRemoveClickListener l) {
        this.selected = selected;
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_step, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step s = selected.get(position);
        int resId = holder.itemView.getContext().getResources()
                .getIdentifier(s.getImageResName(), "drawable", holder.itemView.getContext().getPackageName());
        holder.img.setImageResource(resId);
        holder.btnRemove.setOnClickListener(v -> listener.onRemove(position));
    }

    @Override
    public int getItemCount() { return selected.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ImageButton btnRemove;
        ViewHolder(View v) {
            super(v);
            img = v.findViewById(R.id.imgSelected);
            btnRemove = v.findViewById(R.id.btnRemoveStep);
        }
    }
}