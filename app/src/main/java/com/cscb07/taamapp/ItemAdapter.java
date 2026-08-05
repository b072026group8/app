package com.cscb07.taamapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.bumptech.glide.Glide;
import android.widget.ImageView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private static final String TAG = "ItemAdapter";
    private List<Item> itemList;
    private FragmentTransaction transaction;
    @Nullable
    private LayoutOverrides layoutOverrides;
    private int popBackStackId = -1;
    public int getPopBackStackId() { return popBackStackId; }
    public void setPopBackStackId(int popBackStackId) {
        this.popBackStackId = popBackStackId;
    }

    private int itemLimit = 0;  // Let 0 represent "All"

    public ItemAdapter(List<Item> itemList, FragmentTransaction transaction) {
        this(itemList, transaction, null);
    }
    public ItemAdapter(List<Item> itemList, FragmentTransaction transaction, @Nullable LayoutOverrides layoutOverrides) {
        this.itemList = itemList;
        this.transaction = transaction;
        this.layoutOverrides = layoutOverrides;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_adapater, parent, false);
        if (layoutOverrides != null && view.getLayoutParams() == null) {
            Log.e(TAG, "created view's layout params are null, cannot set overrides");
        } else if (layoutOverrides != null) {
            if (layoutOverrides.useWidthOverride()) {
                view.getLayoutParams().width = layoutOverrides.getWidthOverride();
            }
        }
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Log.i(TAG, "binding view holder on index: " + position);
        Item item = itemList.get(position);
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("lotNumber", item.getLotNumber());
                bundle.putInt(ExpandedArtifactViewFragment.ARG_POP_BACK_ID, popBackStackId);
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
        Glide.with(holder.itemView.getContext())
                .load(item.getImage())
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        if (itemList == null) return 0;
        int total = itemList.size();

        // If item limit set is 0 or its greater than total amt of artifacts, than display all
        if (itemLimit <= 0 || itemLimit >= total) {
            return total;
        }
        return itemLimit;
    }

    public void setItemLimit(int limit) {
        this.itemLimit = limit;
        notifyDataSetChanged();
    }
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView content;
        ImageView imageView;
        TextView textViewName, textViewCategory, textViewMaterial, textViewDynastyPeriod;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewCategory = itemView.findViewById(R.id.textViewCategory);
            textViewMaterial = itemView.findViewById(R.id.textViewMaterial);
            textViewDynastyPeriod = itemView.findViewById(R.id.textViewDynastyPeriod);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    /**
     * Holds settings for {@link ItemViewHolder} for overriding default layout values.
     */
    public static class LayoutOverrides {
        private final boolean useWidthOverride;
        private final int widthOverride;

        public LayoutOverrides() {
            useWidthOverride = false;
            widthOverride = -1;
        }
        public LayoutOverrides(int newWidth) {
            useWidthOverride = true;
            widthOverride = newWidth;
        }

        public int getWidthOverride() {
            return widthOverride;
        }
        public boolean useWidthOverride() {
            return useWidthOverride;
        }
    }
}
