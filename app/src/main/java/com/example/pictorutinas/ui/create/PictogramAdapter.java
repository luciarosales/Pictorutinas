package com.example.pictorutinas.ui.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pictorutinas.R;
import com.example.pictorutinas.model.Step;
import java.util.List;

public class PictogramAdapter extends RecyclerView.Adapter<PictogramAdapter.ViewHolder> {
    private List<Step> availableSteps;
    private OnPictoClickListener listener;

    public interface OnPictoClickListener { void onSelected(Step s); }

    public PictogramAdapter(List<Step> list, OnPictoClickListener l) {
        this.availableSteps = list;
        this.listener = l;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pictogram, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Step s = availableSteps.get(position);
        int resId = holder.itemView.getContext().getResources()
                .getIdentifier(s.getImageResName(), "drawable", holder.itemView.getContext().getPackageName());
        holder.img.setImageResource(resId);
        holder.itemView.setOnClickListener(v -> listener.onSelected(s));
    }

    @Override
    public int getItemCount() { return availableSteps.size(); }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        ViewHolder(View v) { super(v); img = v.findViewById(R.id.imgPictoIcon); }
    }
}