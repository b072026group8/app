package com.cscb07.taamapp;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ExpandedArtifactViewFragment extends Fragment{
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String lot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_expanded_artifact_view, container,false);
    db = FirebaseDatabase.getInstance();
    Bundle args = getArguments();
    if (args != null) {
        lot = args.getString("lotNumber");
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

        }
    return view;
    }
}
