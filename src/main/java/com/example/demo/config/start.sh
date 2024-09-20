#!/bin/bash

# Khởi động MariaDB
mysqld_safe &

# Đợi MariaDB khởi động
until mysqladmin ping >/dev/null 2>&1; do
  echo -n "."; sleep 1
done

# Thiết lập mật khẩu root MariaDB (chỉ thực hiện lần đầu tiên)
if [ ! -f "/var/lib/mysql/.root_password_set" ]; then
  mysqladmin -u root password 'your_strong_password'
  touch /var/lib/mysql/.root_password_set
fi

# Các lệnh MySQL tiếp theo sử dụng mật khẩu đã thiết lập
export MYSQL_PWD='your_strong_password'

# Tạo người dùng MariaDB mới với quyền hạn chế
mysql -uroot -e "CREATE USER IF NOT EXISTS 'myuser'@'%' IDENTIFIED BY 'another_strong_password';"
mysql -uroot -e "GRANT SELECT, INSERT, UPDATE, DELETE ON *.* TO 'myuser'@'%';"

# Xóa các tài khoản mặc định và cơ sở dữ liệu test
mysql -uroot -e "DELETE FROM mysql.user WHERE User='';"
mysql -uroot -e "DELETE FROM mysql.user WHERE User='root' AND Host NOT IN ('localhost', '127.0.0.1', '::1');"
mysql -uroot -e "DROP DATABASE IF EXISTS test;"
mysql -uroot -e "DELETE FROM mysql.db WHERE Db='test' OR Db='test\_%';"
mysql -uroot -e "FLUSH PRIVILEGES;"

# Cấu hình phpMyAdmin
cp /var/www/html/phpmyadmin/config.sample.inc.php /var/www/html/phpmyadmin/config.inc.php
BLOWFISH_SECRET=$(openssl rand -hex 32)
sed -i "s/\$cfg\['blowfish_secret'\] = ''/\$cfg['blowfish_secret'] = '$BLOWFISH_SECRET'/" /var/www/html/phpmyadmin/config.inc.php

# Khởi động Apache
apache2ctl -D FOREGROUND