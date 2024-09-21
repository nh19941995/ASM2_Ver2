#!/bin/bash

# Chạy container MySQL
docker run --name mysql-container \
  -e MYSQL_ROOT_PASSWORD=yourpassword \
  -e MYSQL_DATABASE=funix_asm_2 \
  -p 3306:3306 \
  -d mysql:latest
