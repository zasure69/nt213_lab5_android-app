package com.bmw.sampleapp;

import static com.bmw.sampleapp.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField, passwordField;
    private Button logInButton, registerButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        //Tham chiếu các view
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        logInButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        // Lắng nghe thay đổi văn bản
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra nếu tất cả trường đều được nhập
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        // Xử lý sự kiện khi nút Log in được nhấn
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (true) {
                    // Giả lập đăng nhập thành công
                    Toast.makeText(LoginActivity.this, "Log in successfully!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang DisplayActivity
                    Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                    startActivity(intent);

                    // Kết thúc màn hình đăng nhập
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện khi nút Register được nhấn
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo Intent để chuyển sang RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent); // Chuyển sang RegisterActivity
            }
        });



    }

    // Kiểm tra các trường và bật nút nếu đủ điều kiện

    @SuppressLint("NewApi")
    private void checkFields() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Bật nút Sign In nếu tất cả trường không rỗng
        if (!username.isEmpty() && !password.isEmpty()) {
            logInButton.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
            logInButton.setEnabled(true);
        }
        else logInButton.setEnabled(false);
    }
}