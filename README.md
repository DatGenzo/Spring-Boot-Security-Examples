# Spring Boot 4 - Spring Security 7 Examples

Bài tập thực hành Spring Security với Spring Boot 4,
Thymeleaf, Spring Data JPA, MapStruct và SQL Server.

## Thông tin sinh viên

- Họ tên: Đỗ Thành Đạt
- MSSV: 24110192
- Lớp: 241104A

## Danh sách project

| Project | Port | Database | Chức năng |
|---|---:|---|---|
| `spring-security-login-email` | 8088 | `security_login_email` | Đăng nhập bằng email |
| `spring-security-custom-login` | 8089 | `security_custom_login` | Đăng nhập bằng username hoặc email |

## Ví dụ 1 - Login bằng email

Project: `spring-security-login-email`

Chức năng:

- Đăng nhập bằng email
- Trang đăng nhập tùy chỉnh
- Mã hóa mật khẩu bằng BCrypt
- Phân quyền USER và ADMIN
- Bảo vệ đường dẫn `/admin`
- Hiển thị email trên header
- Chuyển đổi Entity sang DTO bằng MapStruct
- Đăng xuất và hủy session
- Giao diện Thymeleaf và Bootstrap
- Hỗ trợ tiếng Việt UTF-8

## Ví dụ 2 - Custom Login

Project: `spring-security-custom-login`

Chức năng:

- Đăng nhập bằng username hoặc email
- Custom `UserDetails`
- Custom `UserDetailsService`
- Custom `DaoAuthenticationProvider`
- Mã hóa mật khẩu bằng BCrypt
- Phân quyền USER và ADMIN
- Bảo vệ đường dẫn `/admin`
- Hiển thị ảnh, họ tên, username, email và role
- Sử dụng Thymeleaf Layout Dialect
- Chuyển đổi Entity sang DTO bằng MapStruct
- Đăng xuất và hủy session
- Hỗ trợ tiếng Việt UTF-8

## Công nghệ

- Java 17
- Spring Boot 4.1.1
- Spring Security 7
- Spring MVC
- Spring Data JPA
- Thymeleaf
- Thymeleaf Layout Dialect
- Thymeleaf Spring Security Extras
- MapStruct 1.6.3
- Lombok
- SQL Server
- Bootstrap 5
- Maven

## Cấu hình

Tạo hai database:

- `security_login_email`
- `security_custom_login`

Mỗi project có file `.env.example`.

Sao chép `.env.example` thành `.env`, sau đó cấu hình:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- Các mật khẩu khởi tạo tài khoản

File `.env` không được commit lên GitHub.

## Chạy Ví dụ 1

Di chuyển vào thư mục `spring-security-login-email` và chạy:

`mvn clean spring-boot:run`

Truy cập: `http://localhost:8088`

## Chạy Ví dụ 2

Di chuyển vào thư mục `spring-security-custom-login` và chạy:

`mvn clean spring-boot:run`

Truy cập: `http://localhost:8089`

Nếu các mật khẩu khởi tạo được đặt thành `123456`,
có thể kiểm tra bằng các tài khoản:

| Quyền | Username | Email |
|---|---|---|
| USER | `user01` | `user01@gmail.com` |
| ADMIN | `admin01` | `admin01@gmail.com` |

## Bảo mật

- Password được lưu dưới dạng BCrypt
- Không commit `.env`
- Không lưu mật khẩu database trong source code
- Phân quyền URL bằng Spring Security
- CSRF được giữ ở trạng thái bật
- Logout sử dụng HTTP POST
- Session bị hủy khi đăng xuất