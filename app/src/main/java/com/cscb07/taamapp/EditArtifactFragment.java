package com.cscb07.taamapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class EditArtifactFragment extends Fragment {

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
        spinnerArtifactMaterial = view.findViewById(R.id.spinnerArtifactMaterial);
        spinnerDynasty = view.findViewById(R.id.spinnerDynasty);
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