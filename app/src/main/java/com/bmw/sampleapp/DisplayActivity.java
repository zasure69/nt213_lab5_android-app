package com.bmw.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bmw.sampleapp.mysql_dataset;
import com.bmw.sampleapp.User;

public class DisplayActivity extends AppCompatActivity {
    private Button logOutButton;
    private SQLiteConnector db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);

        db = new SQLiteConnector(this);

        logOutButton = findViewById(R.id.logout);

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang LoginActivity
                Intent intent = new Intent(DisplayActivity.this, LoginActivity.class);
                startActivity(intent);

                // Kết thúc màn hình home
                finish();
            }
        });
    }
}
