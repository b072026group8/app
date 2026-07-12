package com.cscb07.taamapp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class EditArtifactFragmentInstrumentedTest {
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

    @Test
    public void fragmentCreation_EditMode_SetsFieldsAsProvidedItem()
    {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        final String sampleCategory = appContext.getResources().getStringArray(R.array.categories_array)[2];
        final String sampleMaterial = appContext.getResources().getStringArray(R.array.material_array)[2];
        final String sampleDynasty = appContext.getResources().getStringArray(R.array.dynasty_period_array)[2];
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
                "Very curious notes here" ,
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


                assertEquals(inputItem.getLotNumber(), f.textViewLotNumber.getText());
                assertEquals(inputItem.getDescription(), f.editTextArtifactDescription.getText().toString());
                assertEquals(inputItem.getCategory(), f.spinnerCategoryValue);
                assertEquals(inputItem.getMaterial(), f.spinnerMaterialValue);
                assertEquals(inputItem.getDynastyPeriod(), f.spinnerDynastyValue);
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
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
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


                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_CATEGORY, f.spinnerCategoryValue);
                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_MATERIAL, f.spinnerMaterialValue);
                assertEquals(EditArtifactFragment.SPINNER_DEFAULT_DYNASTY, f.spinnerDynastyValue);
            });
        }
    }
}