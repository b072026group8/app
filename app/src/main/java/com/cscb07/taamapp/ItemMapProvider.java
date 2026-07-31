package com.cscb07.taamapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cscb07.taamapp.namedAlias.ItemMappingProvider;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides and maintains a {@link Map} from a String Lot number to artifact {@link Item}s,
 * as updated in the database.
 */
public class ItemMapProvider extends ItemMappingProvider {
    private static final String TAG = "ItemMapProvider";
    private final Map<String, Item> itemMap = new HashMap<>();
    private final DatabaseReference ref;
    private final ChildEventListener dbListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String lot = snapshot.getKey();
            Item item = snapshot.child("value").getValue(Item.class);
            itemMap.put(lot, item);
            updateValue();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            onChildAdded(snapshot, previousChildName);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String lot = snapshot.getKey();
            itemMap.remove(lot);
            updateValue();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            itemMap.remove(previousChildName);
            onChildAdded(snapshot, null);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Child listener cancelled.", error.toException());
        }
    };

    public ItemMapProvider() {
        ref = FirebaseDatabase.getInstance().getReference("artifacts");
        ref.addChildEventListener(dbListener);
    }

    @Override
    protected void finalize() throws Throwable {
        ref.removeEventListener(dbListener);
    }

    @Override
    public Map<String, Item> getValue() {
        return itemMap;
    }
}
