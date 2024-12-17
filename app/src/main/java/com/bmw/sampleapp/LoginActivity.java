package com.bmw.sampleapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField, passwordField;
    private Button loginButton;
    private Button registerButton;
    private SQLiteConnector db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Tham chiếu các view
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFields();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        usernameField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        // Khởi tạo một đối tượng MySQLConnector
        db = new SQLiteConnector(this);

        // Xử lý bấm nút Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Băm password
                String hashedPass = hashPassword(password);

                // Kiểm tra thông tin
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (db.checkUser(username, hashedPass)) {
                    Log.d("Thông báo", "Đăng nhập thành công.");
                    Log.d("Thông báo", "username: " + username);
                    Log.d("Thông báo", "Password: " + hashedPass);
                    // Đăng nhập thành công
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang trang chính của ứng dụng
                    Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Thông báo lỗi
                    Toast.makeText(LoginActivity.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý nút Register -> Chuyển sang trang đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
    @SuppressLint("NewApi")
    private void checkFields() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        // Bật và đổi màu nút Login nếu tất cả trường không rỗng
        if (!username.isEmpty() && !password.isEmpty()) {
            loginButton.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}