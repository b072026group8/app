package com.cscb07.taamapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cscb07.taamapp.util.Provider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides a list of artifact {@link Item}s saved to a specified user's saved collection.
 */
public class SavedArtifactListProvider extends Provider<List<Item>> {
    private static final String TAG = "SavedArtifactListProvider";
    private Map<String, Item> items;
    private final List<String> lotNumbers = new ArrayList<>();
    private final List<Item> itemList = new ArrayList<>();
    @Nullable
    private final SavedArtifactReader reader;

    /**
     * Creates an instance that provides the specified user's saved artifacts.
     * Using the provided {@link Provider<Map>} to map lot numbers to items.
     * <p>
     * Automatically connects a listener to update if the provider is also updated.
     * @param itemMapProvider The provider of the mapping to use.
     * @param uid the uid of the user.
     */
    public SavedArtifactListProvider(@NonNull Provider<Map<String, Item>> itemMapProvider, @NonNull String uid) {
        this(itemMapProvider.getValue(), uid);
        itemMapProvider.registerObserver(map -> items = map);

        itemMapProvider.registerObserver(map -> {
            items = map;
            updateValue();
        });
    }

    /**
     * Creates an instance that provides the specified user's saved artifacts,
     * using the provided {@link Map}.
     * @param itemMap The mapping from Lot number to {@link Item}s.
     * @param uid The uid of the user.
     */
    public SavedArtifactListProvider(@NonNull Map<String, Item> itemMap, @NonNull String uid) {
        items = itemMap;
        if (uid.isEmpty()) {
            reader = null;
            return;
        }
        reader = SavedArtifactReader.getInstance(uid);
        reader.addOnChangedListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lotNumbers.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    lotNumbers.add(child.getKey());
                }
                updateValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Listener cancelled.", error.toException());
            }
        });
    }

    @Override
    public void updateValue() {
        itemList.clear();
        for (String lotNumber : lotNumbers) {
            itemList.add(items.get(lotNumber));
        }
        super.updateValue();
    }

    @Override
    public List<Item> getValue() { return itemList;}
}
