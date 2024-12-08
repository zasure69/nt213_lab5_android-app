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

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, usernameField, passwordField;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        //Tham chiếu các view
        usernameField = findViewById(R.id.username);
        passwordField = findViewById(R.id.password);
        emailField = findViewById(R.id.email);
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
        emailField.addTextChangedListener(textWatcher);
        usernameField.addTextChangedListener(textWatcher);
        passwordField.addTextChangedListener(textWatcher);

        //Xử lý bấm nút Register
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                if (true) {
                    // Giả lập đăng ký thành công
                    Toast.makeText(RegisterActivity.this, "Register successful!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang LoginActivity
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);

                    // Kết thúc màn hình đăng ký
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Register failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // Kiểm tra các trường và bật nút nếu đủ điều kiện
    @SuppressLint("NewApi")
    private void checkFields() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String email = emailField.getText().toString().trim();

        // Bật và đổi màu nút Sign In nếu tất cả trường không rỗng
        if (!email.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
            registerButton.setBackgroundTintList(getResources().getColorStateList(R.color.blue));
            registerButton.setEnabled(true);
        }
        else registerButton.setEnabled(false);
    }
}
