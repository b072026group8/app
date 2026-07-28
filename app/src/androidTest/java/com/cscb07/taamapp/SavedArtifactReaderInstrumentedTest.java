package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;
import androidx.test.filters.LargeTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

import com.cscb07.taamapp.testutil.CallbackStatusLatch;
import com.cscb07.taamapp.testutil.KnownRandom;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class SavedArtifactReaderInstrumentedTest {
    private static final String TAG = "SavedArtifactReaderInstrumentedTest";
    private final String sourceKey = "test-key-value-do-not-use";
    public String getSomeKey(int seed) {
        String key = sourceKey + KnownRandom.nextInt(seed);
        Log.v(TAG, "generated key: " + key);
        return key;
    }
    @NonNull
    private static DatabaseReference getDbRef(String uid) {
        return FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
    }
    @Test
    @LargeTest
    @FlakyTest
    public void addOnSavedArtifactChangedListener_alreadyHadValue_listenerCalled() throws InterruptedException {
        CallbackStatusLatch<DataSnapshot> callbackLatch = new CallbackStatusLatch<>();
        String uid = getSomeKey(34567);
        String lot = getSomeKey(129012);
        DatabaseReference ref = getDbRef(uid);
        try {
            ref.child(lot).setValue(true);
            ValueEventListener listener = new ValueEventListener() {
                boolean disable = false;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (disable) {
                        return;
                    }
                    disable = true;
                    callbackLatch.countDown(snapshot);
                    try {
                        assertEquals(snapshot.getKey(),lot);
                        assertTrue(snapshot.exists());
                        Log.v(TAG, "Callback Success!");
                    } finally {
                        if (lot.equals(snapshot.getKey())) {
                            snapshot.getRef().removeValue();
                        } else {
                            Log.e(TAG, "somewhere else, not deleting: " + snapshot.getRef().getPath());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "listener cancelled.", error.toException());
                    if (disable) {
                        return;
                    }
                    callbackLatch.countDown(null);
                }
            };
            SavedArtifactReader sut = SavedArtifactReader.getInstance(uid);


            sut.addOnSavedArtifactChangedListener(lot, listener);


            assertTrue(callbackLatch.awaitCallback(3000));
            DataSnapshot snapshot = callbackLatch.getResult();
            assertNotNull(snapshot);
            assertEquals(snapshot.getKey(),lot);
            assertTrue(snapshot.exists());
        } finally {
            ref.removeValue()
                    .addOnFailureListener(err -> {
                        throw new RuntimeException("failed to remove test value", err);
                    });
        }
    }

    @Test
    @LargeTest
    @FlakyTest
    public void addOnChangedListener_sampleValues_listenerCalled() throws InterruptedException {
        CallbackStatusLatch<DataSnapshot> callbackLatch = new CallbackStatusLatch<>();
        String uid = getSomeKey(345);
        String[] lots = new String[] {
                getSomeKey(456), getSomeKey(567), getSomeKey(678),
        };
        DatabaseReference ref = getDbRef(uid);
        for (String lot : lots) {
            ref.child(lot).setValue(true);
        }
        try {
            ValueEventListener listener = new ValueEventListener() {
                boolean disable;
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Log.i(TAG, "on data change");
                    if (disable) {
                        return;
                    }
                    disable = true;
                    callbackLatch.countDown(snapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(TAG, "listener cancelled", error.toException());
                    if (disable) { return; }
                    disable = true;
                    callbackLatch.countDown(null);
                }
            };
            SavedArtifactReader sut = SavedArtifactReader.getInstance(uid);


            sut.addOnChangedListener(listener);


            assertTrue(callbackLatch.awaitCallback(5000));
            DataSnapshot snapshot = callbackLatch.getResult();
            assertNotNull(snapshot);
            assertEquals(uid, snapshot.getKey());
            assertTrue(snapshot.exists());
            assertEquals(snapshot.getChildrenCount(), lots.length);
            for (DataSnapshot child : snapshot.getChildren()) {
                assertNotNull(child.getKey());
                assertTrue(Arrays.asList(lots).contains(child.getKey()));
            }
        } finally {
            ref.removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }
    }
}
