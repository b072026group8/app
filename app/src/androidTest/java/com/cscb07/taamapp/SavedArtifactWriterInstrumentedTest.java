package com.cscb07.taamapp;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cscb07.taamapp.util.OperationListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class SavedArtifactWriterInstrumentedTest {
    private final String sourceKey = "test-key-value-do-not-use";
    private Random r = new Random();
    public String getSomeKey() {
        String key = sourceKey + r.nextInt();
        Log.v("Test", "generated key: " + key);
        return key;
    }
    @Test
    @LargeTest
    public void addSavedArtifact_sampleValues_ReflectedInDb() throws InterruptedException {
        String uid = getSomeKey();
        String lot = getSomeKey();
        SavedArtifactWriter sut = new SavedArtifactWriter();

        sut.addSavedArtifact(uid, lot);

        Thread.sleep(1000);
        FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid).get()
                .addOnFailureListener(error -> {
                    Log.e("Test", "get() failed", error);
                    fail();
                })
                .addOnCompleteListener(task -> {
                    assertTrue(task.isComplete());
                    DataSnapshot data = task.getResult();
                    data.getRef().removeValue();

                    assertTrue(data.exists());
                    assertTrue(data.child(lot).exists());
                    Log.v("Test", "Success!");
                });
        Thread.sleep(1000);
    }

    @Test
    @LargeTest
    public void addSavedArtifact_usingListener_ReceiveResults() throws InterruptedException {
        String uid = getSomeKey();
        String lot = getSomeKey();
        SavedArtifactWriter sut = new SavedArtifactWriter();
        OperationListener<Void> listener = new OperationListener<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.v("Test", "Callback Success!");
            }

            @Override
            public void onFailure(Exception value) {
                Log.e("Test", "operation failed.", value);
                fail();
            }
        };

        sut.addSavedArtifact(uid, lot, listener);

        System.out.println("IMPORTANT: check logs for actual value, look for 'Callback Success!'");
        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        // cleanup.
        FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid)
                .removeValue();
    }

    @Test
    @LargeTest
    public void removeSavedArtifact_ValueExists_RemovesItFromDb() throws InterruptedException {
        String uid = getSomeKey();
        String lot = getSomeKey();
        DatabaseReference child = FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
        child.child(lot).setValue(true).addOnFailureListener(err -> {Log.e("Test", "error preparing", err); fail();});
        Thread.sleep(700);;
        SavedArtifactWriter sut = new SavedArtifactWriter();

        sut.removeSavedArtifact(uid, lot);

        Thread.sleep(700);
        child.get()
                .addOnFailureListener(error -> {
                    Log.e("Test", "get() failed", error);
                    fail();
                })
                .addOnCompleteListener(task -> {
                    assertTrue(task.isComplete());
                    DataSnapshot data = task.getResult();
                    data.getRef().removeValue();

                    assertFalse(data.exists());
                    Log.v("Test", "Success!");
                });
        Thread.sleep(700);
    }

    @Test
    @LargeTest
    public void removeSavedArtifact_usingListener_ReceiveResults() throws InterruptedException {
        String uid = getSomeKey();
        String lot = getSomeKey();
        SavedArtifactWriter sut = new SavedArtifactWriter();
        OperationListener<Void> listener = new OperationListener<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.v("Test", "Callback Success!");
            }

            @Override
            public void onFailure(Exception value) {
                Log.e("Test", "operation failed.", value);
                fail();
            }
        };

        sut.removeSavedArtifact(uid, lot, listener);

        System.out.println("IMPORTANT: check logs for actual value, look for 'Callback Success!'");
        Thread.sleep(700);
        Thread.sleep(700);
        Thread.sleep(700);
        // cleanup.
        FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid)
                .removeValue();
    }
}
