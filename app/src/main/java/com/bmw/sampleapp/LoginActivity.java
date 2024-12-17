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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private EditText usernameField, passwordField;
    private Button loginButton;
    private Button registerButton;

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


        // Xử lý bấm nút Login
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Băm password
                String hPassword = hashPassword(password);
                Thread networkThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Thực hiện yêu cầu
                        final String result = callLoginAPI(username, hPassword); // Hàm thực hiện yêu cầu

                        // Cập nhật UI thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (result.contains("success")) {
                                    Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                                    // Chuyển sang trang chính của ứng dụng
                                    Intent intent = new Intent(LoginActivity.this, DisplayActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Log.i("result", result);
                                    try {
                                        JSONObject r = new JSONObject(result);
                                        Toast.makeText(LoginActivity.this, r.getString("message"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        });
                    }
                });
                networkThread.start();

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

    private String callLoginAPI(String username, String password) {
        try {
            // URL của API Login
            URL url = new URL("http://192.168.100.18:8081/login/login.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Tạo JSON data
            String jsonInput = "{\"username\":\"" + username + "\", \"password\":\"" + password + "\"}";

            // Gửi dữ liệu đến server
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
            Log.d("error login", e.toString());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(LoginActivity.this, "Error connecting to server", Toast.LENGTH_SHORT).show();
                }
            });
            return "error";
        }
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