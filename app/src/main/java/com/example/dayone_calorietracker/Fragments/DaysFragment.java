package com.example.dayone_calorietracker.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.DaysAdapter;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.example.dayone_calorietracker.R;

import java.util.ArrayList;
import java.util.List;

public class DaysFragment extends Fragment {

    RecyclerView recyclerView;
    DaysAdapter adapter;
    DaysViewModel viewModel;
    SearchView searchView;

    List<Day> days= new ArrayList<>();

    public DaysFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.days_fragment, container, false);

        recyclerView = view.findViewById(R.id.recDay);
        searchView = view.findViewById(R.id.searchView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DaysAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ViewModel
        viewModel = new ViewModelProvider(this).get(DaysViewModel.class);

        // Observe data
        viewModel.getDays().observe(getViewLifecycleOwner(), day -> {
            days = day;
            adapter.setDays(day);
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterDays(newText);
                return true;
            }
        });

        return view;
    }

    private void filterDays(String text) {
        List<Day> filtered = new ArrayList<>();

        for (Day day : days) {
            if (day.date.toLowerCase().contains(text.toLowerCase())) {
                filtered.add(day);
            }
        }

        adapter.setDays(filtered);
    }
}
