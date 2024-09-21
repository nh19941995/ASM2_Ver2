# Sử dụng hình ảnh MySQL chính thức
FROM mysql:latest

# Đặt biến môi trường cho MySQL
ENV MYSQL_ROOT_PASSWORD=yourpassword
ENV MYSQL_DATABASE=funix_asm_2
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=yourpassword

# Sao chép file init.sql vào thư mục khởi tạo của MySQL
COPY init.sql /docker-entrypoint-initdb.d/
