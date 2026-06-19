#!/bin/bash
set -e
echo "[1/4] Atualizando pacotes..."
sudo apt-get update -qq
echo "[2/4] Instalando MySQL Server..."
sudo DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server > /dev/null
echo "[3/4] Iniciando MySQL..."
sudo service mysql start
echo "[4/4] Configurando acesso root..."
sudo mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root'; FLUSH PRIVILEGES;" 2>/dev/null || \
sudo mysql -e "UPDATE mysql.user SET authentication_string=PASSWORD('root'), plugin='mysql_native_password' WHERE User='root'; FLUSH PRIVILEGES;"
echo "MySQL pronto! user=root senha=root"
