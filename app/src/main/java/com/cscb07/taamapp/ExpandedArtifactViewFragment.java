package com.cscb07.taamapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import android.widget.ImageView;
import com.bumptech.glide.Glide;


public class ExpandedArtifactViewFragment extends Fragment {
    private final String Tag = "ExpandedArtifactViewFragment";
    private FirebaseDatabase db;
    private DatabaseReference ref;
    private String lot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
    View view = inflater.inflate(R.layout.fragment_expanded_artifact_view, container,false);
    db = FirebaseDatabase.getInstance();
    Bundle args = getArguments();
    if (args != null) {
        lot = args.getString("lotNumber");
        ref = db.getReference("artifacts").child(lot);
        Button deleteButton = view.findViewById(R.id.artifactDelete);
        TextView name = view.findViewById(R.id.name);
        ImageView imageView = view.findViewById(R.id.imageView5);
        TextView lotNum = view.findViewById(R.id.Lotnum);
        TextView description = view.findViewById(R.id.description);
        TextView category = view.findViewById(R.id.category);
        TextView material = view.findViewById(R.id.material);
        TextView dynastyPeriod = view.findViewById(R.id.dynastyPeriod);
        TextView culturalOrigin = view.findViewById(R.id.culturalOrigin);
        TextView dimensions = view.findViewById(R.id.dimensions);
        TextView conditionReport = view.findViewById(R.id.conditionReport);
        TextView currentLocation = view.findViewById(R.id.currentLocation);
        TextView acquisitionMethod = view.findViewById(R.id.acquisitionMethod);
        TextView provenance = view.findViewById(R.id.provenance);
        TextView accessionNumber = view.findViewById(R.id.accessionNumber);
        TextView notes = view.findViewById(R.id.notes);

        TextView culturalOriginHeader = view.findViewById(R.id.textView9);
        TextView dimensionsHeader = view.findViewById(R.id.textView10);
        TextView conditionReportHeader = view.findViewById(R.id.textView11);
        TextView currentLocationHeader = view.findViewById(R.id.textView12);
        TextView acquisitionMethodHeader = view.findViewById(R.id.textView13);
        TextView provenanceHeader = view.findViewById(R.id.textView14);
        TextView accessionNumberHeader = view.findViewById(R.id.textView15);
        TextView notesHeader = view.findViewById(R.id.textView16);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot data = snapshot.child("value");
                Item item = data.getValue(Item.class);
                if (item != null) {
                    name.setText(item.getArtifactName());
                    lotNum.setText(item.getLotNumber());
                    description.setText(item.getDescription());
                    category.setText(item.getCategory());
                    material.setText(item.getMaterial());
                    dynastyPeriod.setText(item.getDynastyPeriod());
                    culturalOrigin.setText(item.getCulturalOrigin());
                    dimensions.setText(item.getDimensions());
                    conditionReport.setText(item.getConditionReport());
                    currentLocation.setText(item.getCurrentLocation());
                    acquisitionMethod.setText(item.getAcquisitionMethod());
                    provenance.setText(item.getProvenance());
                    accessionNumber.setText(item.getAccessionNumber());
                    notes.setText(item.getNotes());
                    if (item.getImage() != null && !item.getImage().isEmpty()) {
                        Glide.with(requireContext())
                                .load(item.getImage())
                                .into(imageView);
                    }

                    if(!item.getCulturalOrigin().isEmpty()) {
                        culturalOriginHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getDimensions().isEmpty()) {
                        dimensionsHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getConditionReport().isEmpty()) {
                        conditionReportHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getCurrentLocation().isEmpty()) {
                        currentLocationHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getAcquisitionMethod().isEmpty()) {
                        acquisitionMethodHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getProvenance().isEmpty()) {
                        provenanceHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getAccessionNumber().isEmpty()) {
                        accessionNumberHeader.setVisibility(View.VISIBLE);
                    }
                    if(!item.getNotes().isEmpty()) {
                        notesHeader.setVisibility(View.VISIBLE);
                    }

                }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error fetching artifact fields.", databaseError.toException());
                }
            });

        }


        // Like/Unlike feature
        CheckBox likeButton = view.findViewById(R.id.likeButton);
        FirebaseUser acc = FirebaseAuth.getInstance().getCurrentUser();
        String uid = acc.getUid();

        if (uid != null && lot != null) {
            LikeManager likeManager = new LikeManager();

            // User liked artifacts
            likeManager.checkUserLikes(uid, lot, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    likeButton.setChecked(snapshot.exists());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to load user liked artifacts", error.toException());
                }
            });

            // Like count for artifacts
            likeManager.likeCountUpdater(lot, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Long count = snapshot.getValue(Long.class);

                    // If the count is null, it means no one has liked the artifact yet.
                    likeButton.setText(String.valueOf(count != null ? count : "0"));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Firebase", "Failed to load like count", error.toException());
                }
            });

            // Like button toggled
            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // Guest can't like
                    if (acc.isAnonymous()) {
                        likeButton.setChecked(false);
                        Toast.makeText(getContext(), "Please sign in to like artifacts", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Users and admins can like/unlike
                    likeManager.toggleLike(uid, lot, likeButton.isChecked());
                }
            });
        }


        // Saved artifact feature
        ToggleButton saveArtifactButton = view.findViewById(R.id.saveArtifactToggle);
        saveArtifactButton.setClickable(false);

        if (uid != null) {
            SavedArtifactWriter savedArtifactWriter = new SavedArtifactWriter();
            saveArtifactButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToggleButton instance = (ToggleButton) view;
                    if (uid == null) {
                        Log.w(Tag, "Null user, 'saved artifact' button shouldn't be accessible");
                        return;
                    }
                    if (instance.isChecked()) {
                        Log.i(Tag, "adding artifact " + lot + " to user's collection: " + uid);
                        savedArtifactWriter.addSavedArtifact(uid, lot);
                    } else {
                        Log.i(Tag, "removing artifact " + lot + " from user's collection: " + uid);
                        savedArtifactWriter.removeSavedArtifact(uid, lot);
                    }
                }
            });
            saveArtifactButton.setClickable(false);

            SavedArtifactReader reader = SavedArtifactReader.getInstance(uid);
            reader.addOnSavedArtifactChangedListener(lot, new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    saveArtifactButton.setClickable(true);
                    saveArtifactButton.setChecked(snapshot.exists());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(Tag, "listener cancelled", error.toException());
                }
            });


            // Comments
            Button addComment = view.findViewById(R.id.addComment);
            EditText commentContent = view.findViewById(R.id.commentField);
            if (!acc.isAnonymous()) {
                List<Comment> commentList = new ArrayList<>();
                CommentManager commentManager = new CommentManager(this.lot, uid, getContext(), commentList);
                CommentAdapter adapter = new CommentAdapter(commentList, commentManager);
                addComment.setVisibility(View.VISIBLE);
                commentContent.setVisibility(View.VISIBLE);
                ref = db.getReference("users/" + uid).child("name");
                addComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!commentContent.getText().toString().isBlank()) {
                            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String name = snapshot.getValue(String.class);
                                    Comment comment = new Comment(uid, name, commentContent.getText().toString());
                                    commentManager.addComment(comment);
                                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Log.e(Tag, "Error getting account name");
                                }
                            });
                        } else {
                            Toast.makeText(getContext(), "Comment cannot be empty.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }

        return view;
    }
}
