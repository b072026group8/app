package com.cscb07.taamapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cscb07.taamapp.util.DefaultLogger;
import com.cscb07.taamapp.util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.net.Uri;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

public class EditArtifactFragment extends Fragment {
    private static final String TAG = "EditArtifactFragment";

    @Nullable private final Item initialItem;
    private final boolean isEditing() { return initialItem != null; }
    private final boolean isAdding() { return initialItem == null; }
    private final DbEditorAccess dbAccess;
    private final Logger log;
    TextView textViewLotNumber;
    EditText
            editTextName,
            editTextArtifactDescription,
            editTextCulturalOrigin,
            editTextDimensions,
            editTextConditionReport,
            editTextCurrentLocation,
            editTextAcquisitionMethod,
            editTextProvenance,
            editTextAccessionNumber,
            editTextNotes;
    Spinner
            spinnerArtifactCategory,
            spinnerArtifactMaterial,
            spinnerDynasty;
    private ImageView imageArtifact;
    private Button buttonSelectImage;
    private Uri selectedImageUri;
    private SupabaseImageUploader imageUploader;

    private final ActivityResultLauncher<String> imagePickerLauncher =
            registerForActivityResult(
                    new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedImageUri = uri;
                            imageArtifact.setImageURI(uri);
                        }
                    }
            );
    private Object getSelectOrDefault(Spinner spinner, Object defaultValue)
    {
        int position = spinner.getSelectedItemPosition();
        return position == Spinner.INVALID_POSITION ? defaultValue
                                                    : spinner.getAdapter().getItem(position);
    }
    public String getSpinnerCategory() {
        return getSelectOrDefault(spinnerArtifactCategory, SPINNER_DEFAULT_CATEGORY).toString();
    }
    public String getSpinnerMaterial() {
        return getSelectOrDefault(spinnerArtifactMaterial, SPINNER_DEFAULT_MATERIAL).toString();
    }
    public String getSpinnerDynasty() {
        return getSelectOrDefault(spinnerDynasty, SPINNER_DEFAULT_DYNASTY).toString();
    }

    public static final String SPINNER_DEFAULT_CATEGORY = "select category:";
    public static final String SPINNER_DEFAULT_MATERIAL = "select material:";
    public static final String SPINNER_DEFAULT_DYNASTY = "select dynasty/period:";

    /** @deprecated */
    public EditArtifactFragment()
    {
        this(null, null);
    }
    /** @deprecated */
    public EditArtifactFragment(@Nullable Item initialItem) {
        this(initialItem, null);
    }
    public EditArtifactFragment(@Nullable Item initialItem, DbEditorAccess dbAccess) {
        this(initialItem, dbAccess, DefaultLogger.Instance());
    }
    public EditArtifactFragment(@Nullable Item initialItem, DbEditorAccess dbAccess, Logger log) {
        this.initialItem = initialItem;
        this.dbAccess = dbAccess;
        this.log = log;
        if (dbAccess == null) {
            log.e(TAG, "instantiated with a null dbAccess instance.");
        }
    }
    private ArrayAdapter<CharSequence> initSpinner(Spinner spin, String default_value, int arrayId)
    {
        List<CharSequence> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayId)));
        options.add(0, default_value);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), R.layout.edit_spinner_textview, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        return adapter;
    }
    private boolean trySetSelection(Spinner spin, ArrayAdapter<CharSequence> adapter, String value)
    {
        int idx = adapter.getPosition(value);
        if (idx == -1)
        {
            log.w(TAG, "invalid value '" + value + "' set to spinner id: " + spin.getId());
            return false;
        }
        spin.setSelection(idx);
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_artifact, container, false);
        imageUploader = new SupabaseImageUploader(requireContext());
        textViewLotNumber = view.findViewById(R.id.textViewLotNumber);
        editTextName = view.findViewById(R.id.editTextName);
        editTextArtifactDescription = view.findViewById(R.id.editTextArtifactDescription);
        editTextCulturalOrigin = view.findViewById(R.id.editTextCulturalOrigin);
        editTextDimensions = view.findViewById(R.id.editTextDimensions);
        editTextConditionReport = view.findViewById(R.id.editTextConditionReport);
        editTextCurrentLocation = view.findViewById(R.id.editTextCurrentLocation);
        editTextAcquisitionMethod = view.findViewById(R.id.editTextAcquisitionMethod);
        editTextProvenance = view.findViewById(R.id.editTextProvenance);
        editTextAccessionNumber = view.findViewById(R.id.editTextAccessionNumber);
        editTextNotes = view.findViewById(R.id.editTextNotes);

        imageArtifact = view.findViewById(R.id.imageArtifact);
        buttonSelectImage = view.findViewById(R.id.buttonSelectImage);

        spinnerArtifactCategory = view.findViewById(R.id.spinnerArtifactCategory);
        spinnerArtifactMaterial = view.findViewById(R.id.spinnerArtifactMaterial);
        spinnerDynasty = view.findViewById(R.id.spinnerDynasty);
        ArrayAdapter<CharSequence> categoryAdapter = initSpinner(spinnerArtifactCategory, SPINNER_DEFAULT_CATEGORY, R.array.categories_array);
        ArrayAdapter<CharSequence> materialAdapter = initSpinner(spinnerArtifactMaterial, SPINNER_DEFAULT_MATERIAL, R.array.material_array);
        ArrayAdapter<CharSequence> dynastyAdapter = initSpinner(spinnerDynasty, SPINNER_DEFAULT_DYNASTY, R.array.dynasty_period_array);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        buttonCancel.setOnClickListener(v -> onCancel());
        buttonSave.setOnClickListener(v -> onSave());

        buttonSelectImage.setOnClickListener(v ->
                imagePickerLauncher.launch("image/*")
        );

        if (initialItem == null) {
            if (dbAccess == null) {
                log.wtf(TAG, "dbAccess is null");
                textViewLotNumber.setText("Config Error");
            } else {
                textViewLotNumber.setText(dbAccess.getUniqueLotNumber());
            }

        } else {
            Item initial = initialItem;
            textViewLotNumber.setText(initial.getLotNumber());
            editTextName.setText(initial.getArtifactName());
            editTextArtifactDescription.setText(initial.getDescription());
            editTextCulturalOrigin.setText(initial.getCulturalOrigin());
            editTextDimensions.setText(initial.getDimensions());
            editTextConditionReport.setText(initial.getConditionReport());
            editTextCurrentLocation.setText(initial.getCurrentLocation());
            editTextAcquisitionMethod.setText(initial.getAcquisitionMethod());
            editTextProvenance.setText(initial.getProvenance());
            editTextAccessionNumber.setText(initial.getAccessionNumber());
            editTextNotes.setText(initial.getNotes());

            if (!trySetSelection(spinnerArtifactCategory, categoryAdapter, initial.getCategory()))
                Toast.makeText(getContext(), "Invalid category value: " + initial.getCategory(), Toast.LENGTH_LONG).show();
            if (!trySetSelection(spinnerArtifactMaterial, materialAdapter, initial.getMaterial()))
                Toast.makeText(getContext(), "Invalid material value: " + initial.getCategory(), Toast.LENGTH_LONG).show();
            if (!trySetSelection(spinnerDynasty, dynastyAdapter, initial.getDynastyPeriod()))
                Toast.makeText(getContext(), "Invalid dynasty value: " + initial.getCategory(), Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        onCancel(false);
    }

    private void errorEmptyField(String fieldName) {
        if (getContext() != null)
            Toast.makeText(getContext(), "Please give a " + fieldName, Toast.LENGTH_SHORT).show();
    }
    private void errorUnsetSpinner(String spinnerName) {
        if (getContext() != null)
            Toast.makeText(getContext(), "Please select a " + spinnerName, Toast.LENGTH_SHORT).show();
    }

    private String getTextViewValue(TextView view) { return view.getText().toString().trim(); }
    Item createItem(String imageUrl) {
        return new Item(
                getTextViewValue(textViewLotNumber),
                getTextViewValue(editTextName),
                getTextViewValue(editTextArtifactDescription),
                getSpinnerCategory(),
                getSpinnerMaterial(),
                getSpinnerDynasty(),
                getTextViewValue(editTextCulturalOrigin),
                getTextViewValue(editTextDimensions),
                getTextViewValue(editTextConditionReport),
                getTextViewValue(editTextCurrentLocation),
                getTextViewValue(editTextAcquisitionMethod),
                getTextViewValue(editTextProvenance),
                getTextViewValue(editTextAccessionNumber),
                getTextViewValue(editTextNotes),
                imageUrl
        );
    }
     boolean validateFields() {
        if (editTextName.getText().toString().trim().isEmpty()) {
            errorEmptyField("Name");
            return false;
        }
        if (editTextArtifactDescription.getText().toString().trim().isEmpty()) {
            errorEmptyField("Description");
            return false;
        }
        if (SPINNER_DEFAULT_CATEGORY.equals(getSpinnerCategory())) {
            errorUnsetSpinner("Category");
            return false;
        }
        if (SPINNER_DEFAULT_MATERIAL.equals(getSpinnerMaterial())) {
            errorUnsetSpinner("Material");
            return false;
        }
        if (SPINNER_DEFAULT_DYNASTY.equals(getSpinnerDynasty())) {
            errorUnsetSpinner("Dynasty/Period");
            return false;
        }
        return true;
    }

    private void exitEditArtifact() {
        getParentFragmentManager().popBackStack();
    }
    void onCancel() {
        onCancel(true);
    }
    private void onCancel(boolean cancelling){
        if (isAdding()) {
            if (dbAccess == null)
                log.wtf(TAG, "dbAccess is null, can't cancel addition");
            else
                dbAccess.cancelAdd(textViewLotNumber.getText().toString().trim());
        }
        if (cancelling) {
            exitEditArtifact();
        }
    }
    void saveItem(String imageUrl) {
        DbEditorAccessResult result =
                dbAccess.editItem(createItem(imageUrl));

        switch (result) {
            case SUCCESS:
                exitEditArtifact();
                return;

            case ERROR:
                log.i(TAG, "error uploading to db.");

                if (getContext() != null) {
                    Toast.makeText(
                            getContext(),
                            "Error saving\nPlease try again later",
                            Toast.LENGTH_LONG
                    ).show();
                }
                break;
        }
    }

    void onSave() {
        if (!validateFields())
        {
            return;
        }
        if (dbAccess == null) {
            Toast.makeText(getContext(), "App config error\nNull DbAccess", Toast.LENGTH_LONG).show();
            log.wtf(TAG, "Cannot access Db: DbEditAccess instance is null");
            return;
        }
        String existingImageUrl =
                initialItem == null ? "" : initialItem.getImage();

        if (selectedImageUri == null) {
            saveItem(existingImageUrl);
            return;
        }

        String lotNumber = getTextViewValue(textViewLotNumber);

        imageUploader.uploadImage(
                selectedImageUri,
                lotNumber,
                new SupabaseImageUploader.UploadCallback() {
                    @Override
                    public void onSuccess(String publicUrl) {
                        saveItem(publicUrl);
                    }

                    @Override
                    public void onError(String message) {
                        if (getContext() != null) {
                            Toast.makeText(
                                    getContext(),
                                    message,
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    }
                }
        );
    }
}