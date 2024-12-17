package com.bmw.sampleapp;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private int id;             // ID của người dùng
    private String name;        // Tên người dùng
    private String email;       // Địa chỉ email của người dùng
    private String password;     // Mật khẩu của người dùng

    // Getter và Setter cho ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Getter và Setter cho tên
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter cho mật khẩu (không có setter để bảo mật)
    public String getPassword() {
        return password;
    }

    // Phương thức để thiết lập mật khẩu và tự động hash
    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    // Phương thức hash mật khẩu
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