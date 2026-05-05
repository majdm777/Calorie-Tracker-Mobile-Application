package com.example.dayone_calorietracker.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StatsFragment extends Fragment {

    BarChart barChart;
    RecyclerView recyclerView;
    DaysAdapter adapter;
    DaysViewModel viewModel;
    Button btnLastWeek,btnLastMonth;
    List<Day> GDays;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stats_fragment, container, false);

        barChart = view.findViewById(R.id.barChart);

        recyclerView = view.findViewById(R.id.recDay);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new DaysAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DaysViewModel.class);
        viewModel.getDays().observe(getViewLifecycleOwner(), days -> {
            this.GDays = days;
            getCurrentWeek(days);
            adapter.setDays(days);
        });
        btnLastWeek = view.findViewById(R.id.LastWeek);
        btnLastWeek.setOnClickListener(v->{displayWeek();});

        btnLastMonth =view.findViewById(R.id.LastMonth);
        btnLastMonth.setOnClickListener(v->{displayMonth();});


        return view;
    }

    private void getCurrentWeek(List<Day> allDays) {
        List<Day> week = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = cal.getTime();

        Calendar endCal = (Calendar) cal.clone();
        endCal.add(Calendar.DAY_OF_WEEK, 6);
        Date endOfWeek = endCal.getTime();

        for (Day d : allDays) {
            try {
                Date date = sdf.parse(d.Date);
                if (!date.before(startOfWeek) && !date.after(endOfWeek)) {
                    week.add(d);
                }
            } catch (Exception ignored) {}
        }

        setupChart(week);
    }
    private void getCurrentMonth(List<Day> allDays) {
        List<Day> month = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1); // first day of month
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfMonth = cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // last day of month
        Date endOfMonth = cal.getTime();

        for (Day d : allDays) {
            try {
                Date date = sdf.parse(d.Date);
                if (!date.before(startOfMonth) && !date.after(endOfMonth)) {
                    month.add(d);
                }
            } catch (Exception ignored) {}
        }

        setupChart(month);
    }

    public void displayMonth(){
        getCurrentMonth(GDays);
    }
    public void displayWeek(){
        getCurrentWeek(GDays);
    }


    private void setupChart(List<Day> days) {
        barChart.clear(); // restart everything

        if (days == null || days.isEmpty()) {
            barChart.clear(); // Clear chart if no data
            return;
        }

        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        float totalCal = 0;

        for (int i = 0; i < days.size(); i++) {
            Day day = days.get(i);
            entries.add(new BarEntry(i, (float) day.calorie));
            // Safe substring check for labels
            String label = day.Date.length() > 5 ? day.Date.substring(5) : day.Date;
            labels.add(label);
            totalCal += day.calorie;
        }

        float avgValue = totalCal / days.size();

        BarDataSet dataSet = new BarDataSet(entries, "Calories");

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.3f);

        // 1. Set Data First
        barChart.setData(data);

        // 2. Setup Legend safely
        Legend legend = barChart.getLegend();
        List<LegendEntry> entriesLegend = new ArrayList<>();
        entriesLegend.add(new LegendEntry("Calories", Legend.LegendForm.CIRCLE, 10f, 2f, null, Color.BLUE));
        entriesLegend.add(new LegendEntry("Target", Legend.LegendForm.LINE, 10f, 2f, null, Color.RED));
        entriesLegend.add(new LegendEntry("Average", Legend.LegendForm.LINE, 10f, 2f, null, Color.GREEN));
        legend.setCustom(entriesLegend);

        // 3. Setup Axis and Limit Lines (Crucial: Clear old lines first!)
        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // Prevents multiple lines stacking

        SharedPreferences sp =requireContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        float targetValue = Float.parseFloat(sp.getString("User_Target", "2000"));

        LimitLine targetLine = new LimitLine(targetValue, "Target");
        targetLine.setLineColor(Color.RED);
        targetLine.setLineWidth(2f);

        LimitLine avgLine = new LimitLine(avgValue, "Average");
        avgLine.setLineColor(Color.GREEN);
        avgLine.setLineWidth(2f);
        avgLine.enableDashedLine(10f, 10f, 0f);

        leftAxis.addLimitLine(targetLine);
        leftAxis.addLimitLine(avgLine);

        // 4. Formatting
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

                int index = (int) e.getX(); // position of bar

                Day clickedDay = days.get(index); // 🔥 match your list

                Toast.makeText(requireContext(), ""+clickedDay.calorie, Toast.LENGTH_SHORT).show();


                // 👉 open MealsPerDayFragment (optional)

            }

            @Override
            public void onNothingSelected() {
            }
        });
        // 5. Refresh
        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }
}
