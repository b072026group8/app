package com.cscb07.taamapp;

import static org.junit.Assert.*;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.cscb07.taamapp.testutil.CallbackStatusLatch;
import com.cscb07.taamapp.testutil.KnownRandom;
import com.cscb07.taamapp.util.OperationListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SavedArtifactWriterInstrumentedTest {
    private static final String TAG = "SavedArtifactWriterInstrumentedTest";
    private final String sourceKey = "test-key-value-do-not-use";
    public String getSomeKey(int seed) {
        String key = sourceKey + KnownRandom.nextInt(seed);
        Log.v(TAG, "generated key: " + key);
        return key;
    }
    @Test
    @LargeTest
    public void addSavedArtifact_sampleValues_ReflectedInDb() throws InterruptedException {
        CallbackStatusLatch<Task<DataSnapshot>> callbackLatch = new CallbackStatusLatch<>();
        String uid = getSomeKey(987);
        String lot = getSomeKey(876);
        SavedArtifactWriter sut = new SavedArtifactWriter();

        sut.addSavedArtifact(uid, lot);

        Thread.sleep(1000);
        FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid).get()
                .addOnFailureListener(error -> {
                    Log.e(TAG, "get() failed", error);
                    callbackLatch.countDown(null);
                })
                .addOnCompleteListener(task -> {
                    callbackLatch.countDown(task);
                });
        assertTrue(callbackLatch.awaitCallback(3000));
        Task<DataSnapshot> task = callbackLatch.getResult();
        assertNotNull(task);
        assertTrue(task.isComplete());
        DataSnapshot data = task.getResult();
        data.getRef().removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        assertTrue(data.exists());
        assertTrue(data.child(lot).exists());
    }

    @Test
    @LargeTest
    public void addSavedArtifact_usingListener_ReceiveResults() throws InterruptedException {
        CallbackStatusLatch<Boolean> callbackLatch = new CallbackStatusLatch<>();
        String uid = getSomeKey(765);
        String lot = getSomeKey(654);
        SavedArtifactWriter sut = new SavedArtifactWriter();
        OperationListener<Void> listener = new OperationListener<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.v(TAG, "Callback Success!");
                callbackLatch.countDown(true);
            }

            @Override
            public void onFailure(Exception value) {
                Log.e(TAG, "operation failed.", value);
                callbackLatch.countDown(false);
            }
        };
        try {


            sut.addSavedArtifact(uid, lot, listener);


            assertTrue(callbackLatch.awaitCallback(3000));
        } finally {
            FirebaseDatabase.getInstance()
                    .getReference(SavedArtifactWriter.DB_PATH)
                    .child(uid)
                    .removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }
    }

    @Test
    @LargeTest
    public void removeSavedArtifact_ValueExists_RemovesItFromDb() throws InterruptedException {
        String uid = getSomeKey(432);
        String lot = getSomeKey(321);
        DatabaseReference child = FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid);
        try {
            child.child(lot).setValue(true).addOnFailureListener(err -> {Log.e(TAG, "error preparing", err); fail();});
            Thread.sleep(700);;
            SavedArtifactWriter sut = new SavedArtifactWriter();


            sut.removeSavedArtifact(uid, lot);


            Thread.sleep(700);
            CallbackStatusLatch<Task<DataSnapshot>> callbackLatch = new CallbackStatusLatch<>();
            child.get()
                    .addOnFailureListener(error -> {
                        Log.e(TAG, "get() failed", error);
                        callbackLatch.countDown(null);
                    })
                    .addOnCompleteListener(task -> {
                        callbackLatch.countDown(task);
                        Log.v(TAG, "Success!");
                    });
            assertTrue(callbackLatch.awaitCallback(3000));
            Task<DataSnapshot> task = callbackLatch.getResult();
            assertNotNull(task);
            assertTrue(task.isComplete());
            DataSnapshot data = task.getResult();

            assertFalse(data.exists());
        } finally {
            child.removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }
    }

    @Test
    @LargeTest
    public void removeSavedArtifact_usingListener_ReceiveResults() throws InterruptedException {
        CallbackStatusLatch<Boolean> callbackLatch = new CallbackStatusLatch<>();
        String uid = getSomeKey(210);
        String lot = getSomeKey(109);
        SavedArtifactWriter sut = new SavedArtifactWriter();
        OperationListener<Void> listener = new OperationListener<Void>() {
            @Override
            public void onSuccess(Void value) {
                Log.v(TAG, "Callback Success!");
                callbackLatch.countDown(true);
            }

            @Override
            public void onFailure(Exception value) {
                Log.e(TAG, "operation failed.", value);
                callbackLatch.countDown(false);
            }
        };
        try {


            sut.removeSavedArtifact(uid, lot, listener);


            assertTrue(callbackLatch.awaitCallback(3000));
            assertNotNull(callbackLatch.getResult());
            assertTrue(callbackLatch.getResult());
        } finally {
            FirebaseDatabase.getInstance()
                .getReference(SavedArtifactWriter.DB_PATH)
                .child(uid)
                .removeValue()
                    .addOnFailureListener(err -> {throw new RuntimeException("failed to remove test value", err);} );
        }
    }
}
