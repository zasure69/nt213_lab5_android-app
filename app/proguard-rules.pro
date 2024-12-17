# Giữ lại lớp RegisterActivity và tất cả các phương thức của nó
-keep class com.bmw.sampleapp.RegisterActivity {
    *;
}

# Giữ lại lớp LoginActivity và tất cả các phương thức của nó
-keep class com.bmw.sampleapp.LoginActivity {
    *;
}

# Giữ lại lớp DisplayActivity và tất cả các phương thức của nó
-keep class com.bmw.sampleapp.DisplayActivity {
    *;
}

# Giữ lại lớp SQLiteConnector
-keepclassmembernames class com.bmw.sampleapp.SQLiteConnector {
    public void addUser(com.bmw.sampleapp.User);
    public boolean checkUser(java.lang.String);
    public boolean checkUser(java.lang.String, java.lang.String);
    public java.lang.String getUrl(java.lang.String);
}

# Loại bỏ các phương thức cụ thể không cần thiết
-assumenosideeffects class com.bmw.sampleapp.SQLiteConnector {
    public void deleteUser(com.bmw.sampleapp.User);
    public void updateUser(com.bmw.sampleapp.User);
    public java.util.List getAllUser();
}

# Giữ lại lớp User nếu cần thiết
-keep class com.bmw.sampleapp.User {
    *;
}

# Giữ lại các lớp hoạt động (Activity) trong ứng dụng
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# Giữ lại các phương thức khởi tạo từ XML
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}