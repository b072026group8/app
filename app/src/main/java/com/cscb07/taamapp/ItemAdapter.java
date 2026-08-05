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

/**
 * RecyclerView adapter for displaying artifact cards on the home screen.
 * Handles binding artifact information and navigating to the expanded artifact view.
 */
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

    /**
     * Creates an adapter for displaying artifacts.
     *
     * @param itemList the list of artifacts to display
     * @param transaction the fragment transaction used for navigation
     */
    public ItemAdapter(List<Item> itemList, FragmentTransaction transaction) {
        this(itemList, transaction, null);
    }

    /**
     * Creates an adapter with optional layout overrides.
     *
     * @param itemList the list of artifacts to display
     * @param transaction the fragment transaction used for navigation
     * @param layoutOverrides optional layout overrides for artifact cards
     */
    public ItemAdapter(List<Item> itemList, FragmentTransaction transaction, @Nullable LayoutOverrides layoutOverrides) {
        this.itemList = itemList;
        this.transaction = transaction;
        this.layoutOverrides = layoutOverrides;
    }

    /**
     * Creates a ViewHolder for displaying an artifact card.
     *
     * @param parent the parent view group
     * @param viewType the type of view
     * @return a new ItemViewHolder
     */
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

    /**
     * Binds artifact data to a ViewHolder and handles navigation
     * to the expanded artifact view when an artifact is selected.
     *
     * @param holder the ViewHolder to bind
     * @param position the position of the artifact
     */
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

    /**
     * Returns the number of artifacts currently displayed.
     *
     * @return the number of artifacts
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * ViewHolder containing the views used to display
     * a single artifact card.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        CardView content;
        ImageView imageView;
        TextView textViewName, textViewCategory, textViewMaterial, textViewDynastyPeriod;

        /**
         * Creates a ViewHolder for an artifact card.
         *
         * @param itemView the artifact card view
         */
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
