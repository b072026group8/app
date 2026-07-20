package com.cscb07.taamapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.function.ThrowingRunnable;
import org.junit.runner.JUnitCore;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class EditArtifactFragmentInstrumentedTest {
    @Nullable
    private DataSnapshot snapshot = null;
    @Test
    public void fragmentCreation_AddMode_SetsUiFields() {
        // no setup

        try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class)) {

            scenario.onFragment(f -> {
                assertNotNull(f.textViewLotNumber);
                assertTrue(f.textViewLotNumber.getText().length() > 0);
                assertNotNull(f.editTextName);
                assertNotNull(f.editTextArtifactDescription);
                assertNotNull(f.spinnerArtifactCategory);
                assertNotNull(f.spinnerArtifactMaterial);
                assertNotNull(f.spinnerDynasty);
                assertNotNull(f.editTextCulturalOrigin);
                assertNotNull(f.editTextDimensions);
                assertNotNull(f.editTextConditionReport);
                assertNotNull(f.editTextCurrentLocation);
                assertNotNull(f.editTextAcquisitionMethod);
                assertNotNull(f.editTextProvenance);
                assertNotNull(f.editTextAccessionNumber);
                assertNotNull(f.editTextNotes);
            });
        }
    }

    private Item getExampleItem() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final String sampleCategory = appContext.getResources().getStringArray(R.array.categories_array)[2];
        final String sampleMaterial = appContext.getResources().getStringArray(R.array.material_array)[2];
        final String sampleDynasty = appContext.getResources().getStringArray(R.array.dynasty_period_array)[2];
        return new Item("1-2333-1231",
                "Shield",
                "very brittle old shield.",
                sampleCategory, sampleMaterial, sampleDynasty,
                "Some group",
                "40cm x 40cm disk",
                "edges heavily chipped",
                "Museum",
                "Donated",
                "Some specific location",
                "138918234",
                "Very curious notes here" ,
                ""
        );
    }

    @Test
    public void fragmentCreation_EditMode_SetsFieldsAsProvidedItem()
    {
        Item inputItem = getExampleItem();
        FragmentFactory factory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new EditArtifactFragment(inputItem);
            }
        };


        try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class, null, factory)) {
            scenario.onFragment(f -> {


                assertEquals(inputItem.getLotNumber(), f.textViewLotNumber.getText());
                assertEquals(inputItem.getDescription(), f.editTextArtifactDescription.getText().toString());
                assertEquals(inputItem.getCategory(), f.getSpinnerCategory());
                assertEquals(inputItem.getMaterial(), f.getSpinnerMaterial());
                assertEquals(inputItem.getDynastyPeriod(), f.getSpinnerDynasty());
                assertEquals(inputItem.getCulturalOrigin(), f.editTextCulturalOrigin.getText().toString());
                assertEquals(inputItem.getDimensions(), f.editTextDimensions.getText().toString());
                assertEquals(inputItem.getConditionReport(), f.editTextConditionReport.getText().toString());
                assertEquals(inputItem.getCurrentLocation(), f.editTextCurrentLocation.getText().toString());
                assertEquals(inputItem.getAcquisitionMethod(), f.editTextAcquisitionMethod.getText().toString());
                assertEquals(inputItem.getProvenance(), f.editTextProvenance.getText().toString());
                assertEquals(inputItem.getAccessionNumber(), f.editTextAccessionNumber.getText().toString());
                assertEquals(inputItem.getNotes(), f.editTextNotes.getText().toString());
            });
        }
    }

    @Test
    public void fragmentCreation_EditModeInvalidSpinnerValues_SetsSpinnersToDefault() {
        final String sampleCategory = "invalid";
        final String sampleMaterial = "invalid";
        final String sampleDynasty = "invalid";
        Item inputItem = new Item("A01231",
                "Shield",
                "very brittle old shield.",
                sampleCategory, sampleMaterial, sampleDynasty,
                "Some group",
                "40cm x 40cm disk",
                "edges heavily chipped",
                "Museum",
                "Donated",
                "Some specific location",
                "138918234",
                "Very curious notes here",
                ""
        );
        FragmentFactory factory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new EditArtifactFragment(inputItem);
            }
        };


        try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class, null, factory)) {
            scenario.onFragment(f -> {


                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_CATEGORY, f.getSpinnerCategory());
                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_MATERIAL, f.getSpinnerMaterial());
                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_DYNASTY, f.getSpinnerDynasty());
            });
        }
    }

    @Test
    public void fragmentCreation_AddMode_GetsLotNumber() {
        final String someLot = "sample-lot-123";
        DbEditorAccess access = new DbEditorAccess() {
            @Override
            public String getUniqueLotNumber() {
                return someLot;
            }
            @Override public DbEditorAccessResult editItem(Item item) { throw new RuntimeException("Shouldn't run"); } @Override public void cancelAdd(String lotNumber) {throw new RuntimeException("Shouldn't run"); }
        };
        FragmentFactory factory = new FragmentFactory() {
            @NonNull
            @Override
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new EditArtifactFragment(null, access);
            }
        };

        try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class, null, factory)) {
            scenario.onFragment(f -> {


                assertEquals(someLot, f.textViewLotNumber.getText().toString());
            });
        }
    }

    private void runIsolatedDb(ThrowingRunnable action) throws Throwable {
        FirebaseDatabase.getInstance().goOffline();
        try {
            action.run();
        } finally {
            FirebaseDatabase.getInstance().purgeOutstandingWrites();
        }
    }

    @Test
    public void editingItem_CreatesAndUploads_ReflectedInDb() throws Throwable {
        runIsolatedDb(() -> {
            Item item = getExampleItem();
            final String newName = "Sword";
            FirebaseDatabase.getInstance().getReference("artifacts").child(item.getLotNumber()).setValue(item);
            FragmentFactory factory = new FragmentFactory() {
                @NonNull @Override public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                    return new EditArtifactFragment(item, new FireSupaDbEditorAccess());
                }
            };
            try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class, null, factory)){
                scenario.onFragment(f -> {
                   f.editTextName.setText(newName);


                   f.onSave();
                });
            }

            Thread.sleep(2500);
            CountDownLatch latch = new CountDownLatch(1);
            FirebaseDatabase.getInstance().getReference("artifacts/"+item.getLotNumber()).get()
                    .addOnSuccessListener(data -> {
                        snapshot = data;
                        latch.countDown();
                    });
            assertTrue(latch.await(5, TimeUnit.SECONDS));
            assertNotNull(snapshot);
            Item newItem = snapshot.getValue(Item.class);
            assertNotNull(newItem);
            assertEquals(newName, newItem.getArtifactName());
        });
    }

    @Ignore("Test modifies actual database.")
    @Test
    public void addingItem_CreatesAndUploads_ReflectedInDb() throws Throwable {
        Item item = new Item();
        try {
            item.setArtifactName("Some Name!");
            item.setDescription("Some description");
            FragmentFactory factory = new FragmentFactory() {
                @NonNull @Override public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                    return new EditArtifactFragment(null, new FireSupaDbEditorAccess());
                }
            };
            try (FragmentScenario<EditArtifactFragment> scenario = FragmentScenario.launch(EditArtifactFragment.class, null, factory)){
                scenario.onFragment(f -> {
                    item.setLotNumber(f.textViewLotNumber.getText().toString());
                    f.editTextName.setText(item.getArtifactName());
                    f.editTextName.setText(item.getArtifactName());
                    f.editTextArtifactDescription.setText(item.getDescription());
                    f.spinnerDynasty.setSelection(1);
                    item.setDynastyPeriod(f.getSpinnerDynasty());
                    f.spinnerArtifactCategory.setSelection(1);
                    item.setCategory(f.getSpinnerCategory());
                    f.spinnerArtifactMaterial.setSelection(1);
                    item.setMaterial(f.getSpinnerMaterial());
                    Log.i("Test", "" + f.validateFields());


                    f.onSave();
                });
            }

            Thread.sleep(2500);
            Log.i("Test", item.getLotNumber());
            CountDownLatch latch = new CountDownLatch(1);
            FirebaseDatabase.getInstance().getReference("artifacts").get()
                    .addOnSuccessListener(data -> {
                        snapshot = data;
                        latch.countDown();
                    });
            assertTrue(latch.await(3, TimeUnit.SECONDS));
            assertNotNull(snapshot);
            assertTrue(snapshot.hasChild(item.getLotNumber()));
            Item newItem = snapshot.child(item.getLotNumber()).getValue(Item.class);
            assertEquals(item, newItem);
        } finally {
            if (!item.getLotNumber().isEmpty()) {
                FirebaseDatabase.getInstance().getReference("artifacts").child(item.getLotNumber()).setValue(null);
            }
        }
    }
}