package com.bmw.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayActivity extends AppCompatActivity {
    private Button logOutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_display);

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
