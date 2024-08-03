#!/bin/bash
set -e

# Инициализация базы данных с нужной локалью
initdb --locale=ru_RU.UTF-8 -D /var/lib/postgresql/data

# Запуск PostgreSQL
exec "$@"
