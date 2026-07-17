package com.cscb07.taamapp;

import static org.junit.Assert.assertEquals;

import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.cscb07.taamapp.util.TestLogger;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Strict.class)
public class EditArtifactFragmentTest {
    @NonNull
    private static Item getDefaultItem() {
        final String sampleCategory = "Round thing";
        final String sampleMaterial = "metal thing";
        final String sampleDynasty = "Old ancient Dynasty / 3000BCE";
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
        return inputItem;
    }
    private Spinner mockSpinner(String returnValue)
    {
        int pos = 2;
        SpinnerAdapter adapter = mock(SpinnerAdapter.class);
        when(adapter.getItem(pos)).thenReturn(returnValue);
        Spinner spinnerMock = mock(Spinner.class);
        when(spinnerMock.getSelectedItemPosition()).thenReturn(pos);
        when(spinnerMock.getAdapter()).thenReturn(adapter);
        return spinnerMock;
    }

    public static class MockableEditText extends EditText {
        private final String result;

        public MockableEditText(String result) {
            super(null);
            this.result = result;
        }

        @Override
        public Editable getText() {
            Editable mock = mock(Editable.class);
            when(mock.toString()).thenReturn(result);
            return mock;
        }
    }

    private void stubReturnItem(EditArtifactFragment f, Item item) {
        f.textViewLotNumber = mock(TextView.class);
        when(f.textViewLotNumber.getText()).thenReturn(item.getLotNumber());
        f.editTextName = new MockableEditText(item.getArtifactName());
        f.editTextArtifactDescription = new MockableEditText(item.getDescription());
        f.spinnerArtifactCategory = mockSpinner(item.getCategory());
        f.spinnerArtifactMaterial = mockSpinner(item.getMaterial());
        f.spinnerDynasty = mockSpinner(item.getDynastyPeriod());
        f.editTextCulturalOrigin = new MockableEditText(item.getCulturalOrigin());
        f.editTextDimensions = new MockableEditText(item.getDimensions());
        f.editTextConditionReport = new MockableEditText(item.getConditionReport());
        f.editTextCurrentLocation = new MockableEditText(item.getCurrentLocation());
        f.editTextAcquisitionMethod = new MockableEditText(item.getAcquisitionMethod());
        f.editTextProvenance = new MockableEditText(item.getProvenance());
        f.editTextAccessionNumber = new MockableEditText(item.getAccessionNumber());
        f.editTextNotes = new MockableEditText(item.getNotes());
    }

    @Test
    public void createItem_InitialFilledItem_EqualItem() {
        Item inputItem = getDefaultItem();
        EditArtifactFragment sut = new EditArtifactFragment(null, null, new TestLogger());
        stubReturnItem(sut, inputItem);

        Item result = sut.createItem();

        assertEquals(inputItem.getLotNumber(), result.getLotNumber());
        assertEquals(inputItem.getArtifactName(), result.getArtifactName());
        assertEquals(inputItem.getDescription(), result.getDescription());
        assertEquals(inputItem.getCategory(), result.getCategory());
        assertEquals(inputItem.getMaterial(), result.getMaterial());
        assertEquals(inputItem.getDynastyPeriod(), result.getDynastyPeriod());
        assertEquals(inputItem.getCulturalOrigin(), result.getCulturalOrigin());
        assertEquals(inputItem.getDimensions(), result.getDimensions());
        assertEquals(inputItem.getConditionReport(), result.getConditionReport());
        assertEquals(inputItem.getCurrentLocation(), result.getCurrentLocation());
        assertEquals(inputItem.getAcquisitionMethod(), result.getAcquisitionMethod());
        assertEquals(inputItem.getProvenance(), result.getProvenance());
        assertEquals(inputItem.getAccessionNumber(), result.getAccessionNumber());
        assertEquals(inputItem.getNotes(), result.getNotes());
        assertEquals(inputItem.getImage(), result.getImage());
    }




}
