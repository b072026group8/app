package com.cscb07.taamapp;

import android.database.Observable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cscb07.taamapp.util.UpdateListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Provides and maintains a {@link Map} from a String Lot number to {@link Item}.
 */
public class ItemMapProvider extends Observable<UpdateListener<Map<String, Item>>> {
    private static final String TAG = "ItemMapProvider";
    private final Map<String, Item> itemMap = new HashMap<>();
    private final DatabaseReference ref;
    private final ChildEventListener dbListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            String lot = snapshot.getKey();
            Item item = snapshot.child("value").getValue(Item.class);
            itemMap.put(lot, item);
            notifyListeners();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            onChildAdded(snapshot, previousChildName);
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String lot = snapshot.getKey();
            itemMap.remove(lot);
            notifyListeners();
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

    /**
     * @return A mapping from lot number to item.
     */
    public Map<String, Item> getMap() {
        return itemMap;
    }

    private void notifyListeners() {
        for (UpdateListener<Map<String, Item>> listener : mObservers) {
            listener.onChange(getMap());
        }
    }

}
