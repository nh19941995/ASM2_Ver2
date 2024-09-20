FROM debian:buster-slim

# Cài đặt các gói cần thiết
RUN apt-get update && apt-get install -y \
    mariadb-server \
    php \
    php-mysql \
    apache2 \
    libapache2-mod-php \
    wget

# Tải và cài đặt phpMyAdmin
RUN wget https://files.phpmyadmin.net/phpMyAdmin/5.1.1/phpMyAdmin-5.1.1-all-languages.tar.gz && \
    tar xvf phpMyAdmin-5.1.1-all-languages.tar.gz && \
    mv phpMyAdmin-5.1.1-all-languages /var/www/html/phpmyadmin

# Cấu hình MariaDB
RUN sed -i 's/bind-address\s*=\s*127.0.0.1/bind-address = 0.0.0.0/' /etc/mysql/mariadb.conf.d/50-server.cnf

# Tạo thư mục cho socket MariaDB
RUN mkdir -p /var/run/mysqld && \
    chown mysql:mysql /var/run/mysqld

# Cấu hình Apache ServerName
RUN echo "ServerName localhost" >> /etc/apache2/apache2.conf

# Tạo script khởi động
COPY start.sh /start.sh
RUN chmod +x /start.sh

# Mở cổng
EXPOSE 80 3306

# Khởi chạy script
CMD ["/start.sh"]