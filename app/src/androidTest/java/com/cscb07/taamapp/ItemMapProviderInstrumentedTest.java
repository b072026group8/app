package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.FlakyTest;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.util.Log;

@RunWith(AndroidJUnit4.class)
public class ItemMapProviderInstrumentedTest {
    private static final String TAG = "ItemMapProviderInstrumentedTest";
    @Test
    // Seems this test may fail in wider contexts when sleep duration is too small.
    public void ctor_defaults_FetchesItems() throws InterruptedException {
        FirebaseDatabase.getInstance().getReference("artifacts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "Found " + snapshot.getChildrenCount() + " artifacts.");
                if (snapshot.getChildrenCount() == 0) {
                    Log.e(TAG, "Detected no stored artifacts: test won't work properly.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "error connecting to db", error.toException());
            }
        });
        ItemMapProvider provider;

        provider = new ItemMapProvider();

        for (int i = 0; i < 25; i++) {
            Thread.sleep(100);
            if (!provider.getValue().isEmpty()) {
                break;
            }
        }
        assertNotEquals(0, provider.getValue().size());
    }
}
