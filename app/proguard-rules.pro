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

# Giữ lại các lớp hoạt động (Activity) trong ứng dụng
-keep public class * extends android.app.Activity
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# Giữ lại các phương thức khởi tạo từ XML
-keepclassmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#Tối ưu hóa mã
-optimizationpasses 3 #Thực hiện tối ưu hóa mã 3 lần -> giúp giảm kích thước mã
-allowaccessmodification # Thay đổi các quyền truy cập của các thành phần để tối ưu hóa mã
-mergeinterfacesaggressively # Kết hợp các interface tương tự lại với nhau
# Làm rối tên lớp và phương thức
-obfuscationdictionary proguard-dictionaries/class-names.txt
-classobfuscationdictionary proguard-dictionaries/method-names.txt
-packageobfuscationdictionary proguard-dictionaries/package-names.txt