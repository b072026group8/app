package com.cscb07.taamapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.textViewName.setText(item.getArtifactName());
        holder.textViewCategory.setText(item.getCategory());
        holder.textViewMaterial.setText(item.getMaterial());
        holder.textViewDynastyPeriod.setText(item.getDynastyPeriod());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewCategory, textViewMaterial, textViewDynastyPeriod;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewMaterial = itemView.findViewById(R.id.textViewMaterial);
            textViewDynastyPeriod = itemView.findViewById(R.id.textViewDynastyPeriod);
        }
    }
}
