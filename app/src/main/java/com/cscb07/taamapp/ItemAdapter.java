package com.cscb07.taamapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemList;
    private FragmentTransaction transaction;

    public ItemAdapter(List<Item> itemList, FragmentTransaction transaction) {
        this.itemList = itemList;
        this.transaction = transaction;
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
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("lotNumber", item.getLotNumber());
                ExpandedArtifactViewFragment expanded = new ExpandedArtifactViewFragment();
                expanded.setArguments(bundle);
                transaction.replace(R.id.fragment_container, expanded);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
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
        CardView content;
        TextView textViewName, textViewCategory, textViewMaterial, textViewDynastyPeriod;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewMaterial = itemView.findViewById(R.id.textViewMaterial);
            textViewDynastyPeriod = itemView.findViewById(R.id.textViewDynastyPeriod);
        }
    }
}
