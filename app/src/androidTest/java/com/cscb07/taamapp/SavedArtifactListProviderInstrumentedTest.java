package com.cscb07.taamapp;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cscb07.taamapp.testutil.KnownRandom;
import com.cscb07.taamapp.util.Provider;
import com.cscb07.taamapp.util.UpdateListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class SavedArtifactListProviderInstrumentedTest {
    private static final String TAG = "SavedArtifactListProviderInstrumentedTest";
    private static String getKey(int seed) {
        String key = "random-test-key-do-not-use-" + KnownRandom.nextInt(seed);
        Log.v(TAG, "from seed: " + seed + "\tgenerated key: " + key);
        return key;
    }

    @Test
    public void constructor_sampleDbData_FetchesDbData() throws InterruptedException {
        String uid = getKey(459);
        String lot = getKey(908);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("saved_collection").child(uid).child(lot);
        Log.d(TAG, "In db, modifying: " + dbRef.getPath().toString());
        dbRef.setValue(true);
        try {
            for (int i = 0; i < 20; i++) { Thread.sleep(50); }
            Map<String, Item> map = new HashMap<>();
            map.put(lot, new Item());
            map.get(lot).setLotNumber(lot);
            map.get(lot).setArtifactName("Some Artifact Name");


            SavedArtifactListProvider provider = new SavedArtifactListProvider(map, uid);


            for (int i = 0; i < 20; i++) { Thread.sleep(50); }
            assertEquals(1, provider.getValue().size());
            assertSame(map.get(lot), provider.getValue().get(0));
        } finally {
            Log.d(TAG, "removing db mods");
            dbRef.removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }
    }

    @Test
    public void constructor_GivenUpdatingProvider_ValueChangesCallbackCalled() throws InterruptedException {
        String uid = getKey(4590);
        String lot = getKey(9080);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("saved_collection").child(uid).child(lot);
        Log.d(TAG, "In db, modifying: " + dbRef.getPath().toString());
        dbRef.setValue(true);
        try {
            Map<String, Item> map = new HashMap<>();
            map.put(lot, new Item());
            map.get(lot).setLotNumber(lot);
            map.get(lot).setArtifactName("Some Artifact Name");
            Provider<Map<String, Item>> mapProvider = new Provider<Map<String, Item>>() {
                @Override
                public Map<String, Item> getValue() {
                    return map;
                }
            };
            SavedArtifactListProvider provider = new SavedArtifactListProvider(mapProvider, uid);
            CountDownLatch listenerDone = new CountDownLatch(1); // technically, the SUT isn't async. just convenient due to callback restrictions.
            provider.registerObserver(new UpdateListener<List<Item>>() {
                @Override
                public void onChange(List<Item> value) {
                    listenerDone.countDown();
                }
            });
            for (int i = 0; i < 20; i++) { Thread.sleep(50); }
            Item newItem = new Item();
            newItem.setLotNumber(lot);;
            newItem.setArtifactName("different artifact name");
            map.put(lot, newItem);


            mapProvider.updateValue();


            for (int i = 0; i < 20; i++) { Thread.sleep(50); }
            assertEquals(1, provider.getValue().size());
            assertSame(newItem, provider.getValue().get(0));
            assertTrue(listenerDone.await(10, TimeUnit.MILLISECONDS));
        } finally {
            Log.d(TAG, "removing db mods");
            dbRef.removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }

    }
}
