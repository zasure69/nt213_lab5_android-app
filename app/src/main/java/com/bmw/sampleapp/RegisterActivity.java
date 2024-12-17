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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bmw.sampleapp.SQLiteConnector;
import com.bmw.sampleapp.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, usernameField, passwordField;
    private Button registerButton;
    private Button loginButton;
    private SQLiteConnector db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // Tham chiếu các view
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        emailField = findViewById(R.id.email);
        registerButton = findViewById(R.id.register);
        loginButton = findViewById(R.id.login);

        // Khởi tạo một đối tượng MySQLConnector
        db = new SQLiteConnector(this);

        // Lắng nghe thay đổi văn bản
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

        emailField.addTextChangedListener(textWatcher);
        usernameField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        // Xử lý bấm nút Register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Kiểm tra thông tin
                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else if (db.checkUser(email) || db.checkUser(username, password)) {
                    Toast.makeText(RegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Tạo đối tượng User và thêm vào cơ sở dữ liệu
                    User user = new User();
                    user.setName(username);
                    user.setEmail(email);
                    user.setPassword(password); // Băm mật khẩu sẽ được xử lý trong lớp User
                    db.addUser(user);

                    Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang LoginActivity
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // Kết thúc màn hình đăng ký
                    finish();
                }
            }
        });

        // Xử lý nút Login -> Back to Login form
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    // Kiểm tra các trường và bật nút nếu đủ điều kiện
    @SuppressLint("NewApi")
    private void checkFields() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        // Bật và đổi màu nút Register nếu tất cả trường không rỗng
        if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            registerButton.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
            registerButton.setEnabled(true);
        } else {
            registerButton.setEnabled(false);
        }
    }
}