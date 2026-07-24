package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class SavedArtifactReaderInstrumentedTest {
    private final String sourceKey = "test-key-value-do-not-use";
    private Random r = new Random();
    public String getSomeKey() {
        String key = sourceKey + r.nextInt();
        Log.v("Test", "generated key: " + key);
        return key;
    }
    @NonNull
    private static DatabaseReference getDbRef(String uid) {
        return FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
    }
    boolean hasReceived = false;
    @Test
    public void addOnSavedArtifactChangedListener_alreadyHadValue_listenerCalled() throws InterruptedException {
        String uid = getSomeKey();
        String lot = getSomeKey();
        DatabaseReference ref = getDbRef(uid);
        ref.child(lot).setValue(true);
        hasReceived = false;
        ValueEventListener listener = new ValueEventListener() {
            boolean disable = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (disable) {
                    return;
                }
                hasReceived = true;
                disable = true;
                try {
                    assertEquals(snapshot.getKey(),lot);
                    assertTrue(snapshot.exists());
                    Log.v("Test", "Callback Success!");
                } finally {
                    if (lot.equals(snapshot.getKey())) {
                        snapshot.getRef().removeValue();
                    } else {
                        Log.e("Test", "somewhere else, not deleting: " + snapshot.getRef().getPath());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (disable) {
                    return;
                }
                Log.e("Test", "listener cancelled.", error.toException());
                fail();
            }
        };
        SavedArtifactReader sut = SavedArtifactReader.getInstance(uid);

        sut.addOnSavedArtifactChangedListener(lot, listener);

        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        ref.removeValue();
        assertTrue(hasReceived);
        Log.i("Test", "finishing test");
    }

    @Test
    public void addOnChangedListener_sampleValues_listenerCalled() throws InterruptedException {
        hasReceived = false;
        String uid = getSomeKey();
        String[] lots = new String[] {
                getSomeKey(), getSomeKey(), getSomeKey(),
        };
        DatabaseReference ref = getDbRef(uid);
        for (String lot : lots) {
            ref.child(lot).setValue(true);
        }
        ValueEventListener listener = new ValueEventListener() {
            boolean disable;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (disable) {
                    return;
                }
                disable = true;
                hasReceived = true;
                try {
                    assertTrue(snapshot.exists());
                    assertEquals(snapshot.getChildrenCount(), lots.length);
                    for (DataSnapshot child : snapshot.getChildren()) {
                        assertNotNull(child.getKey());
                        assertTrue(Arrays.asList(lots).contains(child.getKey()));
                    }
                } finally {
                    if (uid.equals(snapshot.getKey())) {
                        snapshot.getRef().removeValue();
                    } else {
                        Log.e("Test", "somewhere else, not deleting: " + snapshot.getRef().getPath());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Test", "listener cancelled", error.toException());
                if (disable) { return; }
                fail();
            }
        };

        SavedArtifactReader sut = SavedArtifactReader.getInstance(uid);

        sut.addOnChangedListener(listener);

        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        ref.removeValue();
    }
}
