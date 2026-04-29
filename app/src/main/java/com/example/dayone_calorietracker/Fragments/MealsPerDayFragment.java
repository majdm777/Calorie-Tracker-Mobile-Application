package com.example.dayone_calorietracker.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.MealsPerDayAdapter;
import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.DataBase.Enitities.MealsPerDay;
import com.example.dayone_calorietracker.Models.MealsPerDayViewModel;
import com.example.dayone_calorietracker.R;

import java.util.ArrayList;
import java.util.List;

public class MealsPerDayFragment extends Fragment {

    RecyclerView recyclerView;
    MealsPerDayAdapter adapter;
    MealsPerDayViewModel viewModel;
    List<MealsPerDay> MPD = new ArrayList<>();

    Bundle bundle;



    public MealsPerDayFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meals_per_day_layout, container, false);
        AppDataBase db = AppDataBase.getInstance(requireContext());
        int dayId= getArguments().getInt("ID");
        Log.d("DB", "Fragment received dayId = " + dayId);
        recyclerView=view.findViewById(R.id.recMealsPerDay);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new MealsPerDayAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MealsPerDayViewModel.class);
        viewModel.getMealsPerDay()
            .observe(getViewLifecycleOwner(), meals -> {
                adapter.setMealsPerDay(meals);
            });


        return view;
    }
}
