package com.example.dayone_calorietracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class UserInfo extends AppCompatActivity {

    EditText Weight;
    EditText Height;
    EditText Age;
    EditText TargetCalorie;

    Button saveButton;

    SharedPreferences sp;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.info_user);
        //linking edit texts
        Weight =findViewById(R.id.user_weight);
        Height=  findViewById(R.id.user_height);
        Age= findViewById(R.id.user_age);
        TargetCalorie = findViewById(R.id.user_target);
        saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInfo();
            }
        });

        //sharedPreference
        sp = getSharedPreferences("UserInfo",MODE_PRIVATE);
        boolean isFirstTime = sp.getBoolean("isFirstTime", true);

        if(!isFirstTime){
            LoadInfo();
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.user_info_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


    }

    public void LoadInfo(){
        String weight = sp.getString("User_Weight","0");

        String height = sp.getString("User_Height","0");

        String age = sp.getString("User_Age","0");

        String targetCalorie = sp.getString("User_Target","2000");

        Weight.setText(weight);
        Height.setText(height);
        Age.setText(age);
        TargetCalorie.setText(targetCalorie);
    }

    public boolean saveInfo(){
        try {
            String weight = Weight.getText().toString();
            String height = Height.getText().toString();
            String age = Age.getText().toString();
            String target = TargetCalorie.getText().toString();

            if(weight.isEmpty() || height.isEmpty() || age.isEmpty() || target.isEmpty()){
                Toast.makeText(this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                return false;
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("User_Weight", weight);
            editor.putString("User_Height", height);
            editor.putString("User_Age", age);
            editor.putString("User_Target", target);
            editor.putBoolean("isFirstTime",false);
            editor.apply();

        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
            return false;
        }

        Toast.makeText(this,"Info Saved",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();


        return true;
    }
}
