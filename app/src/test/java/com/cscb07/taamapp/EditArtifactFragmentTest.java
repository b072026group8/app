package com.cscb07.taamapp;

import com.cscb07.taamapp.util.TestLogger;

import android.text.Editable;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.junit.function.ThrowingRunnable;
import org.junit.runner.RunWith;
import org.junit.Test;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
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

    /**
     * Set up mocks of the value fields of the fragment f, using param item for the values.
     */
    private void stubFields(EditArtifactFragment f, Item item) {
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
        stubFields(sut, inputItem);

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

    @Test
    public void validateFields_1MissingField_ReturnsFalse()
    {
        // Using a loop because JUnit 4 doesn't (really) support parameterized tests.
        System.out.println("Parameterized Test:");
        for (int i = 0; i < 5; i++)
        {
            Item inputItem = getDefaultItem();
            switch (i) {
                case 0:
                    inputItem.setArtifactName("");
                    System.out.println("\tmissing Name");
                    break;
                case 1:
                    inputItem.setDescription("");
                    System.out.println("\tmissing Description");
                    break;
                case 2:
                    inputItem.setCategory(EditArtifactFragment.SPINNER_DEFAULT_CATEGORY);
                    System.out.println("\tmissing Category");
                    break;
                case 3:
                    inputItem.setMaterial(EditArtifactFragment.SPINNER_DEFAULT_MATERIAL);
                    System.out.println("\tmissing Material");
                    break;
                case 4:
                    inputItem.setDynastyPeriod(EditArtifactFragment.SPINNER_DEFAULT_DYNASTY);
                    System.out.println("\tmissing Dynasty/Period");
                    break;
            }
            EditArtifactFragment sut = new EditArtifactFragment(null, null, new TestLogger());
            stubFields(sut, inputItem);

            boolean result = sut.validateFields();

            assertFalse(result);
        }
    }

    @Test
    public void validateFields_FilledItem_ReturnsTrue() {
        Item inputItem = new Item(
                "",
                "name",
                "description",
                "category",
                "material",
                "dynasty",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        );
        EditArtifactFragment sut = new EditArtifactFragment(null, null, new TestLogger());
        stubFields(sut, inputItem);

        boolean result = sut.validateFields();

        assertTrue(result);
    }

    @Test
    public void onSave_SuccessfulAddWrite_ErrorsByExiting() {
        parameterized_onSave_SuccessfulWrite_ErrorsByExiting(0);
    }
    @Test
    public void onSave_SuccessfulEditWrite_ErrorsByExiting() {
        parameterized_onSave_SuccessfulWrite_ErrorsByExiting(1);
    }
    private void parameterized_onSave_SuccessfulWrite_ErrorsByExiting(int i)
    {
        Item item = getDefaultItem();
        DbEditorAccess access = mock(DbEditorAccess.class);
        when(access.editItem(any())).thenReturn(DbEditorAccessResult.SUCCESS);
        EditArtifactFragment sut;
        if (i == 0) {
            System.out.println("\t[Add mode]");
            sut = new EditArtifactFragment(null, access, new TestLogger());
        } else {
            System.out.println("\t[Edit mode");
            sut = new EditArtifactFragment(item, access, new TestLogger());
        }
        stubFields(sut, item);

        ThrowingRunnable action = sut::onSave;

        // occurs because it tries to get the fragment manager
        assertThrows(IllegalStateException.class, action);
        verify(access, atLeastOnce()).editItem(any());
    }

    @Test
    public void onSave_AddWriteError_NothingHappens() {
        parameterized_onSave_WriteError_NothingHappens(0);
    }
    @Test
    public void onSave_EditWriteError_NothingHappens() {
        parameterized_onSave_WriteError_NothingHappens(1);
    }
    public void parameterized_onSave_WriteError_NothingHappens(int i) {
        Item item = getDefaultItem();
        DbEditorAccess access = mock(DbEditorAccess.class);
        when(access.editItem(any())).thenReturn(DbEditorAccessResult.ERROR);
        EditArtifactFragment sut;
        if (i == 0) {
            System.out.println("\t[Add mode]");
            sut = new EditArtifactFragment(null, access, new TestLogger());
        } else {
            System.out.println("\t[Edit mode]");
            sut = new EditArtifactFragment(item, access, new TestLogger());
        }
        stubFields(sut, item);

        sut.onSave();

        verify(sut.textViewLotNumber, never()).setText(any(CharSequence.class));
        verify(access, atLeastOnce()).editItem(any());
    }

    @Test
    public void onCancel_InAddMode_RequestsAddCancellationAndTryExit() {
        final String Lot = "sample-lot-123";
        DbEditorAccess access = mock(DbEditorAccess.class);
        EditArtifactFragment sut = new EditArtifactFragment(null, access, new TestLogger());
        sut.textViewLotNumber = mock(TextView.class);
        when(sut.textViewLotNumber.getText()).thenReturn(Lot);

        ThrowingRunnable testedAction = sut::onCancel;

        // occurs because it tries to get the fragment manager
        assertThrows(IllegalStateException.class, testedAction);
        verify(access, times(1)).cancelAdd(Lot);
        // i.e. the above was the *only* invocation.
        verify(access, times(1)).cancelAdd(any());
    }

    @Test
    public void onCancel_inEditMode_DoNotRequestAddCancellationAndTryExit() {
        DbEditorAccess access = mock(DbEditorAccess.class);
        Item someItem = getDefaultItem();
        EditArtifactFragment sut = new EditArtifactFragment(someItem, access, new TestLogger());
        sut.textViewLotNumber = mock(TextView.class);
        when(sut.textViewLotNumber.getText()).thenReturn(someItem.getLotNumber());

        ThrowingRunnable testedAction = sut::onCancel;

        // occurs because it tries to get the fragment manager
        assertThrows(IllegalStateException.class, testedAction);
        verify(access, never()).cancelAdd(any());
    }
}
