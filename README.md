# Инструкция по запуску приложения
## Первый способ
Использую профайл `local` с использованием `in-memory` базы данных `h2`
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
## Второй способ
Используя `docker-compose.yml` будет использована база данных `PostgreSQL`
```
docker-compose up
```