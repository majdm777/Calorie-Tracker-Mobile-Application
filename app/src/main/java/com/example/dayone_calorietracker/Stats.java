package com.example.dayone_calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dayone_calorietracker.Adapters.DaysAdapter;
import com.example.dayone_calorietracker.DataBase.Enitities.Day;
import com.example.dayone_calorietracker.Models.DaysViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Stats extends AppCompatActivity {

    BarChart barChart;
    RecyclerView recyclerView;
    DaysAdapter adapter;
    DaysViewModel viewModel;

    List<Day> GDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_stats);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.stats), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        barChart = findViewById(R.id.barChart);

        BottomNavigationView bottomNavigationView=findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_Stats);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            if (item.getItemId() == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }

            if (item.getItemId() == R.id.navigation_Stats) {
                Toast.makeText(this,"Stats",Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });

        recyclerView = findViewById(R.id.recStats);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DaysAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(DaysViewModel.class);
        viewModel.getDays().observe(this, days -> {
            this.GDays = days;
            getCurrentWeek(days);
            adapter.setDays(days);
        });

    }

    private void getCurrentWeek(List<Day> allDays) {

        List<Day> week = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek()); // start of week
        Date startOfWeek = cal.getTime();

        for (Day d : allDays) {
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(d.Date);

                if (date.after(startOfWeek) || date.equals(startOfWeek)) {
                    week.add(d);
                }

            } catch (Exception ignored) {}
        }

        setupChart(week);
    }

    private void setupChart(List<Day> days) {
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

        SharedPreferences sp = getSharedPreferences("UserInfo", MODE_PRIVATE);
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

            Toast.makeText(Stats.this, ""+clickedDay.calorie, Toast.LENGTH_SHORT).show();


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