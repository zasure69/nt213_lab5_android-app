package com.bmw.sampleapp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.bmw.sampleapp.User;
public class mysql_dataset {

    // Tạo các biến truy cập vào database
    private static final String URL = "jdbc:mysql://localhost:3306/Lab6?useSSL=false"; // Thay YOUR_HOST và YOUR_PORT
    private static final String USER = "root"; // Tên người dùng MySQL
    private static final String PASSWORD = "24042004"; // Mật khẩu MySQL

    // Liên kết đến database
    private Connection connect() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Kết nối thành công!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Lỗi");
            e.printStackTrace();
        }
        return connection;
    }

    // Tạo 1 user
    public void addUser(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy danh sách tất cả user
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT id, username, email, password FROM users ORDER BY username ASC";

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPassword(resultSet.getString("password"));
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // Kiểm tra user đã tòn tại chưa qua email
    public boolean checkUserExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Nếu có kết quả, người dùng đã tồn tại
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra user đã tồn tại chưa qua tên đăng nhập, mật khẩu
    public boolean checkUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Nếu có kết quả, thông tin đăng nhập là hợp lệ
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}