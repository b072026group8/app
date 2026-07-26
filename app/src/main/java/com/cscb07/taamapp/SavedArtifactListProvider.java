package com.cscb07.taamapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.cscb07.taamapp.util.ListProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Assigns Items to the given {@link List<Item>} of a particular user's saved artifact list.
 */
public class SavedArtifactListProvider implements ListProvider<Item> {
    private static final String TAG = "SavedArtifactListProvider";
    private final Map<String, Item> items;
    private List<Item> itemList;
    SavedArtifactReader reader;
    private final ArrayList<ListChangeListener<Item>> listeners = new ArrayList<>();

    /**
     * Creates an instance to get the artifacts saved in current logged-in user's saved artifact list.
     * @param itemsRef A mapping from lot number to corresponding item. Can be modified afterwards.
     */
    public SavedArtifactListProvider(Map<String, Item> itemsRef) {
        items = itemsRef;
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            Log.w(TAG, "user not logged in.");
            uid = "";
        }
        reader = SavedArtifactReader.getInstance(uid);
    }

    @Override
    public void setList(@NonNull List<Item> instance) {
        instance.clear();
        if (itemList == null) {
            itemList = instance;
            reader.addOnChangedListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    itemList.clear();
                    for (DataSnapshot child : snapshot.getChildren()) {
                        String lot = child.getKey();
                        itemList.add(items.get(lot));
                    }
                    notifyChangeListeners();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "Listener cancelled.", error.toException());
                }
            });
        } else {
            instance.addAll(itemList);
            itemList = instance;
        }

    }

    private void notifyChangeListeners() {
        for (ListChangeListener<Item> listener : listeners) {
            listener.onChange(itemList);
        }
    }

    @Override
    public void addOnChangeListener(ListChangeListener<Item> listener) {
        listeners.add(listener);
    }
}
