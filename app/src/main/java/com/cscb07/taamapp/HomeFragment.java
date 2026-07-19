package com.cscb07.taamapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        Button buttonRecyclerView = view.findViewById(R.id.buttonFilterSaved);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);
        RecyclerView artifactCardGrid = view.findViewById(R.id.artifactCardGrid);

        itemList = new ArrayList<>();
        itemAdapter = new ItemAdapter(itemList);
        itemList.add(new Item("hello", "hi", "god knows", "if this works", "", "", "", "", "", "", "", "", "", "", ""));
        itemList.add(new Item("hello", "hi", "god knows", "if this works", "", "", "", "", "", "", "", "", "", "", ""));
        itemList.add(new Item("hello", "hi", "god knows", "if this works", "", "", "", "", "", "", "", "", "", "", ""));
        itemList.add(new Item("hello", "hi", "god knows", "if this works", "", "", "", "", "", "", "", "", "", "", ""));

        buttonRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonRecyclerView.getText().equals(getString(R.string.show_saved))) {
                    buttonRecyclerView.setText(R.string.show_all);
                } else {
                    buttonRecyclerView.setText(R.string.show_saved);
                }
            }
        });

        buttonManageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new ManageItemsFragment());}
        });

        artifactCardGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
        artifactCardGrid.setAdapter(itemAdapter);

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
