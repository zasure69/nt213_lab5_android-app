package com.bmw.sampleapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterActivity extends AppCompatActivity {
    private EditText emailField, usernameField, passwordField;
    private Button registerButton;
    private Button loginButton;


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
                String hPassword = hashPassword(password);
                Thread networkThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Thực hiện yêu cầu
                        final String result = callRegisterAPI(username, email, hPassword); // Hàm thực hiện yêu cầu

                        // Cập nhật UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("success")) {
                                    Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i("result", result);
                                    try {
                                        JSONObject r = new JSONObject(result);
                                        Toast.makeText(RegisterActivity.this, r.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                        });
                    }
                });
                networkThread.start();


                // Chuyển sang LoginActivity
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);

                // Kết thúc màn hình đăng ký
                finish();

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

    private String callRegisterAPI(String username, String email, String password) {
        try {
            // URL của API Register
            URL url = new URL("http://192.168.100.18:8081/login/signup.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Tạo JSON data để gửi lên server
            String jsonInput = "{\"username\":\"" + username + "\", \"email\":\"" + email + "\", \"password\":\"" + password + "\"}";

            // Gửi dữ liệu lên server
            OutputStream os = conn.getOutputStream();
            os.write(jsonInput.getBytes());
            os.flush();
            os.close();

            // Nhận phản hồi từ server
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();
            return response.toString();

        } catch (Exception e) {
            Log.e("error", e.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(RegisterActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
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


