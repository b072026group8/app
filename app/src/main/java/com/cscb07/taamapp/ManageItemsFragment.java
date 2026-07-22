package com.cscb07.taamapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ManageItemsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_items, container, false);

        Button buttonAddItem = view.findViewById(R.id.buttonAddItem);
        Button buttonDeleteItem = view.findViewById(R.id.buttonDeleteItem);
        Button buttonBack = view.findViewById(R.id.buttonBack);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new EditArtifactFragment(null, new FireSupaDbEditorAccess()));
            }
        });

        // TODO: this is temporary, to test editing feature of `EditArtifactFragment`
        String sampleCategory = getResources().getStringArray(R.array.categories_array)[2];
        String sampleMaterial = getResources().getStringArray(R.array.material_array)[2];
        String sampleDynasty = getResources().getStringArray(R.array.dynasty_period_array)[2];
        view.findViewById(R.id.buttonEditItem).setOnClickListener(
                v -> loadFragment(
                        new EditArtifactFragment(new Item("A01231",
                                "Shield",
                                "very brittle old shield.",
                                sampleCategory, sampleMaterial, sampleDynasty,
                                "Some group",
                                "40cm x 40cm disk",
                                "edges heavily chipped",
                                "Museum",
                                "Donated",
                                "Some specific location",
                                "138918234",
                                "Very curious notes here" ,
                                ""
                        ), new FireSupaDbEditorAccess())));

        buttonDeleteItem.setOnClickListener(v -> loadFragment(new DeleteItemFragment()));

        buttonBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
