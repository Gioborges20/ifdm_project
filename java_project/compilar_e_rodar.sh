#!/bin/bash
set -e
JAR=$(ls lib/mysql-connector-j-*.jar 2>/dev/null | head -1)
if ! command -v javac &>/dev/null; then
    echo "Instalando Java..."
    sudo apt-get update -qq && sudo apt-get install -y openjdk-17-jdk > /dev/null
fi
if [ -z "$JAR" ]; then
    echo "Baixando conector MySQL..."
    mkdir -p lib
    curl -sL "https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.3.0/mysql-connector-j-8.3.0.jar" -o lib/mysql-connector-j-8.3.0.jar
    JAR="lib/mysql-connector-j-8.3.0.jar"
fi
echo "Compilando..."
mkdir -p out
find src -name "*.java" | xargs javac -cp "$JAR" -d out -encoding UTF-8
echo "Executando..."
java -cp "out:$JAR" ifdm.Main
