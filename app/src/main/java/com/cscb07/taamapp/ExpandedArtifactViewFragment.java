package com.cscb07.taamapp;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ExpandedArtifactViewFragment extends Fragment{
    private static final String Tag = "ExpandedArtifactViewFragment";
    public static final String ARG_POP_BACK_ID = Tag + "-popBackId";
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String lot;
    private float relatedArtifactViewWidth = 150;
    private int popBackId = -1;

    /** Gets the id of the state in the {@link androidx.fragment.app.FragmentManager} to
     * pop back to on the home button, or a negative number */
    public int getPopBackId() {
        return popBackId;
    }

    /** Sets the id of the state in the {@link androidx.fragment.app.FragmentManager} to
     * pop back to on the home button.
     * <p>
     * If negative, the current stat is popped back once.
     */
    public void setPopBackId(int popBackId) {
        this.popBackId = popBackId;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_expanded_artifact_view, container,false);
        ImageButton homeButton = view.findViewById(R.id.expandedViewHomeButton);
        db = FirebaseDatabase.getInstance();
        Bundle args = getArguments();
        if (args != null) {
        lot = args.getString("lotNumber");
            popBackId = args.getInt(ARG_POP_BACK_ID);
        ref = db.getReference("artifacts").child(lot);
        Button deleteButton = view.findViewById(R.id.button);
        TextView name = view.findViewById(R.id.name);
        TextView lotNum = view.findViewById(R.id.Lotnum);
        TextView description = view.findViewById(R.id.description);
        TextView category = view.findViewById(R.id.category);
        TextView material = view.findViewById(R.id.material);
        TextView dynastyPeriod = view.findViewById(R.id.dynastyPeriod);
        TextView culturalOrigin = view.findViewById(R.id.culturalOrigin);
        TextView dimensions = view.findViewById(R.id.dimensions);
        TextView conditionReport = view.findViewById(R.id.conditionReport);
        TextView currentLocation = view.findViewById(R.id.currentLocation);
        TextView acquisitionMethod = view.findViewById(R.id.acquisitionMethod);
        TextView provenance = view.findViewById(R.id.provenance);
        TextView accessionNumber = view.findViewById(R.id.accessionNumber);
        TextView notes = view.findViewById(R.id.notes);
            RecyclerView relatedItems = view.findViewById(R.id.relatedArtifactsList);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot data = snapshot.child("value");
                Item item = data.getValue(Item.class);
                if (item != null) {
                    name.setText(item.getArtifactName());
                    lotNum.setText(item.getLotNumber());
                    description.setText(item.getDescription());
                    category.setText(item.getCategory());
                    material.setText(item.getMaterial());
                    dynastyPeriod.setText(item.getDynastyPeriod());
                    culturalOrigin.setText(item.getCulturalOrigin());
                    dimensions.setText(item.getDimensions());
                    conditionReport.setText(item.getConditionReport());
                    currentLocation.setText(item.getCurrentLocation());
                    acquisitionMethod.setText(item.getAcquisitionMethod());
                    provenance.setText(item.getProvenance());
                    accessionNumber.setText(item.getAccessionNumber());
                    notes.setText(item.getNotes());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error fetching artifact fields.", databaseError.toException());
            }
        });


            relatedItems.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            // TODO: temp code.
            ArrayList<Item> items = new ArrayList<>();
            items.add(new Item());
            items.add(new Item());
            items.add(new Item());
            items.add(new Item());
            items.add(new Item());
            items.add(new Item());
            items.add(new Item());
            int widthOverride;
            if (getContext() == null) {
                widthOverride = 400;
                Log.w(Tag, "context is null, can't convert width override. Using default of: " + widthOverride);
            } else {
                widthOverride = (int) TypedValue.applyDimension(COMPLEX_UNIT_DIP, relatedArtifactViewWidth, getContext().getResources().getDisplayMetrics());
            }
            ItemAdapter.LayoutOverrides layoutOverrides = new ItemAdapter.LayoutOverrides(widthOverride);
            ItemAdapter adapter = new ItemAdapter(items, getParentFragmentManager().beginTransaction(), layoutOverrides);
            adapter.setPopBackStackId(popBackId);
            relatedItems.setAdapter(adapter);
        }

        homeButton.setOnClickListener(v -> {
            Log.i(Tag, "popping back to " + popBackId);
            if (popBackId < 0) {
                Log.w(Tag, "popBackId isn't set, popping back 1 state by default (was: " + popBackId + ")");
                getParentFragmentManager().popBackStack();
            } else {
                getParentFragmentManager().popBackStack(popBackId, 0 /* don't pop target as well. */);
            }
        });

        ToggleButton saveArtifactButton = view.findViewById(R.id.saveArtifactToggle);
        saveArtifactButton.setClickable(false);
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            SavedArtifactWriter savedArtifactWriter = new SavedArtifactWriter();
            saveArtifactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToggleButton instance = (ToggleButton) view;
                    if (uid == null) {
                        Log.w(Tag, "Null user, 'saved artifact' button shouldn't be accessible");
                        return;
                    }
                    if (instance.isChecked()) {
                        Log.i(Tag, "adding artifact " + lot + " to user's collection: " + uid);
                        savedArtifactWriter.addSavedArtifact(uid, lot);
                    } else {
                        Log.i(Tag, "removing artifact " + lot + " from user's collection: " + uid);
                        savedArtifactWriter.removeSavedArtifact(uid, lot);
                    }
                }
            });
            saveArtifactButton.setClickable(false);

            SavedArtifactReader reader = SavedArtifactReader.getInstance(uid);
            reader.addOnSavedArtifactChangedListener(lot, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    saveArtifactButton.setClickable(true);
                    if (snapshot.exists()) {
                        saveArtifactButton.setChecked(true);
                    } else {
                        saveArtifactButton.setChecked(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(Tag, "listener cancelled", error.toException());
                }
            });
        }
        return view;
    }
}
