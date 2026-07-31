package com.cscb07.taamapp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cscb07.taamapp.auth.AccountType;
import com.cscb07.taamapp.itemSorting.ItemFilterList;
import com.cscb07.taamapp.util.ListStrategy;
import com.cscb07.taamapp.util.Provider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private final String Tag = "HomeFragment";
    private ItemAdapter itemAdapter;
    private Provider<Map<String, Item>> itemMapProvider;
    private Provider<List<Item>> savedItemProvider;
    private List<Item> itemList;
    private List<Item> savedItemList;
    private ListStrategy<Item> displayItemList;
    private ItemFilterList searchList;
    private FirebaseDatabase db;
    private DatabaseReference itemsRef;
    private FirebaseUser user;

    /**
     * Creates an instance with default implementations of interfaces.
     */
    public HomeFragment() {}

    /**
     * Creates an instance with the specified implementations.
     */
    public HomeFragment(Provider<Map<String, Item>> itemMapProvider, Provider<List<Item>> savedItemProvider) {
        this.itemMapProvider = itemMapProvider;
        this.savedItemProvider = savedItemProvider;
    }

    /**
     * Set default implementations to interfaces that are <b>unset</b>.
     */
    private void setMissingImplementations() {
        if (itemMapProvider == null)
            itemMapProvider = ItemMapProvider.getInstance();
        if (savedItemProvider == null) {
            String uid;
            if (user == null || user.isAnonymous()) {
                uid = "";
                Log.d(Tag, "user not signed in or is anonymous, using empty uid for SavedArtifactListProvider");
            } else {
                uid = user.getUid();
            }
            savedItemProvider = new SavedArtifactListProvider(itemMapProvider, uid);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        Button buttonRecyclerView = view.findViewById(R.id.buttonFilterSaved);
        Button buttonManageItems = view.findViewById(R.id.buttonManageItems);
        RecyclerView artifactCardGrid = view.findViewById(R.id.artifactCardGrid);
        EditText searchBar = view.findViewById(R.id.homeSearchEditText);

        user = FirebaseAuth.getInstance().getCurrentUser();

        setMissingImplementations();

        itemList = new ArrayList<>();
        savedItemList = savedItemProvider.getValue();
        displayItemList = new ListStrategy<>(itemList);
        searchList = new ItemFilterList(displayItemList);
        itemAdapter = new ItemAdapter(searchList, getParentFragmentManager().beginTransaction());
        db = FirebaseDatabase.getInstance();

        itemMapProvider.registerObserver(map -> {
            itemList.clear();
            itemList.addAll(map.values());

            if (displayItemList.getListStrategy() == itemList) {
                searchList.requery();
                itemAdapter.notifyDataSetChanged();
            }
        });

        savedItemProvider.registerObserver(list -> {
            List<Item> previousList = savedItemList;
            savedItemList = list;
            if (displayItemList.getListStrategy() == previousList) {
                displayItemList.setListStrategy(list);
                searchList.requery();
                itemAdapter.notifyDataSetChanged();
            }
        });

        buttonRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (buttonRecyclerView.getText().equals(getString(R.string.show_saved))) {
                    buttonRecyclerView.setText(R.string.show_all);
                    displayItemList.setListStrategy(savedItemList);
                } else {
                    buttonRecyclerView.setText(R.string.show_saved);
                    displayItemList.setListStrategy(itemList);
                }
                searchList.requery();
                itemAdapter.notifyDataSetChanged();
            }
        });

        buttonManageItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { loadFragment(new AddItemFragment());}
        });

        if (user != null && !user.isAnonymous()) {
            buttonRecyclerView.setVisibility(View.VISIBLE);
            DatabaseReference userInstance = db.getReference("users").child(user.getUid()).child("accountType");
            userInstance.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userType = snapshot.getValue(String.class);
                    if (userType != null && userType.equals(AccountType.ADMIN)) {
                        buttonManageItems.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e(Tag, "Something went wrong fetching user account type.");
                }
            });
        }

        artifactCardGrid.setLayoutManager(new GridLayoutManager(getContext(), 3));
        itemAdapter.setPopBackStackId(getParentFragmentManager().getBackStackEntryAt(getParentFragmentManager().getBackStackEntryCount() - 1).getId());
        artifactCardGrid.setAdapter(itemAdapter);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String query = editable.toString().trim();
                Log.d(Tag, "new search query: " + query);
                searchList.queryKeyword(query);
                itemAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
