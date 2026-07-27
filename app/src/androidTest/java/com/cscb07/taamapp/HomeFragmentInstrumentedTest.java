package com.cscb07.taamapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cscb07.taamapp.util.Provider;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import android.util.Log;
import android.view.View;
import android.widget.Button;

@RunWith(AndroidJUnit4.class)
public class HomeFragmentInstrumentedTest {
    private static final String TAG = "HomeFragmentInstrumentedTest";
    @NonNull
    private static Item createSomeItem(String lot) {
        return new Item(lot,
                "sample-name-" + lot,
                "very brittle old shield-" + lot,
                "weaponry", "bronze", "old dynasty period",
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
    }

    @Test
    public void fragmentCreation_HasDefaultMap_ExistsInAdapter() {
        HashMap<String, Item> sourceMap = new HashMap<>();
        sourceMap.put("lot1", createSomeItem("lot1"));
        ArrayList<Item> lotSavedArtifacts = new ArrayList<>();
        Provider<Map<String, Item>> sourceProvider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return sourceMap;
            }
        };
        Provider<List<Item>> savedItemProvider = new Provider<List<Item>>() {
            @Override
            public List<Item> getValue() {
               return lotSavedArtifacts;
            }
        };
        FragmentFactory factory = new FragmentFactory() {
            @Override @NonNull
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new HomeFragment(sourceProvider, savedItemProvider);
            }
        };


        try (FragmentScenario<HomeFragment> scenario = FragmentScenario.launch(HomeFragment.class, null, factory)) {
            scenario.onFragment(f -> {


                assertNotNull(f.getView());
                View view = f.getView();
                RecyclerView gridView = view.findViewById(R.id.artifactCardGrid);
                assertEquals(1, gridView.getAdapter().getItemCount());
            });
        }
    }

    @Test
    public void callbacks_SourceMapChanges_AdapterUpdated() {
        Map<String, Item> sourceMap = new HashMap<>();
        sourceMap.put("lot1", createSomeItem("lot1"));
        List<Item> lotSavedArtifacts = new ArrayList<>();
        Provider<Map<String, Item>> sourceProvider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return sourceMap;
            }
        };
        Provider<List<Item>> savedItemProvider = new Provider<List<Item>>() {
            @Override
            public List<Item> getValue() {
                return lotSavedArtifacts;
            }
        };
        FragmentFactory factory = new FragmentFactory() {
            @Override @NonNull
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new HomeFragment(sourceProvider, savedItemProvider);
            }
        };
        try (FragmentScenario<HomeFragment> scenario = FragmentScenario.launch(HomeFragment.class, null, factory)) {


            sourceMap.put("lot2", createSomeItem("lot2"));
            sourceMap.put("lot3", createSomeItem("lot3"));
            sourceProvider.updateValue();


            scenario.onFragment(f -> {
                assertNotNull(f.getView());
                View view = f.getView();
                RecyclerView gridView = view.findViewById(R.id.artifactCardGrid);
                assertEquals(3, gridView.getAdapter().getItemCount());
            });
        }
    }

    @Test
    public void callbacks_SwitchToSavedArtifacts_AdapterUpdated() {
        Map<String, Item> sourceMap = new HashMap<>();
        sourceMap.put("lot1", createSomeItem("lot1"));
        sourceMap.put("lot2", createSomeItem("lot2"));
        sourceMap.put("lot3", createSomeItem("lot3"));
        List<Item> lotSavedArtifacts = new ArrayList<>();
        lotSavedArtifacts.add(sourceMap.get("lot1"));
        lotSavedArtifacts.add(sourceMap.get("lot2"));
        Provider<Map<String, Item>> sourceProvider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return sourceMap;
            }
        };
        Provider<List<Item>> savedItemProvider = new Provider<List<Item>>() {
            @Override
            public List<Item> getValue() {
                return lotSavedArtifacts;
            }
        };
        FragmentFactory factory = new FragmentFactory() {
            @Override @NonNull
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new HomeFragment(sourceProvider, savedItemProvider);
            }
        };
        try (FragmentScenario<HomeFragment> scenario = FragmentScenario.launch(HomeFragment.class, null, factory)) {
            scenario.onFragment(f -> {


                Button button = f.getView().findViewById(R.id.buttonFilterSaved);
                button.callOnClick();


                RecyclerView gridView = f.getView().findViewById(R.id.artifactCardGrid);
                assertEquals(2, gridView.getAdapter().getItemCount());
            });
        }
    }

    @Test
    public void callbacks_SavedArtifactsUpdated_AdapterUpdated() {
        Map<String, Item> sourceMap = new HashMap<>();
        sourceMap.put("lot1", createSomeItem("lot1"));
        sourceMap.put("lot2", createSomeItem("lot2"));
        sourceMap.put("lot3", createSomeItem("lot3"));
        List<Item> lotSavedArtifacts = new ArrayList<>();
        lotSavedArtifacts.add(sourceMap.get("lot1"));
        lotSavedArtifacts.add(sourceMap.get("lot2"));
        Provider<Map<String, Item>> sourceProvider = new Provider<Map<String, Item>>() {
            @Override
            public Map<String, Item> getValue() {
                return sourceMap;
            }
        };
        Provider<List<Item>> savedItemProvider = new Provider<List<Item>>() {
            @Override
            public List<Item> getValue() {
                return lotSavedArtifacts;
            }
        };
        FragmentFactory factory = new FragmentFactory() {
            @Override @NonNull
            public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
                return new HomeFragment(sourceProvider, savedItemProvider);
            }
        };
        try (FragmentScenario<HomeFragment> scenario = FragmentScenario.launch(HomeFragment.class, null, factory)) {
            scenario.onFragment(f -> {
                Button button = f.getView().findViewById(R.id.buttonFilterSaved);
                button.callOnClick();


                lotSavedArtifacts.add(sourceMap.get("lot3"));
                savedItemProvider.updateValue();


                RecyclerView gridView = f.getView().findViewById(R.id.artifactCardGrid);
                assertEquals(3, gridView.getAdapter().getItemCount());
            });
        }
    }
}
