package com.example.pictorutinas.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pictorutinas.R;
import com.example.pictorutinas.model.Routine;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {
    private final List<Routine> list;
    private final OnRoutineClickListener listener;
    private final OnRoutineLongClickListener longListener;

    public interface OnRoutineClickListener { void onClick(Routine r); }
    public interface OnRoutineLongClickListener { boolean onLongClick(Routine r); }

    public RoutineAdapter(List<Routine> list, OnRoutineClickListener l, OnRoutineLongClickListener ll) {
        this.list = list;
        this.listener = l;
        this.longListener = ll;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_routine, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Routine r = list.get(position);
        holder.tv.setText(r.getName());
        holder.itemView.setOnClickListener(v -> listener.onClick(r));
        holder.itemView.setOnLongClickListener(v -> longListener.onLongClick(r));
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        ViewHolder(View v) { super(v); tv = v.findViewById(R.id.tvRoutineName); }
    }
}