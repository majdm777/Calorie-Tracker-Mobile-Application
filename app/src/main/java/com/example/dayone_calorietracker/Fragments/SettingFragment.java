package com.example.dayone_calorietracker.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.dayone_calorietracker.DataBase.AppDataBase;
import com.example.dayone_calorietracker.R;


public class SettingFragment extends Fragment {

    Button btnReset;
    public SettingFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        btnReset = view.findViewById(R.id.resetBtn);
        btnReset.setOnClickListener(v -> resetData());

        return view;
    }

    public void resetData(){
        new AlertDialog.Builder(getContext())
        .setTitle("Reset Data")
        .setMessage("This will delete everything. Continue?")
        .setPositiveButton("Yes", (d, w) -> {
            AppDataBase db = AppDataBase.getInstance(requireContext());
            db.daydao().deleteAll();
            db.mealsperdaydao().deleteAll();
            db.mealdao().deleteAll();
        })
        .setNegativeButton("Cancel", null)
        .show();
    }

}
