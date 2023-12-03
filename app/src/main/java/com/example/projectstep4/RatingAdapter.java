package com.example.projectstep4;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
//Adapter class for the rating passengers so you can select the items
public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.NameViewHolder>{
    public interface OnItemClickListener {
        void onItemClick(String selectedName);
    }

    private List<String> names;
    private SparseBooleanArray selectedItems;
    private OnItemClickListener itemClickListener;

    public RatingAdapter(List<String> names, OnItemClickListener listener) {
        this.names = names;
        this.selectedItems = new SparseBooleanArray();
        this.itemClickListener = listener;
    }

    @NonNull
    @Override
    public NameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new NameViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull NameViewHolder holder, int position) {
        String name = names.get(position);
        holder.textName.setText(name);


        holder.itemView.setOnClickListener(v -> {
            String selectedName = names.get(position);
            itemClickListener.onItemClick(selectedName);
        });

        holder.itemView.setActivated(selectedItems.get(position, false));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }


    private void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    public static class NameViewHolder extends RecyclerView.ViewHolder {
        public TextView textName;

        public NameViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
        }
    }
}
