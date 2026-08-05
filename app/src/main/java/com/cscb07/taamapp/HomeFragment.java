package com.cscb07.taamapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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
    private final String[] paginationValues = {"All", "12", "24"};
    private int paginationIndex = 0;


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
        if (itemMapProvider == null) {
            Log.i(Tag, "setting itemMapProvider to default instance");
            itemMapProvider = ItemMapProvider.getInstance();
        }
        if (savedItemProvider == null) {
            Log.i(Tag, "setting savedItemProvider to default instance");
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
        Button buttonLogout = view.findViewById(R.id.buttonLogout);
        Button buttonPagination = view.findViewById(R.id.buttonPagination);
        Button buttonLeft = view.findViewById(R.id.buttonLeft);
        Button buttonRight = view.findViewById(R.id.buttonRight);
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

        loadPaginationPref(buttonPagination);

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

        // Logout user
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        // Pagination Button for artifacts
        buttonPagination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePagination();
                buttonPagination.setText(paginationValues[paginationIndex]);
                savePagination(paginationIndex);
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
            public void onClick(View v) { loadFragment(new EditArtifactFragment(null, new FireSupaDbEditorAccess()));}
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
        if (getParentFragmentManager().getBackStackEntryCount() > 0) {
            itemAdapter.setPopBackStackId(getParentFragmentManager().getBackStackEntryAt(getParentFragmentManager().getBackStackEntryCount() - 1).getId());
        } else {
            Log.w(Tag, "No backstack entry, cannot give Id for views to pop back to.");
        }
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

    /**
     * When the back button is pressed on the home page, log out the user instead of just returning them to the login page
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                logout();  // Logout if back button is pressed from the homepage
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        System.out.println("User logged out");
        showToast("User logged out");

        FragmentManager fragmentManager = getParentFragmentManager();

        // Stack should have something. Shouldn't be empty

        if (fragmentManager.getBackStackEntryCount() > 0) {
            // Go to the first view in stack (login page) upon logging out
            fragmentManager.popBackStack(fragmentManager.getBackStackEntryAt(0).getId(), 0);
        }
    }

    /*
    Calling this method automatically cycles to the next pagination setting. All -> 12 -> 24 -> ...
     */
    private void updatePagination() {
        paginationIndex++;
        if (paginationIndex > paginationValues.length - 1) {
            paginationIndex = 0;
        }

        // Update the amt of items on display
        updateLimitInItemAdaptor();
    }

    /*
    Retrieve saved pagination value, and update the button and the actual amount of items on display
     */
    private void loadPaginationPref(Button buttonPagination) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        paginationIndex = sharedPreferences.getInt("pagination_index", 0);

        if (buttonPagination != null) {
            buttonPagination.setText(paginationValues[paginationIndex]);
        }

        // Actually display the correct amount of items
        updateLimitInItemAdaptor();
    }

    /*
    Save the current pagination setting using sharedPreferences
     */
    private void savePagination(int paginationIndex) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("pagination_index", paginationIndex);
        editor.apply();
    }

    /*
    Update the number of artifacts displayed in the ui
     */
    private void updateLimitInItemAdaptor() {
        String selectedPagination = paginationValues[paginationIndex];
        int limit = 0;

        if (!selectedPagination.equals("All")) {
            limit = Integer.parseInt(selectedPagination);
        }

        if (itemAdapter != null) {
            itemAdapter.setItemLimit(limit);
        }
    }

    public void showToast(String m) {
        if (getContext() != null) {
            Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
        }
    }
}
