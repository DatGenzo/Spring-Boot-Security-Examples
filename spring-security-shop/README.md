# Spring Security Shop

Ví dụ 3 trong bài hướng dẫn Spring Boot 4.1.1, Spring Security 7 và MapStruct.

## Chức năng

- Register và xác nhận OTP qua Gmail
- Gửi lại OTP đăng ký
- Login bằng username hoặc email, lưu session
- Logout
- Forgot Password và OTP đặt lại mật khẩu
- CRUD, tìm kiếm, phân trang User
- Role USER và ADMIN
- Đếm số Product của từng User
- CRUD, tìm kiếm, phân trang Product
- Product thuộc User đăng nhập
- Upload, thay thế và xóa ảnh Cloudinary
- Dashboard đếm tổng User và Product
- MapStruct Entity/DTO
- Thymeleaf Layout Dialect

## Sinh viên

- Đỗ Thành Đạt
- MSSV: 24110192
- Lớp: 241104A

## Chạy project

1. Chạy `database-setup.sql` trong SQL Server Management Studio.
2. Sao chép `.env.example` thành `.env` và điền thông tin thật.
3. Chạy `mvn clean spring-boot:run`.
4. Mở `http://localhost:8090`.

File `.env` không được commit lên GitHub.