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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditArtifactFragment extends Fragment {
    private boolean isEditing;
    private boolean isModeAdd() { return !isEditing; }

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


    public static final String SPINNER_DEFAULT_CATEGORY = "select category:";
    public static final String SPINNER_DEFAULT_MATERIAL = "select material:";
    public static final String SPINNER_DEFAULT_DYNASTY = "select dynasty/period:";
    private void initSpinner(Spinner spin, String default_value, int arrayId)
    {
        List<CharSequence> options = new ArrayList<>(Arrays.asList(getResources().getStringArray(arrayId)));
        options.add(0, default_value);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
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
        spinnerArtifactCategory = view.findViewById(R.id.spinnerArtifactCategory);
        initSpinner(spinnerArtifactCategory, SPINNER_DEFAULT_CATEGORY, R.array.categories_array);
        spinnerArtifactMaterial = view.findViewById(R.id.spinnerArtifactMaterial);
        initSpinner(spinnerArtifactMaterial, SPINNER_DEFAULT_MATERIAL, R.array.material_array);
        spinnerDynasty = view.findViewById(R.id.spinnerDynasty);
        initSpinner(spinnerDynasty, SPINNER_DEFAULT_DYNASTY, R.array.dynasty_period_array);
        editTextCulturalOrigin = view.findViewById(R.id.editTextCulturalOrigin);
        editTextDimensions = view.findViewById(R.id.editTextDimensions);
        editTextConditionReport = view.findViewById(R.id.editTextConditionReport);
        editTextCurrentLocation = view.findViewById(R.id.editTextCurrentLocation);
        editTextAcquisitionMethod = view.findViewById(R.id.editTextAcquisitionMethod);
        editTextProvenance = view.findViewById(R.id.editTextProvenance);
        editTextAccessionNumber = view.findViewById(R.id.editTextAccessionNumber);
        editTextNotes = view.findViewById(R.id.editTextNotes);

        Button buttonCancel = view.findViewById(R.id.buttonCancel);
        Button buttonSave = view.findViewById(R.id.buttonSave);

        buttonCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        // TODO: set up save button behaviour.
        buttonSave.setOnClickListener(v -> Toast.makeText(getContext(), "Saving! (not really)", Toast.LENGTH_SHORT).show());


        return view;
    }
}