package com.cscb07.taamapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditArtifactFragment extends Fragment {
    private static final String TAG = "EditArtifactFragment";
    private boolean isEditing;
    private boolean isModeAdd() { return !isEditing; }

    @Nullable private final Item initialItem;
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

    public EditArtifactFragment()
    {
        this(null);
    }
    public EditArtifactFragment(@Nullable Item initialItem)
    {
        this.initialItem = initialItem;
    }
    private ArrayAdapter<CharSequence> initSpinner(Spinner spin, String default_value, int arrayId)
    {
        List<CharSequence> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayId)));
        options.add(0, default_value);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);

        return adapter;
    }
    private boolean trySetSelection(Spinner spin, ArrayAdapter<CharSequence> adapter, String value)
    {
        int idx = adapter.getPosition(value);
        if (idx == -1)
        {
            Log.w(TAG, "invalid value '" + value + "' set to spinner id: " + spin.getId());
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

        spinnerArtifactCategory = view.findViewById(R.id.spinnerArtifactCategory);
        spinnerArtifactMaterial = view.findViewById(R.id.spinnerArtifactMaterial);
        spinnerDynasty = view.findViewById(R.id.spinnerDynasty);
        ArrayAdapter<CharSequence> categoryAdapter = initSpinner(spinnerArtifactCategory, SPINNER_DEFAULT_CATEGORY, R.array.categories_array);
        ArrayAdapter<CharSequence> materialAdapter = initSpinner(spinnerArtifactMaterial, SPINNER_DEFAULT_MATERIAL, R.array.material_array);
        ArrayAdapter<CharSequence> dynastyAdapter = initSpinner(spinnerDynasty, SPINNER_DEFAULT_DYNASTY, R.array.dynasty_period_array);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        // TODO: set up save button behaviour.
        buttonSave.setOnClickListener(v -> Toast.makeText(getContext(), "Saving! (not really)", Toast.LENGTH_SHORT).show());

        Bundle args = getArguments();
        if (initialItem == null) {
            isEditing = false;
            // TODO: set to a unique Lot number.
            textViewLotNumber.setText("Temp Lot Num.");
        } else {
            isEditing = true;
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
}